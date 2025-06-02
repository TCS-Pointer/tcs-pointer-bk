package br.com.pointer.pointer_back.util;

import br.com.pointer.pointer_back.dto.ApiResponse;
import br.com.pointer.pointer_back.dto.PagedListResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ApiResponseUtil {
    private final ModelMapper modelMapper;

    public ApiResponseUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <S, D> ApiResponse<D> map(S source, Class<D> destinationType, String message) {
        D mapped = modelMapper.map(source, destinationType);
        return ApiResponse.<D>builder()
                .message(message)
                .status(200)
                .content(mapped)
                .build();
    }

    public <S, D> ApiResponse<List<D>> mapList(List<S> source, Class<D> destinationType, String message) {
        List<D> mapped = source.stream()
                .map(item -> modelMapper.map(item, destinationType))
                .collect(Collectors.toList());
        return ApiResponse.<List<D>>builder()
                .message(message)
                .status(200)
                .content(mapped)
                .build();
    }

    public <S, D> ApiResponse<Page<D>> mapPage(Page<S> source, Class<D> destinationType, String message) {
        Page<D> mapped = source.map(item -> modelMapper.map(item, destinationType));
        return ApiResponse.<Page<D>>builder()
                .message(message)
                .status(200)
                .content(mapped)
                .build();
    }

    public <T> ApiResponse<T> success(T content, String message) {
        return ApiResponse.<T>builder()
                .message(message)
                .status(200)
                .content(content)
                .build();
    }

    public <T> ApiResponse<T> created(T content, String message) {
        return ApiResponse.<T>builder()
                .message(message)
                .status(201)
                .content(content)
                .build();
    }

    public <T> ApiResponse<T> error(String message, int status) {
        return ApiResponse.<T>builder()
                .message(message)
                .status(status)
                .build();
    }

    public <S, D> ApiResponse<D> mapApiResponse(ApiResponse<S> source, Class<D> resultClass) {
        Type apiResponseType = new TypeToken<ApiResponse<D>>() {}.getType();
        ApiResponse<D> mapped = modelMapper.map(source, apiResponseType);

        if (source.getContent() != null) {
            D contentMapped = modelMapper.map(source.getContent(), resultClass);
            mapped.setContent(contentMapped);
        }

        return mapped;
    }

    public <S, D> ApiResponse<List<D>> mapApiResponseList(ApiResponse<List<S>> source, Class<D> resultClass) {
        Type apiResponseType = new TypeToken<ApiResponse<List<D>>>() {}.getType();
        ApiResponse<List<D>> mapped = modelMapper.map(source, apiResponseType);

        if (source.getContent() != null) {
            List<D> contentMapped = source.getContent()
                    .stream()
                    .map(item -> modelMapper.map(item, resultClass))
                    .collect(Collectors.toList());
            mapped.setContent(contentMapped);
        }

        return mapped;
    }

    public <R, T> ApiResponse<PagedListResponse<T>> mapApiResponsePagedList(ApiResponse<PagedListResponse<R>> source, Class<T> resultClass) {
        Type apiResponseType = new TypeToken<ApiResponse<PagedListResponse<T>>>() {}.getType();
        ApiResponse<PagedListResponse<T>> mapped = modelMapper.map(source, apiResponseType);

        if (source.getContent() != null) {
            PagedListResponse<T> contentMapped = mapPagedListResponse(source.getContent(), resultClass);
            mapped.setContent(contentMapped);
        }

        return mapped;
    }

    private <R, T> PagedListResponse<T> mapPagedListResponse(PagedListResponse<R> source, Class<T> resultClass) {
        PagedListResponse<T> mapped = new PagedListResponse<>();
        mapped.setPage(source.getPage());
        mapped.setSize(source.getSize());
        mapped.setTotalElements(source.getTotalElements());
        mapped.setTotalPages(source.getTotalPages());
        mapped.setHasNext(source.isHasNext());
        mapped.setHasPrevious(source.isHasPrevious());

        if (source.getContent() != null) {
            List<T> contentMapped = source.getContent()
                    .stream()
                    .map(item -> modelMapper.map(item, resultClass))
                    .collect(Collectors.toList());
            mapped.setContent(contentMapped);
        }

        return mapped;
    }
} 