package br.com.pointer.pointer_back.controller;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.FeedbackDTO;
import br.com.pointer.pointer_back.dto.FeedbackResponseDTO;
import br.com.pointer.pointer_back.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @GetMapping("/recebidos")
    public ApiResponse<Page<FeedbackResponseDTO>> listarFeedbacksRecebidos(
            @RequestParam Long userId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page) {
        return feedbackService.listarFeedbacksRecebidos(userId, keyword, page);
    }

    @GetMapping("/enviados")
    public ApiResponse<Page<FeedbackResponseDTO>> listarFeedbacksEnviados(
            @RequestParam Long userId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page) {
        return feedbackService.listarFeedbacksEnviados(userId, keyword, page);
    }
// usando o feedback dto para retornar o feedback criado.
    @PostMapping("/novo")
    public ApiResponse<FeedbackResponseDTO> criarFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        return feedbackService.criarFeedback(feedbackDTO);
    }
} 