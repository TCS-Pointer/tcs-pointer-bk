package br.com.pointer.pointer_back.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ComunicadoNaoEncontradoException extends RuntimeException {

    public ComunicadoNaoEncontradoException(Long id) {
        super("Comunicado não encontrado com ID: " + id);
    }

    public ComunicadoNaoEncontradoException(String message) {
        super(message);
    }

    public ComunicadoNaoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }
} 