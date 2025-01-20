package com.stayfeelen.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class CancionWrapper {

    @JsonProperty("canciones")  // Añadido explícitamente para asegurar que el atributo se mapea correctamente
    private List<Cancion> canciones;

    public CancionWrapper() {} // Constructor por defecto

    public CancionWrapper(List<Cancion> canciones) {
        this.canciones = canciones; // Constructor con parámetros
    }

    public List<Cancion> getCanciones() {
        return canciones;
    }

    public void setCanciones(List<Cancion> canciones) {
        this.canciones = canciones;
    }
}
