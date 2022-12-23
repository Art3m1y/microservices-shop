package ru.art3m1y.petprojectshop.login.dtoes;

import jakarta.validation.constraints.*;

public class RegistrationPersonDTO {
    @NotEmpty(message = "Поле с именем не может быть пустым.")
    private String name;
    @NotEmpty(message = "Поле с фамилией не может быть пустым.")
    private String surname;
    @Min(value = 1900, message = "Год рождения не может быть менее, чем 1900 год.")
    @Max(value = 2022, message = "Год рождения не может быть более, чем 2022 год.")
    private int yearOfBirth;
    @NotEmpty(message = "Поле со страной проживания не может быть пустым.")
    private String country;
    @NotEmpty(message = "Почта не может быть пустой.")
    @Email(message = "Введите почту в правильном формате.")
    private String email;
    @NotEmpty(message = "Пароль не может быть пустой.")
    @Size(min = 5, message = "Минимальная длина пароля составляет 5 символов.")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

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
