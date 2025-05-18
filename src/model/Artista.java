/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Masashi
 */
public class Artista extends Pessoa{
    protected String nomeArtistico;
    protected String genero;

    public Artista(String nomeArtistico, String genero, int id, String nome) {
        super(id, nome);
        this.nomeArtistico = nomeArtistico;
        this.genero = genero;
    }
   

    public String getNomeArtistico() {
        return nomeArtistico;
    }

    public void setNomeArtistico(String nomeArtistico) {
        this.nomeArtistico = nomeArtistico;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
    
    
}
