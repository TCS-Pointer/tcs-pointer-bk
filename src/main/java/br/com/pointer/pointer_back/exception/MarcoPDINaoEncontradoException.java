package br.com.pointer.pointer_back.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MarcoPDINaoEncontradoException extends RuntimeException {

    public MarcoPDINaoEncontradoException(Long id) {
        super("Marco PDI não encontrado com ID: " + id);
    }

    public MarcoPDINaoEncontradoException(String message) {
        super(message);
    }

    public MarcoPDINaoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }
}