package com.stayfeelen.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.stayfeelen.model.Cancion;
import com.stayfeelen.repository.CancionRepository;
import com.stayfeelen.service.CancionService;

@Controller
public class CancionController {

    private static final String JSON_FILE = "canciones.json"; // Nombre del archivo en src/main/resources

    @Autowired
    private CancionService cancionService;
    
    @Autowired
    private CancionRepository cancionRepository;

    @GetMapping("/menu/explorar-canciones")
    public String explorarCanciones(Model model) {
        // List<Cancion> canciones = cargarCancionesDesdeJSON();
        // model.addAttribute("canciones", canciones);
        return "explorar-canciones";
    }

    // private List<Cancion> cargarCancionesDesdeJSON() {
    //     List<Cancion> canciones = new ArrayList<>();

    //     try {
    //         // Usar ClassLoader para acceder al archivo en src/main/resources
    //         InputStream inputStream = getClass().getClassLoader().getResourceAsStream(JSON_FILE);
    //         if (inputStream != null) {
    //             // Leer y mapear el JSON a un objeto con la propiedad 'canciones'
    //             ObjectMapper objectMapper = new ObjectMapper();
    //             // Asumir que el JSON tiene una propiedad "canciones" que es una lista
    //             JsonNode rootNode = objectMapper.readTree(inputStream);
    //             JsonNode cancionesNode = rootNode.path("canciones");

    //             // Mapear la lista de canciones y asociarlas con los archivos en mp3
    //             canciones = objectMapper.readValue(cancionesNode.toString(),
    //                     objectMapper.getTypeFactory().constructCollectionType(List.class, Cancion.class));

    //             // Establecer las rutas de los archivos mp3 en las canciones
    //             for (Cancion cancion : canciones) {
    //                 // Suponiendo que los archivos mp3 tienen el mismo nombre que las canciones en JSON
    //                 String rutaArchivo = "/mp3/" + cancion.getNombre() + ".mp3";
    //                 cancion.setRutaArchivo(rutaArchivo);
    //             }
    //         } else {
    //             System.out.println("No se encontró el archivo JSON en los recursos.");
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }

    //     return canciones;
    // }
    

    @GetMapping("/menu/playlist-detalle/{estadoAnimo}")
    public String mostrarCancionesPorEstadoAnimo(@PathVariable("estadoAnimo") String estadoAnimo, Model model) {
        try {
            // Obtener canciones por estado de ánimo
            List<Cancion> canciones = cancionService.obtenerCancionesPorEstadoAnimo(estadoAnimo);
            model.addAttribute("canciones", canciones);
            model.addAttribute("estadoAnimo", estadoAnimo); // Pasar el estado de ánimo a la vista
            return "canciones"; // Nombre del archivo HTML para la lista de canciones
        } catch (Exception e) {
            e.printStackTrace();
            return "error"; // Si hay un error, redirigir a una página de error
        }
    }
}
