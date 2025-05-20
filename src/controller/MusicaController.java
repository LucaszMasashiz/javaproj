package controller;

import DAO.MusicaDAO;
import model.Musica;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;


/**
 * Controller responsável por gerenciar as operações relacionadas às músicas.
 * Realiza a ponte entre a camada de visualização e a camada de acesso a dados (DAO).
 */
public class MusicaController {
    protected MusicaDAO musicaDAO;
    protected static final Logger LOGGER = Logger.getLogger(MusicaController.class.getName());

    /**
     * Construtor padrão que inicializa o MusicaDAO.
     */
    public MusicaController() {
        this.musicaDAO = new MusicaDAO();
    }

    /**
     * Busca uma lista de músicas pelo nome.
     *
     * @param nome Nome da música a ser buscada.
     * @return Lista de músicas que possuem o nome informado (parcial ou completo).
     */
    public List<Musica> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            LOGGER.warning("Nome da música está vazio.");
            return Collections.emptyList();
        }
        return musicaDAO.findByNome(nome);
    }

/**
     * Busca músicas por gênero.
     *
     * @param genero Gênero musical desejado.
     * @return Lista de músicas do gênero informado.
     */
    public List<Musica> buscarPorGenero(String genero) {
        if (genero == null || genero.trim().isEmpty()) {
            LOGGER.warning("Gênero para busca está vazio.");
            return Collections.emptyList();
        }
        return musicaDAO.findByGenero(genero);
    }
    
    /**
     * Busca músicas pelo identificador do artista.
     *
     * @param artistaId ID do artista.
     * @return Lista de músicas associadas ao artista.
     */
    public List<Musica> buscarPorArtistaId(int artistaId) {
        return musicaDAO.findByArtistaId(artistaId);
    }
    
    /**
     * Busca uma música específica pelo seu ID.
     *
     * @param id ID da música.
     * @return Objeto Musica correspondente ao ID, ou null se não encontrado.
     */
    public Musica buscarPorId(int id) {
        return musicaDAO.findById(id);
    }
    
}