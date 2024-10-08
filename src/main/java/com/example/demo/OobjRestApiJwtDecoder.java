package com.example.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;

import java.util.Map;

/**
 * Decodificador para tokens emitidos pelo Oobj RestAPI
 *
 * @author Wedson Silva
 * @see <a href="https://dev.azure.com/oobj-devops/Engineering/_workitems/edit/2282">Azzure #2282</a>
 * @since 15/08/2024
 */
@RequiredArgsConstructor
@Slf4j
public class OobjRestApiJwtDecoder implements JwtDecoder {

    private final TokenResolver tokenResolver;

    @Override
    public Jwt decode(String token) throws JwtException {

            boolean tokenValid = tokenResolver.isTokenValid(token);
            if (!tokenValid) {
                throw new InvalidBearerTokenException("Token inválido!");
            }

            try {

                // Decodificar o token JWT e validar os claims conforme a lógica específica
                Map<String, Object> claims = tokenResolver.getAllClaimsFromToken(token);

                return Jwt.withTokenValue(token)
                        .headers(headers -> headers.putAll(claims))
                        .claims(c -> c.putAll(claims))
                        .expiresAt(tokenResolver.getClaimFromToken(token, c -> c.getExpiration().toInstant()))
                        .issuedAt(tokenResolver.getClaimFromToken(token, c -> c.getIssuedAt().toInstant()))
                        .build();

            } catch (Throwable ex) {
                log.error("Erro ao validar o token", ex);
                throw new InvalidBearerTokenException("Erro ao autenticar o token JWT!", ex);
            }
    }

}