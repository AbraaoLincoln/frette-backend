package br.com.fretee.freteebackend.frete.repository;

import br.com.fretee.freteebackend.frete.dto.SolicitacaoServicoDTO;
import br.com.fretee.freteebackend.frete.entity.Frete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FreteRepository extends JpaRepository<Frete, Integer> {
    @Query(value = "SELECT * FROM frete.fretes WHERE contratante_id = :id or prestador_servico_id = :id ORDER BY data_atualizacao DESC", nativeQuery = true)
    public List<Frete> findByContratanteIdOrPrestadorServicoId(int id);

    @Query(value = "SELECT * FROM frete.fretes WHERE contratante_id = :id or prestador_servico_id = :id ORDER BY data_atualizacao DESC", nativeQuery = true)
    public Optional<List<Frete>> findFreteStatusAndIdByContratanteIdOrPrestadorServicoId(int id);
}
