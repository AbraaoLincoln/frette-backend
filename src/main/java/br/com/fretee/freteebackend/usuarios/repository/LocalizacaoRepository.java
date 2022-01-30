package br.com.fretee.freteebackend.usuarios.repository;

import br.com.fretee.freteebackend.usuarios.entity.Localizacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalizacaoRepository extends JpaRepository<Localizacao, Integer> {
}
