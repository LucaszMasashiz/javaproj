package controller;

import DAO.PlaylistDAO;
import model.Playlist;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller responsável por gerenciar operações relacionadas a playlists,
 * atuando como camada intermediária entre a interface do usuário e o DAO.
 */
public class PlaylistController {
    protected static final Logger LOGGER = Logger.getLogger(PlaylistController.class.getName());
    protected PlaylistDAO playlistDAO;

    /**
     * Construtor padrão que inicializa o PlaylistDAO.
     */
    public PlaylistController() {
        this.playlistDAO = new PlaylistDAO();
    }

    /**
     * Cria uma nova playlist para o usuário informado.
     *
     * @param usuarioId ID do usuário dono da playlist.
     * @param nome Nome da playlist.
     * @return Objeto Playlist criado ou null em caso de erro/validação.
     */
    public Playlist criarPlaylist(int usuarioId, String nome) {
        if (usuarioId <= 0 || nome == null || nome.trim().isEmpty()) {
            LOGGER.warning("Dados inválidos para criar playlist.");
            return null;
        }
        Playlist playlist = new Playlist(0, usuarioId, nome);
        return playlistDAO.save(playlist);
    }
    
    /**
     * Atualiza os dados de uma playlist existente.
     *
     * @param playlist Objeto Playlist com os dados atualizados.
     * @return Playlist atualizada ou null em caso de erro/validação.
     */
    public Playlist atualizarPlaylist(Playlist playlist) {
        if (playlist == null || playlist.getId() <= 0) {
            LOGGER.warning("Playlist inválida para atualização.");
            return null;
        }
        return playlistDAO.update(playlist);
    }

    /**
     * Remove uma playlist do banco de dados pelo seu ID.
     *
     * @param playlistId ID da playlist a ser removida.
     * @return true se a exclusão for bem-sucedida, false caso contrário.
     */
    public boolean deletarPlaylist(int playlistId) {
        if (playlistId <= 0) {
            LOGGER.warning("ID de playlist inválido para exclusão.");
            return false;
        }
        return playlistDAO.delete(playlistId);
    }

    /**
     * Busca uma playlist pelo seu ID.
     *
     * @param playlistId ID da playlist.
     * @return Objeto Playlist encontrado ou null se não existir.
     */
    public Playlist buscarPorId(int playlistId) {
        if (playlistId <= 0) {
            LOGGER.warning("ID de playlist inválido para busca.");
            return null;
        }
        return playlistDAO.findById(playlistId);
    }

    /**
     * Lista todas as playlists cadastradas no sistema.
     *
     * @return Lista de todas as playlists.
     */
    public List<Playlist> listarTodas() {
        return playlistDAO.findAll();
    }

    /**
     * Lista todas as playlists de um usuário específico.
     *
     * @param usuarioId ID do usuário.
     * @return Lista de playlists do usuário.
     */
    public List<Playlist> listarPorUsuario(int usuarioId) {
        if (usuarioId <= 0) {
            LOGGER.warning("ID de usuário inválido para buscar playlists.");
            return Collections.emptyList();
        }
        return playlistDAO.findByUsuarioId(usuarioId);
    }

    /**
     * Adiciona uma música a uma playlist.
     *
     * @param playlistId ID da playlist.
     * @param musicaId ID da música a ser adicionada.
     * @return true se a operação for bem-sucedida, false caso contrário.
     */
    public boolean adicionarMusica(int playlistId, int musicaId) {
        if (playlistId <= 0 || musicaId <= 0) {
            LOGGER.warning("IDs inválidos para adicionar música à playlist.");
            return false;
        }
        return playlistDAO.addMusicaToPlaylist(playlistId, musicaId);
    }

    /**
     * Remove uma música de uma playlist.
     *
     * @param playlistId ID da playlist.
     * @param musicaId ID da música a ser removida.
     * @return true se a operação for bem-sucedida, false caso contrário.
     */
    public boolean removerMusica(int playlistId, int musicaId) {
        if (playlistId <= 0 || musicaId <= 0) {
            LOGGER.warning("IDs inválidos para remover música da playlist.");
            return false;
        }
        return playlistDAO.removeMusicaFromPlaylist(playlistId, musicaId);
    }
    
    /**
     * Busca uma playlist pelo ID do usuário e nome da playlist.
     *
     * @param usuarioId ID do usuário dono da playlist.
     * @param nome Nome da playlist.
     * @return Objeto Playlist encontrado ou null se não existir.
     */
    public Playlist buscarPorUsuarioIdENome(int usuarioId, String nome) {
        if (usuarioId <= 0 || nome == null || nome.trim().isEmpty()) {
            LOGGER.warning("Dados inválidos para buscar playlist por nome.");
            return null;
        }
        return playlistDAO.findByUsuarioIdAndNome(usuarioId, nome);
    }
    
    /**
     * Lista os IDs das músicas de uma playlist específica.
     *
     * @param playlistId ID da playlist.
     * @return Lista de IDs das músicas.
     */
    public List<Integer> listarMusicas(int playlistId) {
        if (playlistId <= 0) {
            LOGGER.warning("ID de playlist inválido para buscar músicas.");
            return Collections.emptyList();
        }
        return playlistDAO.findMusicasByPlaylistId(playlistId);
    }
}
