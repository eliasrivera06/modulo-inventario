package com.inventariopromart.modulo_inventariopromart.model;
// JPA y Lombok
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "inventario")
@Data
public class Inventario {
    // Atributos de la entidad
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre del solicitante es obligatorio")
    @Column(nullable = false)
    private String nombreSolicitante;
    
    @NotBlank(message = "El código del solicitante es obligatorio")
    @Column(nullable = false)
    private String codigoSolicitante;
    
    @NotBlank(message = "La sede es obligatoria")
    @Column(nullable = false)
    private String sedeOrigen;
    
    @NotNull(message = "La fecha es obligatoria")
    @Column(nullable = false)
    private LocalDate fecha;
    
    @NotBlank(message = "El código del producto es obligatorio")
    @Column(nullable = false)
    private String codigoProducto;
    
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Column(nullable = false)
    private String nombreProducto;
    
    @NotBlank(message = "La categoría es obligatoria")
    @Column(nullable = false)
    private String categoria;
    
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    @Column(nullable = false)
    private Integer cantidad;
    
    @Column(columnDefinition = "TEXT")
    private String comentario;
    
    @NotBlank(message = "El almacén de destino es obligatorio")
    @Column(nullable = false)
    private String almacenDestino;
    
    @NotBlank(message = "El tipo de acción es obligatorio")
    @Column(nullable = false)
    private String tipoAccion;
    
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDate fechaRegistro;
    
    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDate.now();
    }
}
