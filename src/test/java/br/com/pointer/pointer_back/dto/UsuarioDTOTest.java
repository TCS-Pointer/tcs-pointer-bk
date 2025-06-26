package br.com.pointer.pointer_back.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.pointer.pointer_back.model.StatusUsuario;

@DisplayName("UsuarioDTO")
class UsuarioDTOTest {

    private UsuarioDTO usuario;

    @BeforeEach
    void setUp() {
        usuario = new UsuarioDTO();
    }

    @Test
    @DisplayName("Deve criar UsuarioDTO com valores padrão")
    void deveCriarComValoresPadrao() {
        assertNotNull(usuario);
    }

    @Test
    @DisplayName("Deve testar equals e hashCode com objetos iguais")
    void deveTestarEqualsEHashCodeComObjetosIguais() {
        UsuarioDTO usuario2 = new UsuarioDTO();
        assertEquals(usuario, usuario2);
        assertEquals(usuario.hashCode(), usuario2.hashCode());
    }

    @Test
    @DisplayName("Deve testar equals e hashCode com objetos diferentes")
    void deveTestarEqualsEHashCodeComObjetosDiferentes() {
        UsuarioDTO usuario2 = new UsuarioDTO();
        usuario2.setNome("Outro Nome");
        assertNotEquals(usuario, usuario2);
        assertNotEquals(usuario.hashCode(), usuario2.hashCode());
    }

    @Test
    @DisplayName("Deve testar toString não nulo")
    void deveTestarToString() {
        assertNotNull(usuario.toString());
    }

    @Test
    @DisplayName("Deve criar usuário com todos os campos")
    void deveCriarUsuarioComTodosOsCampos() {
        // Given
        String nome = "João Silva";
        String email = "joao@empresa.com";
        String senha = "senha123";
        String setor = "TI";
        String cargo = "Desenvolvedor";
        String tipoUsuario = "COLABORADOR";
        String keycloakId = "123456";
        StatusUsuario status = StatusUsuario.ATIVO;
        
        // When
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setKeycloakId(keycloakId);
        usuario.setSetor(setor);
        usuario.setCargo(cargo);
        usuario.setTipoUsuario(tipoUsuario);
        usuario.setStatus(status);
        
        // Then
        assertNotNull(usuario);
        assertEquals(nome, usuario.getNome());
        assertEquals(email, usuario.getEmail());
        assertEquals(senha, usuario.getSenha());
        assertEquals(keycloakId, usuario.getKeycloakId());
        assertEquals(setor, usuario.getSetor());
        assertEquals(cargo, usuario.getCargo());
        assertEquals(tipoUsuario, usuario.getTipoUsuario());
        assertEquals(status, usuario.getStatus());
    }

    @Test
    @DisplayName("Deve criar usuário com campos nulos")
    void deveCriarUsuarioComCamposNulos() {
        // Given
        UsuarioDTO usuario = new UsuarioDTO();
        
        // When & Then
        assertNotNull(usuario);
        assertNull(usuario.getNome());
        assertNull(usuario.getEmail());
        assertNull(usuario.getKeycloakId());
        assertNull(usuario.getSetor());
        assertNull(usuario.getCargo());
        assertNull(usuario.getTipoUsuario());
        assertNull(usuario.getStatus());
    }

    @Test
    @DisplayName("Deve criar usuário com campos vazios")
    void deveCriarUsuarioComCamposVazios() {
        // Given
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setNome("");
        usuario.setEmail("");
        usuario.setKeycloakId("");
        usuario.setSetor("");
        usuario.setCargo("");
        usuario.setTipoUsuario("");
        // Status não pode ser String vazio, então não testamos
        
        // When & Then
        assertNotNull(usuario);
        assertEquals("", usuario.getNome());
        assertEquals("", usuario.getEmail());
        assertEquals("", usuario.getKeycloakId());
        assertEquals("", usuario.getSetor());
        assertEquals("", usuario.getCargo());
        assertEquals("", usuario.getTipoUsuario());
        // assertEquals("", usuario.getStatus()); // Removido pois status é enum
    }

