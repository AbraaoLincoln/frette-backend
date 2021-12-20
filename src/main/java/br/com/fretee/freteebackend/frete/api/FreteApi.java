package br.com.fretee.freteebackend.frete.api;

import br.com.fretee.freteebackend.frete.dto.FreteDTO;

import java.util.List;

public interface FreteApi {

    public List<FreteDTO> getFretesByContratanteId(int contratanteId);

    public List<FreteDTO> getFretesByPrestadorDeServicoId(int prestadorServicoId);
}
