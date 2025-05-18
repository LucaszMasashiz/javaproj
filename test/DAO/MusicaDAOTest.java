
package DAO;

import connection.ConnectionBD;
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

public class MusicaDAOTest {
    private static MusicaDAO musicaDAO;
    private static ArtistaDAO artistaDAO; // Para gerenciar artistas de teste
    private static Connection conn;
    private static int idArtistaTeste; // Para armazenar o ID de um artista de teste

    @BeforeClass
    public static void setUpClass() throws SQLException {
        conn = ConnectionBD.getInstance().getConnection();
        assertNotNull("A conexão com o banco de dados não pôde ser estabelecida.", conn);

        musicaDAO = new MusicaDAO();
        artistaDAO = new ArtistaDAO(); // DAO para criar artista de referência

        try (Statement stmt = conn.createStatement()) {
            // Cria tabela pessoa (dependência para Artista)
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS pessoa (
                  id SERIAL PRIMARY KEY,
                  nome VARCHAR(100) NOT NULL
                )
            """);
            // Cria tabela artista (dependência para Musica)
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS artista (
                  id INT PRIMARY KEY,
                  nome_artistico VARCHAR(100) NOT NULL UNIQUE,
                  genero VARCHAR(50),
                  FOREIGN KEY (id) REFERENCES pessoa(id) ON DELETE CASCADE
                )
            """);
            // Cria tabela musica
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
        }

