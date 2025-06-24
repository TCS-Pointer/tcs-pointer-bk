package br.com.pointer.pointer_back.controller;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.FeedbackDTO;
import br.com.pointer.pointer_back.dto.FeedbackResponseDTO;
import br.com.pointer.pointer_back.dto.FeedbackStatsResponseDTO;
import br.com.pointer.pointer_back.service.FeedbackService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @GetMapping("/recebidos")
    public ApiResponse<Page<FeedbackResponseDTO>> listarFeedbacksRecebidos(
            @RequestParam String keycloakId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page) {
        return feedbackService.listarFeedbacksRecebidos(keycloakId, keyword, page);
    }

    @GetMapping("/enviados")
    public ApiResponse<Page<FeedbackResponseDTO>> listarFeedbacksEnviados(
            @RequestParam String keycloakId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page) {
        return feedbackService.listarFeedbacksEnviados(keycloakId, keyword, page);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('admin')")
    public ApiResponse<Page<FeedbackResponseDTO>> listarTodosFeedbacks(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page) {
        return feedbackService.listarTodosFeedbacks(keyword, page);
    }

    @PostMapping("/novo")
    public ApiResponse<Void> criarFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        return feedbackService.criarFeedback(feedbackDTO);
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('admin')")
    public ApiResponse<FeedbackStatsResponseDTO> getFeedbackStats() {
        return feedbackService.getFeedbackStats();
    }
} 