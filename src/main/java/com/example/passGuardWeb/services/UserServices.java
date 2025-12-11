package com.example.passGuardWeb.services;

import com.example.auth.models.User;
import com.example.passGuardWeb.repository.UserRepository;
import com.example.passGuardWeb.dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServices {
    private final UserRepository userRepository;

    @Cacheable(value = "users")
    public UserInfoDto getNameAndUsername(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

       return new UserInfoDto(user.getName(), user.getUsername(), user.getName().substring(0, 1).toUpperCase());
    }

    @CacheEvict(value = "users", allEntries = true)
    public UserInfoDto update(Long userId, UserInfoDto userInfoDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(userInfoDto.getName());
        user.setUsername(userInfoDto.getUsername());
        userRepository.save(user);

        log.info("Пользователь с id={} обновлен", userId);
        return new UserInfoDto(user.getName(), user.getUsername(), user.getName().substring(0, 1).toUpperCase());
    }

    @CacheEvict(value = "users", allEntries = true)
    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
        log.info("Пользователь с id={} удален", userId);
    }
}
