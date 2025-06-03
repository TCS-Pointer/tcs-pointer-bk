package br.com.pointer.pointer_back.auth;

import org.keycloak.admin.client.Keycloak;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private Keycloak keycloak;

    @PostMapping
    public ApiResponse<TokenResponse> token(@RequestBody User user) {
        try {
            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("client_id", user.clientId());
            formData.add("client_secret", clientSecret);
            formData.add("username", user.username());
            formData.add("password", user.password());
            formData.add("grant_type", user.grantType());
            formData.add("scope", "openid");
            formData.add("expires_in", "100000");

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);
            String tokenUrl = String.format("%s/realms/%s/protocol/openid-connect/token", authServerUrl, realm);

            ResponseEntity<TokenResponse> response = restTemplate.postForEntity(tokenUrl, entity, TokenResponse.class);
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

    public record User(String password, String clientId, String username, String grantType) {
    }
}
