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

    public UserInfoDto getNameAndUsername(Long userid) {
        User user = userRepository.findById(userid)
                .orElseThrow(() -> new RuntimeException("User not found"));

       return new UserInfoDto(user.getName(), user.getUsername(), user.getUsername().substring(0, 1).toUpperCase());
    }
}
