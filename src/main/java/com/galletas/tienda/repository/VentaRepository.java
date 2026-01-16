package com.galletas.tienda.repository;

import com.galletas.tienda.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    // Esto te permitirá ver los pedidos nuevos arriba en tu lista de administración
    List<Venta> findAllByOrderByFechaDesc();
}