        // Criar um artista de teste para usar nas músicas
        Artista artistaDeTeste = new Artista("Artista Teste Mus", "Indie", 0, "Nome Real Artista Mus");
        Artista artistaSalvo = artistaDAO.save(artistaDeTeste);
        assertNotNull("Artista de teste não pôde ser salvo", artistaSalvo);
        idArtistaTeste = artistaSalvo.getId();
        assertTrue("ID do artista de teste inválido", idArtistaTeste > 0);
    }

    @Before
    public void cleanUp() throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // Limpar musicas primeiro devido à FK
            stmt.execute("TRUNCATE TABLE musica CASCADE");
            // Não vamos limpar artistas e pessoas aqui para manter o idArtistaTeste válido entre os testes de música.
            // Se cada teste de música precisasse de um artista novo/limpo, a lógica seria diferente.
        }
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            try (Statement stmt = conn.createStatement()) {
                // Limpeza final completa
                stmt.execute("TRUNCATE TABLE musica CASCADE");
                stmt.execute("TRUNCATE TABLE artista CASCADE");
                stmt.execute("TRUNCATE TABLE pessoa CASCADE");
            }
            conn.close();
        }
        if (musicaDAO != null) {
            musicaDAO.closeConnection();
        }
        if (artistaDAO != null) {
            artistaDAO.closeConnection();
        }
    }

    @Test
    public void testSaveAndFindById() {
        // ID da música é 0 porque esperamos que seja auto-gerado
        Musica novaMusica = new Musica(0, idArtistaTeste, "Canção Teste", "Pop Experimental", "Album X");
        
        Musica salva = musicaDAO.save(novaMusica);
        assertNotNull("save() não deve retornar null em caso de sucesso", salva);
        assertTrue("ID da música deve ser > 0 após salvar", salva.getId() > 0);
        assertEquals("Nome da música não corresponde", "Canção Teste", salva.getNome());
        assertEquals("ID do artista não corresponde", idArtistaTeste, salva.getArtistaId());
        assertEquals("Gênero não corresponde", "Pop Experimental", salva.getGenero());
        assertEquals("Álbum não corresponde", "Album X", salva.getAlbum());

        Musica encontrada = musicaDAO.findById(salva.getId());
        assertNotNull("findById() deve encontrar a música salva", encontrada);
        assertEquals("ID da música encontrada não corresponde", salva.getId(), encontrada.getId());
        assertEquals("Nome da música encontrada não corresponde", "Canção Teste", encontrada.getNome());
    }

    @Test
    public void testSaveComArtistaInexistenteDeveFalhar() {
        // Tentar salvar música com um artista_id que não existe na tabela artista
        // O banco de dados deve rejeitar devido à constraint de FK
        Musica musicaComArtistaRuim = new Musica(0, 99999, "Musica Fantasma", "Desconhecido", "Nenhum");
        Musica salva = musicaDAO.save(musicaComArtistaRuim);
        assertNull("save() deve retornar null ao tentar salvar música com artista_id inválido", salva);
    }


    @Test
    public void testFindByIdNaoExistente() {
        Musica naoExistente = musicaDAO.findById(88888);
        assertNull("findById() deve retornar null para música não existente", naoExistente);
    }

    @Test
    public void testFindAll() {
        assertTrue("findAll() deve retornar lista vazia inicialmente", musicaDAO.findAll().isEmpty());

        Musica musica1 = new Musica(0, idArtistaTeste, "Musica A", "Rock", "Album Rock");
        Musica musica2 = new Musica(0, idArtistaTeste, "Musica B", "Blues", "Album Blues");
        
        assertNotNull(musicaDAO.save(musica1));
        assertNotNull(musicaDAO.save(musica2));

        List<Musica> todas = musicaDAO.findAll();
        assertNotNull("findAll() não deve retornar null", todas);
        assertEquals("findAll() deve retornar 2 músicas", 2, todas.size());
    }
    
    @Test
    public void testFindByArtistaId() {
        // Criar um segundo artista para garantir que o filtro funciona
        Artista outroArtista = new Artista("Outro Artista Mus", "Jazz", 0, "Nome Real Outro");
        Artista outroArtistaSalvo = artistaDAO.save(outroArtista);
        assertNotNull(outroArtistaSalvo);
        int idOutroArtista = outroArtistaSalvo.getId();

        Musica musica1 = new Musica(0, idArtistaTeste, "Musica Artista1", "Pop", "Album P1");
        Musica musica2 = new Musica(0, idOutroArtista, "Musica Artista2", "Jazz", "Album J1");
        Musica musica3 = new Musica(0, idArtistaTeste, "Outra Musica Artista1", "Pop", "Album P2");

        assertNotNull(musicaDAO.save(musica1));
        assertNotNull(musicaDAO.save(musica2));
        assertNotNull(musicaDAO.save(musica3));

        List<Musica> musicasDoArtistaTeste = musicaDAO.findByArtistaId(idArtistaTeste);
        assertNotNull(musicasDoArtistaTeste);
        assertEquals("Deve encontrar 2 músicas para o artista de teste", 2, musicasDoArtistaTeste.size());
        for (Musica m : musicasDoArtistaTeste) {
            assertEquals("ID do artista incorreto na lista filtrada", idArtistaTeste, m.getArtistaId());
        }

        List<Musica> musicasDoOutroArtista = musicaDAO.findByArtistaId(idOutroArtista);
        assertNotNull(musicasDoOutroArtista);
        assertEquals("Deve encontrar 1 música para o outro artista", 1, musicasDoOutroArtista.size());
        assertEquals(idOutroArtista, musicasDoOutroArtista.get(0).getArtistaId());

        List<Musica> musicasArtistaInexistente = musicaDAO.findByArtistaId(77777);
        assertTrue("Deve retornar lista vazia para artista_id inexistente", musicasArtistaInexistente.isEmpty());
    }

    @Test
    public void testFindByGenero() {
        Musica musicaPop1 = new Musica(0, idArtistaTeste, "Pop Song 1", "Pop", "Pop Album");
        Musica musicaRock = new Musica(0, idArtistaTeste, "Rock Anthem", "Rock", "Rock Hits");
        Musica musicaPop2 = new Musica(0, idArtistaTeste, "Another Pop", "pop", "Pop Album 2"); // Gênero em minúsculas

        assertNotNull(musicaDAO.save(musicaPop1));
        assertNotNull(musicaDAO.save(musicaRock));
        assertNotNull(musicaDAO.save(musicaPop2));

        List<Musica> musicasPop = musicaDAO.findByGenero("Pop"); // ILIKE deve pegar "Pop" e "pop"
        assertNotNull(musicasPop);
        assertEquals("Deve encontrar 2 músicas do gênero Pop (case insensitive)", 2, musicasPop.size());
        for (Musica m : musicasPop) {
            assertTrue("Gênero da música não é Pop (ou variação)", m.getGenero().equalsIgnoreCase("Pop"));
        }
        
        List<Musica> musicasEletronica = musicaDAO.findByGenero("Eletronica");
        assertTrue("Deve retornar lista vazia para gênero inexistente", musicasEletronica.isEmpty());
    }

    @Test
    public void testUpdate() {
        Musica original = new Musica(0, idArtistaTeste, "Musica Original", "Folk", "Album Folk");
        Musica salva = musicaDAO.save(original);
        assertNotNull("Música original não foi salva para o teste de update", salva);

        salva.setNome("Musica Atualizada");
        salva.setGenero("Folk Rock");
        salva.setAlbum("Novo Album Folk");
        // Não vamos mudar o artistaId aqui, mas poderia ser testado

        Musica atualizada = musicaDAO.update(salva);
        assertNotNull("update() não deve retornar null em caso de sucesso", atualizada);
        assertEquals("Nome não foi atualizado corretamente", "Musica Atualizada", atualizada.getNome());
        assertEquals("Gênero não foi atualizado corretamente", "Folk Rock", atualizada.getGenero());
        assertEquals("Álbum não foi atualizado corretamente", "Novo Album Folk", atualizada.getAlbum());

        Musica buscadaAposUpdate = musicaDAO.findById(salva.getId());
        assertNotNull("Não foi possível buscar a música após o update", buscadaAposUpdate);
        assertEquals("Nome buscado não corresponde ao atualizado", "Musica Atualizada", buscadaAposUpdate.getNome());
    }
    
    @Test
    public void testUpdateMusicaNaoExistente() {
        Musica musicaNaoExistente = new Musica(88888, idArtistaTeste, "Nome Fantasma", "Genero Fantasma", "Album Fantasma");
        Musica resultadoUpdate = musicaDAO.update(musicaNaoExistente);
        assertNull("Update em música não existente deveria retornar null (ou conforme a lógica do DAO)", resultadoUpdate);
    }

    @Test
    public void testDelete() {
        Musica musicaParaDeletar = new Musica(0, idArtistaTeste, "Som Apagavel", "Experimental", "Single");
        Musica salva = musicaDAO.save(musicaParaDeletar);
        assertNotNull("Música não foi salva para o teste de delete", salva);

        int idParaDeletar = salva.getId();
        assertTrue("delete() deve retornar true em caso de sucesso", musicaDAO.delete(idParaDeletar));

        Musica deletada = musicaDAO.findById(idParaDeletar);
        assertNull("findById() após delete deve retornar null", deletada);
    }

    @Test
    public void testDeleteNaoExistente() {
        boolean resultadoDelete = musicaDAO.delete(77777);
        assertFalse("delete() para ID não existente deve retornar false (nenhuma linha afetada)", resultadoDelete);
    }
}