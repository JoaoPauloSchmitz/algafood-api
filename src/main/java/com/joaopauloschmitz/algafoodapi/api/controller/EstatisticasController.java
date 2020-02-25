package com.joaopauloschmitz.algafoodapi.api.controller;

import com.joaopauloschmitz.algafoodapi.api.openapi.controller.EstatisticasControllerOpenApi;
import com.joaopauloschmitz.algafoodapi.domain.filter.VendaDiariaFilter;
import com.joaopauloschmitz.algafoodapi.domain.model.dto.VendaDiaria;
import com.joaopauloschmitz.algafoodapi.domain.service.VendaQueryService;
import com.joaopauloschmitz.algafoodapi.domain.service.VendaReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/estatisticas")
public class EstatisticasController implements EstatisticasControllerOpenApi {

    @Autowired
    private VendaQueryService vendaQueryService;

    @Autowired
    private VendaReportService vendaReportService;

    @GetMapping(value = "/vendas-diarias", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter vendaDiariaFilter,
                            @RequestParam(required = false, defaultValue = "+00:00") String timeOffset) {
        return this.vendaQueryService.consultarVendasDiarias(vendaDiariaFilter, timeOffset);
    }

    @GetMapping(value = "/vendas-diarias", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> consultarVendasDiariasPdf(VendaDiariaFilter vendaDiariaFilter,
                                                    @RequestParam(required = false, defaultValue = "+00:00") String timeOffset) {

        byte[] bytesPdf = this.vendaReportService.emitirVendasDiarias(vendaDiariaFilter, timeOffset);

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vendas-diarias.pdf");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .headers(headers)
                .body(bytesPdf);
    }
}
