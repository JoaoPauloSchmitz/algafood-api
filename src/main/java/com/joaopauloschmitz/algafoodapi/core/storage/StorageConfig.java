package com.joaopauloschmitz.algafoodapi.core.storage;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.joaopauloschmitz.algafoodapi.core.storage.StorageProperties.TipoStorage;
import com.joaopauloschmitz.algafoodapi.domain.service.FotoStorageService;
import com.joaopauloschmitz.algafoodapi.infrastructure.service.storage.LocalFotoStorageService;
import com.joaopauloschmitz.algafoodapi.infrastructure.service.storage.S3FotoStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Autowired
    private StorageProperties storageProperties;

    @Bean
    @ConditionalOnProperty(name = "algafood.storage.tipo-storage", havingValue = "s3")
    public AmazonS3 amazonS3() {
        var credentials = new BasicAWSCredentials(
                this.storageProperties.getS3().getIdChaveAcesso(),
                this.storageProperties.getS3().getChaveAcessoSecreta()
        );

        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.storageProperties.getS3().getRegiao())
                .build();
    }

    @Bean
    public FotoStorageService fotoStorageService() {
        if (TipoStorage.S3.equals(storageProperties.getTipoStorage())) {
            return new S3FotoStorageService();
        } else {
            return new LocalFotoStorageService();
        }
    }
}
