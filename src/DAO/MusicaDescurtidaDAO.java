/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import connection.ConnectionBD;
import model.MusicaDescurtida;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Masashi
 */
public class MusicaDescurtidaDAO {
    protected Connection conn;
    protected static final Logger LOGGER = Logger.getLogger(MusicaDescurtidaDAO.class.getName());

    public MusicaDescurtidaDAO() {
        try {
            this.conn = ConnectionBD.getInstance().getConnection();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao conectar ao banco de dados.", e);
            this.conn = null;
        }
    }

    public MusicaDescurtida save(MusicaDescurtida descurtida) {
        if (this.conn == null) {
            LOGGER.severe("Conexão indisponível.");
            return null;
        }
        if (isDescurtida(descurtida.getUsuarioId(), descurtida.getMusicaId())) {
            return null;
        }
        String sql = "INSERT INTO musica_descurtida (usuario_id, musica_id) VALUES (?, ?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, descurtida.getUsuarioId());
            ps.setInt(2, descurtida.getMusicaId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                descurtida.setId(rs.getInt(1));
            }
            return descurtida;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao descurtir música.", ex);
            return null;
        }
    }

    public boolean delete(int usuarioId, int musicaId) {
        if (this.conn == null) {
            LOGGER.severe("Conexão indisponível.");
            return false;
        }
        String sql = "DELETE FROM musica_descurtida WHERE usuario_id = ? AND musica_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, musicaId);
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao remover descurtida.", ex);
            return false;
        }
    }

    public List<MusicaDescurtida> findByUsuarioId(int usuarioId) {
        if (this.conn == null) {
            LOGGER.severe("Conexão indisponível.");
            return new ArrayList<>();
        }
        String sql = "SELECT id, usuario_id, musica_id FROM musica_descurtida WHERE usuario_id = ?";
        List<MusicaDescurtida> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MusicaDescurtida descurtida = new MusicaDescurtida(
                        rs.getInt("id"),
                        rs.getInt("usuario_id"),
                        rs.getInt("musica_id")
                );
                lista.add(descurtida);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar músicas descurtidas.", ex);
        }
        return lista;
    }

    public boolean isDescurtida(int usuarioId, int musicaId) {
        if (this.conn == null) {
            LOGGER.severe("Conexão indisponível.");
            return false;
        }
        String sql = "SELECT 1 FROM musica_descurtida WHERE usuario_id = ? AND musica_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, musicaId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao checar descurtida.", ex);
            return false;
        }
    }
}

