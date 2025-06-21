package br.com.pointer.pointer_back.enums;

public enum StatusPDI {
    EM_ANDAMENTO,
    CONCLUIDO,
    ATRASADO;

    @Override
    public String toString() {
        return this.name();
    }
}