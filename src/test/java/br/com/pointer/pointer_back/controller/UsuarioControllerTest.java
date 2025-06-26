package br.com.pointer.pointer_back.controller;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.*;
import br.com.pointer.pointer_back.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;
    
    @InjectMocks
    private UsuarioController usuarioController;
    
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).build();
    }

    @Test
    void criarUsuario_DeveRetornarOk() throws Exception {
        ApiResponse<UsuarioResponseDTO> response = ApiResponse.success(new UsuarioResponseDTO(), "OK");
        when(usuarioService.criarUsuario(any())).thenReturn(response);
        
        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    void definirSenhaPrimeiroAcesso_DeveRetornarOk() throws Exception {
        ApiResponse<Void> response = ApiResponse.success(null, "OK");
        when(usuarioService.definirSenhaPrimeiroAcesso(any())).thenReturn(response);
        
        mockMvc.perform(post("/api/usuarios/primeiro-acesso")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    void reenviarEmailPrimeiroAcesso_DeveRetornarOk() throws Exception {
        ApiResponse<Void> response = ApiResponse.success(null, "OK");
        when(usuarioService.reenviarEmailPrimeiroAcesso(anyString())).thenReturn(response);
        
        mockMvc.perform(post("/api/usuarios/primeiro-acesso/reenviar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"teste@teste.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    void listarUsuarios_DeveRetornarOk() throws Exception {
        // Arrange
        ApiResponse<Page<UsuarioResponseDTO>> response = ApiResponse.success(null, "OK");
        when(usuarioService.listarUsuarios(any(PageRequest.class), any(), any(), any(), any())).thenReturn(response);
        
        // Act & Assert
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk());
    }

    @Test
    void alterarStatus_DeveRetornarOk() throws Exception {
        ApiResponse<Void> response = ApiResponse.success(null, "OK");
        when(usuarioService.alternarStatusUsuarioPorEmail(any())).thenReturn(response);
        
        mockMvc.perform(put("/api/usuarios/alterar-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    void atualizarUsuario_DeveRetornarOk() throws Exception {
        ApiResponse<UsuarioResponseDTO> response = ApiResponse.success(new UsuarioResponseDTO(), "OK");
        when(usuarioService.atualizarUsuario(any(), eq("abc"))).thenReturn(response);
        
        mockMvc.perform(put("/api/usuarios/abc")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    void atualizarSenha_DeveRetornarOk() throws Exception {
        ApiResponse<Void> response = ApiResponse.success(null, "OK");
        when(usuarioService.atualizarSenhaUsuario(any())).thenReturn(response);
        
        mockMvc.perform(post("/api/usuarios/atualizar-senha")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    void esqueceuSenha_DeveRetornarOk() throws Exception {
        ApiResponse<Void> response = ApiResponse.success(null, "OK");
        when(usuarioService.enviarCodigoVerificacao(anyString())).thenReturn(response);
        
        mockMvc.perform(post("/api/usuarios/esqueceu-senha")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"teste@teste.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    void verificarCodigo_DeveRetornarOk() throws Exception {
        ApiResponse<Void> response = ApiResponse.success(null, "OK");
        when(usuarioService.verificarCodigo(anyString(), anyString())).thenReturn(response);
        
        mockMvc.perform(post("/api/usuarios/verificar-codigo")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"teste@teste.com\",\"codigo\":\"1234\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    void redefinirSenha_DeveRetornarOk() throws Exception {
        ApiResponse<Void> response = ApiResponse.success(null, "OK");
        when(usuarioService.atualizarSenhaUsuario(any())).thenReturn(response);
        
        mockMvc.perform(post("/api/usuarios/redefinir-senha")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    void verificarEmail_DeveRetornarOk() throws Exception {
        ApiResponse<Void> response = ApiResponse.success(null, "OK");
        when(usuarioService.verificarEmailDisponibilidade(anyString())).thenReturn(response);
        
        mockMvc.perform(get("/api/usuarios/verificar-email/teste@teste.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    void buscarUsuario_DeveRetornarOk() throws Exception {
        ApiResponse<UsuarioResponseDTO> response = ApiResponse.success(new UsuarioResponseDTO(), "OK");
        when(usuarioService.buscarUsuario("abc")).thenReturn(response);
        
        mockMvc.perform(get("/api/usuarios/abc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    void buscarEstatisticasTipoUsuario_DeveRetornarOk() throws Exception {
        TipoUsuarioStatsResponseDTO dto = new TipoUsuarioStatsResponseDTO(List.of(), 0L);
        ApiResponse<TipoUsuarioStatsResponseDTO> response = ApiResponse.success(dto, "OK");
        when(usuarioService.buscarEstatisticasTipoUsuario()).thenReturn(response);
        
        mockMvc.perform(get("/api/usuarios/estatisticas/tipos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    void buscarSetoresDistintos_DeveRetornarOk() throws Exception {
        ApiResponse<List<String>> response = ApiResponse.success(List.of(), "OK");
        when(usuarioService.buscarSetoresDistintos()).thenReturn(response);
        
        mockMvc.perform(get("/api/usuarios/setores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    void buscarUsuariosPorSetor_DeveRetornarOk() throws Exception {
        ApiResponse<List<UsuarioResponseDTO>> response = ApiResponse.success(List.of(), "OK");
        when(usuarioService.buscarUsuariosPorSetor("abc")).thenReturn(response);
        
        mockMvc.perform(get("/api/usuarios/setor/abc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    void listarUsuariosFeedback_DeveRetornarOk() throws Exception {
        ApiResponse<List<UsuarioResponePDIDTO>> response = ApiResponse.success(List.of(), "OK");
        when(usuarioService.listarUsuariosFeedback(anyString())).thenReturn(response);
        
        mockMvc.perform(post("/api/usuarios/listar-usuarios-feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"abc\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OK"));
    }
} 