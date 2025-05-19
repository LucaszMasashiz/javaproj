/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import DAO.MusicaDescurtidaDAO;
import java.util.List;
import model.MusicaDescurtida;

/**
 *
 * @author Masashi
 */
public class MusicaDescurtidaController {
    protected MusicaDescurtidaDAO musicaDescurtidaDAO;

    public MusicaDescurtidaController() {
        this.musicaDescurtidaDAO = new MusicaDescurtidaDAO();
    }

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

    public boolean removerDescurtida(int usuarioId, int musicaId) {
        if (usuarioId <= 0 || musicaId <= 0) {
            return false;
        }
        return musicaDescurtidaDAO.delete(usuarioId, musicaId);
    }

    public List<MusicaDescurtida> listarMusicasDescurtidas(int usuarioId) {
        if (usuarioId <= 0) {
            return List.of();
        }
        return musicaDescurtidaDAO.findByUsuarioId(usuarioId);
    }

    public boolean descurtida(int usuarioId, int musicaId) {
        if (usuarioId <= 0 || musicaId <= 0) {
            return false;
        }
        return musicaDescurtidaDAO.isDescurtida(usuarioId, musicaId);
    }
}
