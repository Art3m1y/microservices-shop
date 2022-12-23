package ru.art3m1y.petprojectshop.login.dtoes;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class RestorePasswordAfterTransitionDto {
    @NotEmpty(message = "Пароль не может быть пустой.")
    @Size(min = 5, message = "Минимальная длина пароля составляет 5 символов.")
    private String password;

    @NotEmpty(message = "Пароль подтверждения не может быть пустой.")
    @Size(min = 5, message = "Минимальная длина пароля составляет 5 символов.")
    private String secondPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecondPassword() {
        return secondPassword;
    }

    public void setSecondPassword(String secondPassword) {
        this.secondPassword = secondPassword;
    }
}
