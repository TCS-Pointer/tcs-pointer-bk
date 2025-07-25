package br.com.pointer.pointer_back.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.exception.KeycloakException;
import br.com.pointer.pointer_back.repository.UsuarioRepository;
import br.com.pointer.pointer_back.model.Usuario;

import java.util.Optional;

@RequestMapping("/token")
@RestController
public class TokenController {
    private static final Logger logger = LoggerFactory.getLogger(TokenController.class);

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.auth-server-url:http://localhost:8080}")
    private String authServerUrl;

    @Value("${keycloak.realm:pointer}")
    private String realm;

    @Value("${keycloak.client-id:pointer}")
    private String clientId;

    private final UsuarioRepository usuarioRepository;

    public TokenController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    public ApiResponse<TokenResponse> token(@RequestBody LoginRequest loginRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("client_id", clientId);
            formData.add("client_secret", clientSecret);
            formData.add("username", loginRequest.username());
            formData.add("password", loginRequest.password());
            formData.add("grant_type", "password");
            formData.add("scope", "openid");
            formData.add("expires_in", "100000");

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);
            String tokenUrl = String.format("%s/realms/%s/protocol/openid-connect/token", authServerUrl, realm);

            ResponseEntity<TokenResponse> response = restTemplate.postForEntity(tokenUrl, entity, TokenResponse.class);
            
            // Buscar informações de 2FA do usuário
            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(loginRequest.username());
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                response.getBody().setTwo_factor_enabled(usuario.getTwoFactorEnabled());
                response.getBody().setTwo_factor_configured(usuario.getSecretKey() != null);
            } else {
                // Usuário não encontrado no banco, definir como false
                response.getBody().setTwo_factor_enabled(false);
                response.getBody().setTwo_factor_configured(false);
            }
            
            logger.info("Login do username: {}", loginRequest.username());
            return new ApiResponse<TokenResponse>().ok(response.getBody(), "Token gerado com sucesso");
        } catch (HttpClientErrorException e) {
            logger.error("Erro ao gerar token: {}", e.getMessage(), e);

            String errorMessage;
            String errorCode;
            int statusCode;
            String responseBody = e.getResponseBodyAsString();

            if (responseBody.contains("Account disabled")) {
                errorMessage = "Conta desativada. Entre em contato com o administrador do sistema.";
                errorCode = "ACCOUNT_DISABLED";
                statusCode = 403;
            } else {
                switch (e.getStatusCode().value()) {
                    case 401:
                        errorMessage = "Credenciais inválidas";
                        errorCode = "INVALID_CREDENTIALS";
                        statusCode = 401;
                        break;
                    case 403:
                        errorMessage = "Acesso negado";
                        errorCode = "ACCESS_DENIED";
                        statusCode = 403;
                        break;
                    case 400:
                        errorMessage = "Requisição inválida";
                        errorCode = "INVALID_REQUEST";
                        statusCode = 400;
                        break;
                    default:
                        errorMessage = "Erro ao gerar token";
                        errorCode = "TOKEN_GENERATION_ERROR";
                        statusCode = 500;
                }
            }

            throw new KeycloakException(errorMessage, statusCode, errorCode);
        } catch (Exception e) {
            logger.error("Erro inesperado ao gerar token: {}", e.getMessage(), e);
            throw new KeycloakException(
                    "Erro inesperado ao gerar token: " + e.getMessage(),
                    500,
                    "TOKEN_GENERATION_ERROR");
        }
    }

    public record LoginRequest(String username, String password) {
    }
}
