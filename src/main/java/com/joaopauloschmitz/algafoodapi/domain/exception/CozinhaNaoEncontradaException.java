package com.joaopauloschmitz.algafoodapi.domain.exception;

public class CozinhaNaoEncontradaException extends EntidadeNaoEncontradaException {

    public CozinhaNaoEncontradaException(String mensagem) {
        super(mensagem);
    }

    public CozinhaNaoEncontradaException(Long estadoId) {
        this(String.format("Não existe um cadastro de cozinha com código %d", estadoId));
    }
}
