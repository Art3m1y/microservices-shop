package ru.art3m1y.petprojectshop.apigateway.dtoes;

public class JwtUserInfoDto {
    private String email;
    private String role;

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

    @Override
    public String toString() {
        return "JwtUserInfoDto{" +
                "email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
