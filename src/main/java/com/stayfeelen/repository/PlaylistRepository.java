package com.stayfeelen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.stayfeelen.model.Playlist;
import com.stayfeelen.model.Cancion;
import com.stayfeelen.model.Usuario;


import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    // Buscar playlists por usuario y estado de ánimo
    Optional<Playlist> findByUsuarios_IdAndEstadoAnimo(Long usuarioId, String estadoAnimo);

    // Buscar playlists por ID de usuario
    List<Playlist> findByUsuarios_Id(Long usuarioId);

    Playlist findByUsuarioAndEstadoAnimo(Usuario usuario, String estadoAnimo);
    int countByUsuario(Usuario usuario);
    
    // Buscar playlists por nombre (parcial)
    List<Playlist> findByNombreContaining(String nombre);

    // Eliminar playlists asociadas a un usuario
    void deleteByUsuarios_Id(Long usuarioId);

    // Buscar canciones por ID de playlist
    @Query("SELECT c FROM Cancion c WHERE c.playlist.id = :playlistId")
    List<Cancion> findByPlaylists_Id(Long playlistId);

    // Buscar playlists por estado de ánimo y usuario
    @Query("SELECT p FROM Playlist p JOIN p.usuarios u WHERE u.id = :usuarioId AND p.estadoAnimo = :estadoAnimo")
    List<Playlist> findPlaylistsByEstadoAnimoYUsuario(Long usuarioId, String estadoAnimo);

    // Verificar si un usuario tiene una playlist con un determinado estado de ánimo
    boolean existsByUsuarios_IdAndEstadoAnimo(Long usuarioId, String estadoAnimo);

    // Buscar playlists por estado de ánimo
    List<Playlist> findByEstadoAnimo(String estadoAnimo);
}
