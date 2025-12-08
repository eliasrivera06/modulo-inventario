package com.inventario.modulo_inventario.controller;

import com.inventario.modulo_inventario.entity.Producto;
import com.inventario.modulo_inventario.entity.Solicitud;
import com.inventario.modulo_inventario.service.ProductoService;
import com.inventario.modulo_inventario.service.SolicitudService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
@RequestMapping("/inventario")
public class InventarioController {

    private final ProductoService productoService;
    private final SolicitudService solicitudService;

    public InventarioController(ProductoService productoService, SolicitudService solicitudService) {
        this.productoService = productoService;
        this.solicitudService = solicitudService;
    }

    @GetMapping
    public String inventario(@RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sede,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String marca,
            Model model) {

        // Verificar si el usuario es ADMIN o EMPLEADO
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean esAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<Producto> productos;
        if (esAdmin) {
            // Admin ve todos los productos
            productos = productoService.buscar(keyword, sede, categoria, marca);
        } else {
            // Empleado solo ve productos disponibles
            productos = productoService.buscarDisponibles(keyword, sede, categoria, marca);
        }

        model.addAttribute("productos", productos);
        model.addAttribute("producto", new Producto()); // Para el modal de agregar/editar
        model.addAttribute("solicitud", new Solicitud()); // Para el modal de solicitud
        return "inventario";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/agregar")
    public String agregarProducto(@ModelAttribute Producto producto,
            @RequestParam("imagen") org.springframework.web.multipart.MultipartFile imagen) {
        if (!imagen.isEmpty()) {
            try {
                String fileName = imagen.getOriginalFilename();
                java.nio.file.Path path = java.nio.file.Paths.get("src/main/resources/static/img/" + fileName);
                java.nio.file.Files.createDirectories(path.getParent());
                java.nio.file.Files.copy(imagen.getInputStream(), path,
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                producto.setImagenUrl("/img/" + fileName);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        productoService.guardar(producto);
        return "redirect:/inventario";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/editar")
    public String editarProducto(@ModelAttribute Producto producto) {
        productoService.guardar(producto);
        return "redirect:/inventario";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        productoService.eliminar(id);
        return "redirect:/inventario";
    }

    // Cambiar estado del producto (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/estado/{id}")
    public String cambiarEstadoProducto(@PathVariable Long id) {
        productoService.cambiarEstado(id);
        return "redirect:/inventario";
    }

    @PostMapping("/solicitar")
    public ResponseEntity<InputStreamResource> generarSolicitud(@ModelAttribute Solicitud solicitud) {
        solicitud.setFecha(LocalDateTime.now());
        solicitudService.guardar(solicitud);

        ByteArrayInputStream bis = solicitudService.generarReporteTxt(solicitud);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=solicitud_" + solicitud.getCodigoProducto() + ".txt");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.TEXT_PLAIN)
                .body(new InputStreamResource(bis));
    }
}
