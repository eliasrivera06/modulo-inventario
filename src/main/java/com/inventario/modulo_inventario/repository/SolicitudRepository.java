package com.inventario.modulo_inventario.repository;

import com.inventario.modulo_inventario.entity.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
}
