package com.joaopauloschmitz.algafoodapi.domain.service;

import com.joaopauloschmitz.algafoodapi.domain.filter.VendaDiariaFilter;

public interface VendaReportService {

    byte[] emitirVendasDiarias(VendaDiariaFilter vendaDiariaFilter, String timeOffset);
}
