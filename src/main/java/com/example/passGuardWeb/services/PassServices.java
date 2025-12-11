package com.example.passGuardWeb.services;

import com.example.auth.models.User;
import com.example.passGuardWeb.AesUtil;
import com.example.passGuardWeb.dto.PasswordDto;
import com.example.passGuardWeb.models.Password;
import com.example.passGuardWeb.repository.PassRepository;
//import jakarta.persistence.Cacheable;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.parameters.P;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
//import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class PassServices {
    private final PassRepository passRepository;
    private final PasswordEncoder passwordEncoder;
    private final AesUtil aes;

    @CacheEvict(value = "passwords", allEntries = true)
    public Password passSave(Password password, User user) {
        try {
            String encrypted = aes.encrypt(password.getPassword());
            password.setPassword(encrypted);
            password.setUser(user);
            log.info("Учетная запись {} добавлена", password.getServiceName());
            return passRepository.save(password);
        } catch (Exception e) {
            log.warn("Неудачная попытка добавить учетную запись {}", password.getServiceName());
            throw new RuntimeException("Ошибка шифрования", e);
        }
    }

    @Cacheable(value = "passwords")
    public List<PasswordDto> getAllPasswords(Long userId) {
        List<Password> list = passRepository.findByUserId(userId);
        List<PasswordDto> result = new ArrayList<>(list.size());

        for (Password p : list) {
            String decrypted = aes.decrypt(p.getPassword());

            result.add(new PasswordDto(
                    p.getId(),
                    p.getServiceName(),
                    p.getWebsite(),
                    p.getUsername(),
                    decrypted
            ));
        }

        return result;
    }

    @CacheEvict(value = "passwords", allEntries = true)
    public boolean deleteUserPassword(Long userId, Long passwordId) {
        // Проверим, существует ли пароль у этого пользователя
        Optional<Password> pass = passRepository.findById(passwordId);
        if (pass.isPresent() && pass.get().getUser().getId().equals(userId)) {
            passRepository.deleteByIdAndUserId(passwordId, userId);
            log.info("Учетная запись под id={}, пользователя id={} была удалена" , pass.get().getId(), userId);
            return true;
        }
        log.warn("Неудачная попытка удалить учетную запись {}", pass.get().getServiceName());
        return false;
    }

    @CacheEvict(value = "passwords", allEntries = true)
    public Password updatePassword(Long userId, Long passwordId, PasswordDto passwordDto) {

        // Находим пароль, который принадлежит именно ЭТОМУ пользователю
        Password password = passRepository
                .findByIdAndUserId(passwordId, userId)
                .orElseThrow(() -> new RuntimeException("Учетная запись не найдена"));

        password.setServiceName(passwordDto.getServiceName());
        password.setWebsite(passwordDto.getWebsite());
        password.setUsername(passwordDto.getUsername());

        String encrypted = aes.encrypt(passwordDto.getPassword());
        password.setPassword(encrypted);

        log.info("Учетная запись {} обновлена", passwordDto.getServiceName());
        return passRepository.save(password);
    }

    @Cacheable(value = "passwords", key = "'serviceName_' + #serviceName")
    public List<PasswordDto> getSearchPassword(Long userId, String serviceName) {
        return passRepository.findByUserIdAndServiceNameStartingWithIgnoreCase(userId, serviceName.trim()).stream()
                .map(p -> new PasswordDto(
                        p.getId(),
                        p.getServiceName(),
                        p.getWebsite(),
                        p.getUsername(),
                        aes.decrypt(p.getPassword())
                )).toList();
    }

    @Cacheable(value = "passwords", key = "'ascSortedPassword'")
    public List<PasswordDto> getAscSortedPassword(Long userId) {
        return passRepository.findByUserIdOrderByServiceNameAsc(userId)
                .stream().map(p -> new PasswordDto(
                        p.getId(),
                        p.getServiceName(),
                        p.getWebsite(),
                        p.getUsername(),
                        aes.decrypt(p.getPassword())
                )).toList();
    }

    @Cacheable(value = "passwords", key = "'descSortedPassword'")
    public List<PasswordDto> getDescSortedPassword(Long userId) {
        return passRepository.findByUserIdOrderByServiceNameDesc(userId)
                .stream().map(p -> new PasswordDto(
                        p.getId(),
                        p.getServiceName(),
                        p.getWebsite(),
                        p.getUsername(),
                        aes.decrypt(p.getPassword())
                )).toList();
    }
}
