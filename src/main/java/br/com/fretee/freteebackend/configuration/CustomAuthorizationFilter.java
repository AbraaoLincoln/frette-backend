package br.com.fretee.freteebackend.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final AlgorithmUtil algorithmUtil;

    CustomAuthorizationFilter(AlgorithmUtil algorithmUtil) {
        this.algorithmUtil =  algorithmUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("/api/login")) {
            filterChain.doFilter(request, response);
        }else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            String bearer = "Bearer ";

            if(authorizationHeader != null && authorizationHeader.startsWith(bearer)) {
                try {
                    String token = authorizationHeader.substring(bearer.length());

                    JWTVerifier verifier = JWT.require(algorithmUtil.getAlgorithm()).build();
                    DecodedJWT decodedJWT = verifier.verify(token);

                    String nomeUsuario = decodedJWT.getSubject();
                    String[] permissoesArray = decodedJWT.getClaim("permissoes").asArray(String.class);

                    List<SimpleGrantedAuthority> permissoes = new ArrayList<>();
                    stream(permissoesArray).forEach(permissao -> permissoes.add(new SimpleGrantedAuthority(permissao)));

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(nomeUsuario, null, permissoes);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    filterChain.doFilter(request, response);
                }catch (Exception e) {
                    log.error("Erro loggnig in {}:", e.getMessage());
                    response.setHeader("error", e.getMessage());
                    response.setStatus(FORBIDDEN.value());
                    //response.sendError(FORBIDDEN.value());
                    Map<String, String> error = new HashMap<>();
                    error.put("error_message", e.getMessage());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }
            }else {
                filterChain.doFilter(request, response);
            }

        }
    }
}
