package br.com.fretee.freteebackend.usuarios.service;

import br.com.fretee.freteebackend.usuarios.entity.Veiculo;
import br.com.fretee.freteebackend.usuarios.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VeiculoService {
    @Autowired
    private VeiculoRepository veiculoRepository;
    @Autowired
    private ImagemVeiculoService imagemService;

    public int cadastrarVeiculo(Veiculo veiculo, MultipartFile foto) {
        var imagemId = imagemService.saveImage(foto);
        veiculo.setFoto(imagemId);

        return veiculoRepository.save(veiculo).getId();
    }
}
