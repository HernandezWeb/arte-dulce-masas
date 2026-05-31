package com.galletas.tienda.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Data;

@Entity
@Data
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cliente;
    private String telefono;
    private double totalUsd;
    private double totalBs;
    private String metodoPago;
    private String metodo;
    private String productos;

    private LocalDateTime fecha;
    private String estado = "Recibido"; // Estado inicial por defecto

    @Column(length = 1000)
    private String detalleProductos; // Nombres y cantidades de productos

    // Esto asegura que la fecha se asigne automáticamente al guardar
    @PrePersist
    protected void onCreate() {
        this.fecha = LocalDateTime.now();
    }
}