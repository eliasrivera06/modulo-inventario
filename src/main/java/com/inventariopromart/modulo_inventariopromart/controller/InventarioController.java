package com.inventariopromart.modulo_inventariopromart.controller;
// Spring Web y validación
import com.inventariopromart.modulo_inventariopromart.dto.InventarioRequest;
import com.inventariopromart.modulo_inventariopromart.model.Inventario;
import com.inventariopromart.modulo_inventariopromart.service.InventarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {
// Lógica del controlador
    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @PostMapping
    public ResponseEntity<?> registrarInventario(@Valid @RequestBody InventarioRequest request) {
        try {
            // Convertir DTO a entidad
            Inventario inventario = new Inventario();
            inventario.setNombreSolicitante(request.getNombreSolicitante());
            inventario.setCodigoSolicitante(request.getCodigoSolicitante());
            inventario.setSedeOrigen(request.getSedeOrigen());
            inventario.setFecha(request.getFecha());
            inventario.setCodigoProducto(request.getCodigoProducto());
            inventario.setNombreProducto(request.getNombreProducto());
            inventario.setCategoria(request.getCategoria());
            inventario.setCantidad(request.getCantidad());
            inventario.setComentario(request.getComentario());
            inventario.setAlmacenDestino(request.getAlmacenDestino());
            inventario.setTipoAccion(request.getTipoAccion());
            
            // Guardar en la base de datos
            Inventario inventarioGuardado = inventarioService.guardarInventario(inventario);
            
            // Generar el contenido del archivo de texto
            String contenidoTxt = generarContenidoTxt(inventarioGuardado);
            String nombreArchivo = "solicitud_inventario_" + inventarioGuardado.getId() + ".txt";
            
            // Crear respuesta exitosa
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Solicitud registrada exitosamente");
            response.put("data", inventarioGuardado);
            response.put("ticket", contenidoTxt);
            response.put("nombreArchivo", nombreArchivo);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            // Manejo de errores
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Error al guardar el registro de inventario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    private String generarContenidoTxt(Inventario inventario) {
        return "=== SOLICITUD DE INVENTARIO ===\n\n" +
               "Datos del Solicitante:\n" +
               "----------------------\n" +
               "Nombre: " + inventario.getNombreSolicitante() + "\n" +
               "Código: " + inventario.getCodigoSolicitante() + "\n" +
               "Sede: " + inventario.getSedeOrigen() + "\n" +
               "Fecha: " + inventario.getFecha() + "\n\n" +
               "Datos del Producto:\n" +
               "-------------------\n" +
               "Código: " + inventario.getCodigoProducto() + "\n" +
               "Nombre: " + inventario.getNombreProducto() + "\n" +
               "Categoría: " + inventario.getCategoria() + "\n" +
               "Cantidad: " + inventario.getCantidad() + "\n\n" +
               "Detalles Adicionales:\n" +
               "--------------------\n" +
               "Almacén Destino: " + inventario.getAlmacenDestino() + "\n" +
               "Tipo de Acción: " + inventario.getTipoAccion() + "\n" +
               "Comentarios: " + (inventario.getComentario() != null ? inventario.getComentario() : "Ninguno") + "\n\n" +
               "Fecha de Registro: " + inventario.getFechaRegistro() + "\n" +
               "Número de Solicitud: " + inventario.getId() + "\n\n" +
               "=== FIN DE LA SOLICITUD ===";
    }
}
