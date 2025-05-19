/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import DAO.MusicaCurtidaDAO;
import java.util.List;
import model.MusicaCurtida;

/**
 *
 * @author Masashi
 */
public class MusicaCurtidaController {
    protected MusicaCurtidaDAO musicaCurtidaDAO;

    public MusicaCurtidaController() {
        this.musicaCurtidaDAO = new MusicaCurtidaDAO();
    }

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

    public boolean descurtirMusica(int usuarioId, int musicaId) {
        if (usuarioId <= 0 || musicaId <= 0) {
            return false;
        }
        return musicaCurtidaDAO.delete(usuarioId, musicaId);
    }

    public List<MusicaCurtida> listarMusicasCurtidas(int usuarioId) {
        if (usuarioId <= 0) {
            return List.of();
        }
        return musicaCurtidaDAO.findByUsuarioId(usuarioId);
    }

    public boolean curtido(int usuarioId, int musicaId) {
        if (usuarioId <= 0 || musicaId <= 0) {
            return false;
        }
        return musicaCurtidaDAO.isCurtida(usuarioId, musicaId);
    }
}