package com.joaopauloschmitz.algafoodapi.domain.repository;

import com.joaopauloschmitz.algafoodapi.domain.model.Restaurante;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface RestauranteRepository extends CustomJpaRepository<Restaurante, Long>, RestauranteRepositoryQueries
                                                , JpaSpecificationExecutor<Restaurante> {

//    @Query("from Restaurante r join fetch r.cozinha left join fetch r.formasPagamento")
    @Query("from Restaurante r join fetch r.cozinha")
    List<Restaurante> findAll();

    List<Restaurante> findByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal taxaFinal);

    List<Restaurante> findPorNomeCozinhaId(String nome, Long id);

//    List<Restaurante> findByNomeContainingAndCozinhaId(String nome, Long id);

    Optional<Restaurante> findFirstByNomeContaining(String nome);

    List<Restaurante> findTop2ByNomeContaining(String nome);

    int countByCozinhaId(Long id);

    boolean existsResponsavel(Long restauranteId, Long usuarioId);

}
