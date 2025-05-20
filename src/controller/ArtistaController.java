package controller;

import DAO.ArtistaDAO;
import model.Artista;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller responsável pelas operações de gerenciamento de artistas.
 * Faz a mediação entre a camada de visão e o DAO de artistas.
 * Permite cadastrar, atualizar, remover, buscar e listar artistas.
 *
 * @author Masashi
 */
public class ArtistaController {
    protected ArtistaDAO artistaDAO;
    protected static final Logger LOGGER = Logger.getLogger(ArtistaController.class.getName());

    /**
     * Construtor padrão. Inicializa o DAO de artistas.
     */
    public ArtistaController() {
        this.artistaDAO = new ArtistaDAO();
    }

    /**
     * Salva um novo artista no sistema.
     *
     * @param nomeArtistico Nome artístico do artista.
     * @param genero Gênero musical do artista.
     * @param nomeReal Nome real do artista.
     * @return O artista salvo, ou null se algum campo for inválido ou ocorrer erro.
     */
    public Artista salvarArtista(String nomeArtistico, String genero, String nomeReal) {
        if (nomeArtistico == null || nomeArtistico.trim().isEmpty() ||
            genero == null || genero.trim().isEmpty() ||
            nomeReal == null || nomeReal.trim().isEmpty()) {
            LOGGER.warning("Campos obrigatórios não preenchidos.");
            return null;
        }
        Artista artista = new Artista(nomeArtistico, genero, 0, nomeReal);
        return artistaDAO.save(artista);
    }

 /**
     * Atualiza as informações de um artista existente.
     *
     * @param id ID do artista a ser atualizado.
     * @param nomeArtistico Novo nome artístico.
     * @param genero Novo gênero musical.
     * @param nomeReal Novo nome real.
     * @return O artista atualizado, ou null se algum campo for inválido ou ocorrer erro.
     */
    public Artista atualizarArtista(int id, String nomeArtistico, String genero, String nomeReal) {
        if (id <= 0 ||
            nomeArtistico == null || nomeArtistico.trim().isEmpty() ||
            genero == null || genero.trim().isEmpty() ||
            nomeReal == null || nomeReal.trim().isEmpty()) {
            LOGGER.warning("Campos obrigatórios para atualização não preenchidos ou ID inválido.");
            return null;
        }
        Artista artista = new Artista(nomeArtistico, genero, id, nomeReal);
        return artistaDAO.update(artista);
    }

   /**
     * Remove um artista do sistema.
     *
     * @param id ID do artista a ser removido.
     * @return true se o artista foi removido, false se o ID for inválido ou ocorrer erro.
     */
    public boolean removerArtista(int id) {
        if (id <= 0) {
            LOGGER.warning("ID inválido para remoção.");
            return false;
        }
        return artistaDAO.delete(id);
    }

   /**
     * Busca um artista pelo ID.
     *
     * @param id ID do artista.
     * @return O artista correspondente, ou null se o ID for inválido ou não encontrado.
     */
    public Artista buscarPorId(int id) {
        if (id <= 0) {
            LOGGER.warning("ID inválido para busca.");
            return null;
        }
        return artistaDAO.findById(id);
    }

    /**
     * Lista todos os artistas cadastrados.
     *
     * @return Lista de todos os artistas.
     */
    public List<Artista> listarTodos() {
        return artistaDAO.findAll();
    }

    /**
     * Busca artistas pelo gênero musical.
     *
     * @param genero Gênero a ser buscado.
     * @return Lista de artistas do gênero informado, ou lista vazia se o gênero não for informado.
     */
    public List<Artista> buscarPorGenero(String genero) {
        if (genero == null || genero.trim().isEmpty()) {
            LOGGER.warning("Gênero não informado para busca.");
            return List.of();
        }
        return artistaDAO.findByGenero(genero);
    }

  /**
     * Busca um artista pelo nome artístico.
     *
     * @param nomeArtistico Nome artístico a ser buscado.
     * @return O artista encontrado, ou null se não encontrado ou se o parâmetro for inválido.
     */
    public Artista buscarPorNomeArtistico(String nomeArtistico) {
        if (nomeArtistico == null || nomeArtistico.trim().isEmpty()) {
            LOGGER.warning("Nome artístico não informado para busca.");
            return null;
        }
        return artistaDAO.findByNomeArtistico(nomeArtistico);
    }
}
