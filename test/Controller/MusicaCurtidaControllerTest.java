/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.MusicaCurtida;
import org.junit.*;
import java.util.List;


/**
 *
 * @author Masashi
 */
public class MusicaCurtidaControllerTest {
    private MusicaCurtidaController controller;
    private final int usuarioId = 90; 
    private final int musicaId = 1;   

    @Before
    public void setUp() {
        controller = new MusicaCurtidaController();
        controller.descurtirMusica(usuarioId, musicaId);
    }

    @After
    public void tearDown() {
        controller.descurtirMusica(usuarioId, musicaId);
    }

    @Test
    public void testCurtirMusica() {
        MusicaCurtida curtida = controller.curtirMusica(usuarioId, musicaId);
        Assert.assertNotNull("Não conseguiu curtir música!", curtida);
        Assert.assertTrue("Música deveria estar curtida.", controller.curtido(usuarioId, musicaId));
    }

    @Test
    public void testNaoPermitirCurtirDuasVezes() {
        MusicaCurtida primeira = controller.curtirMusica(usuarioId, musicaId);
        MusicaCurtida segunda = controller.curtirMusica(usuarioId, musicaId);
        Assert.assertNotNull(primeira);
        Assert.assertNull("Não deve curtir duas vezes a mesma música.", segunda);
    }

    @Test
    public void testDescurtirMusica() {
        controller.curtirMusica(usuarioId, musicaId);
        boolean descurtiu = controller.descurtirMusica(usuarioId, musicaId);
        Assert.assertTrue("Não conseguiu descurtir música.", descurtiu);
        Assert.assertFalse("Música não deveria estar mais curtida.", controller.curtido(usuarioId, musicaId));
    }

    @Test
    public void testListarMusicasCurtidas() {
        controller.curtirMusica(usuarioId, musicaId);
        List<MusicaCurtida> curtidas = controller.listarMusicasCurtidas(usuarioId);
        Assert.assertNotNull("Lista de músicas curtidas não deve ser nula", curtidas);
        Assert.assertTrue("Deveria conter a música curtida", 
            curtidas.stream().anyMatch(c -> c.getMusicaId() == musicaId));
    }

    @Test
    public void testJaCurtiu() {
        Assert.assertFalse(controller.curtido(usuarioId, musicaId));
        controller.curtirMusica(usuarioId, musicaId);
        Assert.assertTrue(controller.curtido(usuarioId, musicaId));
    }
}