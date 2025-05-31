package br.com.pointer.pointer_back.enums;

public enum StatusMarcoPDI {
    PENDENTE,
    CONCLUIDO;

    @Override
    public String toString() {
        return this.name();
    }
}