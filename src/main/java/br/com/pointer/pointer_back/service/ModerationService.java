package br.com.pointer.pointer_back.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Service
public class ModerationService {
    
    private static final Logger logger = LoggerFactory.getLogger(ModerationService.class);
    
    @Value("${gemini.api.url}")
    private String geminiApiUrl;
    
    @Value("${gemini.api.key}")
    private String geminiApiKey;
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    public ModerationService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }
    
    /**
     * Gera o prompt para moderação de conteúdo
     */
    private String getPrompt(String textoUsuario) {
        return """
            Você é um moderador de conteúdo. Avalie o seguinte texto enviado por um usuário.
            
            Seu trabalho é identificar se ele contém qualquer tipo de:
            - Linguagem ofensiva
            - Agressividade verbal
            - Preconceito (raça, gênero, religião, etc.)
            - Palavrões ou insultos
            - Conteúdo desrespeitoso ou inapropriado
            
            Responda apenas com:
            - "OK" → se o texto estiver adequado
            - "OFENSIVO" → se o texto for impróprio de alguma forma
            
            Texto do usuário:
            """ + textoUsuario + """
            """;
    }
    
    /**
     * Modera um texto usando a API do Gemini
     * @param textoUsuario texto a ser moderado
     * @return "OK" se aprovado, "OFENSIVO" se rejeitado
     * @throws ModerationException se houver erro na moderação
     */
    public String moderarTexto(String textoUsuario) throws ModerationException {
        logger.info("Iniciando moderação de texto");
        
        if (geminiApiKey == null || geminiApiKey.trim().isEmpty()) {
            logger.error("Chave da API do Gemini não configurada");
            throw new ModerationException("Chave da API do Gemini não configurada");
        }
        
        if (textoUsuario == null || textoUsuario.trim().isEmpty()) {
            logger.warn("Texto vazio fornecido para moderação");
            return "OK";
        }
        
        try {
            String prompt = getPrompt(textoUsuario);
            
            // Construir payload da requisição
            GeminiRequest request = new GeminiRequest();
            request.setContents(List.of(new Content(List.of(new Part(prompt)))));
            
            // Configurar headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<GeminiRequest> entity = new HttpEntity<>(request, headers);
            
            // Fazer requisição para a API do Gemini
            String url = geminiApiUrl + "?key=" + geminiApiKey;
            logger.debug("Fazendo requisição para: {}", url);
            
            ResponseEntity<GeminiResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                GeminiResponse.class
            );
            
            if (response.getBody() == null) {
                throw new ModerationException("Resposta vazia da API Gemini");
            }
            
            // Extrair resposta do Gemini
            String geminiText = extractGeminiResponse(response.getBody());
            if (geminiText == null || geminiText.trim().isEmpty()) {
                throw new ModerationException("Resposta inesperada da API Gemini");
            }
            
            // Processar resposta
            String resultado = processarResposta(geminiText);
            logger.info("Moderação concluída: {}", resultado);
            
            return resultado;
            
        } catch (RestClientException e) {
            logger.error("Erro na comunicação com API Gemini: {}", e.getMessage());
            throw new ModerationException("Erro ao consultar moderação Gemini: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Erro inesperado na moderação: {}", e.getMessage());
            throw new ModerationException("Erro inesperado na moderação: " + e.getMessage());
        }
    }
    
    /**
     * Extrai o texto da resposta do Gemini
     */
    private String extractGeminiResponse(GeminiResponse response) {
        if (response.getCandidates() == null || response.getCandidates().isEmpty()) {
            return null;
        }
        
        Candidate candidate = response.getCandidates().get(0);
        if (candidate.getContent() == null || candidate.getContent().getParts() == null || 
            candidate.getContent().getParts().isEmpty()) {
            return null;
        }
        
        Part part = candidate.getContent().getParts().get(0);
        return part.getText();
    }
    
    /**
     * Processa a resposta do Gemini e retorna o resultado da moderação
     */
    private String processarResposta(String geminiText) {
        String textoLimpo = geminiText.trim().toUpperCase();
        
        if (textoLimpo.contains("OK")) {
            return "OK";
        }
        
        if (textoLimpo.contains("OFENSIVO")) {
            return "OFENSIVO";
        }
        
        // Se não conseguir identificar claramente, loga e retorna OFENSIVO por segurança
        logger.warn("Resposta ambígua do Gemini: '{}'. Retornando OFENSIVO por segurança.", geminiText);
        return "OFENSIVO";
    }
    
    /**
     * Verifica se um texto é apropriado
     * @param texto texto a ser verificado
     * @return true se aprovado, false se rejeitado
     */
    public boolean isTextoAprovado(String texto) {
        try {
            String resultado = moderarTexto(texto);
            return "OK".equals(resultado);
        } catch (ModerationException e) {
            logger.error("Erro na moderação, rejeitando texto por segurança: {}", e.getMessage());
            return false;
        }
    }
    
    // Classes para serialização/deserialização da API do Gemini
    
    public static class GeminiRequest {
        @JsonProperty("contents")
        private List<Content> contents;
        
        public List<Content> getContents() { return contents; }
        public void setContents(List<Content> contents) { this.contents = contents; }
    }
    
    public static class Content {
        @JsonProperty("parts")
        private List<Part> parts;
        
        public Content() {}
        
        public Content(List<Part> parts) {
            this.parts = parts;
        }
        
        public List<Part> getParts() { return parts; }
        public void setParts(List<Part> parts) { this.parts = parts; }
    }
    
    public static class Part {
        @JsonProperty("text")
        private String text;
        
        public Part() {}
        
        public Part(String text) {
            this.text = text;
        }
        
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }
    
    public static class GeminiResponse {
        @JsonProperty("candidates")
        private List<Candidate> candidates;
        
        public List<Candidate> getCandidates() { return candidates; }
        public void setCandidates(List<Candidate> candidates) { this.candidates = candidates; }
    }
    
    public static class Candidate {
        @JsonProperty("content")
        private Content content;
        
        public Content getContent() { return content; }
        public void setContent(Content content) { this.content = content; }
    }
    
    /**
     * Exceção customizada para erros de moderação
     */
    public static class ModerationException extends RuntimeException {
        public ModerationException(String message) {
            super(message);
        }
        
        public ModerationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
} 