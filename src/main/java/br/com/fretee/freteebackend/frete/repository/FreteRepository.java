package br.com.fretee.freteebackend.frete.repository;

import br.com.fretee.freteebackend.frete.entity.Frete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FreteRepository extends JpaRepository<Frete, Integer> {
    @Query(value = "SELECT * FROM frete.fretes WHERE contratante_id = :id or prestador_servico_id = :id", nativeQuery = true)
    public List<Frete> findByContratanteIdOrPrestadorServicoId(int id);
}
