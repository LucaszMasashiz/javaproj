package Controller;

import controller.PlaylistController;
import model.Playlist;
import org.junit.*;
import java.util.List;

public class PlaylistControllerTest {

    private PlaylistController controller;
    private Playlist playlistTest;
    private final int usuarioId = 90; 

    @Before
    public void setUp() {
        controller = new PlaylistController();
        playlistTest = controller.criarPlaylist(usuarioId, "Minha Playlist Teste");
        Assert.assertNotNull("A playlist deve ser criada.", playlistTest);
    }

    @After
    public void tearDown() {
        if (playlistTest != null && playlistTest.getId() > 0) {
            controller.deletarPlaylist(playlistTest.getId());
        }
    }

    @Test
    public void testCriarPlaylist() {
        Playlist p = controller.criarPlaylist(usuarioId, "Outra Playlist");
        Assert.assertNotNull("Deve criar uma nova playlist", p);
        Assert.assertEquals(usuarioId, p.getUsuarioId());
        Assert.assertEquals("Outra Playlist", p.getNome());
        controller.deletarPlaylist(p.getId());
    }

    @Test
    public void testListarPlaylistsPorUsuario() {
        List<Playlist> playlists = controller.listarPorUsuario(usuarioId);
        Assert.assertNotNull(playlists);
        boolean found = false;
        for (Playlist p : playlists) {
            if (p.getId() == playlistTest.getId()) {
                found = true;
                break;
            }
        }
        Assert.assertTrue("A playlist criada deve estar na lista.", found);
    }

    @Test
    public void testAtualizarPlaylist() {
        String novoNome = "Playlist Atualizada";
        playlistTest.setNome(novoNome);
        Playlist atualizada = controller.atualizarPlaylist(playlistTest);
        Assert.assertNotNull(atualizada);
        Assert.assertEquals(novoNome, atualizada.getNome());
    }

    @Test
    public void testDeletarPlaylist() {
        Playlist p = controller.criarPlaylist(usuarioId, "Para Deletar");
        Assert.assertNotNull(p);
        boolean deleted = controller.deletarPlaylist(p.getId());
        Assert.assertTrue("A playlist deve ser deletada.", deleted);
    }

    @Test
    public void testAdicionarERemoverMusicaNaPlaylist() {
        int musicaId = 1111; 
        boolean adicionou = controller.adicionarMusica(playlistTest.getId(), musicaId);
        Assert.assertTrue(adicionou);

        boolean removeu = controller.removerMusica(playlistTest.getId(), musicaId);
        Assert.assertTrue(removeu);
    }

    @Test
    public void testBuscarPlaylistPorId() {
        Playlist p = controller.buscarPorId(playlistTest.getId());
        Assert.assertNotNull(p);
        Assert.assertEquals(playlistTest.getId(), p.getId());
    }
}