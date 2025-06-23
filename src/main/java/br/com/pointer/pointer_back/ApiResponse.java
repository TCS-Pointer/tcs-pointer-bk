package br.com.pointer.pointer_back;

<<<<<<< HEAD
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
=======
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
>>>>>>> 3c46f92a3eab74bba1b2fc31a3bd29ad2f03f3ce

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String message;
    private int status;
    private T content;
    private List<String> errors;

    @JsonIgnore
    public boolean isOk() {
        return status == 200;
    }

<<<<<<< HEAD
    // Métodos estáticos para sucesso
    public static <T> ApiResponse<T> success(T content, String message) {
        return ApiResponse.<T>builder()
                .message(message)
                .content(content)
                .status(200)
                .build();
    }

    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .message(message)
                .status(200)
                .build();
    }

    // Métodos estáticos para erro
    public static <T> ApiResponse<T> error(String message, int status) {
        return ApiResponse.<T>builder()
                .message(message)
                .status(status)
                .build();
    }

    public static <T> ApiResponse<T> error(String message, List<String> errors, int status) {
        return ApiResponse.<T>builder()
                .message(message)
                .errors(errors)
                .status(status)
                .build();
    }

    // Métodos estáticos para status específicos
    public static <T> ApiResponse<T> badRequest(String message) {
        return error(message, 400);
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return error(message, 404);
    }

    public static <T> ApiResponse<T> conflict(String message) {
        return error(message, 409);
    }

    public static <T> ApiResponse<T> unprocessableEntity(String message) {
        return error(message, 422);
    }

    public static <T> ApiResponse<T> internalServerError(String message) {
        return error(message, 500);
    }

    // Métodos de mapeamento com ModelMapper
    public static <T, R> ApiResponse<R> map(T source, Class<R> targetClass, String message) {
        if (source == null) {
            return notFound(message);
        }

        ModelMapper modelMapper = new ModelMapper();
        R mappedContent = modelMapper.map(source, targetClass);
        return success(mappedContent, message);
    }

    public static <T, R> ApiResponse<List<R>> mapList(List<T> sourceList, Class<R> targetClass, String message) {
        if (sourceList == null || sourceList.isEmpty()) {
            return success(List.of(), message);
        }

        ModelMapper modelMapper = new ModelMapper();
        List<R> mappedList = sourceList.stream()
                .map(source -> modelMapper.map(source, targetClass))
                .collect(Collectors.toList());

        return success(mappedList, message);
    }

    public static <T, R> ApiResponse<Page<R>> mapPage(Page<T> sourcePage, Class<R> targetClass, String message) {
        if (sourcePage == null || sourcePage.isEmpty()) {
            return success(Page.empty(), message);
        }

        ModelMapper modelMapper = new ModelMapper();
        List<R> mappedContent = sourcePage.getContent().stream()
                .map(source -> modelMapper.map(source, targetClass))
                .collect(Collectors.toList());

        Page<R> mappedPage = new PageImpl<>(mappedContent, sourcePage.getPageable(), sourcePage.getTotalElements());
        return success(mappedPage, message);
    }

    // Métodos de mapeamento com ModelMapper customizado
    public static <T, R> ApiResponse<R> map(T source, Class<R> targetClass, String message, ModelMapper modelMapper) {
        if (source == null) {
            return notFound(message);
        }

        R mappedContent = modelMapper.map(source, targetClass);
        return success(mappedContent, message);
    }

    public static <T, R> ApiResponse<List<R>> mapList(List<T> sourceList, Class<R> targetClass, String message,
            ModelMapper modelMapper) {
        if (sourceList == null || sourceList.isEmpty()) {
            return success(List.of(), message);
        }

        List<R> mappedList = sourceList.stream()
                .map(source -> modelMapper.map(source, targetClass))
                .collect(Collectors.toList());

        return success(mappedList, message);
    }

    public static <T, R> ApiResponse<Page<R>> mapPage(Page<T> sourcePage, Class<R> targetClass, String message,
            ModelMapper modelMapper) {
        if (sourcePage == null || sourcePage.isEmpty()) {
            return success(Page.empty(), message);
        }

        List<R> mappedContent = sourcePage.getContent().stream()
                .map(source -> modelMapper.map(source, targetClass))
                .collect(Collectors.toList());

        Page<R> mappedPage = new PageImpl<>(mappedContent, sourcePage.getPageable(), sourcePage.getTotalElements());
        return success(mappedPage, message);
    }

    // Métodos de instância (mantidos para compatibilidade)
=======
>>>>>>> 3c46f92a3eab74bba1b2fc31a3bd29ad2f03f3ce
    public ApiResponse<T> ok(T content, String message) {
        return ApiResponse.<T>builder()
                .message(message)
                .content(content)
                .status(200)
                .build();
    }

<<<<<<< HEAD
=======
    public ApiResponse<T> notFound(String message) {
        return ApiResponse.<T>builder()
                .message(message)
                .status(404)
                .build();
    }

    public ApiResponse<T> badRequest(String message) {
        return ApiResponse.<T>builder()
                .message(message)
                .status(400)
                .build();
    }

>>>>>>> 3c46f92a3eab74bba1b2fc31a3bd29ad2f03f3ce
    public ApiResponse<T> badRequest(String message, List<String> errors) {
        return ApiResponse.<T>builder()
                .message(message)
                .errors(errors)
                .status(400)
                .build();
    }

    public ApiResponse<T> conflict(String message, T content) {
        return ApiResponse.<T>builder()
                .message(message)
                .status(409)
                .content(content)
                .build();
    }
<<<<<<< HEAD
=======

    public ApiResponse<T> unprocessableEntity(String message) {
        return ApiResponse.<T>builder()
                .message(message)
                .status(422)
                .build();
    }
>>>>>>> 3c46f92a3eab74bba1b2fc31a3bd29ad2f03f3ce
}