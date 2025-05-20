package controller;

import DAO.MusicaCurtidaDAO;
import java.util.List;
import model.MusicaCurtida;

/**
 * Controller responsável pelas operações de curtidas de músicas.
 * Atua como ponte entre a camada de apresentação e o DAO de músicas curtidas.
 *
 * @author Masashi
 */

public class MusicaCurtidaController {
    protected MusicaCurtidaDAO musicaCurtidaDAO;

    /**
     * Construtor padrão que inicializa o MusicaCurtidaDAO.
     */
    public MusicaCurtidaController() {
        this.musicaCurtidaDAO = new MusicaCurtidaDAO();
    }

    /**
     * Permite ao usuário curtir uma música, se ainda não tiver curtido.
     *
     * @param usuarioId ID do usuário que está curtindo a música.
     * @param musicaId  ID da música a ser curtida.
     * @return Objeto MusicaCurtida salvo, ou null se inválido ou já curtido.
     */
    public MusicaCurtida curtirMusica(int usuarioId, int musicaId) {
        if (usuarioId <= 0 || musicaId <= 0) {
            return null;
        }
        if (musicaCurtidaDAO.isCurtida(usuarioId, musicaId)) {
            return null; 
        }
        MusicaCurtida curtida = new MusicaCurtida(usuarioId, musicaId);
        return musicaCurtidaDAO.save(curtida);
    }

    /**
     * Permite ao usuário remover a curtida de uma música.
     *
     * @param usuarioId ID do usuário.
     * @param musicaId  ID da música a ser descurtida.
     * @return true se a operação foi bem-sucedida, false caso contrário.
     */
    public boolean descurtirMusica(int usuarioId, int musicaId) {
        if (usuarioId <= 0 || musicaId <= 0) {
            return false;
        }
        return musicaCurtidaDAO.delete(usuarioId, musicaId);
    }

    /**
     * Lista todas as músicas curtidas pelo usuário.
     *
     * @param usuarioId ID do usuário.
     * @return Lista de MusicaCurtida associadas ao usuário.
     */
    public List<MusicaCurtida> listarMusicasCurtidas(int usuarioId) {
        if (usuarioId <= 0) {
            return List.of();
        }
        return musicaCurtidaDAO.findByUsuarioId(usuarioId);
    }

    /**
     * Verifica se uma música está curtida por um determinado usuário.
     *
     * @param usuarioId ID do usuário.
     * @param musicaId  ID da música.
     * @return true se a música já foi curtida pelo usuário, false caso contrário.
     */
    public boolean curtido(int usuarioId, int musicaId) {
        if (usuarioId <= 0 || musicaId <= 0) {
            return false;
        }
        return musicaCurtidaDAO.isCurtida(usuarioId, musicaId);
    }
}