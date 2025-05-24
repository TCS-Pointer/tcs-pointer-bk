package br.com.pointer.pointer_back.enums;

public enum StatusMarcoPDI {
    AGUARDANDO,
    FEITO;

    @Override
    public String toString() {
        return this.name();
    }
}