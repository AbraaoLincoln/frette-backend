package br.com.fretee.freteebackend.usuarios.service;

import br.com.fretee.freteebackend.exceptions.PrestadorServicoNotFoundException;
import br.com.fretee.freteebackend.exceptions.UsuarioNotFoundException;
import br.com.fretee.freteebackend.usuarios.dto.NovoPrestadorServico;
import br.com.fretee.freteebackend.usuarios.dto.PrestadorServicoDTO;
import br.com.fretee.freteebackend.usuarios.dto.VeiculoDTO;
import br.com.fretee.freteebackend.usuarios.entity.Localizacao;
import br.com.fretee.freteebackend.usuarios.entity.PrestadorServico;
import br.com.fretee.freteebackend.usuarios.entity.Usuario;
import br.com.fretee.freteebackend.usuarios.entity.Veiculo;
import br.com.fretee.freteebackend.usuarios.repository.PrestadorServicoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Optional;

@Service
@Slf4j
public class PrestadorServicoService {
    @Autowired
    private PrestadorServicoRepository prestadorServicoRepository;
    @Autowired
    private VeiculoService veiculoService;
    @Autowired
    private UsuarioService usuarioService;

    public void cadastraUsuarioComoPrestadorServico(Principal principal, NovoPrestadorServico novoPrestadorServico, VeiculoDTO veiculoDTO, MultipartFile fotoVeiculo) throws UsuarioNotFoundException {
        log.info("Cadastrando novo prestador de servico: {}", principal.getName());

        Usuario usuario = usuarioService.findUsuarioByNomeUsuario(principal.getName());
        var veiculoId = veiculoService.cadastrarVeiculo(veiculoDTO.toVeiculo(), fotoVeiculo);
        var prestadorServico = novoPrestadorServico.toPrestadorService(usuario);
        prestadorServico.setVeiculo( new Veiculo());
        prestadorServico.getVeiculo().setId(veiculoId);
        prestadorServicoRepository.save(prestadorServico);
    }

    public PrestadorServicoDTO getPrestadorServicoInfo(Principal principal) throws UsuarioNotFoundException, PrestadorServicoNotFoundException {
        Usuario usuario = usuarioService.findUsuarioByNomeUsuario(principal.getName());
        PrestadorServico prestadorServico = findByUsuarioId(usuario.getId());

        return new PrestadorServicoDTO(usuario, prestadorServico);
    }

    public PrestadorServico findByUsuarioId(int usuarioId) throws PrestadorServicoNotFoundException {
        Optional<PrestadorServico> prestadorServicoOptional = prestadorServicoRepository.findByUsuarioId(usuarioId);
        if(prestadorServicoOptional.isEmpty()) throw new PrestadorServicoNotFoundException();
        return prestadorServicoOptional.get();
    }

    public void atualizarLocalizacao(Principal principal, Localizacao localizacao) throws UsuarioNotFoundException, PrestadorServicoNotFoundException {
        Usuario usuario = usuarioService.findUsuarioByNomeUsuario(principal.getName());
        PrestadorServico prestadorServico = findByUsuarioId(usuario.getId());
        prestadorServico.setLatitude(localizacao.getLatitude());
        prestadorServico.setLongitude(localizacao.getLongitude());
        prestadorServicoRepository.save(prestadorServico);
    }
}
