package com.inventariopromart.modulo_inventariopromart.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InventarioRequest {
    
    @NotBlank(message = "El nombre del solicitante es obligatorio")
    private String nombreSolicitante;
    
    @NotBlank(message = "El código del solicitante es obligatorio")
    private String codigoSolicitante;
    
    @NotBlank(message = "La sede es obligatoria")
    private String sedeOrigen;
    
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;
    
    @NotBlank(message = "El código del producto es obligatorio")
    private String codigoProducto;
    
    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombreProducto;
    
    @NotBlank(message = "La categoría es obligatoria")
    private String categoria;
    
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;
    
    private String comentario;
    
    @NotBlank(message = "El almacén de destino es obligatorio")
    private String almacenDestino;
    
    @NotBlank(message = "El tipo de acción es obligatorio")
    private String tipoAccion;
}
