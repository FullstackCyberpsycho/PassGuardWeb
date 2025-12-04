package com.example.auth.services;

import com.example.auth.dto.RegisterRequest;
import com.example.auth.models.User;
import com.example.auth.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServices {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(RegisterRequest registerRequest) {
        if (authRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Пользователь с таким username уже существует");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setName(registerRequest.getName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        return authRepository.save(user);
    }

    public User login(String username, String password) {
        User user = authRepository.findByUsername(username);

        if (user == null) {
            return null;
        }
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }

        //System.out.println("Не верный пароль от " + username);
        return null;
    }

}
