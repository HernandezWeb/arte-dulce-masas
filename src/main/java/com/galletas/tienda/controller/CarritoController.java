package com.galletas.tienda.controller;

import com.galletas.tienda.model.Producto;
import com.galletas.tienda.model.Venta;
import com.galletas.tienda.repository.ProductoRepository;
import com.galletas.tienda.repository.VentaRepository;
import com.galletas.tienda.service.TasaService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final ProductoRepository productoRepository;
    private final TasaService tasaService;
    private final VentaRepository ventaRepository;

    @GetMapping
    public String verCarrito(HttpSession session, Model model) {
        @SuppressWarnings("unchecked")
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        double totalUsd = carrito.stream().mapToDouble(p -> p.getPrecio() != null ? p.getPrecio() : 0.0).sum();
        double tasaBcv = tasaService.obtenerTasaActual();
        double totalBs = totalUsd * tasaBcv;

        model.addAttribute("carrito", carrito);
        model.addAttribute("totalUsd", totalUsd);
        model.addAttribute("tasaBcv", tasaBcv);
        model.addAttribute("totalBs", totalBs);

        return "carrito";
    }

    @GetMapping("/limpiar-y-salir")
    public String limpiarYSalir(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/agregar")
    public String agregarAlCarrito(@RequestParam(name = "id", required = true) Long id, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");

        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        if (id != null) {
            Producto producto = productoRepository.findById(id).orElse(null);
            if (producto != null) {
                carrito.add(producto);
            }
        }

        session.setAttribute("carrito", carrito);
        session.setAttribute("carritoCount", carrito.size());

        return "redirect:/catalogoGalletas";
    }

    @PostMapping("/eliminar/{index}")
    public String eliminarProducto(@PathVariable("index") int index, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");

        if (carrito != null && index >= 0 && index < carrito.size()) {
            carrito.remove(index);
            session.setAttribute("carrito", carrito);
            session.setAttribute("carritoCount", carrito.size());
        }

        return "redirect:/carrito";
    }

    @PostMapping("/finalizar")
    public String finalizarPedido(
            @RequestParam("cliente") String cliente,
            @RequestParam("telefono") String telefono,
            @RequestParam("metodo") String metodo,
            HttpSession session,
            Model model) {

        @SuppressWarnings("unchecked")
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");

        if (carrito == null || carrito.isEmpty()) {
            return "redirect:/catalogoGalletas";
        }

        double tasaActual = tasaService.obtenerTasaActual();
        double totalUsd = carrito.stream().mapToDouble(p -> p.getPrecio() != null ? p.getPrecio() : 0.0).sum();
        double totalBs = totalUsd * tasaActual;

        StringBuilder detalles = new StringBuilder();
        for (Producto p : carrito) {
            detalles.append("- ").append(p.getNombre()).append("\n");
        }

        Venta nuevaVenta = new Venta();
        nuevaVenta.setCliente(Objects.requireNonNull(cliente));
        nuevaVenta.setTelefono(Objects.requireNonNull(telefono));
        nuevaVenta.setTotalUsd(totalUsd);
        nuevaVenta.setTotalBs(totalBs);
        nuevaVenta.setMetodoPago(Objects.requireNonNull(metodo));
        nuevaVenta.setDetalleProductos(detalles.toString());
        ventaRepository.save(nuevaVenta);

        String nroTelefono = "584122793635";
        String mensaje = "Hola Arte Dulce y Masas! Soy " + cliente + ". He realizado un pedido:\n" +
                detalles.toString() +
                "\nTotal: $" + String.format("%.2f", totalUsd) + " / Bs. " + String.format("%.2f", totalBs) +
                "\nPago: " + metodo;

        String urlWhatsapp = "https://wa.me/" + nroTelefono + "?text="
                + java.net.URLEncoder.encode(mensaje, java.nio.charset.StandardCharsets.UTF_8);

        model.addAttribute("cliente", cliente);
        model.addAttribute("urlWhatsapp", urlWhatsapp);

        session.removeAttribute("carrito");
        session.setAttribute("carritoCount", 0);

        return "exito";
    }
}