package br.com.pointer.pointer_back.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.AlterarStatusDTO;
import br.com.pointer.pointer_back.dto.KeycloakResponseDTO;
import br.com.pointer.pointer_back.dto.PrimeiroAcessoDTO;
import br.com.pointer.pointer_back.dto.TipoUsuarioStatsDTO;
import br.com.pointer.pointer_back.dto.TipoUsuarioStatsResponseDTO;
import br.com.pointer.pointer_back.dto.UpdatePasswordDTO;
import br.com.pointer.pointer_back.dto.UsuarioDTO;
import br.com.pointer.pointer_back.dto.UsuarioResponseDTO;
import br.com.pointer.pointer_back.dto.UsuarioUpdateDTO;
import br.com.pointer.pointer_back.exception.KeycloakException;
import br.com.pointer.pointer_back.exception.SetorCargoInvalidoException;
import br.com.pointer.pointer_back.exception.TokenExpiradoException;
import br.com.pointer.pointer_back.exception.TokenInvalidoException;
import br.com.pointer.pointer_back.exception.UsuarioNaoEncontradoException;
import br.com.pointer.pointer_back.model.StatusUsuario;
import br.com.pointer.pointer_back.model.Usuario;
import br.com.pointer.pointer_back.repository.UsuarioRepository;
import br.com.pointer.pointer_back.util.ValidationUtil;

