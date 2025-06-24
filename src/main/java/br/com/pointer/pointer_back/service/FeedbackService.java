package br.com.pointer.pointer_back.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.DestinatarioDTO;
import br.com.pointer.pointer_back.dto.FeedbackDTO;
import br.com.pointer.pointer_back.dto.FeedbackResponseDTO;
import br.com.pointer.pointer_back.dto.FeedbackStatsDTO;
import br.com.pointer.pointer_back.dto.FeedbackStatsResponseDTO;
import br.com.pointer.pointer_back.dto.RemetenteDTO;
import br.com.pointer.pointer_back.enums.TipoFeedback;
import br.com.pointer.pointer_back.exception.UsuarioNaoEncontradoException;
import br.com.pointer.pointer_back.model.Feedback;
import br.com.pointer.pointer_back.model.Usuario;
import br.com.pointer.pointer_back.repository.FeedbackRepository;
import br.com.pointer.pointer_back.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackService.class);
    private final FeedbackRepository feedbackRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    public ApiResponse<Void> criarFeedback(FeedbackDTO feedbackDTO) {
        long startTime = System.nanoTime(); // Início da medição de tempo

        try {
            if (!notasSaoValidas(feedbackDTO)) {
                return ApiResponse.badRequest("As notas devem estar entre 1 e 5");
            }

            List<Usuario> usuarios = usuarioRepository.findByIdIn(
                    List.of(feedbackDTO.getIdUsuarioRemetente(), feedbackDTO.getIdUsuarioDestinatario())
            );


            boolean remetenteExiste = usuarios.stream()
                    .anyMatch(u -> u.getId().equals(feedbackDTO.getIdUsuarioRemetente()));
            boolean destinatarioExiste = usuarios.stream()
                    .anyMatch(u -> u.getId().equals(feedbackDTO.getIdUsuarioDestinatario()));

            if (!remetenteExiste) {
                logger.error("Usuário remetente não encontrado: {}", feedbackDTO.getIdUsuarioRemetente());
                return ApiResponse.notFound("Usuário remetente não encontrado");
            }

            if (!destinatarioExiste) {
                logger.error("Usuário destinatário não encontrado: {}", feedbackDTO.getIdUsuarioDestinatario());
                return ApiResponse.notFound("Usuário destinatário não encontrado");
            }

            Feedback feedback = modelMapper.map(feedbackDTO, Feedback.class);
            feedbackRepository.save(feedback);
            logger.info("Feedback criado com sucesso (ID {}), tempo total: {} ms", feedback.getIdFeedback());

            return ApiResponse.success("Feedback criado com sucesso");

        } catch (Exception e) {
            logger.error("Erro ao criar feedback", e);
            return ApiResponse.internalServerError("Erro ao criar feedback: " + e.getMessage());
        }
    }


    public ApiResponse<Page<FeedbackResponseDTO>> listarFeedbacksRecebidos(String keycloakId, String keyword,
            int page) {
        try {
            Usuario usuario = usuarioRepository.findByKeycloakId(keycloakId)
                    .orElseThrow(() -> new UsuarioNaoEncontradoException(keycloakId));

            Pageable pageable = PageRequest.of(page, 2, Sort.by("dtEnvio").descending());
            Page<Feedback> feedbacks = buscarFeedbacksPorKeywordRecebidos(usuario.getId(), keyword, pageable);

            Page<FeedbackResponseDTO> response = feedbacks.map(this::toFeedbackResponseDTO);

            logger.info("Feedbacks recebidos listados com sucesso: {}", response);
            return ApiResponse.success(response, "Feedbacks recebidos listados com sucesso");

        } catch (Exception e) {
            logger.error("Erro ao listar feedbacks recebidos: {}", e.getMessage());
            return ApiResponse.internalServerError("Erro ao listar feedbacks recebidos: " + e.getMessage());
        }
    }

    public ApiResponse<Page<FeedbackResponseDTO>> listarFeedbacksEnviados(String keycloakId, String keyword, int page) {
        try {
            Usuario usuario = usuarioRepository.findByKeycloakId(keycloakId)
                    .orElseThrow(() -> new UsuarioNaoEncontradoException(keycloakId));

            Pageable pageable = PageRequest.of(page, 2, Sort.by("dtEnvio").descending());
            Page<Feedback> feedbacks = buscarFeedbacksPorKeywordEnviados(usuario.getId(), keyword, pageable);

            Page<FeedbackResponseDTO> response = feedbacks.map(this::toFeedbackResponseDTO);

            return ApiResponse.success(response, "Feedbacks enviados listados com sucesso");

        } catch (Exception e) {
            return ApiResponse.internalServerError("Erro ao listar feedbacks enviados: " + e.getMessage());
        }
    }

    public ApiResponse<Page<FeedbackResponseDTO>> listarTodosFeedbacks(String keyword, int page) {
        try {
            Pageable pageable = PageRequest.of(page, 2, Sort.by("dtEnvio").descending());
            Page<Feedback> feedbacks = buscarFeedbacksPorKeyword(keyword, pageable);

            Page<FeedbackResponseDTO> response = feedbacks.map(this::toFeedbackResponseDTO);
            return ApiResponse.success(response, "Feedbacks listados com sucesso");

        } catch (Exception e) {
            logger.error("Erro ao listar feedbacks: {}", e.getMessage());
            return ApiResponse.internalServerError("Erro ao listar feedbacks: " + e.getMessage());
        }
    }

    public ApiResponse<FeedbackStatsResponseDTO> getFeedbackStats() {
        try {
            List<FeedbackStatsDTO> stats = feedbackRepository.findFeedbackStats();
            Long totalGeral = stats.stream()
                    .mapToLong(FeedbackStatsDTO::getTotal)
                    .sum();

            FeedbackStatsResponseDTO response = new FeedbackStatsResponseDTO();
            response.setStats(stats);
            response.setTotalGeral(totalGeral);
            return ApiResponse.success(response, "Estatísticas de feedback listadas com sucesso");
        } catch (Exception e) {
            return ApiResponse.internalServerError("Erro ao listar estatísticas de feedback: " + e.getMessage());
        }
    }

    // Métodos auxiliares

    private boolean isValidRating(Integer rating) {
        return rating != null && rating >= 1 && rating <= 5;
    }

    private boolean notasSaoValidas(FeedbackDTO dto) {
        return isValidRating(dto.getAvComunicacao()) &&
                isValidRating(dto.getAvProdutividade()) &&
                isValidRating(dto.getResolucaoDeProblemas()) &&
                isValidRating(dto.getTrabalhoEmEquipe());
    }

    private FeedbackResponseDTO toFeedbackResponseDTO(Feedback feedback) {
        FeedbackResponseDTO dto = modelMapper.map(feedback, FeedbackResponseDTO.class);

        // Buscar destinatário apenas se necessário
        if (feedback.getIdUsuarioDestinatario() != null) {
            Usuario destinatario = usuarioRepository.findById(feedback.getIdUsuarioDestinatario()).orElse(null);
            if (destinatario != null) {
                DestinatarioDTO destinatarioDTO = new DestinatarioDTO();
                destinatarioDTO.setUsuarioId(destinatario.getId());
                destinatarioDTO.setNome(destinatario.getNome());
                destinatarioDTO.setCargo(destinatario.getCargo());
                dto.setDestinatarioDTO(destinatarioDTO);
            }
        }

        // Buscar remetente apenas se não for anônimo
        if (!Boolean.TRUE.equals(feedback.getAnonimo()) && feedback.getIdUsuarioRemetente() != null) {
            Usuario remetente = usuarioRepository.findById(feedback.getIdUsuarioRemetente()).orElse(null);
            if (remetente != null) {
                RemetenteDTO remetenteDTO = new RemetenteDTO();
                remetenteDTO.setUsuarioId(remetente.getId());
                remetenteDTO.setNome(remetente.getNome());
                remetenteDTO.setCargo(remetente.getCargo());
                dto.setRemetenteDTO(remetenteDTO);
            }
        } else {
            dto.setRemetenteDTO(null);
        }

        return dto;
    }

    private Page<Feedback> buscarFeedbacksPorKeyword(String keyword, Pageable pageable) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            String keywordUpper = keyword.trim().toUpperCase();
            try {
                TipoFeedback tipo = TipoFeedback.valueOf(keywordUpper);
                return feedbackRepository.findAllByKeyword(tipo.name(), pageable);
            } catch (IllegalArgumentException e) {
                return feedbackRepository.findAllByKeyword(keyword.trim(), pageable);
            }
        }
        return feedbackRepository.findAll(pageable);
    }

    private Page<Feedback> buscarFeedbacksPorKeywordRecebidos(Long userId, String keyword, Pageable pageable) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            String keywordUpper = keyword.trim().toUpperCase();
            try {
                TipoFeedback tipo = TipoFeedback.valueOf(keywordUpper);
                return feedbackRepository.findRecebidosByKeyword(userId, tipo.name(), pageable);
            } catch (IllegalArgumentException e) {
                return feedbackRepository.findRecebidosByKeyword(userId, keyword.trim(), pageable);
            }
        }
        return feedbackRepository.findByIdUsuarioDestinatario(userId, pageable);
    }

    private Page<Feedback> buscarFeedbacksPorKeywordEnviados(Long userId, String keyword, Pageable pageable) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            String keywordUpper = keyword.trim().toUpperCase();
            try {
                TipoFeedback tipo = TipoFeedback.valueOf(keywordUpper);
                return feedbackRepository.findEnviadosByKeyword(userId, tipo.name(), pageable);
            } catch (IllegalArgumentException e) {
                return feedbackRepository.findEnviadosByKeyword(userId, keyword.trim(), pageable);
            }
        }
        return feedbackRepository.findByIdUsuarioRemetente(userId, pageable);
    }

}
