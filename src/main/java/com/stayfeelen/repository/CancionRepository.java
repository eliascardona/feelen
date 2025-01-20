package com.stayfeelen.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.stayfeelen.model.Cancion;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import com.stayfeelen.model.Usuario;


@Repository
public interface CancionRepository extends JpaRepository<Cancion, Long> {

    // Método para buscar canciones por estado de ánimo
    List<Cancion> findByEstadoAnimo(String estadoAnimo);

    // Método para buscar canciones por nombre (si es necesario)
    List<Cancion> findByNombreContainingIgnoreCase(String nombre);

    // Método para obtener canciones asociadas a una playlist
    
    @Query("SELECT c FROM Cancion c WHERE c.playlist.id = :playlistId")
    List<Cancion> findByPlaylists_Id(Long playlistId);

    boolean existsByNombre(String nombre);
    
    List<Cancion> findByNombre(String nombre);

    List<Cancion> findByUsuario(Usuario usuario);


}

