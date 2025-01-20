package com.stayfeelen.controller.rest;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.stayfeelen.model.Cancion;
// import com.stayfeelen.model.Cancion;


@RestController
public class CancionRESTController {

    // @Autowired
    // CancionRESTService cancionRESTService;
    private static final String JSON_FILE = "canciones.json"; // Nombre del archivo en src/main/resources

    private List<Cancion> cargarCancionesDesdeJSON() throws Exception {
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
            System.out.println("---------" + "printStackTrace()" + " de metodo interno controller cargarCancionesDesdeJSON()");
            e.printStackTrace();
            throw e;
        }

        return canciones;
    }

    @GetMapping("/api/menu/explorar-canciones")
    public ResponseEntity<String> obtenerCanciones() {
        try {
            // Cargar las canciones desde el archivo JSON
            List<Cancion> canciones = cargarCancionesDesdeJSON();

            // Crear la estructura de respuesta personalizada
            Gson gson = new Gson();
            String respuestaJSON = gson.toJson(new RespuestaCustom(canciones));

            // Retornar la respuesta con un estado HTTP 200 (OK)
            return new ResponseEntity<>(respuestaJSON, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("---------" + "printStackTrace()" + " de endpoint HTTP");
            e.printStackTrace();
            return new ResponseEntity<>("error" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Clase interna para la respuesta personalizada
    public static class RespuestaCustom {

        private List<Cancion> payload;

        public RespuestaCustom(List<Cancion> payload) {
            this.payload = payload;
        }

        public List<Cancion> getPayload() {
            return payload;
        }

        public void setPayload(List<Cancion> payload) {
            this.payload = payload;
        }
    }
}
