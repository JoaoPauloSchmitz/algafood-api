package com.joaopauloschmitz.algafoodapi.domain.repository;

import com.joaopauloschmitz.algafoodapi.domain.model.Cozinha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CozinhaRepository extends JpaRepository<Cozinha, Long> {

}
