package DAO;
import model.MusicaCurtida;
import org.junit.*;
import java.util.List;

public class MusicaCurtidaDAOTeste {
    private MusicaCurtidaDAO dao;
    private final int usuarioId = 90;    
    private final int musicaId = 67;    
    private int idCurtida = 0;

    @Before
    public void setUp() {
        dao = new MusicaCurtidaDAO();
        dao.delete(usuarioId, musicaId); 
    }

    @After
    public void tearDown() {
        dao.delete(usuarioId, musicaId);
    }

    @Test
    public void testSalvarEChecarCurtida() {
        MusicaCurtida curtida = new MusicaCurtida(usuarioId, musicaId);
        MusicaCurtida salva = dao.save(curtida);
        Assert.assertNotNull("Curtida não foi salva", salva);
        Assert.assertTrue("ID não atribuído", salva.getId() > 0);
        idCurtida = salva.getId();
        boolean existe = dao.isCurtida(usuarioId, musicaId);
        Assert.assertTrue("Curtida deveria existir", existe);
    }

    @Test
    public void testNaoDuplicarCurtida() {
        MusicaCurtida primeira = dao.save(new MusicaCurtida(usuarioId, musicaId));
        Assert.assertNotNull("Primeira curtida não foi salva", primeira);
        MusicaCurtida segunda = dao.save(new MusicaCurtida(usuarioId, musicaId));
        Assert.assertNull("Não deve salvar curtida duplicada", segunda);
    }

    @Test
    public void testBuscarPorUsuario() {
        dao.save(new MusicaCurtida(usuarioId, musicaId));
        List<MusicaCurtida> lista = dao.findByUsuarioId(usuarioId);
        Assert.assertNotNull("Lista não pode ser nula", lista);
        Assert.assertTrue("Curtida não aparece na lista", 
            lista.stream().anyMatch(c -> c.getMusicaId() == musicaId));
    }

    @Test
    public void testDeletarCurtida() {
        dao.save(new MusicaCurtida(usuarioId, musicaId));
        boolean apagou = dao.delete(usuarioId, musicaId);
        Assert.assertTrue("Curtida não foi removida", apagou);
        boolean existe = dao.isCurtida(usuarioId, musicaId);
        Assert.assertFalse("Curtida deveria ter sido removida", existe);
    }
}