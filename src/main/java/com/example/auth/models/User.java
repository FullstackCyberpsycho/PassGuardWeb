package com.example.auth.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Id;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Это поле не должно быть пустым!")
    @Size(max = 55, min = 5, message = "Минимальная длина 5 символов. Максимальная 55 символом")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "Это поле не должно быть пустым!")
    @Size(min = 2, max = 55, message = "Минимальная длина 2 символов. Максимальная 55 символом")
    private String name;

    @NotBlank(message = "Это поле не должно быть пустым!")
    @Size(min = 10, max = 255, message = "Минимальная длина 10 символов. Максимальная 255 символом")
    private String password;
}