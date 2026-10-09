package com.gimnasio.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Cobro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCobro;
    @ManyToOne
    @JoinColumn(name = "id_suscripcion")
    private Suscripcion suscripcion;
    @Column(precision = 10, scale = 2)
    private BigDecimal monto;
    private String metodoPago;
    private LocalDateTime fecha;
    private String estado;

    public Cobro() {
    }

    public Cobro(Suscripcion suscripcion, BigDecimal monto, String metodoPago) {
        this.suscripcion = suscripcion;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.fecha = LocalDateTime.now();
        this.estado = "PAGADO";
    }

    public Integer getIdCobro() {
        return idCobro;
    }

    public void setIdCobro(Integer idCobro) {
        this.idCobro = idCobro;
    }

    public Suscripcion getSuscripcion() {
        return suscripcion;
    }

    public void setSuscripcion(Suscripcion suscripcion) {
        this.suscripcion = suscripcion;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
