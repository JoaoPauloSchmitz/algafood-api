package com.joaopauloschmitz.algafoodapi.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(of = "id")
@Entity
public class FotoProduto {

    @Id
    @Column(name = "produto_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Produto produto;

    private String nomeArquivo;
    private String descricao;
    private String contentType;
    private Long tamanho;

    public Long getRestauranteId() {
        if (this.getProduto() == null) {
            return null;
        }

        return this.getProduto().getRestaurante().getId();
    }
}
