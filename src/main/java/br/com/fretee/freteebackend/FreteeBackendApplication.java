package br.com.fretee.freteebackend;

import br.com.fretee.freteebackend.configuration.JwtUtil;
import br.com.fretee.freteebackend.usuarios.entity.Permissao;
import br.com.fretee.freteebackend.usuarios.entity.PrestadorServico;
import br.com.fretee.freteebackend.usuarios.entity.Usuario;
import br.com.fretee.freteebackend.usuarios.entity.Veiculo;
import br.com.fretee.freteebackend.usuarios.enums.Permissoes;
import br.com.fretee.freteebackend.usuarios.repository.PrestadorServicoRepository;
import br.com.fretee.freteebackend.usuarios.repository.VeiculoRepository;
import br.com.fretee.freteebackend.usuarios.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

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
	public JwtUtil JwtUtil() {
		return new JwtUtil();
	}

	@Bean
	public ImagemUsuarioService imagemUsuarioService(@Value("${fotos.usuarios.diretorio}") String path) {
		return new ImagemUsuarioService(path);
	}

	@Bean
	public ImagemVeiculoService imagemVeiculoService(@Value("${fotos.veiculos.diretorio}") String path) {
		return new ImagemVeiculoService(path);
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
	CommandLineRunner run1(UsuarioService usuarioService, PrestadorServicoRepository prestadorServicoRepository, VeiculoRepository veiculoRepository) {
		String[] args1 = {"Gustavo Luan Felipe Peixoto", "gustavo", "(84) 99658-8429", "male_avatar", "veiculo1"};
		String[] args2 = {"Thales Diego Severino Fogaça", "thales", "(84) 98206-3797", "male_avatar", "veiculo2"};
		String[] args3 = {"Gabriel Juan Ricardo Cardoso", "gabriel", "(84) 99819-9982", "male_avatar", "veiculo3"};
		String[] args4 = {"Márcia Vanessa Rita Monteiro", "marcia", "(84) 99801-0804", "female_avatar", "veiculo4"};
		String[] args5 = {"Nicolas Enzo da Mata", "nicolas", "(84) 99659-9712", "male_avatar", "veiculo5"};

		List<String[]> usuarios = new ArrayList<>();
		usuarios.add(args1);
		usuarios.add(args2);
		usuarios.add(args3);
		usuarios.add(args4);
		usuarios.add(args5);


		return args -> {
			for(String[] usuarioInfo : usuarios) {
				Usuario usuario = new Usuario();
				usuario.setNomeCompleto(usuarioInfo[0]);
				usuario.setNomeUsuario(usuarioInfo[1]);
				usuario.setSenha("123");
				usuario.setTelefone(usuarioInfo[2]);
				usuario.setFoto(usuarioInfo[3]);
				usuario.setPermissoes(new ArrayList<>());
				Permissao permissaoUsuario = new Permissao();
				permissaoUsuario.setId(Permissoes.USUARIO.getValue());
				usuario.getPermissoes().add(permissaoUsuario);
				Permissao permissaoPrestadorServico = new Permissao();
				permissaoUsuario.setId(Permissoes.PRESTADOR_SERVICO.getValue());
				usuario.getPermissoes().add(permissaoPrestadorServico);
				usuarioService.addUsuario(usuario);

				Veiculo veiculo = new Veiculo();
				veiculo.setFoto(usuarioInfo[4]);
				veiculo.setAltura(10);
				veiculo.setComprimento(100);
				veiculo.setLargura(50);
				veiculo = veiculoRepository.save(veiculo);

				PrestadorServico prestadorServico = new PrestadorServico();
				prestadorServico.setVeiculo(veiculo);
				prestadorServico.setUsuario(usuario);
				prestadorServico.setLongitude(50);
				prestadorServico.setLatitude(100);
				prestadorServicoRepository.save(prestadorServico);
			}
		};
	}
}
