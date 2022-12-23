package ru.art3m1y.petprojectshop.login.utils.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.persistence.Version;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class JWTUtil {
    private String secretKeyForAccess = "ASLJHD123IAD8123I";
    private String secretKeyForRefresh = "ASLJHD123IAMSDK";
    private String subject = "Identification details";
    private String issuer = "Art3m1y";
    private Algorithm signAccessToken = Algorithm.HMAC256(secretKeyForAccess);
    private Algorithm signRefreshToken = Algorithm.HMAC256(secretKeyForRefresh);
    public JWTVerifier verifierForAccessToken = JWT.require(signAccessToken)
            .withIssuer(issuer)
            .withSubject(subject)
            .build();
    public JWTVerifier verifierForRefreshToken = JWT.require(signRefreshToken)
            .withIssuer(issuer)
            .withSubject(subject)
            .build();
    private Date expirationAccessDate = Date.from(ZonedDateTime.now().plusMinutes(30).toInstant());
    private Date expirationRefreshDate = Date.from(ZonedDateTime.now().plusDays(3).toInstant());



    public String generateAccessToken(String email, String role) {
        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(expirationAccessDate)
                .withIssuedAt(new Date())
                .withIssuer(issuer)
                .withClaim("email", email)
                .withClaim("role", role)
                .sign(signAccessToken);
    }

    public String generateRefreshToken(long id) {
        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(expirationRefreshDate)
                .withIssuedAt(new Date())
                .withIssuer(issuer)
                .withClaim("id", id)
                .sign(signRefreshToken);
    }

    public boolean verifyAccessToken(String token) {
        try {
            verifierForAccessToken.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public boolean verifyRefreshToken(String token) {
        try {
            verifierForRefreshToken.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public String getEmailFromAccessToken(String token) {
        return verifierForAccessToken.verify(token).getClaim("email").asString();
    }

    public String getRoleFromAccessToken(String token) {
        return verifierForAccessToken.verify(token).getClaim("role").asString();
    }

    public long getIdFromRefreshToken(String token) {
        return verifierForRefreshToken.verify(token).getClaim("id").asLong();
    }

}
