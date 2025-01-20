package com.stayfeelen.service.rest;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stayfeelen.model.Cancion;

public class CancionRESTService {

    private static final String JSON_FILE = "canciones.json"; // Nombre del archivo en src/main/resources

    public List<Cancion> cargarCancionesDesdeJSON() {
        List<Cancion> canciones = new ArrayList<>();

        try {
            // Usar ClassLoader para acceder al archivo en src/main/resources
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(JSON_FILE);
            if (inputStream != null) {
                // Leer y mapear el JSON a un objeto con la propiedad 'canciones'
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(inputStream);
                JsonNode cancionesNode = rootNode.path("canciones");

                // Mapear la lista de canciones y asociarlas con los archivos en mp3
                canciones = objectMapper.readValue(cancionesNode.toString(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Cancion.class));

                // Establecer las rutas de los archivos mp3 en las canciones
                for (Cancion cancion : canciones) {
                    String rutaArchivo = "/mp3/" + cancion.getNombre() + ".mp3";
                    cancion.setRutaArchivo(rutaArchivo);
                }
            } else {
                System.out.println("No se encontró el archivo JSON en los recursos.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return canciones;
    }
}
