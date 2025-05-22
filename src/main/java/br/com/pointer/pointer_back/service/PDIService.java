package br.com.pointer.pointer_back.service;

import br.com.pointer.pointer_back.dto.pdiDTO;
import br.com.pointer.pointer_back.dto.PDIResponseDTO;
import br.com.pointer.pointer_back.exception.PDINaoEncontradoException;
import br.com.pointer.pointer_back.mapper.PDIMapper;
import br.com.pointer.pointer_back.model.PDI;
import br.com.pointer.pointer_back.repository.PDIRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PDIService {

    private static final Logger logger = LoggerFactory.getLogger(PDIService.class);

    private final PDIRepository pdiRepository;
    private final PDIMapper pdiMapper;
    // private final UsuarioService usuarioService;

    public PDIService(PDIRepository pdiRepository, PDIMapper pdiMapper, UsuarioService usuarioService) {
        this.pdiRepository = pdiRepository;
        this.pdiMapper = pdiMapper;
        // this.usuarioService = usuarioService;
    }

    private Long getLoggedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Jwt)) {
            logger.warn("Autenticação inválida ou ausente.");
            throw new AccessDeniedException("Usuário não autenticado ou token inválido.");
        }
        try {
            return Long.parseLong(((Jwt) auth.getPrincipal()).getSubject());
        } catch (NumberFormatException e) {
            logger.error("Formato inválido para ID de usuário: {}", ((Jwt) auth.getPrincipal()).getSubject());
            throw new IllegalStateException("Formato inválido de ID no token JWT.", e);
        }
    }

    private boolean isAdminOrGestor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream().anyMatch(
            role -> role.getAuthority().equals("admin") || role.getAuthority().equals("gestor")
        );
    }

    private void validarPermissaoVisualizacao(PDI pdi) {
        Long userId = getLoggedUserId();
        boolean isOwner = pdi.getIdUsuario().equals(userId);
        if (!isOwner && !isAdminOrGestor()) {
            logger.warn("Usuário {} tentou acessar PDI {} sem permissão.", userId, pdi.getId());
            throw new AccessDeniedException("Você não tem permissão para visualizar este PDI.");
        }
    }

    // private void validarPermissaoGerenciamento(PDI pdi) {
    //     Long userId = getLoggedUserId();
    //     boolean isOwner = pdi.getIdUsuario().equals(userId);
    //     if (!isOwner && !isAdminOrGestor()) {
    //         logger.warn("Usuário {} tentou gerenciar PDI {} sem permissão.", userId, pdi.getId());
    //         throw new AccessDeniedException("Você não tem permissão para gerenciar este PDI.");
    //     }
    // }

    // @Transactional(readOnly = true)
    // public List<PDIResponseDTO> listarTodos() {
    //     Long userId = getLoggedUserId();
    //     List<PDI> pdis = isAdminOrGestor() ? pdiRepository.findAll() : pdiRepository.findByIdUsuario(userId);
    //     return pdis.stream().map(pdiMapper::toResponseDTO).collect(Collectors.toList());
    // }

    @Transactional(readOnly = true)
    public PDIResponseDTO buscarPorId(Long id) {
        PDI pdi = pdiRepository.findById(id)
                .orElseThrow(() -> new PDINaoEncontradoException(id));
        validarPermissaoVisualizacao(pdi);
        return pdiMapper.toResponseDTO(pdi);
    }

    @Transactional
    public PDIResponseDTO criar(pdiDTO dto) {
        if (!isAdminOrGestor()) {
            logger.warn("Usuário tentou criar PDI sem permissão.");
            throw new AccessDeniedException("Apenas ADMIN ou GESTOR podem criar PDIs.");
        }

        if (dto.getIdUsuario() == null) {
            logger.warn("Tentativa de criação de PDI sem ID de usuário.");
            throw new IllegalArgumentException("idUsuario é obrigatório.");
        }

        PDI pdi = pdiMapper.toEntity(dto);
        PDI salvo = pdiRepository.save(pdi);
        return pdiMapper.toResponseDTO(salvo);
    }

    // @Transactional
    // public PDIResponseDTO atualizar(Long id, pdiDTO dto) {
    //     PDI existente = pdiRepository.findById(id)
    //             .orElseThrow(() -> new PDINaoEncontradoException(id));
    //     validarPermissaoGerenciamento(existente);

    //     existente.setObjetivos(dto.getObjetivos());
    //     existente.setIdUsuario(dto.getIdUsuario());
    //     existente.setMetas(dto.getMetas());
    //     existente.setResultadosEsperados(dto.getResultadosEsperados());

    //     return pdiMapper.toResponseDTO(pdiRepository.save(existente));
    // }

    // @Transactional
    // public void deletar(Long id) {
    //     PDI existente = pdiRepository.findById(id)
    //             .orElseThrow(() -> new PDINaoEncontradoException(id));
    //     validarPermissaoGerenciamento(existente);
    //     pdiRepository.deleteById(id);
    // }
}