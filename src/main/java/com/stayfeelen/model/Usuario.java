package com.stayfeelen.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String contraseña; 
    private String email;

    @ManyToMany
    @JoinTable(
        name = "usuario_playlist", 
        joinColumns = @JoinColumn(name = "usuario_id"), 
        inverseJoinColumns = @JoinColumn(name = "playlist_id"))
    private Set<Playlist> playlists = new HashSet<>(); // La relación muchos a muchos con Playlist

    public Usuario() {}

    // Constructor con parámetros para crear usuarios
    public Usuario(String nombre, String contraseña, String email) {
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.email = email;
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

    public String getContraseña() { 
        return contraseña;
    }

    public void setContraseña(String contraseña) { 
        this.contraseña = contraseña;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(Set<Playlist> playlists) {
        this.playlists = playlists;
    }


    // Métodos para agregar y eliminar playlists
    public void agregarPlaylist(Playlist playlist) {
        this.playlists.add(playlist);
        playlist.getUsuarios().add(this); // Asegurar que la relación bidireccional esté sincronizada
    }

    public void eliminarPlaylist(Playlist playlist) {
        this.playlists.remove(playlist);
        playlist.getUsuarios().remove(this); // Eliminar la relación bidireccional
    }

    // Método para verificar si el usuario ya tiene una playlist con un determinado estado de ánimo
    public boolean tienePlaylistConEstado(String estado) {
        return playlists.stream().anyMatch(playlist -> playlist.getEstadoAnimo().equalsIgnoreCase(estado));
    }

    // Método para asegurar que un usuario no tenga más de una playlist de un estado de ánimo
    public boolean puedeCrearPlaylistDeEstado(String estado) {
        return !tienePlaylistConEstado(estado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Usuario other = (Usuario) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", contraseña='" + contraseña + '\'' +
                ", email='" + email + '\'' +  
                '}';
    }
}