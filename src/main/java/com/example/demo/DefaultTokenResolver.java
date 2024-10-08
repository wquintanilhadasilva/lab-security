package com.example.demo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Date;
import java.util.function.Function;


/**
 * Trabalha o token JWT emitido pelo Rest API
 *
 * @author Wedson Silva
 * @see <a href="https://dev.azure.com/oobj-devops/Engineering/_workitems/edit/2756">Azzure #2756</a>
 * @since 22/05/2024
 */
@RequiredArgsConstructor
@Slf4j
public class DefaultTokenResolver implements TokenResolver {

    private final String secret;

    /**
     * Cria uma instância de {@link UserDetails} a partir de um token JWT válido
     * @param jwtToken token JWT
     * @return Instância de {@link UserDetails} padrão do Spring
     */
    public UserDetails getUserDetails(String jwtToken) {
        Claims claims = getAllClaimsFromToken(jwtToken);
        return new User(claims.getSubject(), "", new ArrayList<GrantedAuthority>());
    }

    /**
     * Obtém as {@link Claims} de um token JWT
     * @param token string contendo o token
     * @return referência das características extraídas do token jwt
     */
    @Override
    public Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Could not get all claims Token from passed token");
            claims = null;
        }
        return claims;
    }

    /**
     * Verifica a validade do token jwt
     * @param token string contendo o token jwt
     * @return true caso o token seja válido e false caso não seja válido. Valida também a data de expiração do token
     */
    @Override
    public boolean isTokenValid(String token) {
        try {
            Boolean isOK = true;
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            Date expiration = getExpirationDateFromToken(token);
            Date now = new Date();
            isOK = now.before(expiration);
            return isOK;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extrai a data de expiração do token
     * @param token token jwt
     * @return Instância de {@link Date} com a data de expiração do token
     */
    @Override
    public Date getExpirationDateFromToken(String token) {
        return (Date)this.getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Extrai algum conteúdo do token jwt
     * @param token string contendo o token
     * @param claimsResolver função lambda contendo a informação que deseja extrair
     * @return resultado da aplicação da função lambda
     * @param <T> parametrização do tipo de retorno conforme o tipo de dado desejado para extração
     */
    @Override
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = this.getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

}
