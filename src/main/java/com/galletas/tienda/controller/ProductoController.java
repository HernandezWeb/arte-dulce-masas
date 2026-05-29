package com.galletas.tienda.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.galletas.tienda.model.Producto;
import com.galletas.tienda.repository.ProductoRepository;

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

    // Método necesario para procesar el formulario de nuevo producto
    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto) {
        if (producto != null && producto.getImagenUrl() != null) {
            String url = producto.getImagenUrl();

            // Si el usuario pegó un link de página de ImgBB (ej: https://ibb.co/ABCD)
            // Lo convertimos al formato que la API de ImgBB usa para imágenes directas
            if (url.contains("ibb.co/")) {
                String id = url.substring(url.lastIndexOf("/") + 1);
                url = "https://i.ibb.co/" + id + "/image.jpg";
            }

            producto.setImagenUrl(url);
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