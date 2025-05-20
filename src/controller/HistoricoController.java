package controller;

import DAO.HistoricoDAO;
import model.Historico;
import java.util.List;


/**
 * Controller responsável por gerenciar o histórico de buscas dos usuários.
 * Permite registrar novas buscas e listar buscas realizadas por um usuário.
 */
public class HistoricoController {
    protected HistoricoDAO historicoDAO;

    
    /**
     * Construtor padrão. Inicializa o DAO responsável pelo histórico.
     */
    public HistoricoController() {
        this.historicoDAO = new HistoricoDAO();
    }

    
    /**
     * Registra uma nova busca feita pelo usuário.
     *
     * @param usuarioId   ID do usuário que realizou a busca.
     * @param termoBusca  Termo buscado pelo usuário.
     */
    public void registrarBusca(int usuarioId, String termoBusca) {
        if (termoBusca != null && !termoBusca.trim().isEmpty()) {
            historicoDAO.save(new Historico(usuarioId, termoBusca));
        }
    }

    
    /**
     * Retorna a lista de buscas realizadas por um determinado usuário.
     *
     * @param usuarioId ID do usuário.
     * @return Lista de históricos de busca do usuário.
     */
    public List<Historico> listarBuscasPorUsuario(int usuarioId) {
        return historicoDAO.findByUsuarioId(usuarioId);
    }


}
