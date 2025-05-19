package DAO;

import model.PlaylistMusica;
import org.junit.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import static org.junit.Assert.*;

public class PlaylistMusicaDAOTeste {
   private PlaylistMusicaDAO dao;

    @Before
    public void setUp() throws SQLException {
        dao = new PlaylistMusicaDAO();
       
        Connection conn = dao.conn;
        if (conn != null) {
            conn.createStatement().execute("DELETE FROM playlist_musica WHERE playlist_id IN (30, 32)");
        }
    }

    @Test
    public void testAddMusicaToPlaylist() {
       
        boolean result = dao.addMusicaToPlaylist(30, 67);
        assertTrue(result);

        List<Integer> musicas = dao.findMusicasByPlaylistId(30);
        assertEquals(1, musicas.size());
        assertEquals(Integer.valueOf(67), musicas.get(0));
    }

    @Test
    public void testRemoveMusicaFromPlaylist() {
        
        dao.addMusicaToPlaylist(32, 68);
        boolean result = dao.removeMusicaFromPlaylist(32, 68);
        assertTrue(result);

        List<Integer> musicas = dao.findMusicasByPlaylistId(32);
        assertTrue(musicas.isEmpty());
    }

    @Test
    public void testFindMusicasByPlaylistId() {
        
        dao.addMusicaToPlaylist(30, 69);
        dao.addMusicaToPlaylist(30, 70);

        List<Integer> musicas = dao.findMusicasByPlaylistId(30);
        assertTrue(musicas.contains(69));
        assertTrue(musicas.contains(70));
    }

    @Test
    public void testFindAll() {
       
        dao.addMusicaToPlaylist(32, 71);
        dao.addMusicaToPlaylist(32, 72);

        List<PlaylistMusica> todas = dao.findAll();
        assertTrue(todas.stream().anyMatch(pm -> pm.getPlaylistId() == 32 && pm.getMusicaId() == 71));
        assertTrue(todas.stream().anyMatch(pm -> pm.getPlaylistId() == 32 && pm.getMusicaId() == 72));
    }
}