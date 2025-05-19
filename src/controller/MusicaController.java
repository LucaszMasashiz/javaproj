package controller;

import DAO.MusicaDAO;
import model.Musica;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Masashi
 */
public class MusicaController {
    private MusicaDAO musicaDAO;
    private static final Logger LOGGER = Logger.getLogger(MusicaController.class.getName());

    public MusicaController() {
        this.musicaDAO = new MusicaDAO();
    }

    public List<Musica> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            LOGGER.warning("Nome da música está vazio.");
            return Collections.emptyList();
        }
        return musicaDAO.findByNome(nome);
    }

    public List<Musica> buscarPorGenero(String genero) {
    if (genero == null || genero.trim().isEmpty()) {
        LOGGER.warning("Gênero para busca está vazio.");
        return Collections.emptyList();
    }
    return musicaDAO.findByGenero(genero);
}
}