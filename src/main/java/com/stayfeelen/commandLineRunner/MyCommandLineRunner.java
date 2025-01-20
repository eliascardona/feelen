package com.stayfeelen.commandLineRunner;

import com.stayfeelen.model.Usuario;
import com.stayfeelen.model.Playlist;
import com.stayfeelen.repository.UsuarioRepository;
import com.stayfeelen.repository.PlaylistRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyCommandLineRunner implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PlaylistRepository playlistRepository;

    public MyCommandLineRunner(UsuarioRepository usuarioRepository, PlaylistRepository playlistRepository) {
        this.usuarioRepository = usuarioRepository;
        this.playlistRepository = playlistRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        poblarBD();
    }

    void poblarBD() {
        // Crear usuarios
    	Usuario usuario1 = new Usuario("daniel", "1234", "daniel@ejemplo.com");
        Usuario usuario1Saved = usuarioRepository.save(usuario1);

        // Crear playlist para usuario1
        Playlist playlist1 = new Playlist("Happy Playlist", usuario1Saved); // Asegurarse de pasar el usuario

        // Agregar usuario a la playlist
        playlist1.getUsuarios().add(usuario1Saved);
        playlistRepository.save(playlist1);

        // Crear otro usuario
        Usuario usuario2 = new Usuario("user2", "1234", "user2@ejemplo.com");
        Usuario usuario2Saved = usuarioRepository.save(usuario2);

        // Crear playlist para usuario2
        Playlist playlist2 = new Playlist("Sad Playlist", usuario2Saved); // Asegurarse de pasar el usuario

        // Agregar usuario a la playlist
        playlist2.getUsuarios().add(usuario2Saved);
        playlistRepository.save(playlist2);
    }
}
