package com.galletas.tienda.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

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