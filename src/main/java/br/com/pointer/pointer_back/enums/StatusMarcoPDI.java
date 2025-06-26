package br.com.pointer.pointer_back.enums;

import lombok.Getter;

@Getter
public enum StatusMarcoPDI {
    PENDENTE,
    CONCLUIDO;

    @Override
    public String toString() {
        return this.name();
    }
}