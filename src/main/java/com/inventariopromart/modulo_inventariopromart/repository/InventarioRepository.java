package com.inventariopromart.modulo_inventariopromart.repository;

import com.inventariopromart.modulo_inventariopromart.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    // Puedes agregar métodos personalizados de consulta aquí si los necesitas
}
