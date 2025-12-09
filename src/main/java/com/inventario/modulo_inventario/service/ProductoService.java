package com.inventario.modulo_inventario.service;

import com.inventario.modulo_inventario.entity.PapeleraProducto;
import com.inventario.modulo_inventario.entity.Producto;
import com.inventario.modulo_inventario.repository.PapeleraProductoRepository;
import com.inventario.modulo_inventario.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final PapeleraProductoRepository papeleraProductoRepository;

    public ProductoService(ProductoRepository productoRepository,
            PapeleraProductoRepository papeleraProductoRepository) {
        this.productoRepository = productoRepository;
        this.papeleraProductoRepository = papeleraProductoRepository;
    }

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public List<Producto> buscar(String keyword, String sede, String categoria, String marca) {
        return productoRepository.search(keyword, sede, categoria, marca);
    }

    // Búsqueda solo de productos disponibles (para empleados)
    public List<Producto> buscarDisponibles(String keyword, String sede, String categoria, String marca) {
        return productoRepository.searchDisponibles(keyword, sede, categoria, marca);
    }

    @Transactional
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    // Eliminar producto (mover a papelera primero)
    @Transactional
    public void eliminar(Long id) {
        productoRepository.findById(id).ifPresent(producto -> {
            // Copiar a papelera antes de eliminar
            PapeleraProducto papelera = PapeleraProducto.fromProducto(producto);
            papeleraProductoRepository.save(papelera);
            // Eliminar de productos
            productoRepository.deleteById(id);
        });
    }

    // Cambiar estado del producto (disponible/no disponible)
    @Transactional
    public void cambiarEstado(Long id) {
        productoRepository.findById(id).ifPresent(producto -> {
            producto.setEstado(producto.getEstado() == 1 ? 0 : 1);
            productoRepository.save(producto);
        });
    }

    // ========== MÉTODOS DE PAPELERA ==========

    // Listar productos en papelera
    public List<PapeleraProducto> listarPapelera() {
        return papeleraProductoRepository.findAllByOrderByFechaEliminacionDesc();
    }

    // Restaurar producto desde papelera
    @Transactional
    public void restaurarDesdePapelera(Long papeleraId) {
        papeleraProductoRepository.findById(papeleraId).ifPresent(papelera -> {
            // Crear nuevo producto desde papelera
            Producto producto = papelera.toProducto();
            productoRepository.save(producto);
            // Eliminar de papelera
            papeleraProductoRepository.deleteById(papeleraId);
        });
    }

    // Eliminar permanentemente de papelera
    @Transactional
    public void eliminarPermanentemente(Long papeleraId) {
        papeleraProductoRepository.deleteById(papeleraId);
    }

    // Vaciar papelera completa
    @Transactional
    public void vaciarPapelera() {
        papeleraProductoRepository.deleteAll();
    }
}
