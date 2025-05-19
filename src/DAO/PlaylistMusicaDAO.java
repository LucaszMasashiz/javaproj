package DAO;

import connection.ConnectionBD;
import model.PlaylistMusica;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlaylistMusicaDAO {
    protected Connection conn;
    protected static final Logger LOGGER = Logger.getLogger(PlaylistMusicaDAO.class.getName());

    public PlaylistMusicaDAO() {
        try {
            this.conn = ConnectionBD.getInstance().getConnection();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Falha ao conectar ao banco de dados no construtor do PlaylistMusicaDAO.", e);
            this.conn = null;
        }
    }


    public boolean addMusicaToPlaylist(int playlistId, int musicaId) {
        if (this.conn == null) {
            LOGGER.severe("Operação addMusicaToPlaylist não pode ser executada: conexão indisponível.");
            return false;
        }
        String sql = "INSERT INTO playlist_musica (playlist_id, musica_id) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, playlistId);
            ps.setInt(2, musicaId);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao adicionar música à playlist.", ex);
            return false;
        }
    }

    
    public boolean removeMusicaFromPlaylist(int playlistId, int musicaId) {
        if (this.conn == null) {
            LOGGER.severe("Operação removeMusicaFromPlaylist não pode ser executada: conexão indisponível.");
            return false;
        }
        String sql = "DELETE FROM playlist_musica WHERE playlist_id = ? AND musica_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, playlistId);
            ps.setInt(2, musicaId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao remover música da playlist.", ex);
            return false;
        }
    }

    
    public List<Integer> findMusicasByPlaylistId(int playlistId) {
        if (this.conn == null) {
            LOGGER.severe("Operação findMusicasByPlaylistId não pode ser executada: conexão indisponível.");
            return new ArrayList<>();
        }
        String sql = "SELECT musica_id FROM playlist_musica WHERE playlist_id = ?";
        List<Integer> musicas = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, playlistId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    musicas.add(rs.getInt("musica_id"));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar músicas da playlist.", ex);
        }
        return musicas;
    }

    public List<Integer> findMusicasByPlaylistNome(String nomePlaylist) {
    if (this.conn == null) {
        LOGGER.severe("Operação findMusicasByPlaylistNome não pode ser executada: conexão indisponível.");
        return new ArrayList<>();
    }
    String sql = "SELECT pm.musica_id " +
                 "FROM playlist_musica pm " +
                 "JOIN playlist p ON pm.playlist_id = p.id " +
                 "WHERE LOWER(p.nome) = LOWER(?)";
    List<Integer> musicas = new ArrayList<>();
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, nomePlaylist);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                musicas.add(rs.getInt("musica_id"));
            }
        }
    } catch (SQLException ex) {
        LOGGER.log(Level.SEVERE, "Erro ao buscar músicas pela playlist de nome: " + nomePlaylist, ex);
    }
    return musicas;
}
    
    public List<PlaylistMusica> findAll() {
        if (this.conn == null) {
            LOGGER.severe("Operação findAll não pode ser executada: conexão indisponível.");
            return new ArrayList<>();
        }
        String sql = "SELECT playlist_id, musica_id FROM playlist_musica";
        List<PlaylistMusica> lista = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                PlaylistMusica pm = new PlaylistMusica(rs.getInt("playlist_id"), rs.getInt("musica_id"));
                lista.add(pm);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao listar playlist_musica.", ex);
        }
        return lista;
    }
}
