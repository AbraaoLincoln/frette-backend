package br.com.fretee.freteebackend.usuarios.controllers;

import br.com.fretee.freteebackend.exceptions.UsuarioNotFindException;
import br.com.fretee.freteebackend.usuarios.dto.PrestadorServicoDTO;
import br.com.fretee.freteebackend.usuarios.dto.VeiculoDTO;
import br.com.fretee.freteebackend.usuarios.service.PrestadorServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("fretee/api/prestador-servico")
public class PrestadorServicoController {
    @Autowired
    private PrestadorServicoService prestadorServicoService;

    @PostMapping
    public ResponseEntity cadastrarPrestadorServico(HttpServletResponse response , PrestadorServicoDTO prestadorServicoDTO, VeiculoDTO veiculoDTO, MultipartFile fotoVeiculo) {
        System.out.println("cadastrando prestador de servico");

        try{
            prestadorServicoService.cadastraUsuarioComoPrestadorServico(prestadorServicoDTO, veiculoDTO, fotoVeiculo);
            response.setStatus(HttpStatus.CREATED.value());
        }catch (UsuarioNotFindException unfe) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }

        return new ResponseEntity(null);
    }
}
