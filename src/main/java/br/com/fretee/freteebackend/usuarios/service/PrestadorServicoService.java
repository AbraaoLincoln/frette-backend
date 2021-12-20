package br.com.fretee.freteebackend.usuarios.service;

import br.com.fretee.freteebackend.exceptions.UsuarioNotFindException;
import br.com.fretee.freteebackend.usuarios.dto.PrestadorServicoDTO;
import br.com.fretee.freteebackend.usuarios.dto.VeiculoDTO;
import br.com.fretee.freteebackend.usuarios.entity.Veiculo;
import br.com.fretee.freteebackend.usuarios.repository.PrestadorServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

public class PrestadorServicoService {
    @Autowired
    private PrestadorServicoRepository prestadorServicoRepository;
    @Autowired
    private VeiculoService veiculoService;
    @Autowired
    private UsuarioService usuarioService;

    public void cadastraUsuarioComoPrestadorServico(PrestadorServicoDTO prestadorServicoDTO, VeiculoDTO veiculoDTO, MultipartFile fotoVeiculo) throws UsuarioNotFindException {

        usuarioService.findUsuarioById(prestadorServicoDTO.getUsuarioId());
        var veiculoId = veiculoService.cadastrarVeiculo(veiculoDTO.toVeiculo(), fotoVeiculo);
        var prestadorServico = prestadorServicoDTO.toPrestadorService();
        prestadorServico.setVeiculo( new Veiculo());
        prestadorServico.getVeiculo().setId(veiculoId);
        prestadorServicoRepository.save(prestadorServico);
    }
}
