package com.galletas.tienda.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.galletas.tienda.model.Producto;
import com.galletas.tienda.model.Venta;
import com.galletas.tienda.repository.ProductoRepository;
import com.galletas.tienda.repository.VentaRepository;
import com.galletas.tienda.service.TasaService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

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

    @PostMapping("/vaciar")
    public String vaciarCarrito(HttpSession session) {
        session.removeAttribute("carrito");
        session.setAttribute("carritoCount", 0);
        return "redirect:/catalogoGalletas";
    }

    @PostMapping("/procesar-compra")
    public String procesarCompra(
            @RequestParam("cliente") String cliente,
            @RequestParam("telefono") String telefono,
            @RequestParam("metodo") String metodo,
            HttpSession session) {

        @SuppressWarnings("unchecked")
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");

        if (carrito != null && !carrito.isEmpty()) {
            Venta nuevaVenta = new Venta();
            nuevaVenta.setCliente(cliente);
            nuevaVenta.setTelefono(telefono);

            // Guardamos en ambos campos para asegurar la compatibilidad con el HTML
            nuevaVenta.setMetodoPago(metodo);
            nuevaVenta.setMetodo(metodo);

            String nombresProductos = carrito.stream()
                    .map(p -> "- " + p.getNombre())
                    .collect(Collectors.joining(", "));

            // Guardamos en ambos campos para asegurar la compatibilidad con el HTML
            nuevaVenta.setDetalleProductos(nombresProductos);
            nuevaVenta.setProductos(nombresProductos);

            double totalUsd = carrito.stream().mapToDouble(p -> p.getPrecio() != null ? p.getPrecio() : 0.0).sum();
            double tasaBcv = tasaService.obtenerTasaActual();
            double totalBs = totalUsd * tasaBcv;

            nuevaVenta.setTotalUsd(totalUsd);
            nuevaVenta.setTotalBs(totalBs);

            ventaRepository.save(nuevaVenta);
        }

        session.removeAttribute("carrito");
        session.setAttribute("carritoCount", 0);

        return "redirect:/catalogoGalletas";
    }

    @PostMapping("/agregar")
    public String agregarAlCarrito(@RequestParam(name = "id") Long id, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");

        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        if (id != null) {
            productoRepository.findById(id).ifPresent(carrito::add);
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

    @GetMapping("/limpiar-y-salir")
    public String limpiarYSalir(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}