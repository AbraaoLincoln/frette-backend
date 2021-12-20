package br.com.fretee.freteebackend.usuarios.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.*;

public class ImagemService {
    private String path;

    public InputStream findImageAsInputStream(String fotoId)  {
        var image = new File(path + "/${fotoId}.png");

        try {
            FileInputStream imageInputStream = new FileInputStream(image);
            return imageInputStream;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String saveImage(MultipartFile imageParaSalvar) {
        var uuid = UUID.randomUUID();
        var image = new File(path + "/${uuid}.png");

        try {
            imageParaSalvar.transferTo(image);
            return uuid.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void deleteImagem(String fotoId) {
        try{
            Files.deleteIfExists(Paths.get(path + "/${fotoId}.png"));
        }catch(NoSuchFileException e) {
            System.out.println("Imagem não encontrada");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
