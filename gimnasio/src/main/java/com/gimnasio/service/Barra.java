package com.gimnasio.service;

import java.math.BigDecimal;

public class Barra {

    private final String etiqueta;
    private final BigDecimal valor;
    private final int porcentaje;

    public Barra(String etiqueta, BigDecimal valor, int porcentaje) {
        this.etiqueta = etiqueta;
        this.valor = valor;
        this.porcentaje = porcentaje;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public int getPorcentaje() {
        return porcentaje;
    }
}
