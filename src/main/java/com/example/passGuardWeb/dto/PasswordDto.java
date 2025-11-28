package com.example.passGuardWeb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordDto {
    private Long id;
    private String serviceName;
    private String website;
    private String username;
    private String password;
}
