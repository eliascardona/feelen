package com.stayfeelen.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.stayfeelen.model.Cancion;
import com.stayfeelen.repository.CancionRepository;
import com.stayfeelen.model.CancionWrapper;


import java.io.File;
import java.util.List;

@Component
public class CancionDataLoader implements CommandLineRunner {

    @Autowired
    private CancionRepository cancionRepository;

    @Override
    public void run(String... args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("src/main/resources/canciones.json");

        // Leer el archivo JSON
        CancionWrapper wrapper = mapper.readValue(file, CancionWrapper.class);

        // Guardar canciones en la base de datos si no están ya presentes
        for (Cancion c : wrapper.getCanciones()) {
            if (!cancionRepository.existsByNombre(c.getNombre())) {
                cancionRepository.save(c);
            }
        }
    }
}