package br.com.pointer.pointer_back.enums;

import lombok.Getter;

@Getter
public enum StatusPDI {
    EM_ANDAMENTO,
    CONCLUIDO,
    ATRASADO;

    @Override
    public String toString() {
        return this.name();
    }
}