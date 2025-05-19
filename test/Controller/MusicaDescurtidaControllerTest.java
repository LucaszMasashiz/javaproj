/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import model.MusicaDescurtida;
import org.junit.*;
import java.util.List;
import controller.MusicaDescurtidaController;
/**
 *
 * @author Masashi
 */
public class MusicaDescurtidaControllerTest {
    protected MusicaDescurtidaController controller;
    protected final int usuarioId = 90; 
    protected final int musicaId = 67; 

    @Before
    public void setUp() {
        controller = new MusicaDescurtidaController();
        controller.descurtida(usuarioId, musicaId);
    }

    @After
    public void tearDown() {
        controller.descurtida(usuarioId, musicaId);
    }

    @Test
    public void testDescurtirMusica() {
        MusicaDescurtida descurtida = controller.descurtirMusica(usuarioId, musicaId);
        Assert.assertNotNull("Descurtida não foi salva", descurtida);
        Assert.assertTrue("ID não atribuído", descurtida.getId() > 0);
        Assert.assertTrue("Descurtida deveria existir", controller.descurtida(usuarioId, musicaId));
    }

    @Test
    public void testNaoDuplicarDescurtida() {
        MusicaDescurtida primeira = controller.descurtirMusica(usuarioId, musicaId);
        Assert.assertNotNull("Primeira descurtida não foi salva", primeira);
        MusicaDescurtida segunda = controller.descurtirMusica(usuarioId, musicaId);
        Assert.assertNull("Não deve salvar descurtida duplicada", segunda);
    }

    @Test
    public void testListarDescurtidasPorUsuario() {
        controller.descurtirMusica(usuarioId, musicaId);
        List<MusicaDescurtida> lista = controller.listarMusicasDescurtidas(usuarioId);
        Assert.assertNotNull("Lista não pode ser nula", lista);
        Assert.assertTrue("Descurtida não aparece na lista",
            lista.stream().anyMatch(d -> d.getMusicaId() == musicaId));
    }

    @Test
    public void testDeletarDescurtida() {
        controller.descurtirMusica(usuarioId, musicaId);
        boolean apagou = controller.descurtida(usuarioId, musicaId);
        Assert.assertTrue("Descurtida não foi removida", apagou);
        boolean existe = controller.descurtida(usuarioId, musicaId);
        Assert.assertFalse("Descurtida deveria ter sido removida", existe);
    }
}
