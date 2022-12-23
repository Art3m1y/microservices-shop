package ru.art3m1y.petprojectshop.login.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;
    @Column
    @NotEmpty(message = "Поле с именем не может быть пустым.")
    private String name;
    @Column
    @NotEmpty(message = "Поле с фамилией не может быть пустым.")
    private String surname;
    @Column(name = "year_of_birth")
    @Min(value = 1900, message = "Год рождения не может быть менее, чем 1900 год.")
    @Max(value = 2022, message = "Год рождения не может быть более, чем 2022 год.")
    private int yearOfBirth;
    @Column
    @NotEmpty(message = "Поле со страной проживания не может быть пустым.")
    private String country;
    @Column
    @NotEmpty(message = "Почта не может быть пустой.")
    @Email(message = "Введите почту в правильном формате.")
    private String email;
    @Column
    @NotEmpty(message = "Почта не может быть пустой.")
    @Size(min = 5, message = "Минимальная длина пароля составляет 5 символов.")
    private String password;
    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date createdAt;
    @Column
    private String role;
    @OneToOne(mappedBy = "person")
    private RefreshToken refreshToken;
    @Column(name = "activation_code")
    private String activationCode;

    @Column(name = "restore_password_code")
    private String restorePasswordCode;

    public Person() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getRestorePasswordCode() {
        return restorePasswordCode;
    }

    public void setRestorePasswordCode(String restorePasswordCode) {
        this.restorePasswordCode = restorePasswordCode;
    }
}
