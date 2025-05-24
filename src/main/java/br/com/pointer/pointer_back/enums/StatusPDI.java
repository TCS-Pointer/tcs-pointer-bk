package br.com.pointer.pointer_back.enums;

public enum StatusPDI {
    PENDENTE,
    EM_ANDAMENTO,
    CONCLUIDO,
    CANCELADO;

    @Override
    public String toString() {
        return this.name();
    }
}