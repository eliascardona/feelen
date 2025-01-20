package com.stayfeelen.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stayfeelen.model.Cancion;
import com.stayfeelen.repository.CancionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.stayfeelen.model.CancionWrapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import com.stayfeelen.model.Usuario;
import com.stayfeelen.service.UsuarioService; 

@Service
public class CancionService {
	
	@Autowired
    private CancionRepository cancionRepository;

	@Autowired
    private UsuarioService usuarioService;
	
    private static final String CANCIONES_JSON_PATH = "src/main/resources/canciones.json";
    private static final String MP3_DIR = "src/main/resources/static/mp3";

    // Guardar una canción
    public Cancion save(Cancion cancion) {
        return cancionRepository.save(cancion);
    }

    public List<Cancion> findByEstadoAnimo(String estadoAnimo) {
        return cancionRepository.findByEstadoAnimo(estadoAnimo);
    }

    public Cancion findById(Long id) {
        return cancionRepository.findById(id).orElse(null); 
    }

    // Leer canciones desde el archivo JSON y filtrar por estado de ánimo
    public List<Cancion> obtenerCancionesPorEstadoAnimo(String estadoAnimo) {
        try {
            List<Cancion> canciones = obtenerCanciones();
            // Filtrar por estado de ánimo
            return canciones.stream()
                    .filter(cancion -> cancion.getEstadoAnimo().equalsIgnoreCase(estadoAnimo))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al leer el archivo JSON");
        }
    }
    
    public List<Cancion> obtenerCancionesPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(usuarioId); 
        return cancionRepository.findByUsuario(usuario);
    }

    
    public List<Cancion> obtenerCanciones() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(CANCIONES_JSON_PATH);

        // Leer el archivo JSON con el wrapper
        CancionWrapper wrapper = objectMapper.readValue(file, CancionWrapper.class);
        return wrapper.getCanciones();
    }
    
    public Cancion obtenerCancionPorNombre(String nombre) {
        List<Cancion> canciones = cancionRepository.findByNombre(nombre);
        return canciones.isEmpty() ? null : canciones.get(0); // Retorna la primera canción o null si no se encuentra ninguna
    }

}
