package com.gimnasio.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class DetallePaquete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDetalle;
    @ManyToOne
    @JoinColumn(name = "id_paquete")
    private Paquete paquete;
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private ProductoServicio producto;

    public DetallePaquete() {
    }

    public DetallePaquete(Paquete paquete, ProductoServicio producto) {
        this.paquete = paquete;
        this.producto = producto;
    }

    public Integer getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Paquete getPaquete() {
        return paquete;
    }

    public void setPaquete(Paquete paquete) {
        this.paquete = paquete;
    }

    public ProductoServicio getProducto() {
        return producto;
    }

    public void setProducto(ProductoServicio producto) {
        this.producto = producto;
    }
}
