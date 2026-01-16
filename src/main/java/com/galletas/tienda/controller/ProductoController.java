package com.galletas.tienda.controller;

import com.galletas.tienda.model.Producto;
import com.galletas.tienda.repository.ProductoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller
@RequestMapping("/admin/productos")
public class ProductoController {

    private final ProductoRepository productoRepository;

    // Constructor: Esto elimina las advertencias de inyección
    public ProductoController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // Ver la lista de productos y el resumen estadístico
    @GetMapping
    public String listarProductos(Model model) {
        List<Producto> productos = productoRepository.findAll();
        model.addAttribute("productos", productos);

        // Contamos cuántos hay de cada uno para el resumen administrativo
        model.addAttribute("totalGalletas",
                productos.stream().filter(p -> "Galletas".equals(p.getCategoria())).count());
        model.addAttribute("totalTortas", productos.stream().filter(p -> "Tortas".equals(p.getCategoria())).count());
        model.addAttribute("totalMasas", productos.stream().filter(p -> "Masas".equals(p.getCategoria())).count());
        model.addAttribute("totalPromos",
                productos.stream().filter(p -> "Promociones".equals(p.getCategoria())).count());

        return "admin/productos";
    }

    // Guardar o Actualizar producto (save detecta si existe ID)
    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto) {
        if (producto != null) {
            productoRepository.save(producto);
        }
        return "redirect:/admin/productos";
    }

    @PostMapping("/editar")
    public String editarProducto(@ModelAttribute Producto producto) {
        if (producto != null) {
            productoRepository.save(producto);
        }
        return "redirect:/admin/productos";
    }

    // Eliminar un producto con protección de errores
    @PostMapping("/eliminar")
    public String eliminarProducto(@RequestParam("id") Long id) {
        try {
            if (id != null) {
                productoRepository.deleteById(id);
            }
        } catch (Exception e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
        }
        return "redirect:/admin/productos";
    }
}