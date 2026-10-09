package com.gimnasio.service;

import com.gimnasio.model.Usuario;
import com.gimnasio.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder encriptador = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario buscar(Integer id) {
        return usuarioRepository.findById(id).orElseThrow();
    }

    public void guardar(Usuario usuario) {
        Usuario existente = usuarioRepository.findByNombreUsuario(usuario.getNombreUsuario()).orElse(null);
        if (existente != null && !existente.getIdUsuario().equals(usuario.getIdUsuario())) {
            throw new ReglaNegocioException("Ya existe un usuario con ese nombre.");
        }
        if (usuario.getContrasena() != null && !usuario.getContrasena().isBlank()) {
            usuario.setContrasena(encriptador.encode(usuario.getContrasena()));
        } else if (usuario.getIdUsuario() != null) {
            usuario.setContrasena(buscar(usuario.getIdUsuario()).getContrasena());
        } else {
            throw new ReglaNegocioException("La contraseña es obligatoria para un usuario nuevo.");
        }
        usuarioRepository.save(usuario);
    }

    public void cambiarEstado(Integer id) {
        Usuario usuario = buscar(id);
        usuario.setEstado(!usuario.isEstado());
        usuarioRepository.save(usuario);
    }

    public Usuario autenticar(String usuarioOCorreo, String contrasena) {
        Usuario usuario = usuarioRepository.findFirstByNombreUsuarioOrCorreo(usuarioOCorreo, usuarioOCorreo).orElse(null);
        if (usuario == null || !usuario.isEstado() || !encriptador.matches(contrasena, usuario.getContrasena())) {
            throw new ReglaNegocioException("Usuario o contraseña incorrectos.");
        }
        return usuario;
    }
}
