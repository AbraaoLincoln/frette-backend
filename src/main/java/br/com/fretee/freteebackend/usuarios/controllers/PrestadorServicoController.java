package br.com.fretee.freteebackend.usuarios.controllers;

import br.com.fretee.freteebackend.exceptions.PrestadorServicoNotFoundException;
import br.com.fretee.freteebackend.exceptions.UsuarioAlreadyJoinAsPrestadorServico;
import br.com.fretee.freteebackend.exceptions.UsuarioNotFoundException;
import br.com.fretee.freteebackend.exceptions.VeiculoNotFoundException;
import br.com.fretee.freteebackend.usuarios.dto.NovoPrestadorServico;
import br.com.fretee.freteebackend.usuarios.dto.PrestadorServicoDTO;
import br.com.fretee.freteebackend.usuarios.dto.VeiculoDTO;
import br.com.fretee.freteebackend.usuarios.entity.Localizacao;
import br.com.fretee.freteebackend.usuarios.entity.PrestadorServico;
import br.com.fretee.freteebackend.usuarios.service.ImagemVeiculoService;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/prestador-servico")
@Slf4j
public class PrestadorServicoController {
    @Autowired
    private PrestadorServicoService prestadorServicoService;
    @Autowired
    private VeiculoService veiculoService;

    @PostMapping
    public ResponseEntity cadastrarPrestadorServico(Principal principal, NovoPrestadorServico prestadorServicoDTO, VeiculoDTO veiculoDTO, MultipartFile fotoVeiculo) {

        try{
            prestadorServicoService.cadastraUsuarioComoPrestadorServico(principal, prestadorServicoDTO, veiculoDTO, fotoVeiculo);
            return ResponseEntity.created(new URI("/api/prestador-servico")).build();
        }catch (UsuarioNotFoundException unfe) {
            log.error("Usuario {} nao encontrado", principal.getName());
            return ResponseEntity.badRequest().build();
        } catch (UsuarioAlreadyJoinAsPrestadorServico e) {
            log.error("Usuario {} já esta cadastrado(a) como prestador de servico", principal.getName());
            return ResponseEntity.badRequest().build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/info")
    public ResponseEntity<PrestadorServicoDTO> getPrestadorServicoInfo(Principal principal) {
        try{
            return ResponseEntity.ok().body(prestadorServicoService.getPrestadorServicoInfo(principal));
        } catch (PrestadorServicoNotFoundException e) {
            log.error("Prestador de servico não encontrado: {}", principal.getName());
            return ResponseEntity.badRequest().build();
        } catch (UsuarioNotFoundException e) {
            log.error("Usuario não encontrado: {}", principal.getName());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{nomeUsuario}/foto")
    public ResponseEntity getFotoUsurio(HttpServletResponse response, @PathVariable String nomeUsuario) {
        try{
            prestadorServicoService.getFoto(response, nomeUsuario);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        } catch (UsuarioNotFoundException e) {
            log.error("Usuario não encontrado: {}", nomeUsuario);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{prestadorServicoNome}/veiculo/foto")
    public ResponseEntity getFotoVeiculo(HttpServletResponse response, @PathVariable String prestadorServicoNome) {
        try{
            prestadorServicoService.getFotoVeiculo(response, prestadorServicoNome);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        } catch (UsuarioNotFoundException e) {
            log.error("Usuario {} não encontrado", prestadorServicoNome);
            return ResponseEntity.badRequest().build();
        } catch (PrestadorServicoNotFoundException e) {
            log.error("Prestador de servico {} não encontrado", prestadorServicoNome);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{prestadorServicoNome}/veiculo/info")
    public ResponseEntity<VeiculoDTO> getVeiculoInfo(@PathVariable String prestadorServicoNome) {
        try{
            VeiculoDTO veiculoDTO = prestadorServicoService.getVeiculoInfo(prestadorServicoNome);
            return ResponseEntity.ok().body(veiculoDTO);
        }  catch (UsuarioNotFoundException e) {
            log.error("Usuario {} não encontrado", prestadorServicoNome);
            return ResponseEntity.badRequest().build();
        }  catch (VeiculoNotFoundException e) {
            log.error("Veiculo do suario {} não encontrado", prestadorServicoNome);
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/proximos/{latitude}/{longitude}")
    public ResponseEntity<List<PrestadorServicoDTO>> getPrestadoreDeServicoProximo(@PathVariable double latitude, @PathVariable double longitude, Principal principal) {
        Localizacao localizacao = new Localizacao();
        localizacao.setLatitude(latitude);
        localizacao.setLongitude(longitude);
        List<PrestadorServicoDTO> prestadorServicoDTOS = prestadorServicoService.getPrestadoreDeServicoProximo(localizacao, principal);
        return ResponseEntity.ok().body(prestadorServicoDTOS);
    }
}
