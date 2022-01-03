package br.com.fretee.freteebackend.usuarios.service;

public class ImagemUsuarioService extends ImagemService{
    private String path;

    public ImagemUsuarioService(String path) {
        super.path = path;
    }
    public String getPath() {
        return path;
    }
}
