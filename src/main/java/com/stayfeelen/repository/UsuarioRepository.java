package com.stayfeelen.repository;

import com.stayfeelen.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

 
    Optional<Usuario> findByEmail(String email);

  
    boolean existsByEmail(String email);
    
    Optional<Usuario> findByEmailAndContraseña(String email, String contraseña);

    Optional<Usuario> findById(Long usuarioId);

}
