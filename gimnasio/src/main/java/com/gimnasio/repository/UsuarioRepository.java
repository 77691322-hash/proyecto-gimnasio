package com.gimnasio.repository;

import com.gimnasio.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    Optional<Usuario> findFirstByNombreUsuarioOrCorreo(String nombreUsuario, String correo);
}
