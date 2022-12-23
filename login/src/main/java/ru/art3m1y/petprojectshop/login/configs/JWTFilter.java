package ru.art3m1y.petprojectshop.login.configs;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.art3m1y.petprojectshop.login.services.PersonDetailsService;
import ru.art3m1y.petprojectshop.login.utils.jwt.JWTUtil;

import java.io.IOException;
import java.util.List;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final PersonDetailsService personDetailsService;

    public JWTFilter(JWTUtil jwtUtil, PersonDetailsService personDetailsService) {
        this.jwtUtil = jwtUtil;
        this.personDetailsService = personDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();

        if (!path.equals("/auth/refreshtoken") && !path.equals("/auth/login") && !path.equals("/auth/registration")) {
            checkAccessToken(request, response);
        }

        filterChain.doFilter(request, response);
    }

    private void checkAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            String JWTToken = authHeader.substring(7);
            if (JWTToken.isBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Токен доступа пуст");
            } else {
                try {
                    String email = jwtUtil.getEmailFromAccessToken(JWTToken);

                    UserDetails personDetails = personDetailsService.loadUserByUsername(email);

                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(personDetails, personDetails.getPassword(), personDetails.getAuthorities());

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(token);
                    }
                } catch (JWTVerificationException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный токен доступа " + e.getMessage());
                } catch (UsernameNotFoundException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Пользователь с почтой, указанной в токене доступа, не найден");
                }
            }
        }
    }
}
