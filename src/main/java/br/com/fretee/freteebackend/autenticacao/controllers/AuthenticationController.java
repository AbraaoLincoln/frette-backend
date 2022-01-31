package br.com.fretee.freteebackend.autenticacao.controllers;

import br.com.fretee.freteebackend.configuration.JwtUtil;
import br.com.fretee.freteebackend.usuarios.exceptions.UsuarioNotFoundException;
import br.com.fretee.freteebackend.usuarios.entity.Permissao;
import br.com.fretee.freteebackend.usuarios.entity.Usuario;
import br.com.fretee.freteebackend.usuarios.service.UsuarioService;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/autenticacao")
@Slf4j
public class AuthenticationController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/token/refresh")
    public void refreshUserToken(HttpServletRequest request, HttpServletResponse response) throws IOException, UsuarioNotFoundException {
        log.info("Refreshing token");
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if(authorizationHeader != null && authorizationHeader.startsWith( jwtUtil.bearer)) {
            String refresh_token = authorizationHeader.substring(jwtUtil.bearer.length());

            DecodedJWT decodedJWT = jwtUtil.verify(refresh_token);
            String nomeUsuario = decodedJWT.getSubject();
            Usuario usuario = usuarioService.findUsuarioByNomeUsuario(nomeUsuario);

            String accessToken = jwtUtil.generateAccessToken(usuario.getNomeUsuario(), usuario.getPermissoes().stream().map(Permissao::getNome).collect(Collectors.toList()));

            jwtUtil.writeAccessAndRefreshTokenToBodyOfResponse(accessToken, refresh_token, response);
        }
    }

//    @GetMapping("/token/refresh")
//    public void refreshUserToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        log.info("Refreshing token");
//        String authorizationHeader = request.getHeader(AUTHORIZATION);
//
//        if(authorizationHeader != null && authorizationHeader.startsWith( jwtUtil.bearer)) {
//            try {
//                String refresh_token = authorizationHeader.substring(jwtUtil.bearer.length());
//
//                DecodedJWT decodedJWT = jwtUtil.verify(refresh_token);
//                String nomeUsuario = decodedJWT.getSubject();
//                Usuario usuario = usuarioService.findUsuarioByNomeUsuario(nomeUsuario);
//
//                String accessToken = jwtUtil.generateAccessToken(usuario.getNomeUsuario(), usuario.getPermissoes().stream().map(Permissao::getNome).collect(Collectors.toList()));
//
//                jwtUtil.writeAccessAndRefreshTokenToBodyOfResponse(accessToken, refresh_token, response);
//
//            }catch (UsuarioNotFoundException unf) {
//                log.error("Usuario not found");
//                response.setHeader("error", "Usuario not found");
//                response.setStatus(BAD_REQUEST.value());
//                Map<String, String> error = new HashMap<>();
//                error.put("error_message", unf.getMessage());
//                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//                new ObjectMapper().writeValue(response.getOutputStream(), error);
//            }catch (Exception e) {
//                log.error("Erro loggnig in {}:", e.getMessage());
//                response.setHeader("error", e.getMessage());
//                response.setStatus(FORBIDDEN.value());
//                //response.sendError(FORBIDDEN.value());
//                Map<String, String> error = new HashMap<>();
//                error.put("error_message", e.getMessage());
//                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//                new ObjectMapper().writeValue(response.getOutputStream(), error);
//            }
//        }
//    }
}
