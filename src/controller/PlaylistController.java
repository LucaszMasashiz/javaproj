/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import DAO.PlaylistDAO;
import model.Playlist;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Masashi
 */
public class PlaylistController {
    protected static final Logger LOGGER = Logger.getLogger(PlaylistController.class.getName());
    protected PlaylistDAO playlistDAO;

    public PlaylistController() {
        this.playlistDAO = new PlaylistDAO();
    }

    public Playlist criarPlaylist(int usuarioId, String nome) {
        if (usuarioId <= 0 || nome == null || nome.trim().isEmpty()) {
            LOGGER.warning("Dados inválidos para criar playlist.");
            return null;
        }
        Playlist playlist = new Playlist(0, usuarioId, nome);
        return playlistDAO.save(playlist);
    }

    public Playlist atualizarPlaylist(Playlist playlist) {
        if (playlist == null || playlist.getId() <= 0) {
            LOGGER.warning("Playlist inválida para atualização.");
            return null;
        }
        return playlistDAO.update(playlist);
    }

    public boolean deletarPlaylist(int playlistId) {
        if (playlistId <= 0) {
            LOGGER.warning("ID de playlist inválido para exclusão.");
            return false;
        }
        return playlistDAO.delete(playlistId);
    }

    public Playlist buscarPorId(int playlistId) {
        if (playlistId <= 0) {
            LOGGER.warning("ID de playlist inválido para busca.");
            return null;
        }
        return playlistDAO.findById(playlistId);
    }

    public List<Playlist> listarTodas() {
        return playlistDAO.findAll();
    }

    public List<Playlist> listarPorUsuario(int usuarioId) {
        if (usuarioId <= 0) {
            LOGGER.warning("ID de usuário inválido para buscar playlists.");
            return Collections.emptyList();
        }
        return playlistDAO.findByUsuarioId(usuarioId);
    }

    public boolean adicionarMusica(int playlistId, int musicaId) {
        if (playlistId <= 0 || musicaId <= 0) {
            LOGGER.warning("IDs inválidos para adicionar música à playlist.");
            return false;
        }
        return playlistDAO.addMusicaToPlaylist(playlistId, musicaId);
    }

    public boolean removerMusica(int playlistId, int musicaId) {
        if (playlistId <= 0 || musicaId <= 0) {
            LOGGER.warning("IDs inválidos para remover música da playlist.");
            return false;
        }
        return playlistDAO.removeMusicaFromPlaylist(playlistId, musicaId);
    }

    public List<Integer> listarMusicas(int playlistId) {
        if (playlistId <= 0) {
            LOGGER.warning("ID de playlist inválido para buscar músicas.");
            return Collections.emptyList();
        }
        return playlistDAO.findMusicasByPlaylistId(playlistId);
    }
}
