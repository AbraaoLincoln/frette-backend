package br.com.fretee.freteebackend.frete.repository;

import br.com.fretee.freteebackend.frete.dto.SolicitacaoServicoDTO;
import br.com.fretee.freteebackend.frete.entity.Frete;
import br.com.fretee.freteebackend.frete.enums.StatusFrete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FreteRepository extends JpaRepository<Frete, Integer> {
    @Query(value = "SELECT * FROM frete.fretes WHERE contratante_id = :id or prestador_servico_id = :id ORDER BY data_atualizacao DESC", nativeQuery = true)
    public List<Frete> findByContratanteIdOrPrestadorServicoId(int id);

    @Query(value = "SELECT * FROM frete.fretes WHERE contratante_id = :id or prestador_servico_id = :id ORDER BY data_atualizacao DESC", nativeQuery = true)
    public Optional<List<Frete>> findFreteByContratanteIdOrPrestadorServicoId(int id);

    @Query(value = "SELECT * FROM frete.fretes WHERE (contratante_id = :id or prestador_servico_id = :id) and status = :status1 ORDER BY data, hora", nativeQuery = true)
    public List<Frete> findByContratanteIdOrPrestadorServicoIdAndStatus(int id, String status1);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM frete.fretes inner join usuario.usuario on fretes.contratante_id = usuario.id WHERE usuario.nome_usuario = :nomeUsuario and fretes.id = :freteId)", nativeQuery = true)
    public boolean existsFreteByIdQueUsuarioEContratante(String nomeUsuario, int freteId);
}
