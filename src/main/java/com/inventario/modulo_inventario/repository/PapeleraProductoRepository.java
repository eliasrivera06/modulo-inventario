package com.inventario.modulo_inventario.repository;

import com.inventario.modulo_inventario.entity.PapeleraProducto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PapeleraProductoRepository extends JpaRepository<PapeleraProducto, Long> {

    // Listar todos ordenados por fecha de eliminación (más recientes primero)
    List<PapeleraProducto> findAllByOrderByFechaEliminacionDesc();
}
