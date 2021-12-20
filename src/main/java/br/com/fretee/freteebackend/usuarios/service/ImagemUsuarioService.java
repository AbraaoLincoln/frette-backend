package br.com.fretee.freteebackend.usuarios.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImagemUsuarioService extends ImagemService{
    @Value("${fotos.usuarios.diretorio}")
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
