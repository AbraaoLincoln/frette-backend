package br.com.fretee.freteebackend.frete.service;

import br.com.fretee.freteebackend.frete.api.FreteApi;
import br.com.fretee.freteebackend.frete.dto.FreteDTO;

import java.util.List;

public class FreteService implements FreteApi {
    @Override
    public List<FreteDTO> getFretesByContratanteId(int contratanteId) {
        return null;
    }

    @Override
    public List<FreteDTO> getFretesByPrestadorDeServicoId(int prestadorServicoId) {
        return null;
    }
}