@Service
public class UsuarioService {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;
    private final KeycloakAdminService keycloakAdminService;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final EmailService emailService;
    private final Keycloak keycloak;
    private final String realm;
    private final SetorCargoService setorCargoService;
    private final PrimeiroAcessoService primeiroAcessoService;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            KeycloakAdminService keycloakAdminService,
            ModelMapper modelMapper,
            EmailService emailService,
            Keycloak keycloak,
            SetorCargoService setorCargoService,
            PrimeiroAcessoService primeiroAcessoService,
            @Value("${keycloak.realm}") String realm) {
        this.usuarioRepository = usuarioRepository;
        this.keycloakAdminService = keycloakAdminService;
        this.modelMapper = modelMapper;
        this.emailService = emailService;
        this.keycloak = keycloak;
        this.setorCargoService = setorCargoService;
        this.primeiroAcessoService = primeiroAcessoService;
        this.realm = realm;
    }

    @Transactional
    public ApiResponse<UsuarioResponseDTO> criarUsuario(UsuarioDTO dto) {
        try {
            if (usuarioRepository.existsByEmail(dto.getEmail())) {
                return ApiResponse.badRequest("Já existe um usuário com este email");
            }

            // Validar nome
            ValidationUtil.validarNome(dto.getNome());

            // Validar setor e cargo
            setorCargoService.validarSetorECargo(dto.getSetor(), dto.getCargo());

            String senhaPura = dto.getSenha();
            if (senhaPura == null) {
                // Gerar senha temporária para o Keycloak
                senhaPura = gerarSenhaAleatoria();

                try {
                    // Criar usuário no Keycloak com senha temporária
                    KeycloakResponseDTO keycloakResponse = keycloakAdminService.createUserAndReturnId(
                            dto.getNome(), dto.getEmail(), senhaPura);

                    if (!keycloakResponse.isSuccess()) {
                        return ApiResponse.error(keycloakResponse.getErrorMessage(), keycloakResponse.getStatusCode());
                    }

                    String userId = (String) keycloakResponse.getData();
                    keycloakAdminService.setUserPassword(userId, senhaPura);
                    keycloakAdminService.assignRolesToUser(userId, obterRolesPorTipo(dto.getTipoUsuario()));

                    // Criar usuário no banco de dados
                    Usuario usuario = new Usuario();
                    modelMapper.map(dto, usuario);
                    usuario.setKeycloakId(userId);
                    usuario = usuarioRepository.save(usuario);

                    // Gerar token e enviar email de primeiro acesso
                    String token = primeiroAcessoService.gerarToken(dto.getEmail());
                    emailService.sendPrimeiroAcessoEmail(dto.getEmail(), dto.getNome(), token);

                    UsuarioResponseDTO responseDTO = modelMapper.map(usuario, UsuarioResponseDTO.class);
                    return ApiResponse.success(responseDTO,
                            "Usuário criado com sucesso. Email de primeiro acesso enviado.");
                } catch (KeycloakException e) {
                    logger.error("Erro ao criar usuário no Keycloak: ", e);
                    return ApiResponse.error(e.getErrorMessage(), e.getStatusCode());
                }
            } else {
                // Usuário com senha fornecida - fluxo normal
                try {
                    KeycloakResponseDTO keycloakResponse = keycloakAdminService.createUserAndReturnId(
                            dto.getNome(), dto.getEmail(), senhaPura);

                    if (!keycloakResponse.isSuccess()) {
                        return ApiResponse.error(keycloakResponse.getErrorMessage(), keycloakResponse.getStatusCode());
                    }

                    String userId = (String) keycloakResponse.getData();
                    keycloakAdminService.setUserPassword(userId, senhaPura);
                    keycloakAdminService.assignRolesToUser(userId, obterRolesPorTipo(dto.getTipoUsuario()));

                    Usuario usuario = new Usuario();
                    modelMapper.map(dto, usuario);
                    usuario.setKeycloakId(userId);
                    usuario = usuarioRepository.save(usuario);

                    UsuarioResponseDTO responseDTO = modelMapper.map(usuario, UsuarioResponseDTO.class);
                    return ApiResponse.success(responseDTO, "Usuário criado com sucesso");
                } catch (KeycloakException e) {
                    logger.error("Erro ao criar usuário no Keycloak: ", e);
                    return ApiResponse.error(e.getErrorMessage(), e.getStatusCode());
                }
            }
        } catch (SetorCargoInvalidoException e) {
            logger.error("Erro de validação de setor/cargo: ", e);
            return ApiResponse.badRequest(e.getMessage());
        } catch (KeycloakException e) {
            logger.error("Erro de validação do nome: ", e);
            return ApiResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao criar usuário: ", e);
            return ApiResponse.badRequest("Erro ao criar usuário: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public ApiResponse<Page<UsuarioResponseDTO>> listarUsuarios(PageRequest pageRequest, String tipoUsuario,
            String setor, String status) {
        StatusUsuario statusUsuario = null;
        if (StringUtils.hasText(status)) {
            try {
                statusUsuario = StatusUsuario.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                logger.warn("Status inválido: {}", status);
            }
        }

        Page<Usuario> usuarios = usuarioRepository.findByFilters(tipoUsuario, setor, statusUsuario, pageRequest);
        return ApiResponse.mapPage(usuarios, UsuarioResponseDTO.class, "Usuários listados com sucesso");
    }

    @Transactional
    public ApiResponse<Void> alternarStatusUsuarioPorEmail(AlterarStatusDTO alterarStatusDTO) {
        try {
            Usuario usuario = usuarioRepository.findByEmail(alterarStatusDTO.getEmailChangeStatus())
                    .orElseThrow(() -> new UsuarioNaoEncontradoException(alterarStatusDTO.getEmailChangeStatus()));

            logger.info("Usuario alterando status: {}", alterarStatusDTO.getEmailSend());
            logger.info("Usuario alterado status: {}", usuario.getEmail());

            if (usuario.getStatus().equals(StatusUsuario.ATIVO) &&
                    usuario.getEmail().equals(alterarStatusDTO.getEmailSend())) {
                return ApiResponse.badRequest("Você não pode desabilitar seu próprio perfil");
            }

            try {
                KeycloakResponseDTO keycloakResponse;
                if (usuario.getStatus().equals(StatusUsuario.ATIVO)) {
                    keycloakResponse = keycloakAdminService.disableUser(usuario.getEmail());
                    if (!keycloakResponse.isSuccess()) {
                        throw new KeycloakException(
                                "Erro ao desativar usuário no Keycloak: " + keycloakResponse.getErrorMessage(),
                                keycloakResponse.getStatusCode(),
                                "KEYCLOAK_DISABLE_ERROR");
                    }
                    usuario.setStatus(StatusUsuario.INATIVO);
                } else {
                    keycloakResponse = keycloakAdminService.enableUser(usuario.getEmail());
                    if (!keycloakResponse.isSuccess()) {
                        throw new KeycloakException(
                                "Erro ao ativar usuário no Keycloak: " + keycloakResponse.getErrorMessage(),
                                keycloakResponse.getStatusCode(),
                                "KEYCLOAK_ENABLE_ERROR");
                    }
                    usuario.setStatus(StatusUsuario.ATIVO);
                    logger.info("Usuário ativado: {}", usuario.getEmail());
                    // ususario ativado por tal email

                }

                usuarioRepository.save(usuario);
                return ApiResponse.success("Status do usuário alterado com sucesso");
            } catch (KeycloakException e) {
                logger.error("Erro ao alterar status no Keycloak: {}", e.getMessage(), e);
                throw new KeycloakException(
                        "Erro ao alterar status do usuário no Keycloak: " + e.getMessage(),
                        e.getStatusCode(),
                        e.getErrorCode());
            }
        } catch (UsuarioNaoEncontradoException e) {
            logger.error("Usuário não encontrado: {}", e.getMessage(), e);
            throw new KeycloakException(
                    "Usuário não encontrado com o email: " + alterarStatusDTO.getEmailChangeStatus(),
                    404,
                    "USER_NOT_FOUND");
        } catch (Exception e) {
            logger.error("Erro ao alternar status do usuário: {}", e.getMessage(), e);
            throw new KeycloakException(
                    "Erro interno ao alternar status do usuário: " + e.getMessage(),
                    500,
                    "INTERNAL_SERVER_ERROR");
        }
    }

    @Transactional
    public String gerarSenhaAleatoria() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder senha = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(caracteres.length());
            senha.append(caracteres.charAt(index));
        }
        return senha.toString();
    }

    @Transactional
    public ApiResponse<UsuarioResponseDTO> atualizarUsuario(UsuarioUpdateDTO dto, String keycloakId) {
        try {

            Usuario usuario = usuarioRepository.findByKeycloakId(keycloakId)
                    .orElseThrow(() -> new UsuarioNaoEncontradoException(keycloakId));

            if (dto.getNome() != null) {
                ValidationUtil.validarNome(dto.getNome());
            }

            if (dto.getSetor() != null && dto.getCargo() != null) {
                setorCargoService.validarSetorECargo(dto.getSetor(), dto.getCargo());
            }

            if (dto.getNome() != null) {
                usuario.setNome(dto.getNome());
            }
            if (dto.getSetor() != null) {
                usuario.setSetor(dto.getSetor());
            }
            if (dto.getCargo() != null) {
                usuario.setCargo(dto.getCargo());
            }
            if (dto.getTipoUsuario() != null) {
                usuario.setTipoUsuario(dto.getTipoUsuario());
            }

            usuario = usuarioRepository.save(usuario);

            atualizarUsuarioNoKeycloak(usuario, dto);

            return ApiResponse.map(usuario, UsuarioResponseDTO.class, "Usuário atualizado com sucesso");
        } catch (UsuarioNaoEncontradoException e) {
            return ApiResponse.notFound(e.getMessage());
        } catch (SetorCargoInvalidoException e) {
            logger.error("Erro de validação de setor/cargo: ", e);
            return ApiResponse.badRequest(e.getMessage());
        } catch (KeycloakException e) {
            logger.error("Erro de validação do nome: ", e);
            return ApiResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao atualizar usuário: ", e);
            return ApiResponse.badRequest("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    private void atualizarUsuarioNoKeycloak(Usuario usuario, UsuarioUpdateDTO dto) {
        try {
            List<UserRepresentation> users = keycloak.realm(realm).users().search(usuario.getEmail());

            if (users.isEmpty()) {
                logger.error("Usuário não encontrado no Keycloak com email: {}", usuario.getEmail());
                throw new RuntimeException("Usuário não encontrado no Keycloak");
            }

            String userId = users.get(0).getId();
            logger.info("Usuário encontrado no Keycloak com ID: {}", userId);

            UserRepresentation userRepresentation = new UserRepresentation();
            userRepresentation.setId(userId);
            userRepresentation.setEmail(usuario.getEmail());
            userRepresentation.setUsername(usuario.getEmail());
            userRepresentation.setEnabled(usuario.getStatus() == StatusUsuario.ATIVO);

            if (dto.getNome() != null) {
                String[] nomeCompleto = dto.getNome().split(" ", 2);
                userRepresentation.setFirstName(nomeCompleto[0]);
                userRepresentation.setLastName(nomeCompleto.length > 1 ? nomeCompleto[1] : "");
            }

            Map<String, List<String>> attributes = new HashMap<>();
            attributes.put("setor", Collections.singletonList(usuario.getSetor()));
            attributes.put("tipoUsuario", Collections.singletonList(usuario.getTipoUsuario()));
            userRepresentation.setAttributes(attributes);

            keycloakAdminService.updateUser(userId, userRepresentation);

            if (dto.getTipoUsuario() != null) {
                Set<String> rolesAtuais = obterRolesAtuaisDoUsuario(userId);
                if (!rolesAtuais.isEmpty()) {
                    keycloakAdminService.removeRolesFromUser(userId, rolesAtuais);
                }

                Set<String> novasRoles = obterRolesPorTipo(dto.getTipoUsuario());
                keycloakAdminService.assignRolesToUser(userId, novasRoles);
            }

        } catch (Exception e) {
            logger.error("Erro ao atualizar usuário no Keycloak: ", e);
            throw new RuntimeException("Erro ao atualizar usuário no Keycloak: " + e.getMessage());
        }
    }

    private Set<String> obterRolesAtuaisDoUsuario(String userId) {
        try {
            RealmResource realmResource = keycloak.realm(realm);
            List<RoleRepresentation> roles = realmResource.users().get(userId).roles().realmLevel().listAll();
            return roles.stream()
                    .map(RoleRepresentation::getName)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            logger.error("Erro ao obter roles atuais do usuário: ", e);
            return Set.of();
        }
    }

    private Set<String> obterRolesPorTipo(String tipoUsuario) {
        return switch (tipoUsuario) {
            case "ADMIN" -> Set.of("colaborador", "admin");
            case "GESTOR" -> Set.of("gestor", "colaborador");
            default -> Set.of("colaborador");
        };
    }

    public ApiResponse<Void> resetarSenhaComEmailEKeycloak(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(email));

        String novaSenha = gerarSenhaAleatoria();
        enviarSenhaPorEmail(email, novaSenha, usuario.getNome());
        atualizarSenhaNoKeycloak(email, novaSenha);

        return ApiResponse.success("Senha redefinida com sucesso");
    }

    public ApiResponse<Void> enviarCodigoVerificacao(String email) {
        try {
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new UsuarioNaoEncontradoException(email));
            if (usuario.getStatus().equals(StatusUsuario.INATIVO)) {
                return ApiResponse.badRequest("Usuário inativo");
            }
            emailService.sendVerificationCodeEmail(email, usuario.getNome());
            return ApiResponse.success("Código de verificação enviado com sucesso");
        } catch (UsuarioNaoEncontradoException e) {
            return ApiResponse.notFound(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao enviar código de verificação: ", e);
            return ApiResponse.internalServerError("Erro ao enviar código de verificação");
        }
    }

    public ApiResponse<Void> verificarCodigo(String email, String codigo) {
        try {
            boolean codigoValido = emailService.verifyCode(email, codigo);
            if (!codigoValido) {
                return ApiResponse.badRequest("Código inválido");
            }
            return ApiResponse.success("Código válido");
        } catch (Exception e) {
            logger.error("Erro ao verificar código: ", e);
            return ApiResponse.internalServerError("Erro ao verificar código");
        }
    }

    private void enviarSenhaPorEmail(String email, String senha, String nome) {
        emailService.sendPasswordEmail(email, senha, nome);
    }

    public ApiResponse<Void> atualizarSenhaUsuario(UpdatePasswordDTO updatePasswordDTO) {
        try {
            Usuario usuario = usuarioRepository.findByEmail(updatePasswordDTO.getEmail())
                    .orElseThrow(() -> new UsuarioNaoEncontradoException(updatePasswordDTO.getEmail()));

            KeycloakResponseDTO keycloakResponse = keycloakAdminService.updatePassword(
                    usuario.getKeycloakId(), updatePasswordDTO.getSenha());

            if (!keycloakResponse.isSuccess()) {
                return ApiResponse.error(keycloakResponse.getErrorMessage(), keycloakResponse.getStatusCode());
            }

            emailService.removeVerificationCode(updatePasswordDTO.getEmail());

            return ApiResponse.success("Senha atualizada com sucesso");
        } catch (UsuarioNaoEncontradoException e) {
            return ApiResponse.notFound(e.getMessage());
        } catch (KeycloakException e) {
            return ApiResponse.error(e.getErrorMessage(), e.getStatusCode());
        } catch (Exception e) {
            logger.error("Erro ao atualizar senha do usuário: ", e);
            return ApiResponse.badRequest("Erro ao atualizar senha do usuário: " + e.getMessage());
        }
    }

    private void atualizarSenhaNoKeycloak(String email, String senha) {
        List<UserRepresentation> users = keycloak.realm(realm).users().search(email);
        if (users.isEmpty())
            return;

        String userId = users.get(0).getId();
        keycloakAdminService.updatePassword(userId, senha);
    }

    public boolean existsByEmail(String email) {
        try {
            return usuarioRepository.existsByEmail(email);
        } catch (Exception e) {
            logger.error("Erro ao verificar se o usuário existe: ", e);
            return false;
        }
    }

    public ApiResponse<Void> verificarEmailDisponibilidade(String email) {
        try {
            boolean emailExiste = usuarioRepository.existsByEmail(email);
            if (emailExiste) {
                return ApiResponse.badRequest("Email já cadastrado");
            }
            return ApiResponse.success("Email disponível");
        } catch (Exception e) {
            logger.error("Erro ao verificar disponibilidade do email: ", e);
            return ApiResponse.internalServerError("Erro ao verificar email");
        }
    }

    public ApiResponse<UsuarioResponseDTO> buscarUsuario(String keycloakId) {
        try {
            Usuario usuario = usuarioRepository.findByKeycloakId(keycloakId)
                    .orElseThrow(() -> new UsuarioNaoEncontradoException(keycloakId));
            return ApiResponse.map(usuario, UsuarioResponseDTO.class, "Usuário encontrado com sucesso");
        } catch (UsuarioNaoEncontradoException e) {
            return ApiResponse.notFound(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public ApiResponse<TipoUsuarioStatsResponseDTO> buscarEstatisticasTipoUsuario() {
        try {
            List<TipoUsuarioStatsDTO> stats = usuarioRepository.findTipoUsuarioStats();
            Long totalGeral = stats.stream()
                    .mapToLong(TipoUsuarioStatsDTO::getTotal)
                    .sum();

            TipoUsuarioStatsResponseDTO response = new TipoUsuarioStatsResponseDTO(stats, totalGeral);
            return ApiResponse.success(response, "Estatísticas dos tipos de usuários obtidas com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao buscar estatísticas dos tipos de usuários: ", e);
            return ApiResponse.internalServerError("Erro ao buscar estatísticas: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<String>> buscarSetoresDistintos() {
        try {
            List<String> setores = usuarioRepository.findDistinctSetores();
            return ApiResponse.success(setores, "Setores distintos obtidos com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao buscar setores distintos: ", e);
            return ApiResponse.badRequest("Erro ao buscar setores: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<UsuarioResponseDTO>> buscarUsuariosPorSetor(String keycloakId) {
        Usuario usuario = usuarioRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(keycloakId));

        List<Usuario> usuarios = usuarioRepository.findBySetor(usuario.getSetor(), keycloakId);
        return ApiResponse.mapList(usuarios, UsuarioResponseDTO.class, "Usuários encontrados com sucesso");
    }

    @Transactional
    public ApiResponse<Void> definirSenhaPrimeiroAcesso(PrimeiroAcessoDTO dto) {
        try {
            String email = primeiroAcessoService.validarToken(dto.getToken());

            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new UsuarioNaoEncontradoException(email));

            KeycloakResponseDTO keycloakResponse = keycloakAdminService.updatePassword(
                    usuario.getKeycloakId(), dto.getNovaSenha());

            if (!keycloakResponse.isSuccess()) {
                return ApiResponse.error(keycloakResponse.getErrorMessage(), keycloakResponse.getStatusCode());
            }
            primeiroAcessoService.removerToken(dto.getToken());

            return ApiResponse.success("Senha definida com sucesso");
        } catch (TokenExpiradoException e) {
            return ApiResponse.badRequest("Token expirado");
        } catch (TokenInvalidoException e) {
            return ApiResponse.badRequest("Token inválido");
        } catch (UsuarioNaoEncontradoException e) {
            return ApiResponse.notFound(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao definir senha de primeiro acesso: ", e);
            return ApiResponse.badRequest("Erro ao definir senha: " + e.getMessage());
        }
    }

    @Transactional
    public ApiResponse<Void> reenviarEmailPrimeiroAcesso(String email) {
        try {
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new UsuarioNaoEncontradoException(email));

            String novoToken = primeiroAcessoService.reenviarToken(email);
            emailService.sendPrimeiroAcessoEmail(email, usuario.getNome(), novoToken);

            return ApiResponse.success("Email de primeiro acesso reenviado com sucesso");
        } catch (UsuarioNaoEncontradoException e) {
            return ApiResponse.notFound(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao reenviar email de primeiro acesso: ", e);
            return ApiResponse.badRequest("Erro ao reenviar email: " + e.getMessage());
        }
    }
}