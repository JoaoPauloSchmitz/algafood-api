package com.joaopauloschmitz.algafoodapi.infrastructure.service.query;

import com.joaopauloschmitz.algafoodapi.domain.filter.VendaDiariaFilter;
import com.joaopauloschmitz.algafoodapi.domain.model.Pedido;
import com.joaopauloschmitz.algafoodapi.domain.model.StatusPedido;
import com.joaopauloschmitz.algafoodapi.domain.model.dto.VendaDiaria;
import com.joaopauloschmitz.algafoodapi.domain.service.VendaQueryService;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class VendaQueryServiceImpl implements VendaQueryService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter vendaDiariaFilter,
                                                    String timeOffset) {

        var builder = this.entityManager.getCriteriaBuilder();
        var query = builder.createQuery(VendaDiaria.class);
        var root = query.from(Pedido.class);
        var predicates = new ArrayList<Predicate>();

        var functionConvertTzDataCriacao = builder.function(
                "convert_tz", Date.class, root.get("dataCriacao"),
                builder.literal("+00:00"), builder.literal(timeOffset)
        );

        var functionDateDataCriacao = builder.function(
                "date", Date.class, functionConvertTzDataCriacao);

        var selection = builder.construct(VendaDiaria.class,
                functionDateDataCriacao,
                builder.count(root.get("id")),
                builder.sum(root.get("valorTotal")));

        if (vendaDiariaFilter.getRestauranteId() != null) {
            predicates.add(builder.equal(root.get("restaurante"), vendaDiariaFilter.getRestauranteId()));
        }

        if (vendaDiariaFilter.getDataCriacaoInicio() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"),
                    vendaDiariaFilter.getDataCriacaoInicio()));
        }

        if (vendaDiariaFilter.getDataCriacaoFim() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"),
                    vendaDiariaFilter.getDataCriacaoFim()));
        }

        predicates.add(root.get("status").in(
                StatusPedido.CONFIRMADO, StatusPedido.ENTREGUE));

        query.select(selection);
        query.where(predicates.toArray(new Predicate[0]));
        query.groupBy(functionDateDataCriacao);

        return this.entityManager.createQuery(query).getResultList();
    }
}
