package com.stayfeelen.service;

import com.stayfeelen.model.Usuario;
import com.stayfeelen.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {


    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario validarCredenciales(String email, String contraseña) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isPresent() && usuarioOpt.get().getContraseña().equals(contraseña)) {
            return usuarioOpt.get();
        }
        return null;
    }

    public Usuario obtenerUsuarioActual() {
        return usuarioRepository.findById(1L).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    
    public Usuario obtenerUsuarioPorId(Long usuarioId) {
        return usuarioRepository.findById(usuarioId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    
    // Verificar si el email ya está registrado
    public boolean existeEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    // Registrar un nuevo usuario
    public Usuario registrarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
    
}