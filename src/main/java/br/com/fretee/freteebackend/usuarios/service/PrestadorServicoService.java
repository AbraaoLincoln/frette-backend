package br.com.fretee.freteebackend.usuarios.service;

import br.com.fretee.freteebackend.exceptions.PrestadorServicoNotFoundException;
import br.com.fretee.freteebackend.exceptions.UsuarioAlreadyJoinAsPrestadorServico;
import br.com.fretee.freteebackend.exceptions.UsuarioNotFoundException;
import br.com.fretee.freteebackend.exceptions.VeiculoNotFoundException;
import br.com.fretee.freteebackend.usuarios.dto.NovoPrestadorServico;
import br.com.fretee.freteebackend.usuarios.dto.PrestadorServicoDTO;
import br.com.fretee.freteebackend.usuarios.dto.VeiculoDTO;
import br.com.fretee.freteebackend.usuarios.entity.Localizacao;
import br.com.fretee.freteebackend.usuarios.entity.PrestadorServico;
import br.com.fretee.freteebackend.usuarios.entity.Usuario;
import br.com.fretee.freteebackend.usuarios.entity.Veiculo;
import br.com.fretee.freteebackend.usuarios.repository.PrestadorServicoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PrestadorServicoService {
    @Autowired
    private PrestadorServicoRepository prestadorServicoRepository;
    @Autowired
    private VeiculoService veiculoService;
    @Autowired
    private ImagemVeiculoService imagemVeiculoService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired ImagemUsuarioService imagemUsuarioService;

    public void cadastraUsuarioComoPrestadorServico(Principal principal, NovoPrestadorServico novoPrestadorServico, VeiculoDTO veiculoDTO, MultipartFile fotoVeiculo) throws UsuarioNotFoundException, UsuarioAlreadyJoinAsPrestadorServico {
        log.info("Cadastrando novo prestador de servico: {}", principal.getName());

        Usuario usuario = usuarioService.findUsuarioByNomeUsuario(principal.getName());

        try{
            findByUsuarioId(usuario.getId());
            throw new UsuarioAlreadyJoinAsPrestadorServico();
        }catch(PrestadorServicoNotFoundException e) {
            var veiculoId = veiculoService.cadastrarVeiculo(veiculoDTO.toVeiculo(), fotoVeiculo);
            var prestadorServico = novoPrestadorServico.toPrestadorService(usuario);
            prestadorServico.setVeiculo( new Veiculo());
            prestadorServico.getVeiculo().setId(veiculoId);
            prestadorServicoRepository.save(prestadorServico);
        }
    }

    public PrestadorServicoDTO getPrestadorServicoInfo(Principal principal) throws UsuarioNotFoundException, PrestadorServicoNotFoundException {
        Usuario usuario = usuarioService.findUsuarioByNomeUsuario(principal.getName());
        PrestadorServico prestadorServico = findByUsuarioId(usuario.getId());

        return new PrestadorServicoDTO(usuario, prestadorServico);
    }

    public PrestadorServico findByUsuarioId(int usuarioId) throws PrestadorServicoNotFoundException {
        Optional<PrestadorServico> prestadorServicoOptional = prestadorServicoRepository.findByUsuarioId(usuarioId);
        if(prestadorServicoOptional.isEmpty()) throw new PrestadorServicoNotFoundException();
        return prestadorServicoOptional.get();
    }

    public void atualizarLocalizacao(Principal principal, Localizacao localizacao) throws UsuarioNotFoundException, PrestadorServicoNotFoundException {
        Usuario usuario = usuarioService.findUsuarioByNomeUsuario(principal.getName());
        PrestadorServico prestadorServico = findByUsuarioId(usuario.getId());
        prestadorServico.setLatitude(localizacao.getLatitude());
        prestadorServico.setLongitude(localizacao.getLongitude());
        prestadorServicoRepository.save(prestadorServico);
    }

    public void getFotoVeiculo(HttpServletResponse response, String prestadorServicoNome) throws UsuarioNotFoundException, IOException, PrestadorServicoNotFoundException {
        var usuario = usuarioService.findUsuarioByNomeUsuario(prestadorServicoNome);
        var prestadorServico = findByUsuarioId(usuario.getId());
        var imageInputStream = imagemVeiculoService.findImageAsInputStream(prestadorServico.getVeiculo().getFoto());
        imageInputStream.transferTo(response.getOutputStream());
        response.flushBuffer();
    }

    public List<PrestadorServicoDTO> getPrestadoreDeServicoProximo(Localizacao localizacao, Principal principal) {
        List<PrestadorServicoDTO> prestadoresDeServicoDTO = new ArrayList<>();
        try {
            Usuario usuario = usuarioService.findUsuarioByNomeUsuario(principal.getName());
            Iterable<PrestadorServico> prestadorServicos = prestadorServicoRepository.findAll();

            prestadorServicos.forEach(prestadorServico -> {
                if(usuario.getId() != prestadorServico.getId()) {
                    PrestadorServicoDTO prestadorServicoDTO = new PrestadorServicoDTO(prestadorServico.getUsuario(), prestadorServico);
                    double distancia = calculateDistanceInKilometer(localizacao.getLatitude(), localizacao.getLongitude(), prestadorServicoDTO.getLatitude(), prestadorServicoDTO.getLongitude());
                    prestadorServicoDTO.setDistancia(formatarDouble(distancia, 1));
                    prestadoresDeServicoDTO.add(prestadorServicoDTO);
                }
            });

            prestadoresDeServicoDTO.sort((o1, o2) -> {
                if(o1.getDistancia() > o2.getDistancia()) {
                    return 1;
                }else if(o1.getDistancia() < o2.getDistancia()) {
                    return -1;
                }else {
                    return 0;
                }
            });

        } catch (UsuarioNotFoundException e) {
            log.error("Usuario {} nao encontrado", principal.getName());
        }

        return prestadoresDeServicoDTO;
    }

    private double calculateDistanceInKilometer(double userLat, double userLng,
                                            double venueLat, double venueLng) {
        final  double AVERAGE_RADIUS_OF_EARTH_KM = 6371;
        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        //return (int) (Math.round(AVERAGE_RADIUS_OF_EARTH_KM * c));
        return AVERAGE_RADIUS_OF_EARTH_KM * c;
    }

    private Double formatarDouble(double valor, int precisao) {
        Double doubleFormatado = BigDecimal.valueOf(valor)
                .setScale(precisao, RoundingMode.HALF_UP)
                .doubleValue();

        return doubleFormatado;
    }

    public PrestadorServico findByNomeUsuario(String nomeUsuario) throws UsuarioNotFoundException{
        Optional<PrestadorServico> prestadorServico = prestadorServicoRepository.findByNomeUsuario(nomeUsuario);

        if(prestadorServico.isEmpty()) throw new UsuarioNotFoundException();
        return prestadorServico.get();
    }

    public VeiculoDTO getVeiculoInfo(String nomeUsuario) throws UsuarioNotFoundException, VeiculoNotFoundException {
        PrestadorServico prestadorServico = findByNomeUsuario(nomeUsuario);
        Veiculo veiculo = veiculoService.findVeiculoById(prestadorServico.getVeiculo().getId());

        return new VeiculoDTO(veiculo.getLargura(), veiculo.getAltura(), veiculo.getComprimento(),
                veiculo.getFoto(), veiculo.getId());
    }

    public void getFoto(HttpServletResponse response, String prestadoServicoNomeUsuario) throws UsuarioNotFoundException, IOException {
        var usuario = usuarioService.findUsuarioByNomeUsuario(prestadoServicoNomeUsuario);
        var imageInputStream = imagemUsuarioService.findImageAsInputStream(usuario.getFoto());
        imageInputStream.transferTo(response.getOutputStream());
        response.flushBuffer();
    }
}
