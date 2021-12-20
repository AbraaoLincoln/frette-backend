package br.com.fretee.freteebackend.autenticacao.repository;

import br.com.fretee.freteebackend.autenticacao.entity.Credenciais;
import org.springframework.data.repository.CrudRepository;

public interface CredenciasRepository extends CrudRepository<Credenciais, Integer> {
}
