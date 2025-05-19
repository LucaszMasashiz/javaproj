package Controller;

import DAO.MusicaDAO;
import DAO.ArtistaDAO;
import model.Musica;
import model.Artista;
import org.junit.*;

import java.util.List;

public class MusicaControllerTest {
    private MusicaDAO musicaDAO;
    private ArtistaDAO artistaDAO;
    private Musica musicaCriada;
    private Artista artistaCriado;

    @Before
    public void setUp() {
        artistaDAO = new ArtistaDAO();
        musicaDAO = new MusicaDAO();

        // Cria um artista válido (com id gerado automaticamente)
        artistaCriado = new Artista("Artista Controller Teste", "Pop", 0, "Pessoa Controller");
        artistaCriado = artistaDAO.save(artistaCriado);
        Assert.assertNotNull("Artista não foi salvo", artistaCriado);
    }

    @After
    public void tearDown() {
        if (musicaCriada != null && musicaCriada.getId() > 0) {
            musicaDAO.delete(musicaCriada.getId());
        }
        if (artistaCriado != null && artistaCriado.getId() > 0) {
            artistaDAO.delete(artistaCriado.getId());
        }
    }

    @Test
    public void testSaveMusica() {
        musicaCriada = new Musica(0, artistaCriado.getId(), "Test Song", "Pop", "Test Album");
        Musica salva = musicaDAO.save(musicaCriada);
        Assert.assertNotNull("Musica não foi salva", salva);
        Assert.assertTrue("ID não foi atribuído", salva.getId() > 0);
        Assert.assertEquals("Nome diferente", "Test Song", salva.getNome());
    }

    @Test
    public void testFindById() {
        musicaCriada = new Musica(0, artistaCriado.getId(), "Busca ID", "Rock", "Album Busca");
        musicaCriada = musicaDAO.save(musicaCriada);
        Musica musica = musicaDAO.findById(musicaCriada.getId());
        Assert.assertNotNull("Musica não encontrada por ID", musica);
        Assert.assertEquals("Nome incorreto", "Busca ID", musica.getNome());
    }

    @Test
    public void testUpdateMusica() {
        musicaCriada = new Musica(0, artistaCriado.getId(), "Update Old", "Jazz", "Album Old");
        musicaCriada = musicaDAO.save(musicaCriada);
        musicaCriada.setNome("Update New");
        musicaCriada.setGenero("Blues");
        Musica atualizada = musicaDAO.update(musicaCriada);
        Assert.assertNotNull("Musica não foi atualizada", atualizada);
        Assert.assertEquals("Nome não atualizado", "Update New", atualizada.getNome());
        Assert.assertEquals("Genero não atualizado", "Blues", atualizada.getGenero());
    }

    @Test
    public void testFindAll() {
        musicaCriada = new Musica(0, artistaCriado.getId(), "Find All", "MPB", "Album MPB");
        musicaCriada = musicaDAO.save(musicaCriada);
        List<Musica> musicas = musicaDAO.findAll();
        Assert.assertNotNull("Lista de músicas é nula", musicas);
        Assert.assertTrue("Lista não contém a música criada",
                musicas.stream().anyMatch(m -> "Find All".equals(m.getNome())));
    }

    @Test
    public void testFindByGenero() {
        musicaCriada = new Musica(0, artistaCriado.getId(), "Por Genero", "Samba", "Album Samba");
        musicaCriada = musicaDAO.save(musicaCriada);
        List<Musica> musicas = musicaDAO.findByGenero("Samba");
        Assert.assertNotNull("Lista de músicas por gênero é nula", musicas);
        Assert.assertTrue("Música não encontrada pelo gênero",
                musicas.stream().anyMatch(m -> "Por Genero".equals(m.getNome())));
    }

    @Test
    public void testFindByNome() {
        musicaCriada = new Musica(0, artistaCriado.getId(), "UnicoNomeBusca", "Pop", "BuscaNomeAlbum");
        musicaCriada = musicaDAO.save(musicaCriada);
        List<Musica> musicas = musicaDAO.findByNome("UnicoNomeBusca");
        Assert.assertNotNull("Lista de músicas por nome é nula", musicas);
        Assert.assertTrue("Música não encontrada pelo nome",
                musicas.stream().anyMatch(m -> "UnicoNomeBusca".equals(m.getNome())));
    }

    @Test
    public void testFindByArtistaId() {
        musicaCriada = new Musica(0, artistaCriado.getId(), "Musica Artista", "Rap", "Album Artista");
        musicaCriada = musicaDAO.save(musicaCriada);
        List<Musica> musicas = musicaDAO.findByArtistaId(artistaCriado.getId());
        Assert.assertNotNull("Lista de músicas por artistaId é nula", musicas);
        Assert.assertTrue("Música não encontrada pelo artistaId",
                musicas.stream().anyMatch(m -> "Musica Artista".equals(m.getNome())));
    }

    @Test
    public void testDeleteMusica() {
        musicaCriada = new Musica(0, artistaCriado.getId(), "Delete Song", "Funk", "Album Delete");
        musicaCriada = musicaDAO.save(musicaCriada);
        boolean deletado = musicaDAO.delete(musicaCriada.getId());
        Assert.assertTrue("Música não foi deletada", deletado);
        Musica buscada = musicaDAO.findById(musicaCriada.getId());
        Assert.assertNull("Música ainda existe após remoção", buscada);
        musicaCriada = null; // Para não tentar remover de novo no @After
    }
}