    @Test
    @DisplayName("Deve criar usuário com diferentes tipos")
    void deveCriarUsuarioComDiferentesTipos() {
        // Given
        UsuarioDTO usuario = new UsuarioDTO();
        
        // When & Then - ADMIN
        usuario.setTipoUsuario("ADMIN");
        assertEquals("ADMIN", usuario.getTipoUsuario());
        
        // When & Then - GESTOR
        usuario.setTipoUsuario("GESTOR");
        assertEquals("GESTOR", usuario.getTipoUsuario());
        
        // When & Then - COLABORADOR
        usuario.setTipoUsuario("COLABORADOR");
        assertEquals("COLABORADOR", usuario.getTipoUsuario());
    }

    @Test
    @DisplayName("Deve criar usuário com diferentes status")
    void deveCriarUsuarioComDiferentesStatus() {
        // Given
        UsuarioDTO usuario = new UsuarioDTO();
        
        // When & Then - ATIVO
        usuario.setStatus(StatusUsuario.ATIVO);
        assertEquals(StatusUsuario.ATIVO, usuario.getStatus());
        
        // When & Then - INATIVO
        usuario.setStatus(StatusUsuario.INATIVO);
        assertEquals(StatusUsuario.INATIVO, usuario.getStatus());
        
        // PENDENTE não existe no enum StatusUsuario, então removemos esse teste
    }

    @Test
    @DisplayName("Deve criar usuário com nome longo")
    void deveCriarUsuarioComNomeLongo() {
        // Given
        UsuarioDTO usuario = new UsuarioDTO();
        String nomeLongo = "João Pedro Silva Santos Oliveira Costa Pereira Rodrigues Almeida Ferreira";
        
        // When
        usuario.setNome(nomeLongo);
        
        // Then
        assertEquals(nomeLongo, usuario.getNome());
    }

    @Test
    @DisplayName("Deve criar usuário com email complexo")
    void deveCriarUsuarioComEmailComplexo() {
        // Given
        UsuarioDTO usuario = new UsuarioDTO();
        String emailComplexo = "joao.pedro.silva+teste@empresa-departamento.com.br";
        
        // When
        usuario.setEmail(emailComplexo);
        
        // Then
        assertEquals(emailComplexo, usuario.getEmail());
    }

    @Test
    @DisplayName("Deve criar usuário com keycloakId complexo")
    void deveCriarUsuarioComKeycloakIdComplexo() {
        // Given
        UsuarioDTO usuario = new UsuarioDTO();
        String keycloakIdComplexo = "550e8400-e29b-41d4-a716-446655440000";
        
        // When
        usuario.setKeycloakId(keycloakIdComplexo);
        
        // Then
        assertEquals(keycloakIdComplexo, usuario.getKeycloakId());
    }

    @Test
    @DisplayName("Deve criar usuário com setor e cargo especiais")
    void deveCriarUsuarioComSetorECargoEspeciais() {
        // Given
        UsuarioDTO usuario = new UsuarioDTO();
        String setorEspecial = "TI & Desenvolvimento";
        String cargoEspecial = "Desenvolvedor Full-Stack";
        
        // When
        usuario.setSetor(setorEspecial);
        usuario.setCargo(cargoEspecial);
        
        // Then
        assertEquals(setorEspecial, usuario.getSetor());
        assertEquals(cargoEspecial, usuario.getCargo());
    }

    @Test
    @DisplayName("Deve criar usuário com caracteres especiais")
    void deveCriarUsuarioComCaracteresEspeciais() {
        // Given
        UsuarioDTO usuario = new UsuarioDTO();
        String nomeComEspeciais = "João Pedro Silva-Santos";
        String emailComEspeciais = "joão.pedro@empresa.com";
        String setorComEspeciais = "TI & Desenvolvimento";
        
        // When
        usuario.setNome(nomeComEspeciais);
        usuario.setEmail(emailComEspeciais);
        usuario.setSetor(setorComEspeciais);
        
        // Then
        assertEquals(nomeComEspeciais, usuario.getNome());
        assertEquals(emailComEspeciais, usuario.getEmail());
        assertEquals(setorComEspeciais, usuario.getSetor());
    }

} 