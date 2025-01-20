package com.stayfeelen.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String estadoAnimo;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
        
    @ManyToMany(mappedBy = "playlists")
    private Set<Usuario> usuarios = new HashSet<>(); 

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Cancion> canciones = new HashSet<>(); 

    public Playlist() {}

    // Modificar constructor para aceptar usuarios
    public Playlist(String nombre, String estadoAnimo, Set<Usuario> usuarios) {
        this.nombre = nombre;
        this.estadoAnimo = estadoAnimo;
        this.canciones = new HashSet<>();
        this.usuarios = usuarios;
    }
    
    public Playlist(String estadoAnimo) {
        this.nombre = estadoAnimo;  // Asigna el nombre igual al estadoAnimo
        this.estadoAnimo = estadoAnimo;
        this.canciones = new HashSet<>();  
        this.usuarios = new HashSet<>(); 
    }

    // Nuevo constructor para Playlist con un usuario
    public Playlist(String estadoAnimo, Usuario usuario) {
        this.nombre = "Playlist " + estadoAnimo; // Generar un nombre automático, opcional
        this.estadoAnimo = estadoAnimo;
        this.usuario = usuario;
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

    public String getEstadoAnimo() {
        return estadoAnimo;
    }

    public void setEstadoAnimo(String estadoAnimo) {
        this.estadoAnimo = estadoAnimo;
    }

    public Set<Cancion> getCanciones() {
        return canciones;
    }

    public void setCanciones(Set<Cancion> canciones) {
        this.canciones = canciones;
    }

    public Set<Usuario> getUsuarios() {
        return usuarios; 
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios; 
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    // Método para agregar una canción a la playlist
    public void agregarCancion(Cancion cancion) {
        this.canciones.add(cancion);
    }

    // Método para eliminar una canción de la playlist
    public void eliminarCancion(Cancion cancion) {
        this.canciones.remove(cancion);
    }

    // Método para verificar si una canción ya está en la playlist
    public boolean contieneCancion(Cancion cancion) {
        return this.canciones.contains(cancion);
    }

    // Método para asegurarse de que no se agreguen canciones duplicadas
    public boolean agregarCancionSiNoExistente(Cancion cancion) {
        if (!this.contieneCancion(cancion)) {
            this.agregarCancion(cancion);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        // Ajustar el toString para trabajar con múltiples usuarios
        StringBuilder usuarioNombres = new StringBuilder();
        for (Usuario u : usuarios) {
            usuarioNombres.append(u.getNombre()).append(", ");
        }
        return "Playlist{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", estadoAnimo='" + estadoAnimo + '\'' +
                ", usuarios=" + usuarioNombres.toString() + '}';
    }
}
