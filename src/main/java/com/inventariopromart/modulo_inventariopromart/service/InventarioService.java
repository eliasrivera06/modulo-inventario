package com.inventariopromart.modulo_inventariopromart.service;

import com.inventariopromart.modulo_inventariopromart.model.Inventario;
import com.inventariopromart.modulo_inventariopromart.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventarioService {

    private final InventarioRepository inventarioRepository;

    public InventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    @Transactional
    public Inventario guardarInventario(Inventario inventario) {
        // Aquí podrías agregar lógica adicional de negocio si es necesario
        return inventarioRepository.save(inventario);
    }

    @Transactional(readOnly = true)
    public List<Inventario> listarTodoElInventario() {
        return inventarioRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Inventario obtenerPorId(Long id) {
        return inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro de inventario no encontrado con ID: " + id));
    }
}
