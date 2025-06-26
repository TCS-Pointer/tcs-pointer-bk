package br.com.pointer.pointer_back.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JWTConverterTest {

    @Mock
    private Jwt jwt;

    private JWTConverter jwtConverter;
    private Map<String, Collection<String>> realmAccess;
    private Collection<String> roles;

    @BeforeEach
    void setUp() {
        jwtConverter = new JWTConverter();
        realmAccess = new HashMap<>();
        roles = Arrays.asList("admin", "gestor", "colaborador");
        realmAccess.put("roles", roles);
    }

    @Test
    void convert_ComRolesValidas_DeveRetornarJwtAuthenticationToken() {
        // Arrange
        when(jwt.getClaim("realm_access")).thenReturn(realmAccess);

        // Act
        AbstractAuthenticationToken result = jwtConverter.convert(jwt);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof JwtAuthenticationToken);
        assertEquals(jwt, ((JwtAuthenticationToken) result).getToken());
        
        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertEquals(3, authorities.size());
        
        List<String> expectedRoles = Arrays.asList("ROLE_admin", "ROLE_gestor", "ROLE_colaborador");
        assertTrue(authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .allMatch(expectedRoles::contains));
    }

    @Test
    void convert_ComRoleUnica_DeveRetornarTokenComUmaRole() {
        // Arrange
        Collection<String> singleRole = Arrays.asList("admin");
        realmAccess.put("roles", singleRole);
        when(jwt.getClaim("realm_access")).thenReturn(realmAccess);

        // Act
        AbstractAuthenticationToken result = jwtConverter.convert(jwt);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof JwtAuthenticationToken);
        
        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ROLE_admin", authorities.iterator().next().getAuthority());
    }

    @Test
    void convert_ComRolesVazias_DeveRetornarTokenSemRoles() {
        // Arrange
        Collection<String> emptyRoles = Arrays.asList();
        realmAccess.put("roles", emptyRoles);
        when(jwt.getClaim("realm_access")).thenReturn(realmAccess);

        // Act
        AbstractAuthenticationToken result = jwtConverter.convert(jwt);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof JwtAuthenticationToken);
        
        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertEquals(0, authorities.size());
    }

    @Test
    void convert_ComRolesNulas_DeveRetornarTokenComRolesNulas() {
        // Arrange
        Collection<String> rolesComNulos = Arrays.asList(null, "admin", null);
        realmAccess.put("roles", rolesComNulos);
        when(jwt.getClaim("realm_access")).thenReturn(realmAccess);

        // Act
        AbstractAuthenticationToken result = jwtConverter.convert(jwt);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof JwtAuthenticationToken);
        
        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertEquals(1, authorities.size()); // Apenas a role "admin" é válida
        
        assertEquals("ROLE_admin", authorities.iterator().next().getAuthority());
    }

    @Test
    void convert_ComRealmAccessNulo_DeveRetornarTokenSemRoles() {
        // Arrange
        when(jwt.getClaim("realm_access")).thenReturn(null);

        // Act
        AbstractAuthenticationToken result = jwtConverter.convert(jwt);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof JwtAuthenticationToken);
        
        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertEquals(0, authorities.size());
    }

    @Test
    void convert_ComRolesComEspacos_DeveRetornarTokenComRolesTratadas() {
        // Arrange
        Collection<String> rolesComEspacos = Arrays.asList(" admin ", " gestor ", " colaborador ");
        realmAccess.put("roles", rolesComEspacos);
        when(jwt.getClaim("realm_access")).thenReturn(realmAccess);

        // Act
        AbstractAuthenticationToken result = jwtConverter.convert(jwt);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof JwtAuthenticationToken);
        
        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertEquals(3, authorities.size());
        
        List<String> expectedRoles = Arrays.asList("ROLE_ admin ", "ROLE_ gestor ", "ROLE_ colaborador ");
        assertTrue(authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .allMatch(expectedRoles::contains));
    }

    @Test
    void convert_ComRolesVazias_DeveRetornarTokenComRolesVazias() {
        // Arrange
        Collection<String> rolesVazias = Arrays.asList("", "admin", "");
        realmAccess.put("roles", rolesVazias);
        when(jwt.getClaim("realm_access")).thenReturn(realmAccess);

        // Act
        AbstractAuthenticationToken result = jwtConverter.convert(jwt);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof JwtAuthenticationToken);
        
        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertEquals(3, authorities.size());
        
        List<String> expectedRoles = Arrays.asList("ROLE_", "ROLE_admin", "ROLE_");
        assertTrue(authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .allMatch(expectedRoles::contains));
    }

    @Test
    void convert_ComRolesEspeciais_DeveRetornarTokenComRolesEspeciais() {
        // Arrange
        Collection<String> rolesEspeciais = Arrays.asList("user-admin", "gestor_rh", "colaborador.ti");
        realmAccess.put("roles", rolesEspeciais);
        when(jwt.getClaim("realm_access")).thenReturn(realmAccess);

        // Act
        AbstractAuthenticationToken result = jwtConverter.convert(jwt);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof JwtAuthenticationToken);
        
        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertEquals(3, authorities.size());
        
        List<String> expectedRoles = Arrays.asList("ROLE_user-admin", "ROLE_gestor_rh", "ROLE_colaborador.ti");
        assertTrue(authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .allMatch(expectedRoles::contains));
    }

    @Test
    void convert_ComRolesMuitoLongas_DeveRetornarTokenComRolesLongas() {
        // Arrange
        String roleLonga = "role_muito_longa_com_muitos_caracteres_para_testar_comportamento_com_strings_grandes";
        Collection<String> rolesLongas = Arrays.asList(roleLonga);
        realmAccess.put("roles", rolesLongas);
        when(jwt.getClaim("realm_access")).thenReturn(realmAccess);

        // Act
        AbstractAuthenticationToken result = jwtConverter.convert(jwt);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof JwtAuthenticationToken);
        
        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ROLE_" + roleLonga, authorities.iterator().next().getAuthority());
    }

    @Test
    void convert_ComRolesComCaracteresEspeciais_DeveRetornarTokenComRolesEspeciais() {
        // Arrange
        Collection<String> rolesEspeciais = Arrays.asList("admin@test", "gestor#123", "colaborador$456");
        realmAccess.put("roles", rolesEspeciais);
        when(jwt.getClaim("realm_access")).thenReturn(realmAccess);

        // Act
        AbstractAuthenticationToken result = jwtConverter.convert(jwt);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof JwtAuthenticationToken);
        
        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertEquals(3, authorities.size());
        
        List<String> expectedRoles = Arrays.asList("ROLE_admin@test", "ROLE_gestor#123", "ROLE_colaborador$456");
        assertTrue(authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .allMatch(expectedRoles::contains));
    }

    @Test
    void convert_ComRolesCaseSensitive_DeveRetornarTokenComRolesCaseSensitive() {
        // Arrange
        Collection<String> rolesCaseSensitive = Arrays.asList("Admin", "GESTOR", "Colaborador");
        realmAccess.put("roles", rolesCaseSensitive);
        when(jwt.getClaim("realm_access")).thenReturn(realmAccess);

        // Act
        AbstractAuthenticationToken result = jwtConverter.convert(jwt);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof JwtAuthenticationToken);
        
        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertEquals(3, authorities.size());
        
        List<String> expectedRoles = Arrays.asList("ROLE_Admin", "ROLE_GESTOR", "ROLE_Colaborador");
        assertTrue(authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .allMatch(expectedRoles::contains));
    }

    @Test
    void convert_ComRolesDuplicadas_DeveRetornarTokenComRolesDuplicadas() {
        // Arrange
        Collection<String> rolesDuplicadas = Arrays.asList("admin", "admin", "gestor", "gestor");
        realmAccess.put("roles", rolesDuplicadas);
        when(jwt.getClaim("realm_access")).thenReturn(realmAccess);

        // Act
        AbstractAuthenticationToken result = jwtConverter.convert(jwt);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof JwtAuthenticationToken);
        
        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertEquals(4, authorities.size());
        
        List<String> expectedRoles = Arrays.asList("ROLE_admin", "ROLE_admin", "ROLE_gestor", "ROLE_gestor");
        assertTrue(authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .allMatch(expectedRoles::contains));
    }

    @Test
    void convert_ComJwtNulo_DeveLancarExcecao() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            jwtConverter.convert(null);
        });
    }

    @Test
    void convert_ComRealmAccessSemRoles_DeveRetornarTokenSemRoles() {
        // Arrange
        Map<String, Collection<String>> realmAccessSemRoles = new HashMap<>();
        when(jwt.getClaim("realm_access")).thenReturn(realmAccessSemRoles);

        // Act
        AbstractAuthenticationToken result = jwtConverter.convert(jwt);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof JwtAuthenticationToken);
        
        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertEquals(0, authorities.size());
    }

    @Test
    void convert_ComRolesComNumeros_DeveRetornarTokenComRolesNumericas() {
        // Arrange
        Collection<String> rolesNumericas = Arrays.asList("123", "456", "789");
        realmAccess.put("roles", rolesNumericas);
        when(jwt.getClaim("realm_access")).thenReturn(realmAccess);

        // Act
        AbstractAuthenticationToken result = jwtConverter.convert(jwt);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof JwtAuthenticationToken);
        
        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertEquals(3, authorities.size());
        
        List<String> expectedRoles = Arrays.asList("ROLE_123", "ROLE_456", "ROLE_789");
        assertTrue(authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .allMatch(expectedRoles::contains));
    }
} 