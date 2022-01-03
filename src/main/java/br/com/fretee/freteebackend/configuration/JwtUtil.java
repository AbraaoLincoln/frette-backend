package br.com.fretee.freteebackend.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Component
public class JwtUtil {
    private final String secret = "secret";
    public  final String bearer = "Bearer ";
    private final String issuer = "fretee";
    private final Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
    private final JWTVerifier verifier = JWT.require(algorithm).build();

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public String generateAccessToken(String nomeUsuario, List<String> permissoes) {
        return JWT.create()
                .withSubject(nomeUsuario)
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer(issuer)
                .withClaim("permissoes", permissoes)
                .sign(algorithm);
    }

    public String generateRefreshToken(String nomeUsuario) {
        return JWT.create()
                .withSubject(nomeUsuario)
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public void writeAccessAndRefreshTokenToBodyOfResponse(String accessToken, String refreshToken, HttpServletResponse response) throws IOException {
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    public DecodedJWT verify(String token) {
        return verifier.verify(token);
    }

    public List<SimpleGrantedAuthority> getPermissoes(DecodedJWT decodedJWT) {
        String[] permissoesArray = decodedJWT.getClaim("permissoes").asArray(String.class);
        List<SimpleGrantedAuthority> permissoes = new ArrayList<>();
        stream(permissoesArray).forEach(permissao -> permissoes.add(new SimpleGrantedAuthority(permissao)));

        return permissoes;
    }
}
