package br.com.pointer.pointer_back.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import br.com.pointer.pointer_back.constant.TokenConstants;
import br.com.pointer.pointer_back.exception.TokenExpiradoException;
import br.com.pointer.pointer_back.exception.TokenInvalidoException;
import br.com.pointer.pointer_back.util.TokenUtil;

@Service
public class PrimeiroAcessoService {

    private final Map<String, TokenInfo> tokens = new ConcurrentHashMap<>();

    public String gerarToken(String email) {
        String token = TokenUtil.gerarTokenAleatorio();
        LocalDateTime expiracao = calcularExpiracao();

        tokens.put(token, new TokenInfo(email, expiracao));
        return token;
    }

    public String reenviarToken(String email) {
        removerTokensAntigosPorEmail(email);

        String novoToken = TokenUtil.gerarTokenAleatorio();
        LocalDateTime expiracao = calcularExpiracao();

        tokens.put(novoToken, new TokenInfo(email, expiracao));
        return novoToken;
    }

    public String validarToken(String token) {
        TokenInfo tokenInfo = obterTokenInfo(token);
        validarExpiracao(tokenInfo);

        return tokenInfo.getEmail();
    }

    public void removerToken(String token) {
        tokens.remove(token);
    }

    private TokenInfo obterTokenInfo(String token) {
        TokenInfo tokenInfo = tokens.get(token);
        if (tokenInfo == null) {
            throw new TokenInvalidoException("Token inválido");
        }
        return tokenInfo;
    }

    private void validarExpiracao(TokenInfo tokenInfo) {
        if (LocalDateTime.now().isAfter(tokenInfo.getExpiracao())) {
            tokens.remove(tokenInfo.getEmail());
            throw new TokenExpiradoException("Token expirado");
        }
    }

    private void removerTokensAntigosPorEmail(String email) {
        tokens.entrySet().removeIf(entry -> entry.getValue().getEmail().equals(email));
    }

    private LocalDateTime calcularExpiracao() {
        return LocalDateTime.now().plusMinutes(TokenConstants.TEMPO_EXPIRACAO_MINUTOS);
    }

    private static class TokenInfo {
        private final String email;
        private final LocalDateTime expiracao;

        public TokenInfo(String email, LocalDateTime expiracao) {
            this.email = email;
            this.expiracao = expiracao;
        }

        public String getEmail() {
            return email;
        }

        public LocalDateTime getExpiracao() {
            return expiracao;
        }
    }
}