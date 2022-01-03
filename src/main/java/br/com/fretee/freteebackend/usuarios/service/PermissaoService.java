package br.com.fretee.freteebackend.usuarios.service;

import br.com.fretee.freteebackend.usuarios.entity.Permissao;
import br.com.fretee.freteebackend.usuarios.entity.Usuario;
import br.com.fretee.freteebackend.usuarios.enums.Permissoes;
import br.com.fretee.freteebackend.usuarios.repository.PermissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissaoService {
    @Autowired
    private PermissaoRepository permissaoRepository;

    public void salvarPermissao(String permissaoNome) {
        Permissao permissao = new Permissao();
        permissao.setNome(permissaoNome);
        permissaoRepository.save(permissao);
    }

    public Usuario addPermissaoDeUsuario(Usuario usuario) {
        Permissao permissaoUsuario = new Permissao();
        permissaoUsuario.setId(Permissoes.USUARIO.getValue());
        usuario.getPermissoes().add(permissaoUsuario);
        return usuario;
    }
}
