package com.stayfeelen.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.stayfeelen.model.Cancion;
import com.stayfeelen.model.Playlist;
import com.stayfeelen.model.Usuario;
import com.stayfeelen.repository.CancionRepository;
import com.stayfeelen.repository.PlaylistRepository;
import com.stayfeelen.repository.UsuarioRepository;

import java.util.List;
import java.util.Set;

@Service
public class PlaylistService {

    private final CancionRepository cancionRepository;
    private final PlaylistRepository playlistRepository;
    private final UsuarioRepository usuarioRepository;

    public PlaylistService(CancionRepository cancionRepository, PlaylistRepository playlistRepository, UsuarioRepository usuarioRepository) {
        this.cancionRepository = cancionRepository;
        this.playlistRepository = playlistRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Crear una nueva Playlist
    @Transactional
    public Playlist crearPlaylist(String nombre, String estadoAnimo, Long usuarioId) {
        // Verificar si el usuario existe
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtener canciones por estado de ánimo
        List<Cancion> canciones = cancionRepository.findByEstadoAnimo(estadoAnimo);

        // Verificar si ya existe una playlist con el mismo estado de ánimo para el usuario
        if (playlistRepository.existsByUsuarios_IdAndEstadoAnimo(usuarioId, estadoAnimo)) {
            throw new RuntimeException("Ya tienes una playlist con este estado de ánimo.");
        }

     // Crear la nueva playlist
        Playlist playlist = new Playlist();
        playlist.setNombre(nombre);
        playlist.setEstadoAnimo(estadoAnimo);
        playlist.setCanciones(Set.copyOf(canciones)); // Copiar canciones para no modificar la lista original

     // Añadir la playlist a cada canción
        canciones.forEach(c -> c.setPlaylist(playlist));

        // Guardar y devolver la nueva playlist
        return playlistRepository.save(playlist);
    }

    // Listar todas las playlists
    @Transactional(readOnly = true)
    public List<Playlist> listarPlaylists() {
        return playlistRepository.findAll();
    }

    // Obtener playlists por usuario
    public int obtenerCantidadDePlaylistsPorUsuario(Usuario usuario) {
        return playlistRepository.countByUsuario(usuario);
    }
    
    @Transactional(readOnly = true)
    public List<Playlist> obtenerPlaylistsPorUsuario(Long usuarioId) {
        return playlistRepository.findByUsuarios_Id(usuarioId);
    }
    
    // Obtener una playlist por ID
    @Transactional(readOnly = true)
    public Playlist obtenerPlaylistPorId(Long playlistId) {
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist no encontrada"));
    }

    // Obtener playlist por usuario y estado de ánimo
    @Transactional(readOnly = true)
    public Playlist obtenerPlaylistPorUsuarioYEstado(Long usuarioId, String estadoAnimo) {
        return playlistRepository.findByUsuarios_IdAndEstadoAnimo(usuarioId, estadoAnimo)
                .orElse(null); 
    }
    
 // Guardar una nueva playlist
    public void guardarPlaylist(Playlist playlist) {
        playlistRepository.save(playlist);
    }

    // Agregar canción a una playlist
    public void agregarCancionAPlaylist(Playlist playlist, Cancion cancion) {
        playlist.getCanciones().add(cancion);
        playlistRepository.save(playlist);
    }

    // Eliminar una playlist por ID
    @Transactional
    public void eliminarPlaylist(Long playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist no encontrada"));

        playlistRepository.delete(playlist);
    }

    // Obtener playlists por estado de ánimo
    @Transactional(readOnly = true)
    public List<Playlist> obtenerPlaylistsPorEstadoAnimo(String estadoAnimo) {
        return playlistRepository.findByEstadoAnimo(estadoAnimo);
    }

    // Método para agregar una canción a una playlist
    @Transactional
    public Playlist agregarCancionAPlaylist(Long playlistId, Long cancionId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist no encontrada"));

        Cancion cancion = cancionRepository.findById(cancionId)
                .orElseThrow(() -> new RuntimeException("Canción no encontrada"));

        // Agregar la canción a la playlist
        playlist.getCanciones().add(cancion);
        
        // Guardar la relación entre la canción y la playlist solo en la playlist, ya que es una relación muchos a uno
        cancion.setPlaylist(playlist);

        return playlistRepository.save(playlist);
    }

    // Método para eliminar canciones de una playlist
    @Transactional
    public Playlist eliminarCancionesDePlaylist(Long playlistId, List<Long> cancionIds) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist no encontrada"));

        List<Cancion> canciones = cancionRepository.findAllById(cancionIds);
        
        // Eliminar las canciones de la playlist
        playlist.getCanciones().removeAll(canciones);

        // Eliminar la relación de la canción con la playlist
        canciones.forEach(c -> c.setPlaylist(null));

        return playlistRepository.save(playlist);
    }

    // Obtener usuario por ID
    @Transactional(readOnly = true)
    public Usuario obtenerUsuarioPorId(Long usuarioId) {
        return usuarioRepository.findById(usuarioId).orElse(null);
    }
}
