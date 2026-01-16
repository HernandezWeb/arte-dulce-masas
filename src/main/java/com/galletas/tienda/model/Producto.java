package com.galletas.tienda.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data // Esto crea los Getters y Setters automáticamente con Lombok
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    // Usamos double (minúscula) o validamos que no sea nulo para evitar errores en
    // cálculos
    private Double precio = 0.0;

    @Column(columnDefinition = "TEXT")
    private String imagenUrl; // Aquí pegaremos el link de la foto

    private String categoria;
}