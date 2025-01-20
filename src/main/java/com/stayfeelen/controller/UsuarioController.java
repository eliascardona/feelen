package com.stayfeelen.controller;

import com.stayfeelen.model.Usuario;
import com.stayfeelen.service.UsuarioService;
import com.stayfeelen.model.Playlist;
import com.stayfeelen.service.PlaylistService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.stayfeelen.service.CancionService;
import com.stayfeelen.model.Cancion;

import java.util.List;

@Controller
@RequestMapping("/auth")
@SessionAttributes("usuarioAutenticado") // Esto mantiene al usuario autenticado en la sesión
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final PlaylistService playlistService;
    private final CancionService cancionService;
    
    public UsuarioController(UsuarioService usuarioService, PlaylistService playlistService, CancionService cancionService) {
        this.usuarioService = usuarioService;
        this.playlistService = playlistService;
        this.cancionService = cancionService;
    }

    @GetMapping("/login")
    public String mostrarLoginForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@ModelAttribute("usuario") Usuario usuario, Model model) {
        Usuario usuarioExistente = usuarioService.validarCredenciales(usuario.getEmail(), usuario.getContraseña());
        if (usuarioExistente != null) {
            model.addAttribute("usuarioAutenticado", usuarioExistente); // Guardamos el usuario en el modelo
            model.addAttribute("mensaje", "Inicio de sesión exitoso. ¡Bienvenido " + usuarioExistente.getNombre() + "!");
            return "redirect:/auth/menu"; 
        } else {
            model.addAttribute("error", "Credenciales incorrectas. Por favor, inténtalo de nuevo.");
            return "login";
        }
    }

    @GetMapping("/register")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "register"; // Vista para el formulario de registro
    }

    @PostMapping("/register")
    public String procesarRegistro(@ModelAttribute("usuario") Usuario usuario, RedirectAttributes redirectAttributes) {
        if (usuarioService.existeEmail(usuario.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "El correo electrónico ya está registrado.");
            return "redirect:/auth/register"; 
        }
        // Guardar al nuevo usuario
        usuarioService.registrarUsuario(usuario);
        // Iniciar sesión automáticamente después del registro
        redirectAttributes.addFlashAttribute("mensajeExito", "Usuario registrado con éxito.");
        return "redirect:/auth/login"; 
    }

    @GetMapping("/ver-playlists")
    public String verPlaylistsGuardadas(@SessionAttribute("usuarioAutenticado") Usuario usuario, Model model) {
        if (usuario == null) {
            return "redirect:/auth/login"; // Si el usuario no está autenticado, redirige al login
        }

        List<Playlist> playlists = playlistService.obtenerPlaylistsPorUsuario(usuario.getId());

        model.addAttribute("usuario", usuario);
        model.addAttribute("playlists", playlists);

        return "ver-playlists"; 
    }

    @GetMapping("/ver-canciones-guardadas")
    public String verCancionesGuardadas(@SessionAttribute(name = "usuarioAutenticado", required = false) Usuario usuarioAutenticado, Model model) {
        if (usuarioAutenticado == null) {
            model.addAttribute("canciones", List.of()); // Lista vacía si no hay usuario autenticado
        } else {
            List<Cancion> canciones = cancionService.obtenerCancionesPorUsuario(usuarioAutenticado.getId());
            model.addAttribute("canciones", canciones); // Pasa las canciones al modelo
        }
        
        return "ver-canciones"; // Nombre de la plantilla HTML
    }

    @GetMapping("/menu")
    public String mostrarMenuPrincipal(@SessionAttribute(name = "usuarioAutenticado", required = false) Usuario usuario, Model model) {
        if (usuario == null) {
            return "redirect:/auth/login"; // Si no está autenticado, redirigir al login
        }

        model.addAttribute("mensaje", "¡Bienvenido al menú principal de Stay Feelen, " + usuario.getNombre() + "!");
        return "menu-principal";
    }
}
