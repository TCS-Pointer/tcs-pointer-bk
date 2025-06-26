package br.com.pointer.pointer_back.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.ConfirmarLeituraDTO;
import br.com.pointer.pointer_back.dto.UsuarioComunicadoDTO;
import br.com.pointer.pointer_back.model.Comunicado;
import br.com.pointer.pointer_back.model.ComunicadoLeitura;
import br.com.pointer.pointer_back.model.Usuario;
import br.com.pointer.pointer_back.repository.ComunicadoLeituraRepository;
import br.com.pointer.pointer_back.repository.ComunicadoRepository;
import br.com.pointer.pointer_back.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ComunicadoLeituraService {
    private final ComunicadoLeituraRepository leituraRepository;
    private final ComunicadoRepository comunicadoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public ApiResponse<Void> confirmarLeitura(ConfirmarLeituraDTO confirmarLeituraDTO) {
        Comunicado comunicado = comunicadoRepository.findById(confirmarLeituraDTO.getComunicadoId())
                .orElseThrow(() -> new RuntimeException("Comunicado não encontrado"));
        Usuario usuario = usuarioRepository.findByKeycloakId(confirmarLeituraDTO.getKeycloakId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Optional<ComunicadoLeitura> existente = leituraRepository.findByComunicadoIdAndUsuarioId(comunicado.getId(), usuario.getId());
        if (existente.isEmpty()) {
            ComunicadoLeitura leitura = new ComunicadoLeitura();
            leitura.setComunicadoId(comunicado.getId());
            leitura.setUsuarioId(usuario.getId());
            leitura.setDtLeitura(LocalDateTime.now());
            leituraRepository.save(leitura);
        }
        return ApiResponse.success("Leitura confirmada com sucesso");
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<UsuarioComunicadoDTO>> listarLeitores(Long comunicadoId) {
        Comunicado comunicado = comunicadoRepository.findById(comunicadoId)
                .orElseThrow(() -> new RuntimeException("Comunicado não encontrado"));
        List<ComunicadoLeitura> leituras = leituraRepository.findByComunicadoId(comunicado.getId());
        List<UsuarioComunicadoDTO> dtos = leituras.stream()
                .map(leitura -> {
                    UsuarioComunicadoDTO dto = new UsuarioComunicadoDTO();
                    dto.setId(leitura.getId());
                    dto.setDtLeitura(leitura.getDtLeitura());
                    dto.setUsuarioId(leitura.getUsuarioId());
                    return dto;
                })
                .collect(Collectors.toList());
        return ApiResponse.success(dtos, "Leitores listados com sucesso");
    }
}