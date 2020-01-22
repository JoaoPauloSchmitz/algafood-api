package com.joaopauloschmitz.algafoodapi.domain.repository;

import com.joaopauloschmitz.algafoodapi.domain.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {

}
