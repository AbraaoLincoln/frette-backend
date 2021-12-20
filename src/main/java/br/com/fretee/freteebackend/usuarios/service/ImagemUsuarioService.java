package br.com.fretee.freteebackend.usuarios.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ImagemUsuarioService {
    @Value("${fotos.usuarios.diretorio}")
    private String path;
}
