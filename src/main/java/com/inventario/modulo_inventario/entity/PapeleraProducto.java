package com.inventario.modulo_inventario.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "papelera_productos")
public class PapeleraProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID original del producto (para referencia)
    private Long productoIdOriginal;

    private String codigo;

    private String nombre;

    private String imagenUrl;

    private String sede;

    private String categoria;

    private String marca;

    private String descripcion;

    private Integer stock;

    private BigDecimal precio;

    private Integer estado;

    // Fecha en que fue eliminado
    @Column(nullable = false)
    private LocalDateTime fechaEliminacion;

    // Método para copiar datos desde un Producto
    public static PapeleraProducto fromProducto(Producto producto) {
        PapeleraProducto papelera = new PapeleraProducto();
        papelera.setProductoIdOriginal(producto.getId());
        papelera.setCodigo(producto.getCodigo());
        papelera.setNombre(producto.getNombre());
        papelera.setImagenUrl(producto.getImagenUrl());
        papelera.setSede(producto.getSede());
        papelera.setCategoria(producto.getCategoria());
        papelera.setMarca(producto.getMarca());
        papelera.setDescripcion(producto.getDescripcion());
        papelera.setStock(producto.getStock());
        papelera.setPrecio(producto.getPrecio());
        papelera.setEstado(producto.getEstado());
        papelera.setFechaEliminacion(LocalDateTime.now());
        return papelera;
    }

    // Método para convertir de vuelta a Producto
    public Producto toProducto() {
        Producto producto = new Producto();
        producto.setCodigo(this.codigo);
        producto.setNombre(this.nombre);
        producto.setImagenUrl(this.imagenUrl);
        producto.setSede(this.sede);
        producto.setCategoria(this.categoria);
        producto.setMarca(this.marca);
        producto.setDescripcion(this.descripcion);
        producto.setStock(this.stock);
        producto.setPrecio(this.precio);
        producto.setEstado(1); // Restaurar como disponible
        return producto;
    }
}
