package com.galletas.tienda.controller;

import com.galletas.tienda.model.Venta;
import com.galletas.tienda.model.Producto; // Asegúrate de tener esta importación
import com.galletas.tienda.repository.ProductoRepository;
import com.galletas.tienda.repository.VentaRepository;
import com.galletas.tienda.service.TasaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;

@Controller
public class TiendaController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private TasaService tasaService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    // RUTA PRINCIPAL DEL CATÁLOGO
    @GetMapping("/catalogoGalletas")
    public String catalogoGalletas(@RequestParam(name = "categoria", required = false) String categoria, Model model) {
        double tasa = obtenerTasa();

        if (categoria != null && !categoria.isEmpty()) {
            model.addAttribute("productos", productoRepository.findByCategoria(categoria));
        } else {
            model.addAttribute("productos", productoRepository.findAll());
        }

        model.addAttribute("tasaBCV", tasa);
        return "catalogo";
    }

    // RUTA DEL BUSCADOR (CORREGIDA)
    @GetMapping("/productos/buscar")
    public String buscarProductos(@RequestParam("nombre") String nombre, Model model) {
        double tasa = obtenerTasa();

        List<Producto> resultados = productoRepository.findByNombreContainingIgnoreCase(nombre);
        model.addAttribute("productos", resultados);
        model.addAttribute("tasaBCV", tasa);
        model.addAttribute("busqueda", nombre);

        return "catalogo";
    }

    @PostMapping("/finalizar-pedido")
    @ResponseBody
    public String finalizarPedido(@RequestBody Venta venta) {
        try {
            ventaRepository.save(Objects.requireNonNull(venta));
            return "Pedido guardado con éxito";
        } catch (Exception e) {
            return "Error al procesar el pedido: " + e.getMessage();
        }
    }

    // Método auxiliar para no repetir código de la tasa
    private double obtenerTasa() {
        try {
            return tasaService.obtenerTasaActual();
        } catch (Exception e) {
            return 55.50; // Tasa por defecto si falla el servicio
        }
    }
}