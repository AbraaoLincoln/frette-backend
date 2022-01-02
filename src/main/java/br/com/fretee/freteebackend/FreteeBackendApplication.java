package br.com.fretee.freteebackend;

import br.com.fretee.freteebackend.usuarios.entity.Permissao;
import br.com.fretee.freteebackend.usuarios.entity.Usuario;
import br.com.fretee.freteebackend.usuarios.enums.Permissoes;
import br.com.fretee.freteebackend.usuarios.service.PermissaoService;
import br.com.fretee.freteebackend.usuarios.service.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class FreteeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FreteeBackendApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(PermissaoService permissaoService) {
		return args -> {
			permissaoService.salvarPermissao(Permissoes.ADMIN.toString());
			permissaoService.salvarPermissao(Permissoes.USUARIO.toString());
			permissaoService.salvarPermissao(Permissoes.PRESTADOR_SERVICO.toString());
		};
	}

	@Bean
	CommandLineRunner run1(UsuarioService usuarioService) {
		return args -> {
			Usuario usuario = new Usuario();
			usuario.setNome("Teste");
			usuario.setNomeUsuario("Teste123");
			usuario.setSenha("123");
			Permissao permissao = new Permissao();
			permissao.setId(Permissoes.USUARIO.getValue());
			usuario.setPermissoes(new ArrayList<>());
			usuario.getPermissoes().add(permissao);
			usuarioService.saveUsurio(usuario);
		};
	}
}
