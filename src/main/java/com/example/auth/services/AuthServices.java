package com.example.auth.services;

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

//    @Autowired
//    public AuthServices(AuthRepository authRepository, PasswordEncoder passwordEncoder) {
//        this.authRepository = authRepository;
//        this.passwordEncoder = passwordEncoder;
//    }

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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

        System.out.println("Не верный пароль от " + username);
        return null;
    }


}
