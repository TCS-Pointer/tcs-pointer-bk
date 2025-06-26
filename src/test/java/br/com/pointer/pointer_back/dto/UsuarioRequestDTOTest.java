package br.com.pointer.pointer_back.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

@DisplayName("UsuarioRequestDTO")
class UsuarioRequestDTOTest {

    @Test
    @DisplayName("Deve criar usuário request com todos os campos")
    void deveCriarUsuarioRequestComTodosOsCampos() {
        // Given
        String nome = "João Silva";
        String email = "joao.silva@empresa.com";
        String senha = "senha123";
        String setor = "TI";
        String cargo = "Desenvolvedor";
        String tipoUsuario = "COLABORADOR";
        
        // When
        UsuarioRequestDTO usuario = new UsuarioRequestDTO();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setSetor(setor);
        usuario.setCargo(cargo);
        usuario.setTipoUsuario(tipoUsuario);
        
        // Then
        assertNotNull(usuario);
        assertEquals(nome, usuario.getNome());
        assertEquals(email, usuario.getEmail());
        assertEquals(senha, usuario.getSenha());
        assertEquals(setor, usuario.getSetor());
        assertEquals(cargo, usuario.getCargo());
        assertEquals(tipoUsuario, usuario.getTipoUsuario());
    }

    @Test
    @DisplayName("Deve criar usuário request com campos nulos")
    void deveCriarUsuarioRequestComCamposNulos() {
        // Given
        UsuarioRequestDTO usuario = new UsuarioRequestDTO();
        
        // When & Then
        assertNotNull(usuario);
        assertNull(usuario.getNome());
        assertNull(usuario.getEmail());
        assertNull(usuario.getSenha());
        assertNull(usuario.getSetor());
        assertNull(usuario.getCargo());
        assertNull(usuario.getTipoUsuario());
    }

    @Test
    @DisplayName("Deve criar usuário request com campos vazios")
    void deveCriarUsuarioRequestComCamposVazios() {
        // Given
        UsuarioRequestDTO usuario = new UsuarioRequestDTO();
        usuario.setNome("");
        usuario.setEmail("");
        usuario.setSenha("");
        usuario.setSetor("");
        usuario.setCargo("");
        usuario.setTipoUsuario("");
        
        // When & Then
        assertNotNull(usuario);
        assertEquals("", usuario.getNome());
        assertEquals("", usuario.getEmail());
        assertEquals("", usuario.getSenha());
        assertEquals("", usuario.getSetor());
        assertEquals("", usuario.getCargo());
        assertEquals("", usuario.getTipoUsuario());
    }

    @Test
    @DisplayName("Deve criar usuário request com nome longo")
    void deveCriarUsuarioRequestComNomeLongo() {
        // Given
        UsuarioRequestDTO usuario = new UsuarioRequestDTO();
        String nomeLongo = "Este é um nome muito longo para testar o comportamento do sistema quando recebe strings com muitos caracteres e verificar se há algum limite ou problema de performance";
        
        // When
        usuario.setNome(nomeLongo);
        
        // Then
        assertEquals(nomeLongo, usuario.getNome());
    }

    @Test
    @DisplayName("Deve criar usuário request com email válido")
    void deveCriarUsuarioRequestComEmailValido() {
        // Given
        UsuarioRequestDTO usuario = new UsuarioRequestDTO();
        String emailValido = "usuario@empresa.com.br";
        
        // When
        usuario.setEmail(emailValido);
        
        // Then
        assertEquals(emailValido, usuario.getEmail());
    }

    @Test
    @DisplayName("Deve criar usuário request com email com subdomínio")
    void deveCriarUsuarioRequestComEmailComSubdominio() {
        // Given
        UsuarioRequestDTO usuario = new UsuarioRequestDTO();
        String emailComSubdominio = "usuario@subdominio.empresa.com";
        
        // When
        usuario.setEmail(emailComSubdominio);
        
        // Then
        assertEquals(emailComSubdominio, usuario.getEmail());
    }

    @Test
    @DisplayName("Deve criar usuário request com senha forte")
    void deveCriarUsuarioRequestComSenhaForte() {
        // Given
        UsuarioRequestDTO usuario = new UsuarioRequestDTO();
        String senhaForte = "Senha@123456";
        
        // When
        usuario.setSenha(senhaForte);
        
        // Then
        assertEquals(senhaForte, usuario.getSenha());
    }

    @Test
    @DisplayName("Deve criar usuário request com setor com espaços")
    void deveCriarUsuarioRequestComSetorComEspacos() {
        // Given
        UsuarioRequestDTO usuario = new UsuarioRequestDTO();
        String setorComEspacos = "Recursos Humanos";
        
        // When
        usuario.setSetor(setorComEspacos);
        
        // Then
        assertEquals(setorComEspacos, usuario.getSetor());
    }

