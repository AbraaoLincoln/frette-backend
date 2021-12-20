package br.com.fretee.freteebackend.usuarios.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ImagemVeiculoService extends ImagemService{
    @Value("${fotos.veiculos.diretorio}")
    private String path;
}
