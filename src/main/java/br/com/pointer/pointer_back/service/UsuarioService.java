package br.com.pointer.pointer_back.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import br.com.pointer.pointer_back.dto.UsuarioDTO;
import br.com.pointer.pointer_back.dto.UsuarioResponseDTO;
import br.com.pointer.pointer_back.dto.UsuarioRequestDTO;
import br.com.pointer.pointer_back.exception.UsuarioNaoEncontradoException;
import br.com.pointer.pointer_back.mapper.UsuarioMapper;
import br.com.pointer.pointer_back.model.Usuario;
import br.com.pointer.pointer_back.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final KeycloakAdminService keycloakAdminService;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            UsuarioMapper usuarioMapper,
            KeycloakAdminService keycloakAdminService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.keycloakAdminService = keycloakAdminService;
    }

    public static class CriarUsuarioResponse {
        private boolean sucessoKeycloak;
        private String mensagemKeycloak;
        private String keycloakId;
        private boolean sucessoBanco;
        private String mensagemBanco;
        private UsuarioResponseDTO usuario;

        // Getters e setters
        public boolean isSucessoKeycloak() { return sucessoKeycloak; }
        public void setSucessoKeycloak(boolean sucessoKeycloak) { this.sucessoKeycloak = sucessoKeycloak; }
        public String getMensagemKeycloak() { return mensagemKeycloak; }
        public void setMensagemKeycloak(String mensagemKeycloak) { this.mensagemKeycloak = mensagemKeycloak; }
        public String getKeycloakId() { return keycloakId; }
        public void setKeycloakId(String keycloakId) { this.keycloakId = keycloakId; }
        public boolean isSucessoBanco() { return sucessoBanco; }
        public void setSucessoBanco(boolean sucessoBanco) { this.sucessoBanco = sucessoBanco; }
        public String getMensagemBanco() { return mensagemBanco; }
        public void setMensagemBanco(String mensagemBanco) { this.mensagemBanco = mensagemBanco; }
        public UsuarioResponseDTO getUsuario() { return usuario; }
        public void setUsuario(UsuarioResponseDTO usuario) { this.usuario = usuario; }
    }

    private UsuarioDTO converterRequestParaDTO(UsuarioRequestDTO request) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNome(request.getNome());
        usuarioDTO.setSetor(request.getSetor());
        usuarioDTO.setCargo(request.getCargo());
        usuarioDTO.setTipoUsuario(request.getTipoUsuario());
        return usuarioDTO;
    }

    public ResponseEntity<CriarUsuarioResponse> criarUsuarioCompleto(UsuarioRequestDTO request) {
        UsuarioDTO usuarioDTO = converterRequestParaDTO(request);
        return criarUsuarioCompleto(usuarioDTO, request.getEmail(), request.getSenha());
    }

    public ResponseEntity<CriarUsuarioResponse> criarUsuarioCompleto(UsuarioDTO dto, String email, String senha) {
        CriarUsuarioResponse response = new CriarUsuarioResponse();
        String keycloakId = null;
        // 1. Criação no Keycloak
        try {
            keycloakId = keycloakAdminService.createUserAndReturnId(dto.getNome(), email, senha);
            response.setSucessoKeycloak(true);
            response.setMensagemKeycloak("Usuário criado no Keycloak com sucesso.");
            response.setKeycloakId(keycloakId);
        } catch (Exception e) {
            response.setSucessoKeycloak(false);
            response.setMensagemKeycloak("Erro ao criar usuário no Keycloak: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // 2. Criação no banco local
        try {
            dto.setKeycloakId(keycloakId);
            Usuario usuario = usuarioMapper.toEntity(dto);
            usuario = usuarioRepository.save(usuario);
            response.setSucessoBanco(true);
            response.setMensagemBanco("Usuário salvo no banco com sucesso.");
            response.setUsuario(usuarioMapper.toResponseDTO(usuario));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.setSucessoBanco(false);
            response.setMensagemBanco("Erro ao salvar usuário no banco: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> listarUsuarios(PageRequest pageRequest, String setor, String perfil) {
        Specification<Usuario> spec = Specification.where(null);
        spec = spec.and((root, query, cb) -> {
            assert query != null;
            query.orderBy(cb.desc(root.get("dataCriacao")));
            return null;
        });
        if (StringUtils.hasText(setor)) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("setor"), setor));
        }
        if (StringUtils.hasText(perfil)) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("tipoUsuario"), perfil));
        }
        return usuarioRepository.findAll(spec, pageRequest).map(usuarioMapper::toResponseDTO);
    }

    @Transactional
    public UsuarioResponseDTO atualizarUsuario(UsuarioDTO dto, String id) {
        Usuario usuario = usuarioRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
        usuarioMapper.updateEntityFromDTO(dto, usuario);
        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuario);
    }

    public UsuarioResponseDTO buscarUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id.toString()));
        return usuarioMapper.toResponseDTO(usuario);
    }

    public UsuarioResponseDTO buscarUsuarioPorKeycloakId(String keycloakId) {
        Usuario usuario = usuarioRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado com keycloakId: " + keycloakId));
        return usuarioMapper.toResponseDTO(usuario);
    }
}