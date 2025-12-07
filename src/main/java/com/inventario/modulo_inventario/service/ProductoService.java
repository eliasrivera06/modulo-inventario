package com.inventario.modulo_inventario.service;

import com.inventario.modulo_inventario.entity.Producto;
import com.inventario.modulo_inventario.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public List<Producto> buscar(String keyword, String sede, String categoria, String marca) {
        return productoRepository.search(keyword, sede, categoria, marca);
    }

    @Transactional
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Transactional
    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }
}
