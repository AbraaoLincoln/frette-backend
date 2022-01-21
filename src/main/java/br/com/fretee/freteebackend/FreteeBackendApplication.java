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
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
	public FirebaseMessaging getFirebaseMessaging(@Value("${firebase.serviceAccount.path}") String pathServiceAccount) {
		try {
			FileInputStream serviceAccount = new FileInputStream(pathServiceAccount);
			FirebaseOptions options = FirebaseOptions.builder()
										  .setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
			FirebaseApp firebaseApp = FirebaseApp.initializeApp(options);
			return FirebaseMessaging.getInstance(firebaseApp);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
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
		String[] args1 = {"Gustavo Luan Felipe Peixoto", "gustavo", "(84) 99658-8429","male_avatar", "veiculo1"};
		String[] args2 = {"Thales Diego Severino Fogaca", "thales", "(84) 98206-3797", "male_avatar", "veiculo2"};
		String[] args3 = {"Gabriel Juan Ricardo Cardoso", "gabriel", "(84) 99819-9982", "male_avatar", "veiculo3"};
		String[] args4 = {"Marcia Vanessa Rita Monteiro", "marcia", "(84) 99801-0804", "female_avatar", "veiculo4"};
		String[] args5 = {"Nicolas Enzo da Mata", "nicolas", "(84) 99659-9712", "male_avatar", "veiculo5"};
		String[] userTest = {"Teste da Silva", "teste", "(84) 99660-9717", "female_avatar", "veiculo5"};
		float[] args6 = {4.5F, 0, 3.4F, 5, 4.1F};

		List<String[]> usuarios = new ArrayList<>();
		usuarios.add(args1);
		usuarios.add(args2);
		usuarios.add(args3);
		usuarios.add(args4);
		usuarios.add(args5);

		return args -> {
			//double lat = Math.random();
			//double log = Math.random();
			double lat = 37.5219983;
			double log = -122.184;

			int index = 0;

			for(String[] usuarioInfo : usuarios) {

				Usuario usuario = new Usuario();
				usuario.setNomeCompleto(usuarioInfo[0]);
				usuario.setNomeUsuario(usuarioInfo[1]);
				usuario.setSenha("123");
				usuario.setTelefone(usuarioInfo[2]);
				usuario.setFoto(usuarioInfo[3]);
				usuario.setReputacao(args6[index]);
				usuario.setPermissoes(new ArrayList<>());
				Permissao permissaoUsuario = new Permissao();
				permissaoUsuario.setId(Permissoes.USUARIO.getValue());
				usuario.getPermissoes().add(permissaoUsuario);
				Permissao permissaoPrestadorServico = new Permissao();
				permissaoUsuario.setId(Permissoes.PRESTADOR_SERVICO.getValue());
				usuario.getPermissoes().add(permissaoPrestadorServico);
				usuarioService.addUsuarioTeste(usuario);

				Veiculo veiculo = new Veiculo();
				veiculo.setFoto(usuarioInfo[4]);
				veiculo.setAltura(10);
				veiculo.setComprimento(100);
				veiculo.setLargura(50);
				veiculo = veiculoRepository.save(veiculo);

				PrestadorServico prestadorServico = new PrestadorServico();
				prestadorServico.setVeiculo(veiculo);
				prestadorServico.setUsuario(usuario);
				prestadorServico.setLongitude(log);
				prestadorServico.setLatitude(lat);
				prestadorServicoRepository.save(prestadorServico);

				lat += Math.random();
				log += Math.random();
				index++;
			}

			Usuario usuario = new Usuario();
			usuario.setNomeCompleto(userTest[0]);
			usuario.setNomeUsuario(userTest[1]);
			usuario.setSenha("123");
			usuario.setTelefone(userTest[2]);
			usuario.setFoto(userTest[3]);
			usuario.setReputacao(4.2f);
			usuario.setPermissoes(new ArrayList<>());
			Permissao permissaoUsuario = new Permissao();
			permissaoUsuario.setId(Permissoes.USUARIO.getValue());
			usuario.getPermissoes().add(permissaoUsuario);
			usuarioService.addUsuarioTeste(usuario);
		};
	}
}
