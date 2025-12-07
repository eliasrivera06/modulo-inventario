package com.inventario.modulo_inventario.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String codigo;

    @NotBlank
    private String password;

    @NotBlank
    private String nombre;

    private String rol; // ADMIN, EMPLEADO (Determined by logic)
}
