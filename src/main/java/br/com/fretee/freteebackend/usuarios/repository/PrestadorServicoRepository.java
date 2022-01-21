package br.com.fretee.freteebackend.usuarios.repository;

import br.com.fretee.freteebackend.usuarios.entity.PrestadorServico;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PrestadorServicoRepository extends CrudRepository<PrestadorServico, Integer> {
    public Optional<PrestadorServico> findByUsuarioId(int usuarioId);

    @Query(value = "select * from usuario.usuario u inner join usuario.prestador_servico ps on u.id = ps.usuario_id where nome_usuario = :nomeUsuario", nativeQuery = true)
    public Optional<PrestadorServico> findByNomeUsuario(String nomeUsuario);
}
