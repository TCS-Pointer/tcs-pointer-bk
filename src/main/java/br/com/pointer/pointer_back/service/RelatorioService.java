package br.com.pointer.pointer_back.service;

import br.com.pointer.pointer_back.model.Comunicado;
import br.com.pointer.pointer_back.model.Feedback;
import br.com.pointer.pointer_back.model.PDI;
import br.com.pointer.pointer_back.model.Usuario;
import br.com.pointer.pointer_back.repository.ComunicadoRepository;
import br.com.pointer.pointer_back.repository.FeedbackRepository;
import br.com.pointer.pointer_back.repository.PDIRepository;
import br.com.pointer.pointer_back.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final UsuarioRepository usuarioRepository;
    private final PDIRepository pdiRepository;
    private final ComunicadoRepository comunicadoRepository;
    private final FeedbackRepository feedbackRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Pattern HTML_TAG_PATTERN = Pattern.compile("<[^>]*>");

    /**
     * Remove tags HTML de uma string
     */
    private String removeHtmlTags(String html) {
        if (html == null) {
            return "";
        }
        return HTML_TAG_PATTERN.matcher(html).replaceAll("").trim();
    }

    public byte[] gerarRelatorioUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                     "Nome", "Email", "Setor", "Cargo", "Tipo Usuário", 
                     "Data Criação", "Status"))) {
            
            for (Usuario usuario : usuarios) {
                csvPrinter.printRecord(
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getSetor(),
                    usuario.getCargo(),
                    usuario.getTipoUsuario(),
                    usuario.getDataCriacao() != null ? usuario.getDataCriacao().format(DATE_FORMATTER) : "",
                    usuario.getStatus()
                );
            }
            
            csvPrinter.flush();
            return baos.toByteArray();
            
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar relatório de usuários", e);
        }
    }

    public byte[] gerarRelatorioPDI() {
        List<PDI> pdis = pdiRepository.findAll();
        
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                     "Título", "Descrição", "Destinatário", "Status", 
                     "Data Início", "Data Fim", "Data Criação", "Marcos"))) {
            
            for (PDI pdi : pdis) {
                // Formatar os marcos como uma string
                String marcos = "";
                if (pdi.getMarcos() != null && !pdi.getMarcos().isEmpty()) {
                    marcos = pdi.getMarcos().stream()
                        .map(marco -> String.format("%s (%s - %s)", 
                            marco.getTitulo(), 
                            marco.getStatus(), 
                            marco.getDtFinal() != null ? marco.getDtFinal().format(DATE_ONLY_FORMATTER) : ""))
                        .reduce("", (a, b) -> a.isEmpty() ? b : a + "; " + b);
                }
                
                csvPrinter.printRecord(
                    pdi.getTitulo(),
                    pdi.getDescricao(),
                    pdi.getDestinatario() != null ? pdi.getDestinatario().getNome() : "",
                    pdi.getStatus(),
                    pdi.getDtInicio() != null ? pdi.getDtInicio().format(DATE_ONLY_FORMATTER) : "",
                    pdi.getDtFim() != null ? pdi.getDtFim().format(DATE_ONLY_FORMATTER) : "",
                    pdi.getDataCriacao() != null ? pdi.getDataCriacao().format(DATE_FORMATTER) : "",
                    marcos
                );
            }
            
            csvPrinter.flush();
            return baos.toByteArray();
            
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar relatório de PDI", e);
        }
    }

    public byte[] gerarRelatorioComunicados() {
        List<Comunicado> comunicados = comunicadoRepository.findAll();
        
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                     "Título", "Descrição", "Setores", "Apenas Gestores", 
                     "Data Publicação", "Ativo"))) {
            
            for (Comunicado comunicado : comunicados) {
                String setores = comunicado.getSetores() != null ? 
                    String.join("; ", comunicado.getSetores()) : "";
                
                csvPrinter.printRecord(
                    removeHtmlTags(comunicado.getTitulo()),
                    removeHtmlTags(comunicado.getDescricao()),
                    setores,
                    comunicado.isApenasGestores(),
                    comunicado.getDataPublicacao() != null ? comunicado.getDataPublicacao().format(DATE_FORMATTER) : "",
                    comunicado.isAtivo()
                );
            }
            
            csvPrinter.flush();
            return baos.toByteArray();
            
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar relatório de comunicados", e);
        }
    }

    public byte[] gerarRelatorioFeedback() {
        List<Feedback> feedbacks = feedbackRepository.findAll();
        
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                     "Assunto", "Tipo Feedback", "Ações Recomendadas", "Anônimo",
                     "Avaliação Comunicação", "Avaliação Produtividade", "Resolução de Problemas",
                     "Trabalho em Equipe", "Pontos Fortes", "Pontos Melhoria", "Destinatário", "Remetente", "Data Envio"))) {
            
            for (Feedback feedback : feedbacks) {
                // Buscar dados dos usuários
                String destinatario = "";
                String remetente = "";
                
                // Sempre buscar destinatário (quem recebe o feedback)
                if (feedback.getIdUsuarioDestinatario() != null) {
                    Optional<Usuario> destinatarioOpt = usuarioRepository.findById(feedback.getIdUsuarioDestinatario());
                    if (destinatarioOpt.isPresent()) {
                        destinatario = destinatarioOpt.get().getNome();
                    }
                }
                
                // Buscar remetente (quem envia o feedback)
                if (feedback.getAnonimo()) {
                    remetente = "Anônimo";
                } else {
                    if (feedback.getIdUsuarioRemetente() != null) {
                        Optional<Usuario> remetenteOpt = usuarioRepository.findById(feedback.getIdUsuarioRemetente());
                        if (remetenteOpt.isPresent()) {
                            remetente = remetenteOpt.get().getNome();
                        }
                    }
                }
                
                csvPrinter.printRecord(
                    feedback.getAssunto(),
                    feedback.getTipoFeedback(),
                    feedback.getAcoesRecomendadas(),
                    feedback.getAnonimo(),
                    feedback.getAvComunicacao(),
                    feedback.getAvProdutividade(),
                    feedback.getResolucaoDeProblemas(),
                    feedback.getTrabalhoEmEquipe(),
                    feedback.getPontosFortes(),
                    feedback.getPontosMelhoria(),
                    destinatario,
                    remetente,
                    feedback.getDtEnvio() != null ? feedback.getDtEnvio().format(DATE_FORMATTER) : ""
                );
            }
            
            csvPrinter.flush();
            return baos.toByteArray();
            
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar relatório de feedback", e);
        }
    }
} 