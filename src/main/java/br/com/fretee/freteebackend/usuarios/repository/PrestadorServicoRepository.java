package br.com.fretee.freteebackend.usuarios.repository;

import br.com.fretee.freteebackend.usuarios.entity.PrestadorServico;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Optional;

public interface PrestadorServicoRepository extends CrudRepository<PrestadorServico, Integer> {
    public Optional<PrestadorServico> findByUsuarioId(int usuarioId);

    @Query(value = "select * from usuario.usuario u inner join usuario.prestador_servico ps on u.id = ps.usuario_id where nome_usuario = :nomeUsuario", nativeQuery = true)
    public Optional<PrestadorServico> findByNomeUsuario(String nomeUsuario);

    @Modifying
    @Transactional
    @Query(value = "update usuario.prestador_servico set soma_notas_avaliacao = soma_notas_avaliacao + :nota, numero_de_fretes_realizados = numero_de_fretes_realizados + 1 where usuario_id = :usuarioId", nativeQuery = true)
    public void atualizarSomaDasNotasEFretesRealizados(int usuarioId, float nota);
}
