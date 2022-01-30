package br.com.fretee.freteebackend.usuarios.dto;

import br.com.fretee.freteebackend.usuarios.entity.PrestadorServico;
import br.com.fretee.freteebackend.usuarios.entity.Usuario;
import lombok.Data;

@Data
public class PrestadorServicoDTO {
    private String nomeCompleto;
    private String nomeUsuario;
    private float reputacao;
    private String telefone;
    private String foto;
    private int veiculo;
    private Double distancia;

    public PrestadorServicoDTO(Usuario usuario, PrestadorServico prestadorServico) {
        this.nomeCompleto = formatarNome(usuario.getNomeCompleto());
        this.nomeUsuario = usuario.getNomeUsuario();
        this.reputacao = prestadorServico.getReputacao();
        this.telefone = usuario.getTelefone();
        this.foto = usuario.getFoto();
        this.veiculo = prestadorServico.getVeiculo().getId();
    }

    private String formatarNome(String nomeCompleto) {
        int count = 0;
        int indexSegundoSpaco = 0;
        for(int i = 0; i < nomeCompleto.length(); i++) {
            if(Character.isWhitespace(nomeCompleto.charAt(i))) count++;

            if(count == 2) {
                indexSegundoSpaco = i;
                break;
            }
        }

        if(count <= 1) {
            return nomeCompleto;
        }else {
            return nomeCompleto.substring(0, indexSegundoSpaco);
        }

    }
}
