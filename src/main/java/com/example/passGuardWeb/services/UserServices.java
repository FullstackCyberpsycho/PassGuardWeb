package com.example.passGuardWeb.services;

import com.example.auth.models.User;
import com.example.passGuardWeb.repository.UserRepository;
import com.example.passGuardWeb.dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServices {
    private final UserRepository userRepository;

    public UserInfoDto getNameAndUsername(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

       return new UserInfoDto(user.getName(), user.getUsername(), user.getName().substring(0, 1).toUpperCase());
    }

    public UserInfoDto update(Long userId, UserInfoDto userInfoDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(userInfoDto.getName());
        user.setUsername(userInfoDto.getUsername());
        userRepository.save(user);

        return new UserInfoDto(user.getName(), user.getUsername(), user.getName().substring(0, 1).toUpperCase());
    }

    public void deleteById(Long userID) {
        userRepository.deleteById(userID);
    }
}
