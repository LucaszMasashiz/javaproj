package controller;

import model.Historico;
import org.junit.*;

import java.util.List;

public class HistoricoControllerTeste {
    private HistoricoController historicoController;
    protected final int usuarioId = 90;
    protected int idHistoricoCriado = 0;

    @Before
    public void setUp() {
        historicoController = new HistoricoController();
        idHistoricoCriado = 0;
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testRegistrarEListarHistorico() {
        String termoBusca = "busca controller test";
        historicoController.registrarBusca(usuarioId, termoBusca);

        List<Historico> historicos = historicoController.listarBuscasPorUsuario(usuarioId);

        System.out.println("Históricos encontrados para usuário " + usuarioId + ":");
        for (Historico h : historicos) {
            System.out.println("ID: " + h.getId() + " | Termo: [" + h.getTermoBusca() + "]");
        }

        boolean achou = false;
        for (Historico h : historicos) {
            if (h.getTermoBusca().trim().equals(termoBusca.trim())) {
                idHistoricoCriado = h.getId();
                achou = true;
                break;
            }
        }
        Assert.assertTrue("Histórico não encontrado pelo termo de busca", achou);
    }

    @Test
    public void testRegistrarBuscaNaoSalvaVazio() {
        int countAntes = historicoController.listarBuscasPorUsuario(usuarioId).size();
        historicoController.registrarBusca(usuarioId, "    "); // espaço em branco
        int countDepois = historicoController.listarBuscasPorUsuario(usuarioId).size();
        Assert.assertEquals("Busca vazia deveria ser ignorada", countAntes, countDepois);
    }

    @Test
    public void testListarHistoricoVazioParaUsuarioInexistente() {
        int usuarioFalso = 999999; // deve ser um ID que não existe!
        List<Historico> historicos = historicoController.listarBuscasPorUsuario(usuarioFalso);
        Assert.assertNotNull("Lista de históricos não pode ser nula", historicos);
        Assert.assertTrue("Lista deve ser vazia para usuário inexistente", historicos.isEmpty());
    }
}