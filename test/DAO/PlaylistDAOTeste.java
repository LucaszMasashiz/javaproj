
package DAO;

import connection.ConnectionBD;
import model.Playlist;
import model.Usuario; 
import model.Artista;
import model.Musica;  

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.AfterClass;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.*;

public class PlaylistDAOTeste {
    protected static PlaylistDAO playlistDAO;
    protected static UsuarioDAO usuarioDAO;
    protected static ArtistaDAO artistaDAO;
    protected static MusicaDAO musicaDAO;
    protected static Connection conn;

    protected static int idUsuarioTeste;
    protected static int idArtistaTesteP;
    protected static int idMusicaTeste1;
    protected static int idMusicaTeste2;


    @BeforeClass
    public static void setUpClass() throws SQLException {
        conn = ConnectionBD.getInstance().getConnection();
        assertNotNull("A conexão com o banco de dados não pôde ser estabelecida.", conn);

        playlistDAO = new PlaylistDAO();
        usuarioDAO = new UsuarioDAO();
        artistaDAO = new ArtistaDAO();
        musicaDAO = new MusicaDAO();

        try (Statement stmt = conn.createStatement()) {
           
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS pessoa (
                  id SERIAL PRIMARY KEY,
                  nome VARCHAR(100) NOT NULL
                )
            """);
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS usuario (
                  id INT PRIMARY KEY,
                  email VARCHAR(100) NOT NULL UNIQUE,
                  senha INT NOT NULL,
                  FOREIGN KEY (id) REFERENCES pessoa(id) ON DELETE CASCADE
                )
            """);
             stmt.execute("""
                CREATE TABLE IF NOT EXISTS artista (
                  id INT PRIMARY KEY,
                  nome_artistico VARCHAR(100) NOT NULL UNIQUE,
                  genero VARCHAR(50),
                  FOREIGN KEY (id) REFERENCES pessoa(id) ON DELETE CASCADE
                )
            """);
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS musica (
                  id SERIAL PRIMARY KEY,
                  artista_id INT NOT NULL,
                  nome VARCHAR(100) NOT NULL,
                  genero VARCHAR(50),
                  album VARCHAR(100),
                  FOREIGN KEY (artista_id) REFERENCES artista(id) ON DELETE CASCADE
                )
            """);
            
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS playlist (
                  id SERIAL PRIMARY KEY,
                  usuario_id INT NOT NULL,
                  nome VARCHAR(100) NOT NULL,
                  FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
                )
            """);
          
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS playlist_musica (
                  playlist_id INT NOT NULL,
                  musica_id INT NOT NULL,
                  PRIMARY KEY (playlist_id, musica_id),
                  FOREIGN KEY (playlist_id) REFERENCES playlist(id) ON DELETE CASCADE,
                  FOREIGN KEY (musica_id) REFERENCES musica(id) ON DELETE CASCADE
                )
            """);
        }

       
        Usuario usuarioDeTeste = new Usuario(13, "user.playlist@exemplo.com", 123, "User Playlist Test");
        Usuario usuarioSalvo = usuarioDAO.save(usuarioDeTeste);
        assertNotNull("Usuário de teste não pôde ser salvo", usuarioSalvo);
        idUsuarioTeste = usuarioSalvo.getId();
        assertTrue("ID do usuário de teste inválido", idUsuarioTeste > 0);


        Artista artistaDeTeste = new Artista("Artista Playlist Test", "Pop", 0, "Nome Real Artista Playlist");
        Artista artistaSalvo = artistaDAO.save(artistaDeTeste);
        assertNotNull("Artista de teste (playlist) não pôde ser salvo", artistaSalvo);
        idArtistaTesteP = artistaSalvo.getId();

        
        Musica musica1 = new Musica(0, idArtistaTesteP, "Musica Playlist 1", "Pop", "Album P1");
        Musica musica2 = new Musica(0, idArtistaTesteP, "Musica Playlist 2", "Rock", "Album P2");
        Musica musicaSalva1 = musicaDAO.save(musica1);
        Musica musicaSalva2 = musicaDAO.save(musica2);
        assertNotNull(musicaSalva1);
        assertNotNull(musicaSalva2);
        idMusicaTeste1 = musicaSalva1.getId();
        idMusicaTeste2 = musicaSalva2.getId();
    }

    @Before
    public void cleanUp() throws SQLException {
        try (Statement stmt = conn.createStatement()) {
           
            stmt.execute("TRUNCATE TABLE playlist_musica CASCADE");
            stmt.execute("TRUNCATE TABLE playlist CASCADE");
            
        }
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("TRUNCATE TABLE playlist_musica CASCADE");
                stmt.execute("TRUNCATE TABLE playlist CASCADE");
                stmt.execute("TRUNCATE TABLE musica CASCADE");
                stmt.execute("TRUNCATE TABLE artista CASCADE");
                stmt.execute("TRUNCATE TABLE usuario CASCADE");
                stmt.execute("TRUNCATE TABLE pessoa CASCADE");
            }
            conn.close();
        }
        if (playlistDAO != null) playlistDAO.closeConnection();
        if (musicaDAO != null) musicaDAO.closeConnection();
        if (artistaDAO != null) artistaDAO.closeConnection();
        if (usuarioDAO != null) usuarioDAO.closeConnection();
    }

    @Test
    public void testSaveAndFindById() {
        Playlist novaPlaylist = new Playlist(0, idUsuarioTeste, "Minha Playlist Favorita");
        
        Playlist salva = playlistDAO.save(novaPlaylist);
        assertNotNull("save() não deve retornar null para playlist", salva);
        assertTrue("ID da playlist deve ser > 0", salva.getId() > 0);
        assertEquals("Nome da playlist não corresponde", "Minha Playlist Favorita", salva.getNome());
        assertEquals("ID do usuário não corresponde", idUsuarioTeste, salva.getUsuarioId());

        Playlist encontrada = playlistDAO.findById(salva.getId());
        assertNotNull("findById() deve encontrar a playlist", encontrada);
        assertEquals("ID da playlist encontrada não corresponde", salva.getId(), encontrada.getId());
        assertEquals("Nome da playlist encontrada não corresponde", "Minha Playlist Favorita", encontrada.getNome());
    }

    @Test
    public void testSaveComUsuarioInexistenteDeveFalhar() {
        Playlist playlistComUsuarioRuim = new Playlist(0, 99999, "Playlist Fantasma");
        Playlist salva = playlistDAO.save(playlistComUsuarioRuim);
        assertNull("save() deve retornar null ao tentar salvar playlist com usuario_id inválido", salva);
    }

    @Test
    public void testFindByIdNaoExistente() {
        Playlist naoExistente = playlistDAO.findById(77777);
        assertNull("findById() deve retornar null para playlist não existente", naoExistente);
    }

    @Test
    public void testFindAll() {
        assertTrue("findAll() deve retornar lista vazia inicialmente", playlistDAO.findAll().isEmpty());

        Playlist p1 = new Playlist(0, idUsuarioTeste, "Playlist Rock");
        Playlist p2 = new Playlist(0, idUsuarioTeste, "Playlist Pop");
        assertNotNull(playlistDAO.save(p1));
        assertNotNull(playlistDAO.save(p2));

        List<Playlist> todas = playlistDAO.findAll();
        assertNotNull("findAll() não deve retornar null", todas);
        assertEquals("findAll() deve retornar 2 playlists", 2, todas.size());
    }

    @Test
    public void testFindByUsuarioId() {
       
        Usuario outroUsuario = new Usuario(11, "outro.user.playlist@exemplo.com", 456, "Outro User Playlist");
        Usuario outroUsuarioSalvo = usuarioDAO.save(outroUsuario);
        assertNotNull(outroUsuarioSalvo);
        int idOutroUsuario = outroUsuarioSalvo.getId();

        Playlist p1User1 = new Playlist(0, idUsuarioTeste, "Favoritas User 1");
        Playlist p2UserOutro = new Playlist(0, idOutroUsuario, "Relax User Outro");
        Playlist p3User1 = new Playlist(0, idUsuarioTeste, "Treino User 1");

        assertNotNull(playlistDAO.save(p1User1));
        assertNotNull(playlistDAO.save(p2UserOutro));
        assertNotNull(playlistDAO.save(p3User1));

        List<Playlist> playlistsUser1 = playlistDAO.findByUsuarioId(idUsuarioTeste);
        assertNotNull(playlistsUser1);
        assertEquals("Deve encontrar 2 playlists para o usuário de teste", 2, playlistsUser1.size());
        for(Playlist p : playlistsUser1) {
            assertEquals(idUsuarioTeste, p.getUsuarioId());
        }

        List<Playlist> playlistsOutroUser = playlistDAO.findByUsuarioId(idOutroUsuario);
        assertNotNull(playlistsOutroUser);
        assertEquals("Deve encontrar 1 playlist para o outro usuário", 1, playlistsOutroUser.size());
        assertEquals(idOutroUsuario, playlistsOutroUser.get(0).getUsuarioId());
        
        List<Playlist> playlistUsuarioInexistente = playlistDAO.findByUsuarioId(88888);
        assertTrue("Deve retornar lista vazia para usuario_id inexistente", playlistUsuarioInexistente.isEmpty());
    }

    @Test
    public void testUpdate() {
        Playlist original = new Playlist(0, idUsuarioTeste, "Playlist Antiga");
        Playlist salva = playlistDAO.save(original);
        assertNotNull("Playlist original não foi salva", salva);

        salva.setNome("Playlist Nova & Melhorada");
        
        Playlist atualizada = playlistDAO.update(salva);
        assertNotNull("update() não deve retornar null", atualizada);
        assertEquals("Nome não foi atualizado", "Playlist Nova & Melhorada", atualizada.getNome());

        Playlist buscada = playlistDAO.findById(salva.getId());
        assertNotNull(buscada);
        assertEquals("Nome buscado não confere com o atualizado", "Playlist Nova & Melhorada", buscada.getNome());
    }

    @Test
    public void testUpdatePlaylistNaoExistente() {
        Playlist playlistNaoExistente = new Playlist(77777, idUsuarioTeste, "Nome Fantasma");
        Playlist resultadoUpdate = playlistDAO.update(playlistNaoExistente);
        assertNull("Update em playlist não existente deveria retornar null", resultadoUpdate);
    }

    @Test
    public void testDelete() {
        Playlist p = new Playlist(0, idUsuarioTeste, "Playlist Para Deletar");
        Playlist salva = playlistDAO.save(p);
        assertNotNull(salva);

        assertTrue("delete() deve retornar true", playlistDAO.delete(salva.getId()));
        assertNull("findById() deve retornar null após delete", playlistDAO.findById(salva.getId()));
    }

    @Test
    public void testDeleteNaoExistente() {
        assertFalse("delete() para ID não existente deve retornar false", playlistDAO.delete(77777));
    }

    
    @Test
    public void testAddAndRemoveMusicaFromPlaylist() {
        Playlist playlist = new Playlist(0, idUsuarioTeste, "Playlist de Músicas Variadas");
        Playlist playlistSalva = playlistDAO.save(playlist);
        assertNotNull(playlistSalva);
        int playlistId = playlistSalva.getId();

        
        assertTrue("Falha ao adicionar musica 1", playlistDAO.addMusicaToPlaylist(playlistId, idMusicaTeste1));
        assertTrue("Falha ao adicionar musica 2", playlistDAO.addMusicaToPlaylist(playlistId, idMusicaTeste2));

       
        List<Integer> musicasNaPlaylist = playlistDAO.findMusicasByPlaylistId(playlistId);
        assertEquals("Deveria haver 2 músicas na playlist", 2, musicasNaPlaylist.size());
        assertTrue("Musica 1 não encontrada na playlist", musicasNaPlaylist.contains(idMusicaTeste1));
        assertTrue("Musica 2 não encontrada na playlist", musicasNaPlaylist.contains(idMusicaTeste2));

     
        assertFalse("Adicionar música duplicada deveria falhar ou retornar false", playlistDAO.addMusicaToPlaylist(playlistId, idMusicaTeste1));
        assertEquals("Número de músicas não deve mudar após tentativa de duplicata", 2, playlistDAO.findMusicasByPlaylistId(playlistId).size());


        assertTrue("Falha ao remover musica 1", playlistDAO.removeMusicaFromPlaylist(playlistId, idMusicaTeste1));
        musicasNaPlaylist = playlistDAO.findMusicasByPlaylistId(playlistId);
        assertEquals("Deveria haver 1 música na playlist após remoção", 1, musicasNaPlaylist.size());
        assertFalse("Musica 1 ainda encontrada após remoção", musicasNaPlaylist.contains(idMusicaTeste1));
        assertTrue("Musica 2 não encontrada após remoção da musica 1", musicasNaPlaylist.contains(idMusicaTeste2));

     
        assertFalse("Remover música não existente na playlist deveria retornar false", playlistDAO.removeMusicaFromPlaylist(playlistId, 98765));
    }

    @Test
    public void testFindMusicasByPlaylistId_PlaylistVazia() {
        Playlist playlist = new Playlist(0, idUsuarioTeste, "Playlist Vazia");
        Playlist playlistSalva = playlistDAO.save(playlist);
        assertNotNull(playlistSalva);

        List<Integer> musicas = playlistDAO.findMusicasByPlaylistId(playlistSalva.getId());
        assertTrue("Lista de músicas de playlist vazia deveria ser vazia", musicas.isEmpty());
    }
    
    @Test
    public void testDeletePlaylistComMusicas() {
        Playlist playlist = new Playlist(0, idUsuarioTeste, "Playlist Com Musicas Para Deletar");
        Playlist playlistSalva = playlistDAO.save(playlist);
        assertNotNull(playlistSalva);
        int playlistId = playlistSalva.getId();

        playlistDAO.addMusicaToPlaylist(playlistId, idMusicaTeste1);
        playlistDAO.addMusicaToPlaylist(playlistId, idMusicaTeste2);
        assertEquals(2, playlistDAO.findMusicasByPlaylistId(playlistId).size());

        assertTrue("Falha ao deletar playlist", playlistDAO.delete(playlistId));
        assertNull("Playlist não deveria ser encontrada após delete", playlistDAO.findById(playlistId));
        
        
        List<Integer> musicasAposDeletePlaylist = playlistDAO.findMusicasByPlaylistId(playlistId);
        assertTrue("Não deveria haver músicas associadas à playlist deletada se ON DELETE CASCADE funcionou", musicasAposDeletePlaylist.isEmpty());
        
        assertNotNull("Musica 1 ainda deve existir na tabela musica", musicaDAO.findById(idMusicaTeste1));
        assertNotNull("Musica 2 ainda deve existir na tabela musica", musicaDAO.findById(idMusicaTeste2));

    }
}