package com.galletas.tienda.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @GetMapping("/login")
    public String mostrarLogin() {
        return "admin/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Esto borra la sesión y el atributo "adminLogueado"
        session.invalidate();
        // Te devuelve al inicio de la página para clientes
        return "redirect:/";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam("password") String password, HttpSession session) {
        // Define aquí tu contraseña real para el negocio
        if ("16230776".equals(password)) {
            session.setAttribute("adminLogueado", true);
            return "redirect:/admin/ventas";
        }
        return "redirect:/admin/login?error=true";
    }
}