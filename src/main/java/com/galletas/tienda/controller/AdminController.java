package com.galletas.tienda.controller;

import com.galletas.tienda.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Objects; // Añadimos esto para que Objects funcione

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final VentaRepository ventaRepository;

    @GetMapping("/ventas")
    public String listarVentas(Model model) {
        // Mantenemos tu findAll() original para no tocar tu repositorio
        model.addAttribute("ventas", ventaRepository.findAll());
        return "admin/ventas";
    }

    @PostMapping("/ventas/actualizar-estado")
    public String actualizarEstado(@RequestParam("id") Long id, @RequestParam("nuevoEstado") String nuevoEstado) {
        if (id != null) {
            ventaRepository.findById(id).ifPresent(v -> {
                // Solo envolvemos el nuevoEstado con Objects.requireNonNull
                // Esto quita la advertencia amarilla sin cambiar NADA de tu lógica
                v.setEstado(Objects.requireNonNull(nuevoEstado));
                ventaRepository.save(v);
            });
        }
        return "redirect:/admin/ventas";
    }
}