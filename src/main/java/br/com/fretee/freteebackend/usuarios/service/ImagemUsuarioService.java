package br.com.fretee.freteebackend.usuarios.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.UUID;

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

    @Override
    public InputStream findImageAsInputStream(String fotoId)  {
        super.path = this.path;
        return super.findImageAsInputStream(fotoId);
    }

    @Override
    public String saveImage(MultipartFile imageParaSalvar) {
        super.path = this.path;
        return super.saveImage(imageParaSalvar);
    }

    @Override
    public void deleteImagem(String fotoId) {
        super.path = this.path;
        super.deleteImagem(fotoId);
    }
}
