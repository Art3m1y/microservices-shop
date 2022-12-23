package ru.art3m1y.petprojectshop.apigateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.art3m1y.petprojectshop.apigateway.dtoes.JwtUserInfoDto;

@Component
public class LoginFilter extends AbstractGatewayFilterFactory {
    private final WebClient.Builder webClientBuilder;

    public LoginFilter(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Object config) {
        return ((exchange, chain) -> {
            if (exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION) == null) {
                return chain.filter(exchange);
            }

            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

            String[] authHeaderArray = authHeader.split(" ");

            if (authHeaderArray.length != 2 || !authHeaderArray[0].equals("Bearer")) {
                return chain.filter(exchange);
            }

            String url = "http://localhost:8765/login/auth/checkaccesstoken?accessToken=" + authHeaderArray[1];

            return webClientBuilder.build()
                    .get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(JwtUserInfoDto.class)
                    .map(jwtUserInfoDto -> {
                        exchange.getRequest().mutate().header("x-auth-user-email", jwtUserInfoDto.getEmail());
                        exchange.getRequest().mutate().header("x-auth-user-role", jwtUserInfoDto.getRole());
                        return exchange;
                    }).flatMap(chain::filter);
        });
    }
}
