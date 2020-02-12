package com.joaopauloschmitz.algafoodapi.infrastructure.repository;

import com.joaopauloschmitz.algafoodapi.domain.model.FotoProduto;
import com.joaopauloschmitz.algafoodapi.domain.repository.ProdutoRepositoryQueries;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ProdutoRepositoryImpl implements ProdutoRepositoryQueries {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public FotoProduto save(FotoProduto fotoProduto) {
        return this.entityManager.merge(fotoProduto);
    }

    @Override
    public void delete(FotoProduto fotoProduto) {
        this.entityManager.remove(fotoProduto);
    }
}
