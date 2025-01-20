package com.stayfeelen.controller;

import com.stayfeelen.model.Playlist;
import com.stayfeelen.model.Cancion;
import com.stayfeelen.service.PlaylistService;
import com.stayfeelen.service.CancionService;
import com.stayfeelen.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.stayfeelen.model.Usuario;

import java.util.List;

@Controller
@RequestMapping("/menu")
public class PlaylistController {

    private final PlaylistService playlistService;
    private final CancionService cancionService;
    private final UsuarioService usuarioService;

    @Autowired
    public PlaylistController(PlaylistService playlistService, CancionService cancionService, UsuarioService usuarioService) {
        this.playlistService = playlistService;
        this.cancionService = cancionService;
        this.usuarioService = usuarioService;
    }

    // Mostrar las opciones de playlist para elegir
    @GetMapping("/crear-playlist")
    public String mostrarPlaylists(Model model) {
        model.addAttribute("estadoAnimoList", List.of("Feliz", "Triste", "Agresivo", "Amor"));
        return "seleccionar-playlist"; 
    }

    // Mostrar detalle de playlist seleccionada con canciones del estado de ánimo correspondiente
    @GetMapping("/ver-playlists/detalle/{id}")
    public String mostrarDetallePlaylist(@PathVariable String estadoAnimo, @SessionAttribute("usuarioId") Long usuarioId, Model model) {
        Playlist playlist = playlistService.obtenerPlaylistPorUsuarioYEstado(usuarioId, estadoAnimo);
        List<Cancion> canciones = cancionService.findByEstadoAnimo(estadoAnimo);

        if (playlist == null) {
            playlist = playlistService.crearPlaylist(estadoAnimo, estadoAnimo, usuarioId);
        }

        model.addAttribute("playlistId", playlist.getId());
        model.addAttribute("estadoAnimo", estadoAnimo);
        model.addAttribute("canciones", canciones);
        return "agregar-canciones"; 
    }

    // Agregar una canción a una playlist
    @PostMapping("/playlist-detalle/agregar")
    public String agregarCancionAPlaylist(@RequestParam Long playlistId, @RequestParam Long cancionId) {
        playlistService.agregarCancionAPlaylist(playlistId, cancionId);
        return "redirect:/menu/playlist-detalle/" + playlistService.obtenerPlaylistPorId(playlistId).getEstadoAnimo();
    }
    
 // Mostrar todas las playlists del usuario actual
    @GetMapping("/ver-playlists")
    public String verPlaylists(Model model) {
        // Obtiene el usuario actual a través del servicio
        Usuario usuario = usuarioService.obtenerUsuarioActual();

        // Verifica si el usuario es válido antes de continuar
        if (usuario == null) {
            model.addAttribute("usuario", new Usuario());
            model.addAttribute("playlists", List.of());
            return "ver-playlists";
        }

        // Recupera las playlists asociadas al usuario actual
        List<Playlist> playlists = playlistService.obtenerPlaylistsPorUsuario(usuario.getId());
        model.addAttribute("usuario", usuario);
        model.addAttribute("playlists", playlists);

        return "ver-playlists";
    }

    
    @PostMapping("/guardar-cancion")
    public String guardarCancion(@RequestParam String nombreCancion, 
                                 @RequestParam String estadoAnimo, 
                                 @RequestParam Long usuarioId) { // Añadimos el ID del usuario
        Usuario usuario = usuarioService.obtenerUsuarioPorId(usuarioId); // Obtener usuario actual
        Cancion cancion = cancionService.obtenerCancionPorNombre(nombreCancion);

        // Busca la playlist del usuario con el estado de ánimo correspondiente
        Playlist playlist = playlistService.obtenerPlaylistPorUsuarioYEstado(usuarioId, estadoAnimo);

        if (playlist == null) {
            // Crea una nueva playlist si no existe
            playlist = new Playlist(estadoAnimo, usuario);
            playlistService.guardarPlaylist(playlist);
        }

        // Agrega la canción a la playlist
        playlistService.agregarCancionAPlaylist(playlist, cancion);

        return "redirect:/menu/ver-playlists"; // Redirigir a la vista de playlists
    }


}
