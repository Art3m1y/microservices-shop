package ru.art3m1y.petprojectshop.login.dtoes;

public class JwtUserInfoDto {
    private String email;
    private String role;

    public JwtUserInfoDto(String email, String role) {
        this.email = email;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
