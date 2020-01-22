package com.joaopauloschmitz.algafoodapi.domain.repository;

import com.joaopauloschmitz.algafoodapi.domain.model.FormaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Long> {

}
