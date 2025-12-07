package com.inventario.modulo_inventario.config;

import com.inventario.modulo_inventario.entity.Producto;
import com.inventario.modulo_inventario.entity.Usuario;
import com.inventario.modulo_inventario.repository.ProductoRepository;
import com.inventario.modulo_inventario.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, ProductoRepository productoRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            // Crear o Actualizar Admin
            Usuario admin = usuarioRepository.findByCodigo("A12345678").orElse(new Usuario());
            admin.setCodigo("A12345678");
            admin.setNombre("Juan Alvarez");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setRol("ADMIN");
            usuarioRepository.save(admin);
            System.out.println(">>> USUARIO ADMIN ACTUALIZADO: A12345678 / 123456");

            // Crear o Actualizar Empleado
            Usuario empleado = usuarioRepository.findByCodigo("E12345678").orElse(new Usuario());
            empleado.setCodigo("E12345678");
            empleado.setNombre("Elias Rivera");
            empleado.setPassword(passwordEncoder.encode("123456"));
            empleado.setRol("EMPLEADO");
            usuarioRepository.save(empleado);
            System.out.println(">>> USUARIO EMPLEADO ACTUALIZADO: E12345678 / 123456");

            // Productos Iniciales
            List<Producto> productos = Arrays.asList(
                    crearProducto("P001", "Taladro Percutor", "/img/taladropercutor.webp", "Sede Chimbote",
                            "Herramientas", "Bosch", "Taladro de alta potencia", 50, new BigDecimal("250.00")),
                    crearProducto("P002", "Juego de Destornilladores", "/img/juegodedestornilladores.png", "Sede Norte",
                            "Herramientas", "Stanley", "Set de 12 piezas", 100, new BigDecimal("45.90")),
                    crearProducto("P003", "Pintura Latex Blanca", "/img/pinturalatexblanca.webp", "Sede Sur",
                            "Pinturas", "CPP", "Balde de 4 litros", 30, new BigDecimal("60.00")));

            for (Producto p : productos) {
                if (productoRepository.findByCodigo(p.getCodigo()).isEmpty()) {
                    productoRepository.save(p);
                    System.out.println(">>> PRODUCTO CREADO: " + p.getNombre());
                } else {
                    System.out.println(">>> PRODUCTO YA EXISTE: " + p.getNombre());
                }
            }
        };
    }

    private Producto crearProducto(String codigo, String nombre, String imagenUrl, String sede, String categoria,
            String marca, String descripcion, Integer stock, BigDecimal precio) {
        Producto p = new Producto();
        p.setCodigo(codigo);
        p.setNombre(nombre);
        p.setImagenUrl(imagenUrl);
        p.setSede(sede);
        p.setCategoria(categoria);
        p.setMarca(marca);
        p.setDescripcion(descripcion);
        p.setStock(stock);
        p.setPrecio(precio);
        return p;
    }
}
