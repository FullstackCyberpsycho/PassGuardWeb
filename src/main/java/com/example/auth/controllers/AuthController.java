package com.example.auth.controllers;

import com.example.auth.dto.JwtResponse;
import com.example.auth.dto.LoginRequest;
import com.example.auth.dto.RegisterRequest;
import com.example.auth.models.User;
import com.example.auth.repository.AuthRepository;
import com.example.passGuardWeb.repository.UserRepository;
import com.example.auth.services.AuthServices;
import com.example.auth.services.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthServices authServices;
    private final AuthRepository authRepository;

    /*@PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        User user = new User();

        user.setUsername(request.getUsername());
        user.setName(request.getName());
        user.setPassword(request.getPassword());
        authServices.register(user);
        log.info("Новый пользователь зарегистрирован: {}", user.getUsername());
        return ResponseEntity.ok("зарегистрирован");
    }*/

//    @PostMapping("/register")
//    public ResponseEntity<?> register(
//            @RequestHeader(value = "Authorization", required = false) String authHeader,
//                        @RequestBody RegisterRequest registerRequest) {
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body("Вы уже авторизованы — регистрация второго аккаунта запрещена");
//        }
//
//        try {
//            User user = authServices.register(registerRequest);
//
//            return ResponseEntity.ok("Регистрация успешна");
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }


    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody RegisterRequest dto) {

        // Если пользователь авторизован — запрещаем создавать второй аккаунт
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Вы уже авторизованы — нельзя создавать второй аккаунт");
        }

        try {
            authServices.register(dto); // теперь работает с DTO
            return ResponseEntity.ok("Регистрация успешна");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    /*@PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody RegisterRequest request) {

        // 1. Запрещаем регистрацию, если пользователь уже авторизован
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Вы уже авторизованы. Создание второго аккаунта запрещено.");
        }

        try {
            authServices.register(request);
            return ResponseEntity.ok("Пользователь зарегистрирован");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }*/

    /*@PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        // Проверяем, есть ли уже хотя бы один пользователь
        if (authRepository.count() > 0) {
            System.out.println("Регистрация недоступна: аккаунт уже существует.");
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Регистрация недоступна: аккаунт уже существует.");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setName(request.getName());
        user.setPassword(request.getPassword());

        authServices.register(user);
        log.info("Новый пользователь зарегистрирован: {}", user.getUsername());

        return ResponseEntity.ok("Пользователь зарегистрирован");
    }*/


//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
//
//        if (!authRepository.findAll().isEmpty()) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body("Регистрация недоступна: аккаунт уже существует.");
//        }
//
//        User user = new User();
//        user.setUsername(request.getUsername());
//        user.setName(request.getName());
//        user.setPassword(request.getPassword());
//
//        authServices.register(user);
//
//        return ResponseEntity.ok("Пользователь зарегистрирован");
//    }


    /*@PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        // Проверяем, существует ли пользователь с таким именем
        if (authRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Пользователь с таким логином уже существует");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setName(request.getName());
        user.setPassword(request.getPassword());

        authServices.register(user);

        return ResponseEntity.ok("Пользователь зарегистрирован");
    }*/

    /*@PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if (authRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Пользователь с таким логином уже существует");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setName(request.getName());
        user.setPassword(request.getPassword());

        authServices.register(user);

        return ResponseEntity.ok("зарегистрирован");
    }*/





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