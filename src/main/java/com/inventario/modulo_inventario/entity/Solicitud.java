package com.inventario.modulo_inventario.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "solicitudes")
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Datos del solicitante
    private String nombreSolicitante;
    private String codigoSolicitante;
    private String sedeOrigen;
    private LocalDateTime fecha;

    // Datos del producto
    private String codigoProducto;
    private String nombreProducto;
    private String categoriaProducto;
    private Integer cantidadSolicitada;

    // Otros datos
    private String motivo;
    private String destino;
    private String tipoAccion; // "CONSULTA", "TRANSFERENCIA"

    // Estado de la solicitud: "Enviado", "Recibido", "Realizado"
    @Column(nullable = false)
    private String estado = "Enviado";
}
