package com.inventario.modulo_inventario.repository;

import com.inventario.modulo_inventario.entity.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    // Listar todas ordenadas por fecha descendente
    List<Solicitud> findAllByOrderByFechaDesc();

    // Buscar por nombre de solicitante o código de encargado
    @Query("SELECT s FROM Solicitud s WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(s.nombreSolicitante) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.codigoSolicitante) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "ORDER BY s.fecha DESC")
    List<Solicitud> buscarPorNombreOCodigo(@Param("keyword") String keyword);
}
