package br.com.fretee.freteebackend.usuarios.repository;

import br.com.fretee.freteebackend.usuarios.entity.Permissao;
import org.springframework.data.repository.CrudRepository;

public interface PermissaoRepository extends CrudRepository<Permissao, InternalError> {

}
