package ru.art3m1y.petprojectshop.login.dtoes;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class RestorePasswordDto {
    @NotEmpty(message = "Почта не может быть пустой.")
    @Email(message = "Введите почту в правильном формате.")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
