package controller;

import DAO.MusicaDescurtidaDAO;
import java.util.List;
import model.MusicaDescurtida;

/**
 * Controller responsável pelas operações de descurtir músicas.
 * Faz a ponte entre a camada de apresentação e a camada DAO para a entidade MusicaDescurtida.
 */
public class MusicaDescurtidaController {
    protected MusicaDescurtidaDAO musicaDescurtidaDAO;

    /**
     * Construtor padrão que inicializa o MusicaDescurtidaDAO.
     */
    public MusicaDescurtidaController() {
        this.musicaDescurtidaDAO = new MusicaDescurtidaDAO();
    }

    /**
     * Marca uma música como descurtida por um usuário, se ainda não tiver sido descurtida.
     *
     * @param usuarioId ID do usuário.
     * @param musicaId ID da música a ser descurtida.
     * @return Objeto MusicaDescurtida salvo, ou null se já existir ou se parâmetros forem inválidos.
     */
    public MusicaDescurtida descurtirMusica(int usuarioId, int musicaId) {
        if (usuarioId <= 0 || musicaId <= 0) {
            return null;
        }
        if (musicaDescurtidaDAO.isDescurtida(usuarioId, musicaId)) {
            return null;
        }
        MusicaDescurtida descurtida = new MusicaDescurtida(usuarioId, musicaId);
        return musicaDescurtidaDAO.save(descurtida);
    }

    /**
     * Remove a marcação de descurtida de uma música para o usuário informado.
     *
     * @param usuarioId ID do usuário.
     * @param musicaId ID da música.
     * @return true se a operação foi bem-sucedida, false caso contrário.
     */
    public boolean removerDescurtida(int usuarioId, int musicaId) {
        if (usuarioId <= 0 || musicaId <= 0) {
            return false;
        }
        return musicaDescurtidaDAO.delete(usuarioId, musicaId);
    }

    /**
     * Lista todas as músicas descurtidas por um usuário.
     *
     * @param usuarioId ID do usuário.
     * @return Lista de MusicaDescurtida associadas ao usuário.
     */
    public List<MusicaDescurtida> listarMusicasDescurtidas(int usuarioId) {
        if (usuarioId <= 0) {
            return List.of();
        }
        return musicaDescurtidaDAO.findByUsuarioId(usuarioId);
    }

    /**
     * Verifica se uma música está descurtida por um determinado usuário.
     *
     * @param usuarioId ID do usuário.
     * @param musicaId ID da música.
     * @return true se a música já foi descurtida pelo usuário, false caso contrário.
     */
    public boolean descurtida(int usuarioId, int musicaId) {
        if (usuarioId <= 0 || musicaId <= 0) {
            return false;
        }
        return musicaDescurtidaDAO.isDescurtida(usuarioId, musicaId);
    }
}
