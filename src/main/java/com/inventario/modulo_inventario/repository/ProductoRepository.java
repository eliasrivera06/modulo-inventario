package com.inventario.modulo_inventario.repository;

import com.inventario.modulo_inventario.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
        @Query("SELECT p FROM Producto p WHERE " +
                        "(:keyword IS NULL OR :keyword = '' OR " +
                        "LOWER(p.nombre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(p.codigo) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(p.marca) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(p.categoria) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
                        "(:sede IS NULL OR :sede = '' OR p.sede = :sede) AND " +
                        "(:categoria IS NULL OR :categoria = '' OR p.categoria = :categoria) AND " +
                        "(:marca IS NULL OR :marca = '' OR p.marca = :marca)")
        List<Producto> search(@Param("keyword") String keyword,
                        @Param("sede") String sede,
                        @Param("categoria") String categoria,
                        @Param("marca") String marca);

        Optional<Producto> findByCodigo(String codigo);
}
