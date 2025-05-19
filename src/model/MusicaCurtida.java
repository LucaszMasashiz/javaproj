/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Masashi
 */
public class MusicaCurtida {
    protected int id;
    protected int usuarioId;
    protected int musicaId;

    public MusicaCurtida(int id, int usuarioId, int musicaId) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.musicaId = musicaId;
    }
    
    public MusicaCurtida(int usuarioId, int musicaId) {
        this.usuarioId = usuarioId;
        this.musicaId = musicaId;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getMusicaId() {
        return musicaId;
    }

    public void setMusicaId(int musicaId) {
        this.musicaId = musicaId;
    }
    
    
}



    
