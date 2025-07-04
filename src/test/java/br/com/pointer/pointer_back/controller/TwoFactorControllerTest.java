package br.com.pointer.pointer_back.controller;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.TwoFactorSetupDTO;
import br.com.pointer.pointer_back.dto.TwoFactorVerifyDTO;
import br.com.pointer.pointer_back.dto.TwoFactorStatusDTO;
import br.com.pointer.pointer_back.service.TwoFactorService;
import br.com.pointer.pointer_back.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TwoFactorControllerTest {

    @Mock
    private TwoFactorService twoFactorService;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private TwoFactorController twoFactorController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private TwoFactorSetupDTO setupDTO;
    private TwoFactorVerifyDTO verifyDTO;
    private TwoFactorStatusDTO statusDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(twoFactorController).build();
        objectMapper = new ObjectMapper();

        // Setup TwoFactorSetupDTO
        setupDTO = new TwoFactorSetupDTO();
        setupDTO.setSecretKey("JBSWY3DPEHPK3PXP");
        setupDTO.setQrCodeUrl("otpauth://totp/Pointer%20App:joao@teste.com?secret=JBSWY3DPEHPK3PXP&issuer=Pointer%20App");

        // Setup TwoFactorVerifyDTO
        verifyDTO = new TwoFactorVerifyDTO();
        verifyDTO.setEmail("joao@teste.com");
        verifyDTO.setCode(123456);

        // Setup TwoFactorStatusDTO
        statusDTO = new TwoFactorStatusDTO();
        statusDTO.setTwoFactorEnabled(true);
        statusDTO.setTwoFactorConfigured(true);
    }

    @Test
    @WithMockUser(roles = "admin")
    void setupTwoFactor_ComPermissao_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<TwoFactorSetupDTO> response = ApiResponse.success(setupDTO, "2FA configurado com sucesso");
        when(usuarioService.setupTwoFactor("user-123")).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/2fa/setup/user-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("2FA configurado com sucesso"))
                .andExpect(jsonPath("$.content.secretKey").value("JBSWY3DPEHPK3PXP"))
                .andExpect(jsonPath("$.content.qrCodeUrl").value("otpauth://totp/Pointer%20App:joao@teste.com?secret=JBSWY3DPEHPK3PXP&issuer=Pointer%20App"));

        verify(usuarioService).setupTwoFactor("user-123");
    }

    @Test
    @WithMockUser(roles = "colaborador")
    void setupTwoFactor_ComPermissaoColaborador_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<TwoFactorSetupDTO> response = ApiResponse.success(setupDTO, "2FA configurado com sucesso");
        when(usuarioService.setupTwoFactor("user-123")).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/2fa/setup/user-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        verify(usuarioService).setupTwoFactor("user-123");
    }

    @Test
    void setupTwoFactor_SemPermissao_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<TwoFactorSetupDTO> response = ApiResponse.success(setupDTO, "2FA configurado com sucesso");
        when(usuarioService.setupTwoFactor("user-123")).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/2fa/setup/user-123"))
                .andExpect(status().isOk());

        verify(usuarioService).setupTwoFactor("user-123");
    }

    @Test
    @WithMockUser(roles = "admin")
    void getTwoFactorStatus_ComPermissao_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<TwoFactorStatusDTO> response = ApiResponse.success(statusDTO, "Status 2FA obtido com sucesso");
        when(usuarioService.getTwoFactorStatus("user-123")).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/2fa/status/user-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Status 2FA obtido com sucesso"))
                .andExpect(jsonPath("$.content.twoFactorEnabled").value(true))
                .andExpect(jsonPath("$.content.twoFactorConfigured").value(true));

        verify(usuarioService).getTwoFactorStatus("user-123");
    }

    @Test
    void getTwoFactorStatus_SemPermissao_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<TwoFactorStatusDTO> response = ApiResponse.success(statusDTO, "Status 2FA obtido com sucesso");
        when(usuarioService.getTwoFactorStatus("user-123")).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/2fa/status/user-123"))
                .andExpect(status().isOk());

        verify(usuarioService).getTwoFactorStatus("user-123");
    }

    @Test
    void verifyTwoFactor_ComDadosValidos_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<Void> response = ApiResponse.success(null, "2FA verificado com sucesso");
        when(usuarioService.verifyTwoFactor(any(TwoFactorVerifyDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/2fa/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(verifyDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("2FA verificado com sucesso"));

        verify(usuarioService).verifyTwoFactor(any(TwoFactorVerifyDTO.class));
    }

    @Test
    void verifyTwoFactor_DadosInvalidos_DeveRetornar400() throws Exception {
        // Arrange
        verifyDTO.setCode(0);
        ApiResponse<Void> response = ApiResponse.badRequest("Código é obrigatório");
        when(usuarioService.verifyTwoFactor(any(TwoFactorVerifyDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/2fa/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(verifyDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Código é obrigatório"));

        verify(usuarioService).verifyTwoFactor(any(TwoFactorVerifyDTO.class));
    }

    @Test
    @WithMockUser(roles = "admin")
    void activateTwoFactor_ComPermissao_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<Void> response = ApiResponse.success(null, "2FA ativado com sucesso");
        when(usuarioService.activateTwoFactor(anyString(), any(TwoFactorVerifyDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/2fa/activate/user-123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(verifyDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("2FA ativado com sucesso"));

        verify(usuarioService).activateTwoFactor("user-123", verifyDTO);
    }

    @Test
    void activateTwoFactor_SemPermissao_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<Void> response = ApiResponse.success(null, "2FA ativado com sucesso");
        when(usuarioService.activateTwoFactor(anyString(), any(TwoFactorVerifyDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/2fa/activate/user-123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(verifyDTO)))
                .andExpect(status().isOk());

        verify(usuarioService).activateTwoFactor("user-123", verifyDTO);
    }

    @Test
    @WithMockUser(roles = "admin")
    void disableTwoFactor_ComPermissao_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<Void> response = ApiResponse.success(null, "2FA desabilitado com sucesso");
        when(usuarioService.disableTwoFactor("user-123")).thenReturn(response);

        // Act & Assert
        mockMvc.perform(delete("/api/2fa/disable/user-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("2FA desabilitado com sucesso"));

        verify(usuarioService).disableTwoFactor("user-123");
    }

    @Test
    void disableTwoFactor_SemPermissao_DeveRetornar200() throws Exception {
        // Arrange
        ApiResponse<Void> response = ApiResponse.success(null, "2FA desabilitado com sucesso");
        when(usuarioService.disableTwoFactor("user-123")).thenReturn(response);

        // Act & Assert
        mockMvc.perform(delete("/api/2fa/disable/user-123"))
                .andExpect(status().isOk());

        verify(usuarioService).disableTwoFactor("user-123");
    }

    @Test
    @WithMockUser(roles = "admin")
    void setupTwoFactor_ErroNoService_DeveRetornar400() throws Exception {
        // Arrange
        ApiResponse<TwoFactorSetupDTO> response = ApiResponse.badRequest("Erro ao configurar 2FA");
        when(usuarioService.setupTwoFactor("user-123")).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/2fa/setup/user-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Erro ao configurar 2FA"));

        verify(usuarioService).setupTwoFactor("user-123");
    }

    @Test
    @WithMockUser(roles = "admin")
    void getTwoFactorStatus_UsuarioNaoEncontrado_DeveRetornar400() throws Exception {
        // Arrange
        ApiResponse<TwoFactorStatusDTO> response = ApiResponse.badRequest("Usuário não encontrado");
        when(usuarioService.getTwoFactorStatus("user-999")).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/2fa/status/user-999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Usuário não encontrado"));

        verify(usuarioService).getTwoFactorStatus("user-999");
    }

    @Test
    void verifyTwoFactor_CodigoIncorreto_DeveRetornar400() throws Exception {
        // Arrange
        verifyDTO.setCode(999999);
        ApiResponse<Void> response = ApiResponse.badRequest("Código inválido");
        when(usuarioService.verifyTwoFactor(any(TwoFactorVerifyDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/2fa/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(verifyDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Código inválido"));

        verify(usuarioService).verifyTwoFactor(any(TwoFactorVerifyDTO.class));
    }
} 