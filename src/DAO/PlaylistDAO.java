package DAO;

import connection.ConnectionBD;
import model.Playlist;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlaylistDAO {
    protected Connection conn;
    protected static final Logger LOGGER = Logger.getLogger(PlaylistDAO.class.getName());

    public PlaylistDAO() {
        try {
            this.conn = ConnectionBD.getInstance().getConnection();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Falha ao conectar ao banco de dados no construtor do PlaylistDAO.", e);
            this.conn = null;
        }
    }

    public Playlist save(Playlist p) {
        if (this.conn == null) {
            LOGGER.severe("Operação save (Playlist) não pode ser executada: conexão com banco de dados indisponível.");
            return null;
        }

      
        String sql = "INSERT INTO playlist (usuario_id, nome) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getUsuarioId());
            ps.setString(2, p.getNome());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                LOGGER.severe("Falha ao inserir Playlist, nenhuma linha afetada.");
                return null;
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setId(rs.getInt(1));
                } else {
                    LOGGER.warning("Falha ao obter ID gerado para Playlist.");
                }
            }
            return p;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao salvar Playlist: " + p.getNome(), ex);
            return null;
        }
    }

    public Playlist update(Playlist p) {
        if (this.conn == null) {
            LOGGER.severe("Operação update (Playlist) não pode ser executada: conexão indisponível.");
            return null;
        }

        String sql = "UPDATE playlist SET usuario_id = ?, nome = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p.getUsuarioId());
            ps.setString(2, p.getNome());
            ps.setInt(3, p.getId());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                return p;
            } else {
                LOGGER.warning("Nenhuma playlist atualizada. Verificar se o ID " + p.getId() + " existe.");
                return null;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar Playlist com ID: " + p.getId(), ex);
            return null;
        }
    }

    public boolean delete(int id) {
        if (this.conn == null) {
            LOGGER.severe("Operação delete (Playlist) não pode ser executada: conexão indisponível.");
            return false;
        }

        String sql = "DELETE FROM playlist WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao deletar Playlist com ID: " + id, ex);
            return false;
        }
    }

    public Playlist findById(int id) {
        if (this.conn == null) {
            LOGGER.severe("Operação findById (Playlist) não pode ser executada: conexão indisponível.");
            return null;
        }

        String sql = "SELECT id, usuario_id, nome FROM playlist WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return converteParaPlaylist(rs);
                }
                return null; 
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar Playlist por ID: " + id, ex);
            return null;
        }
    }

    public List<Playlist> findAll() {
        if (this.conn == null) {
            LOGGER.severe("Operação findAll (Playlist) não pode ser executada: conexão indisponível.");
            return Collections.emptyList();
        }

        String sql = "SELECT id, usuario_id, nome FROM playlist";
        List<Playlist> lista = new ArrayList<>();

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Playlist playlist = converteParaPlaylist(rs);
                if (playlist != null) {
                    lista.add(playlist);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao listar todas as Playlists.", ex);
            return Collections.emptyList();
        }
        return lista;
    }

    public List<Playlist> findByUsuarioId(int usuarioId) {
        if (this.conn == null) {
            LOGGER.severe("Operação findByUsuarioId (Playlist) não pode ser executada: conexão indisponível.");
            return Collections.emptyList();
        }

        String sql = "SELECT id, usuario_id, nome FROM playlist WHERE usuario_id = ?";
        List<Playlist> lista = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Playlist playlist = converteParaPlaylist(rs);
                    if (playlist != null) {
                        lista.add(playlist);
                    }
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar Playlists por usuarioId: " + usuarioId, ex);
            return Collections.emptyList();
        }
        return lista;
    }

    private Playlist converteParaPlaylist(ResultSet rs) {
        try {
            int id = rs.getInt("id");
            int usuarioId = rs.getInt("usuario_id");
            String nome = rs.getString("nome");
            return new Playlist(id, usuarioId, nome);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao converter ResultSet para Playlist.", ex);
            return null;
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
         
            LOGGER.log(Level.SEVERE, "Erro ao adicionar musica " + musicaId + " à playlist " + playlistId, ex);
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
            LOGGER.log(Level.SEVERE, "Erro ao remover musica " + musicaId + " da playlist " + playlistId, ex);
            return false;
        }
    }

    public List<Integer> findMusicasByPlaylistId(int playlistId) {
        if (this.conn == null) {
            LOGGER.severe("Operação findMusicasByPlaylistId não pode ser executada: conexão indisponível.");
            return Collections.emptyList();
        }
        String sql = "SELECT musica_id FROM playlist_musica WHERE playlist_id = ?";
        List<Integer> musicaIds = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, playlistId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    musicaIds.add(rs.getInt("musica_id"));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar musicas da playlist " + playlistId, ex);
        }
        return musicaIds;
    }

    public void closeConnection() {
        if (this.conn != null) {
            try {
                this.conn.close();
                LOGGER.info("Conexão do PlaylistDAO fechada.");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Erro ao fechar a conexão do PlaylistDAO.", e);
            }
        }
    }
}