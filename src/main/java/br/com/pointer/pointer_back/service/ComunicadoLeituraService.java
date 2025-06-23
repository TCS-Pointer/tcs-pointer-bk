package br.com.pointer.pointer_back.service;

import br.com.pointer.pointer_back.model.Comunicado;
import br.com.pointer.pointer_back.model.ComunicadoLeitura;
import br.com.pointer.pointer_back.model.Usuario;
import br.com.pointer.pointer_back.repository.ComunicadoLeituraRepository;
import br.com.pointer.pointer_back.repository.ComunicadoRepository;
import br.com.pointer.pointer_back.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComunicadoLeituraService {
    private final ComunicadoLeituraRepository leituraRepository;
    private final ComunicadoRepository comunicadoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public void confirmarLeitura(Long comunicadoId, String keycloakId) {
        Comunicado comunicado = comunicadoRepository.findById(comunicadoId)
                .orElseThrow(() -> new RuntimeException("Comunicado não encontrado"));
        Usuario usuario = usuarioRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Optional<ComunicadoLeitura> existente = leituraRepository.findByComunicadoAndUsuario(comunicado, usuario);
        if (existente.isEmpty()) {
            ComunicadoLeitura leitura = new ComunicadoLeitura();
            leitura.setComunicado(comunicado);
            leitura.setUsuario(usuario);
            leitura.setDataLeitura(LocalDateTime.now());
            leituraRepository.save(leitura);
        }
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarLeitores(Long comunicadoId) {
        Comunicado comunicado = comunicadoRepository.findById(comunicadoId)
                .orElseThrow(() -> new RuntimeException("Comunicado não encontrado"));
        return leituraRepository.findByComunicado(comunicado)
                .stream()
                .map(ComunicadoLeitura::getUsuario)
                .collect(Collectors.toList());
    }
} 