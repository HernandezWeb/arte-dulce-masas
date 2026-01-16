package com.galletas.tienda.repository;

import com.galletas.tienda.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Filtra los productos por categoría exacta
    // Usamos String para que coincida con el tipo definido en la clase Producto
    List<Producto> findByCategoria(String categoria);

    List<Producto> findByNombreContainingIgnoreCase(String nombre);
}