package com.joaopauloschmitz.algafoodapi.domain.service;

import com.joaopauloschmitz.algafoodapi.domain.filter.VendaDiariaFilter;
import com.joaopauloschmitz.algafoodapi.domain.model.dto.VendaDiaria;

import java.util.List;

public interface VendaQueryService {

    List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter vendaDiariaFilter, String timeOffset);
}
