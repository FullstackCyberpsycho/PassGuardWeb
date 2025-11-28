package com.example.auth.controllers;

import com.example.auth.dto.JwtResponse;
import com.example.auth.dto.LoginRequest;
import com.example.auth.dto.RegisterRequest;
import com.example.auth.models.User;
import com.example.auth.repository.UserRepository;
import com.example.auth.services.AuthServices;
import com.example.auth.services.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthServices authServices;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setName(request.getName());
        user.setPassword(request.getPassword()); // НЕ кодируем здесь
        authServices.register(user); // кодирование внутри сервиса
        log.info("Новый пользователь зарегистрирован: {}", user.getUsername());
        return ResponseEntity.ok("зарегистрирован");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Неудачная попытка входа: {}", request.getUsername());
            throw new RuntimeException("Неверный пароль");
        }

        String token = jwtService.generateToken(user.getId());

        log.info("Пользователь вошёл: id={}, username={}", user.getId(), user.getUsername());

        return ResponseEntity.ok(new JwtResponse(token));
    }
}
