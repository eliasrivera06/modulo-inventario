package com.inventario.modulo_inventario.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String codigo;

    @NotBlank
    private String nombre;

    private String imagenUrl;

    @NotBlank
    private String sede;

    @NotBlank
    private String categoria;

    @NotBlank
    private String marca;

    private String descripcion;

    @NotNull
    @PositiveOrZero
    private Integer stock;

    @NotNull
    @PositiveOrZero
    private BigDecimal precio;
}
