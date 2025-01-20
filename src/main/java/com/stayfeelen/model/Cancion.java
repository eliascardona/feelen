package com.stayfeelen.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cancion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre; 
    private String autor; 
    private String duracion;

    @JsonProperty("estadoAnimo")
    private String estadoAnimo;
    private String rutaArchivo;

    @ManyToOne
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;


    public Cancion() {}

    public Cancion(String nombre, String duracion, String estadoAnimo, String autor) {
        this.nombre = nombre;
        this.autor = autor; 
        this.duracion = duracion;
        this.estadoAnimo = estadoAnimo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getEstadoAnimo() {
        return estadoAnimo;
    }

    public void setEstadoAnimo(String estadoAnimo) {
        this.estadoAnimo = estadoAnimo;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    @Override
    public String toString() {
        return "Cancion{" +
               "id=" + id +
               ", nombre='" + nombre + '\'' +
               ", duracion='" + duracion + '\'' +
               ", estadoAnimo='" + estadoAnimo + '\'' +
               ", autor='" + autor + '\'' +  
               '}';
    }
}
