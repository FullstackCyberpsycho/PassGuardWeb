package com.example.passGuardWeb.controllers;

import com.example.auth.services.JwtService;
import com.example.passGuardWeb.dto.UserInfoDto;
import com.example.passGuardWeb.services.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
}
