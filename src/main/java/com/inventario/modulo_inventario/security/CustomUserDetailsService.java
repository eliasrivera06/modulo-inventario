package com.inventario.modulo_inventario.security;

import com.inventario.modulo_inventario.entity.Usuario;
import com.inventario.modulo_inventario.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String codigo) throws UsernameNotFoundException {
        System.out.println("Intento de login con código: " + codigo);

        Usuario usuario = usuarioRepository.findByCodigo(codigo)
                .orElseThrow(() -> {
                    System.out.println("Usuario no encontrado: " + codigo);
                    return new UsernameNotFoundException("Usuario no encontrado con código: " + codigo);
                });

        System.out.println("Usuario encontrado: " + usuario.getCodigo());
        System.out.println("Hash en DB: " + usuario.getPassword());

        String role;
        if (codigo.startsWith("A")) {
            role = "ADMIN";
        } else if (codigo.startsWith("E")) {
            role = "EMPLEADO";
        } else {
            role = "EMPLEADO";
        }

        System.out.println("Rol asignado: " + role);

        return User.builder()
                .username(usuario.getCodigo())
                .password(usuario.getPassword())
                .roles(role)
                .build();
    }
}
