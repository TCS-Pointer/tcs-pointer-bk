package br.com.pointer.pointer_back.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.*;
import br.com.pointer.pointer_back.exception.SetorCargoInvalidoException;
import br.com.pointer.pointer_back.exception.UsuarioNaoEncontradoException;
import br.com.pointer.pointer_back.model.StatusUsuario;
import br.com.pointer.pointer_back.model.Usuario;
import br.com.pointer.pointer_back.repository.UsuarioRepository;
import br.com.pointer.pointer_back.util.ValidationUtil;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private KeycloakAdminService keycloakAdminService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private EmailService emailService;

    @Mock
    private org.keycloak.admin.client.Keycloak keycloak;

    @Mock
    private SetorCargoService setorCargoService;

    @Mock
    private PrimeiroAcessoService primeiroAcessoService;

    @Mock
    private TwoFactorService twoFactorService;

    @InjectMocks
    private UsuarioService usuarioService;

    private UsuarioDTO usuarioDTO;
    private Usuario usuario;
    private UsuarioResponseDTO usuarioResponseDTO;
    private KeycloakResponseDTO keycloakResponseDTO;

    @BeforeEach
    void setUp() {
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNome("João Silva");
        usuarioDTO.setEmail("joao@teste.com");
        usuarioDTO.setTipoUsuario("COLABORADOR");
        usuarioDTO.setSetor("TI");
        usuarioDTO.setCargo("Desenvolvedor");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@teste.com");
        usuario.setSetor("TI");
        usuario.setCargo("Desenvolvedor");
        usuario.setTipoUsuario("COLABORADOR");
        usuario.setKeycloakId("keycloak-123");
        usuario.setStatus(StatusUsuario.ATIVO);

        usuarioResponseDTO = new UsuarioResponseDTO();
        usuarioResponseDTO.setId(1L);
        usuarioResponseDTO.setNome("João Silva");
        usuarioResponseDTO.setEmail("joao@teste.com");
        usuarioResponseDTO.setSetor("TI");
        usuarioResponseDTO.setCargo("Desenvolvedor");
        usuarioResponseDTO.setTipoUsuario("COLABORADOR");
        usuarioResponseDTO.setKeycloakId("keycloak-123");
        usuarioResponseDTO.setStatus(StatusUsuario.ATIVO);

        keycloakResponseDTO = new KeycloakResponseDTO();
        keycloakResponseDTO.setSuccess(true);

        // Configurar mocks lenient para ModelMapper - usando any() para evitar strict stubbing
        lenient().when(modelMapper.map(any(UsuarioDTO.class), eq(Usuario.class))).thenReturn(usuario);
        lenient().when(modelMapper.map(any(Usuario.class), eq(UsuarioResponseDTO.class))).thenReturn(usuarioResponseDTO);
        lenient().when(modelMapper.map(any(Usuario.class), eq(UsuarioResponePDIDTO.class))).thenReturn(new UsuarioResponePDIDTO());
        
        // Configurar mocks do PrimeiroAcessoService
        lenient().when(primeiroAcessoService.gerarToken(anyString())).thenReturn("token-123");
        
        // Configurar mocks do Keycloak para evitar NullPointerException
        org.keycloak.admin.client.resource.RealmResource realmResource = mock(org.keycloak.admin.client.resource.RealmResource.class);
        org.keycloak.admin.client.resource.UsersResource usersResource = mock(org.keycloak.admin.client.resource.UsersResource.class);
        org.keycloak.admin.client.resource.UserResource userResource = mock(org.keycloak.admin.client.resource.UserResource.class);
        
        lenient().when(keycloak.realm("pointer")).thenReturn(realmResource);
        lenient().when(realmResource.users()).thenReturn(usersResource);
        lenient().when(usersResource.get(anyString())).thenReturn(userResource);
    }

    @Test
    void criarUsuario_ComSucesso_DeveRetornarUsuarioCriado() {
        // Arrange
        usuarioDTO.setSenha("senha123@"); // Adicionar senha para não ir para o fluxo de primeiro acesso
        when(usuarioRepository.existsByEmail(usuarioDTO.getEmail())).thenReturn(false);
        lenient().doNothing().when(setorCargoService).validarSetorECargo(usuarioDTO.getSetor(), usuarioDTO.getCargo());
        when(keycloakAdminService.createUserAndReturnId(anyString(), anyString(), anyString())).thenReturn(keycloakResponseDTO);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        ApiResponse<UsuarioResponseDTO> response = usuarioService.criarUsuario(usuarioDTO);

        // Assert
        assertTrue(response.isOk());
        assertEquals("Usuário criado com sucesso", response.getMessage());
        assertNotNull(response.getContent());
        verify(usuarioRepository).existsByEmail(usuarioDTO.getEmail());
        verify(keycloakAdminService).createUserAndReturnId(anyString(), anyString(), anyString());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void criarUsuario_ComEmailExistente_DeveRetornarErro() {
        // Arrange
        when(usuarioRepository.existsByEmail(usuarioDTO.getEmail())).thenReturn(true);

        // Act
        ApiResponse<UsuarioResponseDTO> response = usuarioService.criarUsuario(usuarioDTO);

        // Assert
        assertFalse(response.isOk());
        assertEquals("Já existe um usuário com este email", response.getMessage());
        verify(usuarioRepository).existsByEmail(usuarioDTO.getEmail());
        verifyNoInteractions(keycloakAdminService);
    }

    @Test
    void criarUsuario_ComSetorCargoInvalido_DeveRetornarErro() {
        // Arrange
        when(usuarioRepository.existsByEmail(usuarioDTO.getEmail())).thenReturn(false);
        doThrow(new SetorCargoInvalidoException("Setor ou cargo inválido"))
                .when(setorCargoService).validarSetorECargo(usuarioDTO.getSetor(), usuarioDTO.getCargo());

        // Act
        ApiResponse<UsuarioResponseDTO> response = usuarioService.criarUsuario(usuarioDTO);

        // Assert
        assertFalse(response.isOk());
        assertEquals("Setor ou cargo inválido", response.getMessage());
        verify(setorCargoService).validarSetorECargo(usuarioDTO.getSetor(), usuarioDTO.getCargo());
        verifyNoInteractions(keycloakAdminService);
    }

    @Test
    void criarUsuario_ComSenhaNula_DeveGerarSenhaAleatoria() {
        // Arrange
        usuarioDTO.setSenha(null);
        usuarioDTO.setTipoUsuario("ADMIN");

        when(usuarioRepository.existsByEmail("joao@teste.com")).thenReturn(false);
        when(keycloakAdminService.createUserAndReturnId(anyString(), anyString(), anyString())).thenReturn(keycloakResponseDTO);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        ApiResponse<UsuarioResponseDTO> response = usuarioService.criarUsuario(usuarioDTO);

        // Assert
        assertTrue(response.isOk());
        assertEquals("Usuário criado com sucesso. Email de primeiro acesso enviado.", response.getMessage());
        verify(usuarioRepository).existsByEmail("joao@teste.com");
        verify(keycloakAdminService).createUserAndReturnId(anyString(), anyString(), anyString());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void listarUsuarios_ComSucesso_DeveRetornarListaPaginada() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Usuario> page = new PageImpl<>(Arrays.asList(usuario), pageRequest, 1);
        
        when(usuarioRepository.findByFilters(anyString(), anyString(), any(StatusUsuario.class), anyString(), any(PageRequest.class)))
                .thenReturn(page);

        // Act
        ApiResponse<Page<UsuarioResponseDTO>> response = usuarioService.listarUsuarios(pageRequest, "ADMIN", "TI", "ATIVO", "joão");

        // Assert
        assertTrue(response.isOk());
        assertEquals("Usuários listados com sucesso", response.getMessage());
        assertNotNull(response.getContent());
        assertEquals(1, response.getContent().getTotalElements());
        verify(usuarioRepository).findByFilters("ADMIN", "TI", StatusUsuario.ATIVO, "joão", pageRequest);
    }

    @Test
    void alternarStatusUsuarioPorEmail_ComSucesso_DeveAlterarStatus() {
        // Arrange
        AlterarStatusDTO dto = new AlterarStatusDTO();
        dto.setEmailChangeStatus("joao@teste.com");
        dto.setEmailSend("admin@teste.com");
        
        when(usuarioRepository.findByEmail(dto.getEmailChangeStatus())).thenReturn(Optional.of(usuario));
        when(keycloakAdminService.disableUser(usuario.getEmail())).thenReturn(keycloakResponseDTO);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        ApiResponse<Void> response = usuarioService.alternarStatusUsuarioPorEmail(dto);

        // Assert
        assertTrue(response.isOk());
        assertEquals("Status do usuário alterado com sucesso", response.getMessage());
        verify(usuarioRepository).findByEmail(dto.getEmailChangeStatus());
        verify(keycloakAdminService).disableUser(usuario.getEmail());
    }

    @Test
    void alternarStatusUsuarioPorEmail_ComUsuarioNaoEncontrado_DeveRetornarErro() {
        // Arrange
        AlterarStatusDTO dto = new AlterarStatusDTO();
        dto.setEmailChangeStatus("joao@teste.com");
        dto.setEmailSend("admin@teste.com");
        
        when(usuarioRepository.findByEmail(dto.getEmailChangeStatus())).thenReturn(Optional.empty());

        // Act & Assert
        br.com.pointer.pointer_back.exception.KeycloakException exception = assertThrows(
            br.com.pointer.pointer_back.exception.KeycloakException.class,
            () -> usuarioService.alternarStatusUsuarioPorEmail(dto)
        );
        
        assertEquals("Usuário não encontrado com o email: joao@teste.com", exception.getMessage());
        verify(usuarioRepository).findByEmail(dto.getEmailChangeStatus());
        verifyNoInteractions(keycloakAdminService);
    }

    @Test
    void gerarSenhaAleatoria_DeveRetornarSenhaValida() {
        // Act
        String senha = usuarioService.gerarSenhaAleatoria();

        // Assert
        assertNotNull(senha);
        assertEquals(10, senha.length());
        assertTrue(senha.matches("[a-zA-Z0-9]+"));
    }

    @Test
    void atualizarUsuario_ComSucesso_DeveAtualizarUsuario() {
        // Arrange
        UsuarioUpdateDTO updateDTO = new UsuarioUpdateDTO();
        updateDTO.setNome("João Silva Atualizado");
        updateDTO.setSetor("TI");
        updateDTO.setCargo("Desenvolvedor Senior");
        updateDTO.setTipoUsuario("GESTOR");

        when(usuarioRepository.findByKeycloakId("keycloak-123")).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Mock do Keycloak com realm correto
        org.keycloak.admin.client.resource.RealmResource realmResource = mock(org.keycloak.admin.client.resource.RealmResource.class);
        org.keycloak.admin.client.resource.UsersResource usersResource = mock(org.keycloak.admin.client.resource.UsersResource.class);
        org.keycloak.admin.client.resource.UserResource userResource = mock(org.keycloak.admin.client.resource.UserResource.class);
        org.keycloak.admin.client.resource.RoleMappingResource roleMappingResource = mock(org.keycloak.admin.client.resource.RoleMappingResource.class);
        org.keycloak.admin.client.resource.RoleScopeResource roleScopeResource = mock(org.keycloak.admin.client.resource.RoleScopeResource.class);
        org.keycloak.representations.idm.UserRepresentation userRepresentation = new org.keycloak.representations.idm.UserRepresentation();
        userRepresentation.setId("keycloak-123");
        java.util.List<org.keycloak.representations.idm.UserRepresentation> userList = java.util.Collections.singletonList(userRepresentation);
        java.util.List<org.keycloak.representations.idm.RoleRepresentation> roleList = java.util.Collections.emptyList();
        lenient().when(keycloak.realm(isNull())).thenReturn(realmResource);
        lenient().when(keycloak.realm(eq("pointer"))).thenReturn(realmResource);
        lenient().when(realmResource.users()).thenReturn(usersResource);
        lenient().when(usersResource.search(anyString())).thenReturn(userList);
        lenient().when(usersResource.get(anyString())).thenReturn(userResource);
        lenient().when(userResource.roles()).thenReturn(roleMappingResource);
        lenient().when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);
        lenient().when(roleScopeResource.listAll()).thenReturn(roleList);

        // Act
        ApiResponse<UsuarioResponseDTO> response = usuarioService.atualizarUsuario(updateDTO, "keycloak-123");

        // Assert
        assertTrue(response.isOk());
        assertEquals("Usuário atualizado com sucesso", response.getMessage());
        verify(usuarioRepository).findByKeycloakId("keycloak-123");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void resetarSenhaComEmailEKeycloak_ComSucesso_DeveResetarSenha() {
        // Arrange
        String email = "joao@teste.com";
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(keycloakAdminService.updatePassword(eq(usuario.getKeycloakId()), anyString())).thenReturn(keycloakResponseDTO);
        // Mock do Keycloak com realm correto
        org.keycloak.admin.client.resource.RealmResource realmResource = mock(org.keycloak.admin.client.resource.RealmResource.class);
        org.keycloak.admin.client.resource.UsersResource usersResource = mock(org.keycloak.admin.client.resource.UsersResource.class);
        org.keycloak.representations.idm.UserRepresentation userRepresentation = new org.keycloak.representations.idm.UserRepresentation();
        userRepresentation.setId(usuario.getKeycloakId());
        java.util.List<org.keycloak.representations.idm.UserRepresentation> userList = java.util.Collections.singletonList(userRepresentation);
        lenient().when(keycloak.realm(isNull())).thenReturn(realmResource);
        lenient().when(keycloak.realm(eq("pointer"))).thenReturn(realmResource);
        lenient().when(realmResource.users()).thenReturn(usersResource);
        lenient().when(usersResource.search(email)).thenReturn(userList);

        // Act
        ApiResponse<Void> response = usuarioService.resetarSenhaComEmailEKeycloak(email);

        // Assert
        assertTrue(response.isOk());
        assertEquals("Senha redefinida com sucesso", response.getMessage());
        verify(usuarioRepository).findByEmail(email);
        verify(keycloakAdminService).updatePassword(eq(usuario.getKeycloakId()), anyString());
    }

    @Test
    void atualizarSenhaUsuario_ComSucesso_DeveAtualizarSenha() {
        // Arrange
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();
        updatePasswordDTO.setEmail("joao@teste.com");
        updatePasswordDTO.setSenha("NovaSenha123@"); // Garantir que o campo correto está setado
        
        when(usuarioRepository.findByEmail(updatePasswordDTO.getEmail())).thenReturn(Optional.of(usuario));
        when(keycloakAdminService.updatePassword(anyString(), eq("NovaSenha123@"))).thenReturn(keycloakResponseDTO);

        // Act
        ApiResponse<Void> response = usuarioService.atualizarSenhaUsuario(updatePasswordDTO);

        // Assert
        assertTrue(response.isOk());
        assertEquals("Senha atualizada com sucesso", response.getMessage());
        verify(usuarioRepository).findByEmail(updatePasswordDTO.getEmail());
        verify(keycloakAdminService).updatePassword(anyString(), eq("NovaSenha123@"));
        // Não verifica mais o save, pois o método não salva no banco
    }

    @Test
    void enviarCodigoVerificacao_ComSucesso_DeveEnviarCodigo() {
        // Arrange
        when(usuarioRepository.findByEmail("joao@teste.com")).thenReturn(Optional.of(usuario));

        // Act
        ApiResponse<Void> response = usuarioService.enviarCodigoVerificacao("joao@teste.com");

        // Assert
        assertTrue(response.isOk());
        assertEquals("Código de verificação enviado com sucesso", response.getMessage());
        verify(usuarioRepository).findByEmail("joao@teste.com");
    }

    @Test
    void verificarCodigo_ComSucesso_DeveVerificarCodigo() {
        // Arrange
        when(emailService.verifyCode(anyString(), anyString())).thenReturn(true);

        // Act
        ApiResponse<Void> response = usuarioService.verificarCodigo("joao@teste.com", "123456");

        // Assert
        assertTrue(response.isOk());
        assertEquals("Código válido", response.getMessage());
        verify(emailService).verifyCode("joao@teste.com", "123456");
    }

    @Test
    void existsByEmail_ComEmailExistente_DeveRetornarTrue() {
        // Arrange
        String email = "joao@teste.com";
        when(usuarioRepository.existsByEmail(email)).thenReturn(true);

        // Act
        boolean exists = usuarioService.existsByEmail(email);

        // Assert
        assertTrue(exists);
        verify(usuarioRepository).existsByEmail(email);
    }

    @Test
    void existsByEmail_ComEmailInexistente_DeveRetornarFalse() {
        // Arrange
        String email = "joao@teste.com";
        when(usuarioRepository.existsByEmail(email)).thenReturn(false);

        // Act
        boolean exists = usuarioService.existsByEmail(email);

        // Assert
        assertFalse(exists);
        verify(usuarioRepository).existsByEmail(email);
    }

    @Test
    void verificarEmailDisponibilidade_ComEmailDisponivel_DeveRetornarSucesso() {
        // Arrange
        String email = "joao@teste.com";
        when(usuarioRepository.existsByEmail(email)).thenReturn(false);

        // Act
        ApiResponse<Void> response = usuarioService.verificarEmailDisponibilidade(email);

        // Assert
        assertTrue(response.isOk());
        assertEquals("Email disponível", response.getMessage());
        verify(usuarioRepository).existsByEmail(email);
    }

    @Test
    void verificarEmailDisponibilidade_ComEmailIndisponivel_DeveRetornarErro() {
        // Arrange
        when(usuarioRepository.existsByEmail("joao@teste.com")).thenReturn(true);

        // Act
        ApiResponse<Void> response = usuarioService.verificarEmailDisponibilidade("joao@teste.com");

        // Assert
        assertFalse(response.isOk());
        assertEquals("Email já cadastrado", response.getMessage());
        verify(usuarioRepository).existsByEmail("joao@teste.com");
    }

    @Test
    void buscarUsuario_ComSucesso_DeveRetornarUsuario() {
        // Arrange
        when(usuarioRepository.findByKeycloakId("keycloak-123")).thenReturn(Optional.of(usuario));

        // Act
        ApiResponse<UsuarioResponseDTO> response = usuarioService.buscarUsuario("keycloak-123");

        // Assert
        assertTrue(response.isOk());
        assertEquals("Usuário encontrado com sucesso", response.getMessage());
        assertNotNull(response.getContent());
        verify(usuarioRepository).findByKeycloakId("keycloak-123");
    }

    @Test
    void buscarUsuario_ComUsuarioNaoEncontrado_DeveRetornarErro() {
        // Arrange
        when(usuarioRepository.findByKeycloakId("keycloak-123")).thenReturn(Optional.empty());

        // Act
        ApiResponse<UsuarioResponseDTO> response = usuarioService.buscarUsuario("keycloak-123");

        // Assert
        assertFalse(response.isOk());
        assertEquals("keycloak-123", response.getMessage());
        verify(usuarioRepository).findByKeycloakId("keycloak-123");
        verifyNoInteractions(modelMapper);
    }

    @Test
    void testBuscarEstatisticasTipoUsuario() {
        // Arrange
        List<TipoUsuarioStatsDTO> stats = Arrays.asList(
            new TipoUsuarioStatsDTO("COLABORADOR", 10L),
            new TipoUsuarioStatsDTO("GESTOR", 5L)
        );

        when(usuarioRepository.findTipoUsuarioStats()).thenReturn(stats);

        // Act
        ApiResponse<TipoUsuarioStatsResponseDTO> response = usuarioService.buscarEstatisticasTipoUsuario();

        // Assert
        assertTrue(response.isOk());
        verify(usuarioRepository).findTipoUsuarioStats();
    }

    @Test
    void buscarSetoresDistintos_ComSucesso_DeveRetornarSetores() {
        // Arrange
        List<String> setores = Arrays.asList("TI", "RH", "Financeiro");
        when(usuarioRepository.findDistinctSetores()).thenReturn(setores);

        // Act
        ApiResponse<List<String>> response = usuarioService.buscarSetoresDistintos();

        // Assert
        assertTrue(response.isOk());
        assertEquals("Setores distintos obtidos com sucesso", response.getMessage());
        assertNotNull(response.getContent());
        assertEquals(3, response.getContent().size());
        verify(usuarioRepository).findDistinctSetores();
    }

    @Test
    void buscarUsuariosPorSetor_ComSucesso_DeveRetornarUsuarios() {
        // Arrange
        String keycloakId = "keycloak-123";
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findBySetor(usuario.getSetor(), keycloakId)).thenReturn(usuarios);

        // Act
        ApiResponse<List<UsuarioResponseDTO>> response = usuarioService.buscarUsuariosPorSetor(keycloakId);

        // Assert
        assertTrue(response.isOk());
        assertEquals("Usuários encontrados com sucesso", response.getMessage());
        assertNotNull(response.getContent());
        assertEquals(1, response.getContent().size());
        verify(usuarioRepository).findByKeycloakId(keycloakId);
        verify(usuarioRepository).findBySetor(usuario.getSetor(), keycloakId);
    }

    @Test
    void definirSenhaPrimeiroAcesso_ComSucesso_DeveDefinirSenha() {
        // Arrange
        PrimeiroAcessoDTO primeiroAcessoDTO = new PrimeiroAcessoDTO();
        primeiroAcessoDTO.setEmail("joao@teste.com");
        primeiroAcessoDTO.setToken("token-123");
        primeiroAcessoDTO.setNovaSenha("novaSenha123");
        
        when(primeiroAcessoService.validarToken(primeiroAcessoDTO.getToken())).thenReturn("joao@teste.com");
        when(usuarioRepository.findByEmail(primeiroAcessoDTO.getEmail())).thenReturn(Optional.of(usuario));
        
        KeycloakResponseDTO keycloakResponse = new KeycloakResponseDTO();
        keycloakResponse.setSuccess(true);
        when(keycloakAdminService.updatePassword(anyString(), anyString())).thenReturn(keycloakResponse);

        // Act
        ApiResponse<Void> response = usuarioService.definirSenhaPrimeiroAcesso(primeiroAcessoDTO);

        // Assert
        assertTrue(response.isOk());
        assertEquals("Senha definida com sucesso", response.getMessage());
        verify(primeiroAcessoService).validarToken(primeiroAcessoDTO.getToken());
        verify(usuarioRepository).findByEmail(primeiroAcessoDTO.getEmail());
        verify(keycloakAdminService).updatePassword(anyString(), anyString());
        verify(primeiroAcessoService).removerToken(primeiroAcessoDTO.getToken());
    }

    @Test
    void reenviarEmailPrimeiroAcesso_ComSucesso_DeveReenviarEmail() {
        // Arrange
        when(usuarioRepository.findByEmail("joao@teste.com")).thenReturn(Optional.of(usuario));
        when(primeiroAcessoService.reenviarToken(anyString())).thenReturn("token-123");

        // Act
        ApiResponse<Void> response = usuarioService.reenviarEmailPrimeiroAcesso("joao@teste.com");

        // Assert
        assertTrue(response.isOk());
        assertEquals("Email de primeiro acesso reenviado com sucesso", response.getMessage());
        verify(usuarioRepository).findByEmail("joao@teste.com");
        verify(primeiroAcessoService).reenviarToken("joao@teste.com");
    }

    @Test
    void setupTwoFactor_ComSucesso_DeveConfigurar2FA() {
        // Arrange
        when(usuarioRepository.findByKeycloakId("keycloak-123")).thenReturn(Optional.of(usuario));
        when(twoFactorService.generateSecretKey()).thenReturn("JBSWY3DPEHPK3PXP");
        when(twoFactorService.generateQRCodeUrl(anyString(), anyString(), anyString())).thenReturn("otpauth://totp/...");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        ApiResponse<TwoFactorSetupDTO> response = usuarioService.setupTwoFactor("keycloak-123");

        // Assert
        assertTrue(response.isOk());
        assertEquals("2FA configurado com sucesso. Escaneie o QR Code e confirme com o código.", response.getMessage());
        assertNotNull(response.getContent());
        verify(usuarioRepository).findByKeycloakId("keycloak-123");
        verify(twoFactorService).generateSecretKey();
        verify(twoFactorService).generateQRCodeUrl(anyString(), anyString(), anyString());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void verifyTwoFactor_ComSucesso_DeveVerificar2FA() {
        // Arrange
        TwoFactorVerifyDTO verifyDTO = new TwoFactorVerifyDTO();
        verifyDTO.setEmail("joao@teste.com");
        verifyDTO.setCode(123456);
        
        // Configurar usuário com 2FA habilitado
        usuario.setTwoFactorEnabled(true);
        usuario.setSecretKey("JBSWY3DPEHPK3PXP");
        
        when(usuarioRepository.findByEmail(verifyDTO.getEmail())).thenReturn(Optional.of(usuario));
        when(twoFactorService.validateCode(anyString(), anyInt())).thenReturn(true);

        // Act
        ApiResponse<Void> response = usuarioService.verifyTwoFactor(verifyDTO);

        // Assert
        assertTrue(response.isOk());
        assertEquals("Código 2FA válido", response.getMessage());
        verify(twoFactorService).validateCode(anyString(), anyInt());
    }

    @Test
    void activateTwoFactor_ComSucesso_DeveAtivar2FA() {
        // Arrange
        TwoFactorVerifyDTO verifyDTO = new TwoFactorVerifyDTO();
        verifyDTO.setEmail("joao@teste.com");
        verifyDTO.setCode(123456);
        
        // Configurar usuário com secretKey mas 2FA desabilitado
        usuario.setSecretKey("JBSWY3DPEHPK3PXP");
        usuario.setTwoFactorEnabled(false);
        
        when(usuarioRepository.findByKeycloakId("keycloak-123")).thenReturn(Optional.of(usuario));
        when(twoFactorService.validateCode(anyString(), anyInt())).thenReturn(true);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        ApiResponse<Void> response = usuarioService.activateTwoFactor("keycloak-123", verifyDTO);

        // Assert
        assertTrue(response.isOk());
        assertEquals("2FA ativado com sucesso", response.getMessage());
        verify(usuarioRepository).findByKeycloakId("keycloak-123");
        verify(twoFactorService).validateCode(anyString(), anyInt());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void disableTwoFactor_ComSucesso_DeveDesativar2FA() {
        // Arrange
        when(usuarioRepository.findByKeycloakId("keycloak-123")).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        ApiResponse<Void> response = usuarioService.disableTwoFactor("keycloak-123");

        // Assert
        assertTrue(response.isOk());
        assertEquals("2FA desabilitado com sucesso", response.getMessage());
        verify(usuarioRepository).findByKeycloakId("keycloak-123");
    }

    @Test
    void getTwoFactorStatus_ComSucesso_DeveRetornarStatus() {
        // Arrange
        when(usuarioRepository.findByKeycloakId("keycloak-123")).thenReturn(Optional.of(usuario));

        // Act
        ApiResponse<TwoFactorStatusDTO> response = usuarioService.getTwoFactorStatus("keycloak-123");

        // Assert
        assertTrue(response.isOk());
        assertEquals("Status do 2FA obtido com sucesso", response.getMessage());
        assertNotNull(response.getContent());
        verify(usuarioRepository).findByKeycloakId("keycloak-123");
    }

    @Test
    void testCriarUsuarioComExcecaoKeycloak() {
        // Arrange
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNome("João Silva");
        dto.setEmail("joao@teste.com");
        dto.setTipoUsuario("COLABORADOR");
        dto.setSetor("TI");
        dto.setCargo("Desenvolvedor");

        when(usuarioRepository.existsByEmail("joao@teste.com")).thenReturn(false);
        when(keycloakAdminService.createUserAndReturnId(anyString(), anyString(), anyString()))
                .thenThrow(new RuntimeException("Erro no Keycloak"));

        // Act
        ApiResponse<UsuarioResponseDTO> response = usuarioService.criarUsuario(dto);

        // Assert
        assertFalse(response.isOk());
        assertTrue(response.getMessage().contains("Erro ao criar usuário"));
    }

    @Test
    void testListarUsuariosFeedback() {
        // Arrange
        String keycloakId = "user123";
        List<Usuario> usuarios = Arrays.asList(new Usuario(), new Usuario());
        
        when(usuarioRepository.findByStatusAndKeycloakIdNot(StatusUsuario.ATIVO, keycloakId)).thenReturn(usuarios);

        // Act
        ApiResponse<List<UsuarioResponePDIDTO>> response = usuarioService.listarUsuariosFeedback(keycloakId);

        // Assert
        assertTrue(response.isOk());
        verify(usuarioRepository).findByStatusAndKeycloakIdNot(StatusUsuario.ATIVO, keycloakId);
    }

    @Test
    void testSetupTwoFactor() {
        // Arrange
        String keycloakId = "user123";
        Usuario usuario = new Usuario();
        usuario.setEmail("joao@teste.com");

        when(usuarioRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.of(usuario));
        when(twoFactorService.generateSecretKey()).thenReturn("JBSWY3DPEHPK3PXP");
        when(twoFactorService.generateQRCodeUrl(anyString(), anyString(), anyString())).thenReturn("otpauth://totp/...");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        ApiResponse<TwoFactorSetupDTO> response = usuarioService.setupTwoFactor(keycloakId);

        // Assert
        assertTrue(response.isOk());
        assertEquals("2FA configurado com sucesso. Escaneie o QR Code e confirme com o código.", response.getMessage());
        assertNotNull(response.getContent());
        verify(twoFactorService).generateSecretKey();
        verify(twoFactorService).generateQRCodeUrl(anyString(), anyString(), anyString());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void testVerifyTwoFactorCodigoInvalido() {
        // Arrange
        TwoFactorVerifyDTO verifyDTO = new TwoFactorVerifyDTO();
        verifyDTO.setEmail("joao@teste.com");
        verifyDTO.setCode(123456);

        Usuario usuario = new Usuario();
        usuario.setEmail("joao@teste.com");
        usuario.setTwoFactorEnabled(true);
        usuario.setSecretKey("secret123");

        when(usuarioRepository.findByEmail(verifyDTO.getEmail())).thenReturn(Optional.of(usuario));
        when(twoFactorService.validateCode(anyString(), anyInt())).thenReturn(false);

        // Act
        ApiResponse<Void> response = usuarioService.verifyTwoFactor(verifyDTO);

        // Assert
        assertFalse(response.isOk());
        assertEquals("Código 2FA inválido", response.getMessage());
    }

    @Test
    void testActivateTwoFactor() {
        // Arrange
        String keycloakId = "user123";
        TwoFactorVerifyDTO verifyDTO = new TwoFactorVerifyDTO();
        verifyDTO.setEmail("joao@teste.com");
        verifyDTO.setCode(123456);

        Usuario usuario = new Usuario();
        usuario.setEmail("joao@teste.com");
        usuario.setSecretKey("secret123");
        usuario.setTwoFactorEnabled(false);

        when(usuarioRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.of(usuario));
        when(twoFactorService.validateCode(anyString(), anyInt())).thenReturn(true);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        ApiResponse<Void> response = usuarioService.activateTwoFactor(keycloakId, verifyDTO);

        // Assert
        assertTrue(response.isOk());
        assertEquals("2FA ativado com sucesso", response.getMessage());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void testActivateTwoFactorCodigoInvalido() {
        // Arrange
        String keycloakId = "user123";
        TwoFactorVerifyDTO verifyDTO = new TwoFactorVerifyDTO();
        verifyDTO.setEmail("joao@teste.com");
        verifyDTO.setCode(123456);

        Usuario usuario = new Usuario();
        usuario.setEmail("joao@teste.com");
        usuario.setSecretKey("secret123");
        usuario.setTwoFactorEnabled(false);

        when(usuarioRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.of(usuario));
        when(twoFactorService.validateCode(anyString(), anyInt())).thenReturn(false);

        // Act
        ApiResponse<Void> response = usuarioService.activateTwoFactor(keycloakId, verifyDTO);

        // Assert
        assertFalse(response.isOk());
        assertEquals("Código 2FA inválido", response.getMessage());
    }

    @Test
    void testDisableTwoFactor() {
        // Arrange
        String keycloakId = "user123";
        Usuario usuario = new Usuario();
        usuario.setEmail("joao@teste.com");
        usuario.setNome("João Silva");

        when(usuarioRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        ApiResponse<Void> response = usuarioService.disableTwoFactor(keycloakId);

        // Assert
        assertTrue(response.isOk());
        assertEquals("2FA desabilitado com sucesso", response.getMessage());
        verify(usuarioRepository).save(usuario);
        verify(emailService).sendTwoFactorDisabledEmail(usuario.getEmail(), usuario.getNome());
    }

    @Test
    void testGetTwoFactorStatus() {
        // Arrange
        String keycloakId = "user123";
        Usuario usuario = new Usuario();
        usuario.setEmail("joao@teste.com");

        when(usuarioRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.of(usuario));

        // Act
        ApiResponse<TwoFactorStatusDTO> response = usuarioService.getTwoFactorStatus(keycloakId);

        // Assert
        assertTrue(response.isOk());
        assertEquals("Status do 2FA obtido com sucesso", response.getMessage());
        assertNotNull(response.getContent());
        verify(usuarioRepository).findByKeycloakId(keycloakId);
    }
} 