    @Test
    @DisplayName("Deve criar usuário request com cargo com espaços")
    void deveCriarUsuarioRequestComCargoComEspacos() {
        // Given
        UsuarioRequestDTO usuario = new UsuarioRequestDTO();
        String cargoComEspacos = "Analista de Sistemas Sênior";
        
        // When
        usuario.setCargo(cargoComEspacos);
        
        // Then
        assertEquals(cargoComEspacos, usuario.getCargo());
    }

    @Test
    @DisplayName("Deve criar usuário request com diferentes tipos de usuário")
    void deveCriarUsuarioRequestComDiferentesTiposUsuario() {
        // Given
        UsuarioRequestDTO usuario = new UsuarioRequestDTO();
        
        // When & Then - COLABORADOR
        usuario.setTipoUsuario("COLABORADOR");
        assertEquals("COLABORADOR", usuario.getTipoUsuario());
        
        // When & Then - GESTOR
        usuario.setTipoUsuario("GESTOR");
        assertEquals("GESTOR", usuario.getTipoUsuario());
        
        // When & Then - RH
        usuario.setTipoUsuario("RH");
        assertEquals("RH", usuario.getTipoUsuario());
        
        // When & Then - ADMIN
        usuario.setTipoUsuario("ADMIN");
        assertEquals("ADMIN", usuario.getTipoUsuario());
    }

    @Test
    @DisplayName("Deve verificar equals e hashCode")
    void deveVerificarEqualsEHashCode() {
        // Given
        UsuarioRequestDTO usuario1 = new UsuarioRequestDTO();
        usuario1.setNome("João Silva");
        usuario1.setEmail("joao@empresa.com");
        usuario1.setTipoUsuario("COLABORADOR");
        
        UsuarioRequestDTO usuario2 = new UsuarioRequestDTO();
        usuario2.setNome("João Silva");
        usuario2.setEmail("joao@empresa.com");
        usuario2.setTipoUsuario("COLABORADOR");
        
        UsuarioRequestDTO usuario3 = new UsuarioRequestDTO();
        usuario3.setNome("Maria Silva");
        usuario3.setEmail("maria@empresa.com");
        usuario3.setTipoUsuario("GESTOR");
        
        // When & Then
        assertEquals(usuario1, usuario2);
        assertNotEquals(usuario1, usuario3);
        assertEquals(usuario1.hashCode(), usuario2.hashCode());
        assertNotEquals(usuario1.hashCode(), usuario3.hashCode());
    }

    @Test
    @DisplayName("Deve verificar toString")
    void deveVerificarToString() {
        // Given
        UsuarioRequestDTO usuario = new UsuarioRequestDTO();
        usuario.setNome("João Silva");
        usuario.setEmail("joao@empresa.com");
        usuario.setTipoUsuario("COLABORADOR");
        
        // When
        String toString = usuario.toString();
        
        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("nome=João Silva"));
        assertTrue(toString.contains("email=joao@empresa.com"));
        assertTrue(toString.contains("tipoUsuario=COLABORADOR"));
    }

    @Test
    @DisplayName("Deve criar usuário request com caracteres especiais")
    void deveCriarUsuarioRequestComCaracteresEspeciais() {
        // Given
        UsuarioRequestDTO usuario = new UsuarioRequestDTO();
        String nomeComEspeciais = "João Silva dos Santos";
        String emailComEspeciais = "joão.silva@empresa.com";
        String setorComEspeciais = "Recursos Humanos & Administração";
        
        // When
        usuario.setNome(nomeComEspeciais);
        usuario.setEmail(emailComEspeciais);
        usuario.setSetor(setorComEspeciais);
        
        // Then
        assertEquals(nomeComEspeciais, usuario.getNome());
        assertEquals(emailComEspeciais, usuario.getEmail());
        assertEquals(setorComEspeciais, usuario.getSetor());
    }

    @Test
    @DisplayName("Deve criar usuário request com valores extremos")
    void deveCriarUsuarioRequestComValoresExtremos() {
        // Given
        UsuarioRequestDTO usuario = new UsuarioRequestDTO();
        String nomeExtremo = "A".repeat(1000);
        String emailExtremo = "a".repeat(100) + "@" + "b".repeat(100) + ".com";
        
        // When
        usuario.setNome(nomeExtremo);
        usuario.setEmail(emailExtremo);
        
        // Then
        assertEquals(nomeExtremo, usuario.getNome());
        assertEquals(emailExtremo, usuario.getEmail());
    }

    @Test
    @DisplayName("Deve criar usuário request com espaços em branco")
    void deveCriarUsuarioRequestComEspacosEmBranco() {
        // Given
        UsuarioRequestDTO usuario = new UsuarioRequestDTO();
        String nomeComEspacos = "   João Silva   ";
        String emailComEspacos = "  joao@empresa.com  ";
        
        // When
        usuario.setNome(nomeComEspacos);
        usuario.setEmail(emailComEspacos);
        
        // Then
        assertEquals(nomeComEspacos, usuario.getNome());
        assertEquals(emailComEspacos, usuario.getEmail());
    }
} 