package com.example.passGuardWeb.models;

import com.example.auth.models.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "passwords")
public class Password {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Это поле не должно быть пустым!")
    @Size(max = 255, message = "Максимальная длина 255 символом")
    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Size(max = 255, message = "Максимальная длина 255 символом")
    private String website;

    @NotBlank(message = "Это поле не должно быть пустым!")
    @Size(max = 255, message = "Максимальная длина 255 символом")
    private String username;

    @NotBlank(message = "Это поле не должно быть пустым!")
    private String password;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}