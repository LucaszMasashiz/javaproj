package DAO;

import model.Historico;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class HistoricoDAOTeste {

    private HistoricoDAO historicoDAO;
    private Historico historicoCriado;

    @Before
    public void setUp() {
        historicoDAO = new HistoricoDAO();
        historicoCriado = null;
    }

    @After
    public void tearDown() {
        if (historicoCriado != null && historicoCriado.getId() > 0) {
            historicoDAO.delete(historicoCriado.getId());
        }
    }

    @Test
    public void testSaveHistorico() {
        // Use um usuário que exista no banco (ex: 90)
        historicoCriado = new Historico(90, "busca teste DAO");
        Historico salvo = historicoDAO.save(historicoCriado);

        Assert.assertNotNull("Histórico não foi salvo", salvo);
        Assert.assertTrue("ID não foi atribuído", salvo.getId() > 0);
        Assert.assertEquals("Termo diferente", "busca teste DAO", salvo.getTermoBusca());
    }

    @Test
    public void testFindByUsuarioId() {
        // Use outro termo para garantir busca por usuario_id 94
        historicoCriado = new Historico(94, "busca usuario 94");
        historicoCriado = historicoDAO.save(historicoCriado);

        List<Historico> historicos = historicoDAO.findByUsuarioId(94);

        Assert.assertNotNull("Lista de históricos é nula", historicos);
        Assert.assertTrue("Histórico não encontrado pelo usuarioId",
                historicos.stream().anyMatch(h -> "busca usuario 94".equals(h.getTermoBusca())));
    }
}
