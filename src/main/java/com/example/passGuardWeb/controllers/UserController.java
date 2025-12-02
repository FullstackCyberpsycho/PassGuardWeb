package com.example.passGuardWeb.controllers;

//import com.example.auth.models.User;
import com.example.auth.services.JwtService;
import com.example.passGuardWeb.dto.UserInfoDto;
import com.example.passGuardWeb.services.UserServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserServices userServices;
    private final JwtService jwtService;

    @GetMapping("/info")
    public ResponseEntity<UserInfoDto> getInfo(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtService.extractUserId(token);

        return ResponseEntity.status(HttpStatus.OK).body(userServices.getNameAndUsername(userId));
    }

    @PutMapping("/update")
    public ResponseEntity<UserInfoDto> update(@RequestHeader("Authorization") String authHeader,
                                       @RequestBody @Valid UserInfoDto user) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtService.extractUserId(token);

        return ResponseEntity.status(HttpStatus.OK).body(userServices.updateUser(userId, user));
    }
}
