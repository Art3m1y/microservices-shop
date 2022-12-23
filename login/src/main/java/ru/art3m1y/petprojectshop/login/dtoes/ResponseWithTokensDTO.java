package ru.art3m1y.petprojectshop.login.dtoes;

public class ResponseWithTokensDTO {
    private String accessToken;
    private String refreshToken;

    public ResponseWithTokensDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public ResponseWithTokensDTO(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
