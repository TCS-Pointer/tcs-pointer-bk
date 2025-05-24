package br.com.pointer.pointer_back.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PDINaoEncontradoException extends RuntimeException {

    public PDINaoEncontradoException(Long id) {
        super("PDI não encontrado com ID: " + id);
    }

    public PDINaoEncontradoException(String message) {
        super(message);
    }

    public PDINaoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }
} 