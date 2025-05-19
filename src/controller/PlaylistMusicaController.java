
package controller;

import DAO.PlaylistMusicaDAO;
import model.PlaylistMusica;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlaylistMusicaController {
    private static final Logger LOGGER = Logger.getLogger(PlaylistMusicaController.class.getName());
    private PlaylistMusicaDAO playlistMusicaDAO;

    public PlaylistMusicaController() {
        this.playlistMusicaDAO = new PlaylistMusicaDAO();
    }

    // Adiciona música à playlist
    public boolean adicionarMusica(int playlistId, int musicaId) {
        if (playlistId <= 0 || musicaId <= 0) {
            LOGGER.warning("IDs inválidos para adicionar música à playlist.");
            return false;
        }
        return playlistMusicaDAO.addMusicaToPlaylist(playlistId, musicaId);
    }

    // Remove música da playlist
    public boolean removerMusica(int playlistId, int musicaId) {
        if (playlistId <= 0 || musicaId <= 0) {
            LOGGER.warning("IDs inválidos para remover música da playlist.");
            return false;
        }
        return playlistMusicaDAO.removeMusicaFromPlaylist(playlistId, musicaId);
    }

    // Lista todas as músicas de uma playlist (IDs das músicas)
    public List<Integer> listarMusicasPorPlaylist(int playlistId) {
        if (playlistId <= 0) {
            LOGGER.warning("ID de playlist inválido para buscar músicas.");
            return java.util.Collections.emptyList();
        }
        return playlistMusicaDAO.findMusicasByPlaylistId(playlistId);
    }
    // Busca todas as músicas de uma playlist pelo nome da playlist
    public List<Integer> listarMusicasPorNomePlaylist(String nomePlaylist) {
        if (nomePlaylist == null || nomePlaylist.trim().isEmpty()) {
            LOGGER.warning("Nome da playlist inválido para buscar músicas.");
            return java.util.Collections.emptyList();
        }
        return playlistMusicaDAO.findMusicasByPlaylistNome(nomePlaylist);
    }
    // (Opcional) Lista todas as relações playlist-musica
    public List<PlaylistMusica> listarTodasRelacoes() {
        return playlistMusicaDAO.findAll();
    }
}