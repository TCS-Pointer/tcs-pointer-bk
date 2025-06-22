package br.com.pointer.pointer_back.service;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.FeedbackDTO;
import br.com.pointer.pointer_back.dto.FeedbackResponseDTO;
import br.com.pointer.pointer_back.model.Feedback;
import br.com.pointer.pointer_back.repository.FeedbackRepository;
import br.com.pointer.pointer_back.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    // Criar feedback
    //implementado feedback response dto para retornar o feedback criado
    public ApiResponse<FeedbackResponseDTO> criarFeedback(FeedbackDTO feedbackDTO) {
        try {
            // Validações das notas (1 a 5)
            if (!isValidRating(feedbackDTO.getAvComunicacao()) ||
                !isValidRating(feedbackDTO.getAvProdutividade()) ||
                !isValidRating(feedbackDTO.getResolucaoDeProblemas()) ||
                !isValidRating(feedbackDTO.getTrabalhoEmEquipe())) {
                return ApiResponse.badRequest("As notas devem estar entre 1 e 5");
            }

            // Verificar se os usuários existem
            if (!usuarioRepository.existsById(feedbackDTO.getIdUsuarioDestinatario())) {
                return ApiResponse.notFound("Usuário destinatário não encontrado");
            }
            
            if (!usuarioRepository.existsById(feedbackDTO.getIdUsuarioRemetente())) {
                return ApiResponse.notFound("Usuário remetente não encontrado");
            }

            Feedback feedback = modelMapper.map(feedbackDTO, Feedback.class);
            Feedback feedbackSalvo = feedbackRepository.save(feedback);
            
            FeedbackResponseDTO response = modelMapper.map(feedbackSalvo, FeedbackResponseDTO.class);
            return ApiResponse.success(response, "Feedback criado com sucesso");
            
        } catch (Exception e) {
            return ApiResponse.internalServerError("Erro ao criar feedback: " + e.getMessage());
        }
    }
    // Lista de feedbacks recebidos pelo usuario
    public ApiResponse<Page<FeedbackResponseDTO>> listarFeedbacksRecebidos(Long userId, String keyword, int page) {
        try {
            Pageable pageable = PageRequest.of(page, 2, Sort.by("dtEnvio").descending());
            Page<Feedback> feedbacks;
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                feedbacks = feedbackRepository.findRecebidosByKeyword(userId, keyword.trim(), pageable);
            } else {
                feedbacks = feedbackRepository.findByIdUsuarioDestinatario(userId, pageable);
            }
            
            Page<FeedbackResponseDTO> response = feedbacks.map(feedback -> 
                modelMapper.map(feedback, FeedbackResponseDTO.class));
            
            return ApiResponse.success(response, "Feedbacks recebidos listados com sucesso");
            
        } catch (Exception e) {
            return ApiResponse.internalServerError("Erro ao listar feedbacks recebidos: " + e.getMessage());
        }
    }
            // Lista de feedbacks enviados
    public ApiResponse<Page<FeedbackResponseDTO>> listarFeedbacksEnviados(Long userId, String keyword, int page) {
        try {
            Pageable pageable = PageRequest.of(page, 2, Sort.by("dtEnvio").descending());
            Page<Feedback> feedbacks;
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                feedbacks = feedbackRepository.findEnviadosByKeyword(userId, keyword.trim(), pageable);
            } else {
                feedbacks = feedbackRepository.findByIdUsuarioRemetente(userId, pageable);
            }
            
            Page<FeedbackResponseDTO> response = feedbacks.map(feedback -> 
                modelMapper.map(feedback, FeedbackResponseDTO.class));
            
            return ApiResponse.success(response, "Feedbacks enviados listados com sucesso");
            
        } catch (Exception e) {
            return ApiResponse.internalServerError("Erro ao listar feedbacks enviados: " + e.getMessage());
        }
    }

    private boolean isValidRating(Integer rating) {
        return rating != null && rating >= 1 && rating <= 5;
    }
} 