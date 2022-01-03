package br.com.fretee.freteebackend.frete.service;

import br.com.fretee.freteebackend.frete.api.FreteApi;
import br.com.fretee.freteebackend.frete.dto.FreteDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FreteService implements FreteApi {
    @Override
    public List<FreteDTO> getFretesByContratanteId(int contratanteId) {
        return null;
    }

    @Override
    public List<FreteDTO> getFretesByPrestadorDeServicoId(int prestadorServicoId) {
        return null;
    }

    @Override
    public int getNumeroDeFretesRealizados(int usuarioId) {
        return 0;
    }
}
