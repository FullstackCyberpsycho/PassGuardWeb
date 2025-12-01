package com.example.passGuardWeb.services;

import com.example.auth.models.User;
import com.example.passGuardWeb.AesUtil;
import com.example.passGuardWeb.dto.PasswordDto;
import com.example.passGuardWeb.models.Password;
import com.example.passGuardWeb.repository.PassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PassServices {
    private final PassRepository passRepository;
    private final PasswordEncoder passwordEncoder;
    private final AesUtil aes;

    public Password passSave(Password password, User user) {
        try {
            String encrypted = aes.encrypt(password.getPassword());
            password.setPassword(encrypted);
            password.setUser(user);
            return passRepository.save(password);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка шифрования", e);
        }
    }

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

    public boolean deleteUserPassword(Long userId, Long passwordId) {
        // Проверим, существует ли пароль у этого пользователя
        Optional<Password> pass = passRepository.findById(passwordId);
        if (pass.isPresent() && pass.get().getUser().getId().equals(userId)) {
            passRepository.deleteByIdAndUserId(passwordId, userId);
            return true;
        }
        return false;
    }

//    public Password updatePassword(PasswordDto passwordDto)  {//, Long userId, Long passwordId) {
//        Password password = new Password();
//        password.setServiceName(passwordDto.getServiceName());
//        password.setWebsite(passwordDto.getWebsite());
//        password.setUsername(passwordDto.getUsername());
//
//        String encrypted = aes.encrypt(passwordDto.getPassword());
//        password.setPassword(encrypted);
//
//        return passRepository.save(password);
//    }

    public Password updatePassword(Long userId, Long passwordId, PasswordDto dto) {

        // 1. Находим пароль, который принадлежит именно ЭТОМУ пользователю
        Password password = passRepository
                .findByIdAndUserId(passwordId, userId)
                .orElseThrow(() -> new RuntimeException("Password not found or access denied"));

        // 2. Обновляем данные
        password.setServiceName(dto.getServiceName());
        password.setWebsite(dto.getWebsite());
        password.setUsername(dto.getUsername());

        // 3. Шифруем, только если передан новый пароль
//        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
//            String encrypted = aes.encrypt(dto.getPassword());
//            existing.setPassword(encrypted);
//        }
        String encrypted = aes.encrypt(dto.getPassword());
        password.setPassword(encrypted);

        // 4. Сохраняем обновлённый объект
        return passRepository.save(password);
    }

}
