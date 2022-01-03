package br.com.fretee.freteebackend.usuarios.repository;

import br.com.fretee.freteebackend.usuarios.entity.PrestadorServico;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PrestadorServicoRepository extends CrudRepository<PrestadorServico, Integer> {
    public Optional<PrestadorServico> findByUsuarioId(int usuarioId);
}
