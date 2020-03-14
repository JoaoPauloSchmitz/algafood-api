package com.joaopauloschmitz.algafoodapi.api.v1.assembler;

import com.joaopauloschmitz.algafoodapi.api.v1.AlgaLinks;
import com.joaopauloschmitz.algafoodapi.api.v1.controller.RestauranteController;
import com.joaopauloschmitz.algafoodapi.api.v1.model.RestauranteModel;
import com.joaopauloschmitz.algafoodapi.core.security.AlgaSecurity;
import com.joaopauloschmitz.algafoodapi.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class RestauranteModelAssembler extends
        RepresentationModelAssemblerSupport<Restaurante, RestauranteModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AlgaLinks algaLinks;

    @Autowired
    private AlgaSecurity algaSecurity;

    public RestauranteModelAssembler() {
        super(RestauranteController.class, RestauranteModel.class);
    }

    @Override
    public RestauranteModel toModel(Restaurante restaurante) {
        RestauranteModel restauranteModel = createModelWithId(restaurante.getId(), restaurante);
        this.modelMapper.map(restaurante, restauranteModel);

        if (this.algaSecurity.podeConsultarRestaurantes()) {
            restauranteModel.add(this.algaLinks.linkToRestaurantes("restaurantes"));
        }

        if (this.algaSecurity.podeGerenciarCadastroRestaurantes()) {
            if (restaurante.ativacaoPermitida()) {
                restauranteModel.add(
                        this.algaLinks.linkToRestauranteAtivacao(restaurante.getId(), "ativar"));
            }

            if (restaurante.inativacaoPermitida()) {
                restauranteModel.add(
                        this.algaLinks.linkToRestauranteInativacao(restaurante.getId(), "inativar"));
            }
        }

        if (this.algaSecurity.podeGerenciarFuncionamentoRestaurantes(restaurante.getId())) {
            if (restaurante.aberturaPermitida()) {
                restauranteModel.add(
                        this.algaLinks.linkToRestauranteAbertura(restaurante.getId(), "abrir"));
            }

            if (restaurante.fechamentoPermitido()) {
                restauranteModel.add(
                        this.algaLinks.linkToRestauranteFechamento(restaurante.getId(), "fechar"));
            }
        }

        if (this.algaSecurity.podeConsultarRestaurantes()) {
            restauranteModel.add(this.algaLinks.linkToProdutos(restaurante.getId(), "produtos"));
        }

        if (this.algaSecurity.podeConsultarCozinhas()) {
            restauranteModel.getCozinha().add(
                    this.algaLinks.linkToCozinha(restaurante.getCozinha().getId()));
        }

        if (this.algaSecurity.podeConsultarCidades()) {
            if (restauranteModel.getEndereco() != null
                    && restauranteModel.getEndereco().getCidade() != null) {
                restauranteModel.getEndereco().getCidade().add(
                        this.algaLinks.linkToCidade(restaurante.getEndereco().getCidade().getId()));
            }
        }

        if (this.algaSecurity.podeConsultarRestaurantes()) {
            restauranteModel.add(this.algaLinks.linkToRestauranteFormasPagamento(restaurante.getId(),
                    "formas-pagamento"));
        }

        if (this.algaSecurity.podeGerenciarCadastroRestaurantes()) {
            restauranteModel.add(this.algaLinks.linkToRestauranteResponsaveis(restaurante.getId(),
                    "responsaveis"));
        }

        return restauranteModel;
    }

    @Override
    public CollectionModel<RestauranteModel> toCollectionModel(Iterable<? extends Restaurante> entities) {
        CollectionModel<RestauranteModel> collectionModel = super.toCollectionModel(entities);

        if (this.algaSecurity.podeConsultarRestaurantes()) {
            collectionModel.add(this.algaLinks.linkToRestaurantes());
        }

        return collectionModel;
    }
}
