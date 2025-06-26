package br.com.pointer.pointer_back.service;

import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();

    public void sendPasswordEmail(String recipientEmail, String password, String name) {
        if (recipientEmail == null || recipientEmail.trim().isEmpty()) {
            throw new NullPointerException("Email não pode ser nulo ou vazio");
        }
        logger.info("Iniciando envio de email com senha para: {}", recipientEmail);
        String templateId = "d-d833e5b4a1e84774b566a29a2bd2e984";
        String subject = "Sua senha de acesso - Pointer";
        Map<String, String> dynamicData = Map.of("senha", password, "nome", name);

        Mail mail = createTemplateMail(recipientEmail, templateId, subject, dynamicData);
        sendMail(mail);
        logger.info("Email com senha enviado com sucesso para: {}", recipientEmail);
    }

    public void sendPrimeiroAcessoEmail(String recipientEmail, String name, String token) {
        if (recipientEmail == null || recipientEmail.trim().isEmpty()) {
            throw new NullPointerException("Email não pode ser nulo ou vazio");
        }
        logger.info("Iniciando envio de email de primeiro acesso para: {}", recipientEmail);
        String templateId = "d-d09158bb610d47f58c3a85e11bdee6ad";
        String subject = "Pointer";
        String link = frontendUrl + "/primeiro-acesso?token=" + token;
        Map<String, String> dynamicData = Map.of("nome", name, "link", link);

        logger.info("Link de primeiro acesso gerado: {}", link);
        logger.info("Template ID: {}", templateId);

        Mail mail = createTemplateMail(recipientEmail, templateId, subject, dynamicData);
        sendMail(mail);
        logger.info("Email de primeiro acesso enviado com sucesso para: {}", recipientEmail);
    }


    public void sendTwoFactorDisabledEmail(String recipientEmail, String name) {
        if (recipientEmail == null || recipientEmail.trim().isEmpty()) {
            throw new NullPointerException("Email não pode ser nulo ou vazio");
        }
        logger.info("Iniciando envio de email com 2FA desabilitado para: {}", recipientEmail);
        String templateId = "d-0cb0b3546b0d4e308eaff6cb1688df83";
        String subject = "Pointer";
        Map<String, String> dynamicData = Map.of("nome", name);
        Mail mail = createTemplateMail(recipientEmail, templateId, subject, dynamicData);
        sendMail(mail);
        logger.info("Email com 2FA desabilitado enviado com sucesso para: {}", recipientEmail);
    }
    public void sendVerificationCodeEmail(String recipientEmail, String name) {
        logger.info("Iniciando envio de email com código de verificação para: {}", recipientEmail);
        String templateId = "d-8b3d5327466b48239ddc170d54af8bce";
        String subject = "Código de verificação - Pointer";
        String code = generateRandomCode();
        Map<String, String> dynamicData = Map.of("codigo", code, "nome", name);
        Mail mail = createTemplateMail(recipientEmail, templateId, subject, dynamicData);
        verificationCodes.put(recipientEmail, code);
        sendMail(mail);
        logger.info(sendGridApiKey);
        logger.info("Email com código de verificação enviado com sucesso para: {}", recipientEmail);
    }

    public boolean verifyCode(String email, String code) {
        if (email == null || code == null) {
            return false;
        }
        boolean isValid = code.equals(verificationCodes.get(email));
        logger.info("Verificação de código para {}: {}", email, isValid ? "válido" : "inválido");
        return isValid;
    }

    public void removeVerificationCode(String email) {
        if (email != null) {
            verificationCodes.remove(email);
            logger.info("Código de verificação removido para: {}", email);
        }
    }


    private Mail createTemplateMail(String recipientEmail, String templateId, String subject, Map<String, String> dynamicData) {
        logger.debug("Criando email com template {} para: {}", templateId, recipientEmail);
        Email from = new Email("no-replypointer@bol.com.br");
        Email to = new Email(recipientEmail);
        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setSubject(subject);
        mail.setTemplateId(templateId);

        Personalization personalization = new Personalization();
        personalization.addTo(to);
        dynamicData.forEach(personalization::addDynamicTemplateData);

        mail.addPersonalization(personalization);
        logger.debug("Email criado com assunto: {} e dados dinâmicos: {}", subject, dynamicData);
        return mail;
    }

    private void sendMail(Mail mail) {
        logger.debug("Iniciando envio de email via SendGrid");
        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
            logger.info("Email enviado com sucesso via SendGrid");
        } catch (IOException ex) {
            logger.error("Erro ao enviar email via SendGrid: {}", ex.getMessage(), ex);
            throw new EmailSendingException("Failed to send email", ex);
        }
    }

    private String generateRandomCode() {
        Random random = new Random();
        int code = 100_000 + random.nextInt(900_000);
        String codeStr = String.valueOf(code);
        logger.debug("Código aleatório gerado: {}", codeStr);
        return codeStr;
    }

    private static class EmailSendingException extends RuntimeException {
        public EmailSendingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
