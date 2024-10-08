package com.example.demo;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.function.Function;

public interface TokenResolver {

    /**
     * Cria uma instância de {@link UserDetails} a partir de um token JWT válido
     * @param jwtToken token JWT
     * @return Instância de {@link UserDetails} padrão do Spring
     */
    UserDetails getUserDetails(String jwtToken);

    /**
     * Obtém as {@link Claims} de um token JWT
     * @param token string contendo o token
     * @return referência das características extraídas do token jwt
     */
    Claims getAllClaimsFromToken(String token);

    /**
     * Verifica a validade do token jwt
     * @param token string contendo o token jwt
     * @return true caso o token seja válido e false caso não seja válido. Valida também a data de expiração do token
     */
    boolean isTokenValid(String token);
    /**
     * Extrai a data de expiração do token
     * @param token token jwt
     * @return Instância de {@link Date} com a data de expiração do token
     */
    Date getExpirationDateFromToken(String token);
    /**
     * Extrai algum conteúdo do token jwt
     * @param token string contendo o token
     * @param claimsResolver função lambda contendo a informação que deseja extrair
     * @return resultado da aplicação da função lambda
     * @param <T> parametrização do tipo de retorno conforme o tipo de dado desejado para extração
     */
    <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver);
}
