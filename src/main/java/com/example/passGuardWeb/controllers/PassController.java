package com.example.passGuardWeb.controllers;

import com.example.auth.models.User;
import com.example.passGuardWeb.repository.UserRepository;
import com.example.auth.services.JwtService;
import com.example.passGuardWeb.dto.PasswordDto;
import com.example.passGuardWeb.models.Password;
import com.example.passGuardWeb.services.PassServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/pass")
public class PassController {
    private final PassServices passServices;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @PostMapping("/add-pass")
    public ResponseEntity<Password> addPass(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody @Valid Password password) throws Exception {

        // 1. Берем токен из заголовка
        String token = authHeader.replace("Bearer ", "");

        // 2. Получаем userId из токена
        Long userId = jwtService.extractUserId(token);

        // 3. Находим пользователя в БД
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 4. Сохраняем пароль, привязанный к пользователю
        return ResponseEntity.status(HttpStatus.CREATED).body(passServices.passSave(password, user));
    }

    @GetMapping("/all-pass")
    private ResponseEntity<List<PasswordDto>> getAllPasswords(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtService.extractUserId(token);

        return ResponseEntity.status(HttpStatus.OK).body(passServices.getAllPasswords(userId));
    }

    @DeleteMapping("/delete-pass/{passwordId}")
    public ResponseEntity<String> deleteUserPassword(@PathVariable Long passwordId,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtService.extractUserId(token);

        if (passServices.deleteUserPassword(userId, passwordId)) {
            return ResponseEntity.ok("Пароль удалён");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пароль не найден");
        }
    }

    @PutMapping("/update-pass/{passwordId}")
    public ResponseEntity<Password> updatePassword(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long passwordId,
            @RequestBody @Valid PasswordDto passwordDto) {

        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtService.extractUserId(token);

        return ResponseEntity.ok(passServices.updatePassword(userId, passwordId, passwordDto));
    }
}
