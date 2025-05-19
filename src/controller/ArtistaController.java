/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import DAO.ArtistaDAO;
import model.Artista;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Masashi
 */
public class ArtistaController {
    private ArtistaDAO artistaDAO;
    private static final Logger LOGGER = Logger.getLogger(ArtistaController.class.getName());

    public ArtistaController() {
        this.artistaDAO = new ArtistaDAO();
    }

    
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

   
    public boolean removerArtista(int id) {
        if (id <= 0) {
            LOGGER.warning("ID inválido para remoção.");
            return false;
        }
        return artistaDAO.delete(id);
    }

   
    public Artista buscarPorId(int id) {
        if (id <= 0) {
            LOGGER.warning("ID inválido para busca.");
            return null;
        }
        return artistaDAO.findById(id);
    }

    
    public List<Artista> listarTodos() {
        return artistaDAO.findAll();
    }

    
    public List<Artista> buscarPorGenero(String genero) {
        if (genero == null || genero.trim().isEmpty()) {
            LOGGER.warning("Gênero não informado para busca.");
            return List.of();
        }
        return artistaDAO.findByGenero(genero);
    }

  
    public Artista buscarPorNomeArtistico(String nomeArtistico) {
        if (nomeArtistico == null || nomeArtistico.trim().isEmpty()) {
            LOGGER.warning("Nome artístico não informado para busca.");
            return null;
        }
        return artistaDAO.findByNomeArtistico(nomeArtistico);
    }
}
