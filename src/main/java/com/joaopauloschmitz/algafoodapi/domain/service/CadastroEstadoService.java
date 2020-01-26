package com.joaopauloschmitz.algafoodapi.domain.service;

import com.joaopauloschmitz.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.joaopauloschmitz.algafoodapi.domain.exception.EstadoNaoEncontradoException;
import com.joaopauloschmitz.algafoodapi.domain.model.Estado;
import com.joaopauloschmitz.algafoodapi.domain.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroEstadoService {

    private static final String MSG_ESTADO_EM_USO
            = "Estado de código %d não pode ser removido, pois está em uso";

    @Autowired
    private EstadoRepository estadoRepository;

    @Transactional
    public Estado salvar(Estado estado) {
        return this.estadoRepository.save(estado);
    }

    @Transactional
    public void excluir(Long id) {
        try {
            this.estadoRepository.deleteById(id);
            this.estadoRepository.flush();

        } catch (EmptyResultDataAccessException e) {
            throw new EstadoNaoEncontradoException(id);

        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(String.format(MSG_ESTADO_EM_USO, id));
        }
    }

    public Estado buscarOuFalhar(Long id) {
        return this.estadoRepository.findById(id)
                .orElseThrow(() -> new EstadoNaoEncontradoException(id));
    }
}
