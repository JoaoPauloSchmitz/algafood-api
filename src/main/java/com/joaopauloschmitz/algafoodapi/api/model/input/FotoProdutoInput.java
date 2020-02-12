package com.joaopauloschmitz.algafoodapi.api.model.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.joaopauloschmitz.algafoodapi.core.validation.FileContentType;
import com.joaopauloschmitz.algafoodapi.core.validation.FileSize;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FotoProdutoInput {

    @NotNull
    @FileSize(max = "500KB")
    @FileContentType(allowed = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
    private MultipartFile arquivo;

    @NotBlank
    private String descricao;

}
