
package controller;

import DAO.HistoricoDAO;
import model.Historico;
import java.util.List;

public class HistoricoController {
    protected HistoricoDAO historicoDAO;

    public HistoricoController() {
        this.historicoDAO = new HistoricoDAO();
    }

    public void registrarBusca(int usuarioId, String termoBusca) {
        if (termoBusca != null && !termoBusca.trim().isEmpty()) {
            historicoDAO.save(new Historico(usuarioId, termoBusca));
        }
    }

    public List<Historico> listarBuscasPorUsuario(int usuarioId) {
        return historicoDAO.findByUsuarioId(usuarioId);
    }


}
