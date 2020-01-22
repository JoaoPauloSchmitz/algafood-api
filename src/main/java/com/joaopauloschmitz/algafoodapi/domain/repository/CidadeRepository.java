package com.joaopauloschmitz.algafoodapi.domain.repository;

import com.joaopauloschmitz.algafoodapi.domain.model.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> {

    List<Cidade> findByNomeContaining(String nome);
    Optional<Cidade> findByNome(String nome);
    boolean existsByNome(String nome);

}
