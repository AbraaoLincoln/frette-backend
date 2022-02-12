package br.com.fretee.freteebackend.usuarios.controllers;

import br.com.fretee.freteebackend.usuarios.exceptions.*;
import br.com.fretee.freteebackend.usuarios.dto.NovoPrestadorServico;
import br.com.fretee.freteebackend.usuarios.dto.PrestadorServicoDTO;
import br.com.fretee.freteebackend.usuarios.dto.VeiculoDTO;
import br.com.fretee.freteebackend.usuarios.entity.Localizacao;
import br.com.fretee.freteebackend.usuarios.service.PrestadorServicoService;
import br.com.fretee.freteebackend.usuarios.service.VeiculoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/prestador-servico")
@Slf4j
public class PrestadorServicoController {
    @Autowired
    private PrestadorServicoService prestadorServicoService;
    @Autowired
    private VeiculoService veiculoService;

    @PostMapping
    public ResponseEntity cadastrarPrestadorServico(Principal principal, NovoPrestadorServico prestadorServicoDTO, VeiculoDTO veiculoDTO, @RequestParam MultipartFile fotoVeiculo) throws UsuarioNotFoundException, UsuarioAlreadyJoinAsPrestadorServico, URISyntaxException {
        prestadorServicoService.cadastraUsuarioComoPrestadorServico(principal, prestadorServicoDTO, veiculoDTO, fotoVeiculo);
        return ResponseEntity.created(new URI("/api/prestador-servico")).build();
    }

    @GetMapping("/info")
    public ResponseEntity<PrestadorServicoDTO> getPrestadorServicoInfo(Principal principal) throws PrestadorServicoNotFoundException, UsuarioNotFoundException {
        return ResponseEntity.ok().body(prestadorServicoService.getPrestadorServicoInfo(principal));
    }

    @GetMapping("/{nomeUsuario}/foto")
    public ResponseEntity getFotoUsurio(HttpServletResponse response, @PathVariable String nomeUsuario) throws IOException, UsuarioNotFoundException {
        prestadorServicoService.getFoto(response, nomeUsuario);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{prestadorServicoNome}/veiculo/foto")
    public ResponseEntity getFotoVeiculo(HttpServletResponse response, @PathVariable String prestadorServicoNome) throws PrestadorServicoNotFoundException, IOException, UsuarioNotFoundException {
        prestadorServicoService.getFotoVeiculo(response, prestadorServicoNome);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{prestadorServicoNome}/veiculo/info")
    public ResponseEntity<VeiculoDTO> getVeiculoInfo(@PathVariable String prestadorServicoNome) throws VeiculoNotFoundException, UsuarioNotFoundException {
        VeiculoDTO veiculoDTO = prestadorServicoService.getVeiculoInfo(prestadorServicoNome);
        return ResponseEntity.ok().body(veiculoDTO);
    }

    @GetMapping("/proximos/{latitude}/{longitude}")
    public ResponseEntity<List<PrestadorServicoDTO>> getPrestadoreDeServicoProximo(@PathVariable double latitude, @PathVariable double longitude, Principal principal) {
        Localizacao localizacao = new Localizacao();
        localizacao.setLatitude(latitude);
        localizacao.setLongitude(longitude);
        List<PrestadorServicoDTO> prestadorServicoDTOS = prestadorServicoService.getPrestadoreDeServicoProximo(localizacao, principal);
        return ResponseEntity.ok().body(prestadorServicoDTOS);
    }

    @PutMapping("/veiculo/{veiculoId}")
    public ResponseEntity atualizarVeiculoInfo(Principal principal, @PathVariable int veiculoId, VeiculoDTO veiculoDTO, Optional<MultipartFile> fotoVeiculo) throws PrestadorServicoNotFoundException, UsuarioNaoEDonoDoVeiculoException, VeiculoNotFoundException {
        prestadorServicoService.atualizarVeiculoInfo(principal, veiculoId, veiculoDTO, fotoVeiculo.orElseGet(() -> null));
        return ResponseEntity.ok().build();
    }
}
