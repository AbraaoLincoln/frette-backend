package br.com.fretee.freteebackend.usuarios.repository;

import br.com.fretee.freteebackend.usuarios.entity.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {
    public Optional<Usuario> findByNomeUsuario(String nomeUsuario);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM usuario.usuario WHERE nome_usuario = ?1);", nativeQuery = true)
    public boolean verificarSeNomeUsuarioJaEstaEmUso(String nomeUsuario);

    @Transactional
    @Query(value = "UPDATE usuario SET firebase_token = :token WHERE id = :id", nativeQuery = true)
    public void atualizarFirebaseToken(int id, String token);
}
