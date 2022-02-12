package br.com.fretee.freteebackend.usuarios.service;

import br.com.fretee.freteebackend.usuarios.exceptions.VeiculoNotFoundException;
import br.com.fretee.freteebackend.usuarios.dto.VeiculoDTO;
import br.com.fretee.freteebackend.usuarios.entity.PrestadorServico;
import br.com.fretee.freteebackend.usuarios.entity.Veiculo;
import br.com.fretee.freteebackend.usuarios.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class VeiculoService {
    @Autowired
    private VeiculoRepository veiculoRepository;
    @Autowired
    private ImagemVeiculoService imagemService;

    public int cadastrarVeiculo(Veiculo veiculo, MultipartFile foto) {
        //TODO: validar veiculo
        var imagemId = imagemService.saveImage(foto);
        veiculo.setFoto(imagemId);

        return veiculoRepository.save(veiculo).getId();
    }

    public Veiculo findVeiculoById(int veiculoId) throws VeiculoNotFoundException {
        Optional<Veiculo> veiculoOptional = veiculoRepository.findById(veiculoId);
        if(veiculoOptional.isEmpty()) throw new VeiculoNotFoundException("Veiculo de id = " + veiculoId + "nao encontrado");
        return veiculoOptional.get();
    }

    public VeiculoDTO getVeiculoInfoDoPrestadorServico(PrestadorServico prestadorServico) throws VeiculoNotFoundException {
        Veiculo veiculo = findVeiculoById(prestadorServico.getVeiculo().getId());

        return new VeiculoDTO(veiculo.getLargura(), veiculo.getAltura(), veiculo.getComprimento(),
                              veiculo.getFoto(), veiculo.getId(), veiculo.getPlaca());
    }

    public Veiculo atualizarVeiculoInfo(int veiculoId, VeiculoDTO veiculoDTO, MultipartFile fotoVeiculo) throws VeiculoNotFoundException {
        Veiculo veiculo = findVeiculoById(veiculoId);

        if(veiculoDTO.getAltura() != null) {
            veiculo.setAltura(veiculoDTO.getAltura());
        }

        if(veiculoDTO.getLargura() != null) {
            veiculo.setLargura(veiculoDTO.getLargura());
        }

        if(veiculoDTO.getComprimento() != null) {
            veiculo.setComprimento(veiculoDTO.getComprimento());
        }

        if(veiculoDTO.getPlaca() != null && !veiculoDTO.getPlaca().isEmpty()) {
            veiculo.setPlaca(veiculoDTO.getPlaca());
        }

        if(fotoVeiculo != null) {
            String fotoVeiculoId = veiculo.getFoto();
            veiculo.setFoto(imagemService.saveImage(fotoVeiculo));
            imagemService.deleteImagem(fotoVeiculoId);
        }

        return veiculoRepository.save(veiculo);
    }
}
