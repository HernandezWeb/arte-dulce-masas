package com.galletas.tienda.controller;

import com.galletas.tienda.model.Venta;
import com.galletas.tienda.repository.ProductoRepository;
import com.galletas.tienda.repository.VentaRepository;
import com.galletas.tienda.service.TasaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Objects; // <--- IMPORTANTE PARA QUITAR ADVERTENCIAS

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

    @GetMapping("/catalogoGalletas")
    public String catalogoGalletas(@RequestParam(name = "categoria", required = false) String categoria, Model model) {
        try {
            if (categoria != null && !categoria.isEmpty()) {
                model.addAttribute("productos", productoRepository.findByCategoria(categoria));
            } else {
                model.addAttribute("productos", productoRepository.findAll());
            }
        } catch (Exception e) {
            model.addAttribute("productos", new java.util.ArrayList<>());
        }

        double tasa = 55.50;
        try {
            tasa = tasaService.obtenerTasaActual();
        } catch (Exception e) {
        }

        model.addAttribute("tasaBCV", tasa);
        return "catalogo";
    }

    // --- MÉTODO ACTUALIZADO SIN ADVERTENCIAS ---
    @PostMapping("/finalizar-pedido")
    @ResponseBody
    public String finalizarPedido(@RequestBody Venta venta) {
        try {
            // Usamos Objects.requireNonNull para asegurar al IDE que 'venta' no es nulo
            // Esto quita el aviso amarillo de "Null type safety"
            ventaRepository.save(Objects.requireNonNull(venta));
            return "Pedido guardado con éxito";
        } catch (Exception e) {
            return "Error al procesar el pedido: " + e.getMessage();
        }
    }
}