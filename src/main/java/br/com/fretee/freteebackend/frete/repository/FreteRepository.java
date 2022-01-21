package br.com.fretee.freteebackend.frete.repository;

import br.com.fretee.freteebackend.frete.entity.Frete;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreteRepository extends JpaRepository<Frete, Integer> {
}
