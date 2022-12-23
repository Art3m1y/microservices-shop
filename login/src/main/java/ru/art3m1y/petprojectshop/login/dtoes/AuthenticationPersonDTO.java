package ru.art3m1y.petprojectshop.login.dtoes;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

public class AuthenticationPersonDTO {
    @NotEmpty(message = "Почта не может быть пустой.")
    private String email;
    @NotEmpty(message = "Пароль не может быть пустой.")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
