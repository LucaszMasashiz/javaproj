package DAO;

import model.MusicaDescurtida;
import org.junit.*;

import java.util.List;

public class MusicaDescurtidaDAOTeste {
    protected MusicaDescurtidaDAO dao;
    protected final int usuarioId = 90; 
    protected final int musicaId = 67;  
    
    @Before
    public void setUp() {
        dao = new MusicaDescurtidaDAO();
        dao.delete(usuarioId, musicaId);
    }

    @After
    public void tearDown() {
        dao.delete(usuarioId, musicaId);
    }

    @Test
    public void testSalvarEDescurtir() {
        MusicaDescurtida descurtida = new MusicaDescurtida(usuarioId, musicaId);
        MusicaDescurtida salva = dao.save(descurtida);
        Assert.assertNotNull("Descurtida não foi salva", salva);
        Assert.assertTrue("ID não atribuído", salva.getId() > 0);
        Assert.assertTrue("Descurtida deveria existir", dao.isDescurtida(usuarioId, musicaId));
    }

    @Test
    public void testNaoDuplicarDescurtida() {
        MusicaDescurtida primeira = dao.save(new MusicaDescurtida(usuarioId, musicaId));
        Assert.assertNotNull("Primeira descurtida não foi salva", primeira);
        MusicaDescurtida segunda = dao.save(new MusicaDescurtida(usuarioId, musicaId));
        Assert.assertNull("Não deve salvar descurtida duplicada", segunda);
    }

    @Test
    public void testBuscarPorUsuario() {
        dao.save(new MusicaDescurtida(usuarioId, musicaId));
        List<MusicaDescurtida> lista = dao.findByUsuarioId(usuarioId);
        Assert.assertNotNull("Lista não pode ser nula", lista);
        Assert.assertTrue("Descurtida não aparece na lista", 
            lista.stream().anyMatch(d -> d.getMusicaId() == musicaId));
    }

    @Test
    public void testDeletarDescurtida() {
        dao.save(new MusicaDescurtida(usuarioId, musicaId));
        boolean apagou = dao.delete(usuarioId, musicaId);
        Assert.assertTrue("Descurtida não foi removida", apagou);
        boolean existe = dao.isDescurtida(usuarioId, musicaId);
        Assert.assertFalse("Descurtida deveria ter sido removida", existe);
    }
}