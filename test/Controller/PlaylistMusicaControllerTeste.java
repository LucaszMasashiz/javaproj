
package controller;

import model.PlaylistMusica;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class PlaylistMusicaControllerTeste{
    private PlaylistMusicaController controller;

    @Before
    public void setUp() throws SQLException {
        controller = new PlaylistMusicaController();
        // Limpa as relações dessas playlists antes de cada teste
        controller.removerMusica(30, 67);
        controller.removerMusica(30, 68);
        controller.removerMusica(30, 69);
        controller.removerMusica(32, 70);
        controller.removerMusica(32, 71);
        controller.removerMusica(32, 72);
    }

    @Test
    public void testAdicionarMusica() {
        boolean result = controller.adicionarMusica(30, 67);
        assertTrue(result);

        List<Integer> musicas = controller.listarMusicasPorPlaylist(30);
        assertTrue(musicas.contains(67));
    }

    @Test
    public void testRemoverMusica() {
        // Adiciona antes de remover
        controller.adicionarMusica(30, 68);
        boolean result = controller.removerMusica(30, 68);
        assertTrue(result);

        List<Integer> musicas = controller.listarMusicasPorPlaylist(30);
        assertFalse(musicas.contains(68));
    }

    @Test
    public void testListarMusicasPorPlaylist() {
        controller.adicionarMusica(32, 70);
        controller.adicionarMusica(32, 71);

        List<Integer> musicas = controller.listarMusicasPorPlaylist(32);
        assertTrue(musicas.contains(70));
        assertTrue(musicas.contains(71));
    }

    @Test
    public void testListarTodasRelacoes() {
        controller.adicionarMusica(30, 69);
        controller.adicionarMusica(32, 72);

        List<PlaylistMusica> todas = controller.listarTodasRelacoes();
        boolean achou30_69 = todas.stream().anyMatch(pm -> pm.getPlaylistId() == 30 && pm.getMusicaId() == 69);
        boolean achou32_72 = todas.stream().anyMatch(pm -> pm.getPlaylistId() == 32 && pm.getMusicaId() == 72);

        assertTrue(achou30_69);
        assertTrue(achou32_72);
    }
}