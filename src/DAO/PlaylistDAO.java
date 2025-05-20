package DAO;

import connection.ConnectionBD;
import model.Playlist;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object (DAO) para a entidade Playlist.
 * Responsável por realizar operações de CRUD e manipulação de playlists no banco de dados.
 * 
 * @author Masashi
 */
public class PlaylistDAO {
    protected Connection conn;
    protected static final Logger LOGGER = Logger.getLogger(PlaylistDAO.class.getName());

    /**
     * Construtor. Inicializa a conexão com o banco de dados.
     */
    public PlaylistDAO() {
        try {
            this.conn = ConnectionBD.getInstance().getConnection();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Falha ao conectar ao banco de dados no construtor do PlaylistDAO.", e);
            this.conn = null;
        }
    }

    /**
     * Salva uma nova playlist no banco de dados.
     * 
     * @param p Objeto Playlist a ser salvo.
     * @return A playlist salva com o ID gerado, ou null em caso de erro.
     */
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

     /**
     * Atualiza uma playlist existente no banco de dados.
     * 
     * @param p Objeto Playlist com dados atualizados.
     * @return A playlist atualizada, ou null em caso de erro.
     */
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

    /**
     * Remove uma playlist do banco de dados pelo ID.
     * 
     * @param id ID da playlist a ser removida.
     * @return true se removido com sucesso, false caso contrário.
     */
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

    /**
     * Busca uma playlist pelo seu ID.
     * 
     * @param id ID da playlist.
     * @return Playlist encontrada ou null se não existir.
     */
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

    /**
     * Busca todas as playlists no banco de dados.
     * 
     * @return Lista de playlists encontradas.
     */
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

    /**
     * Busca todas as playlists de um determinado usuário.
     * 
     * @param usuarioId ID do usuário.
     * @return Lista de playlists do usuário.
     */
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

    /**
     * Converte um ResultSet para um objeto Playlist.
     * 
     * @param rs ResultSet da consulta.
     * @return Objeto Playlist.
     */
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

     /**
     * Adiciona uma música a uma playlist.
     * 
     * @param playlistId ID da playlist.
     * @param musicaId ID da música.
     * @return true se adicionado com sucesso, false caso contrário.
     */
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

    /**
     * Remove uma música de uma playlist.
     * 
     * @param playlistId ID da playlist.
     * @param musicaId ID da música.
     * @return true se removido com sucesso, false caso contrário.
     */
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

    /**
     * Busca os IDs das músicas associadas a uma playlist.
     * 
     * @param playlistId ID da playlist.
     * @return Lista de IDs das músicas.
     */
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
    
    /**
     * Busca uma playlist de um usuário pelo nome da playlist, ignorando diferenças de maiúsculas/minúsculas.
     * 
     * @param usuarioId ID do usuário.
     * @param nome Nome da playlist (case-insensitive).
     * @return Playlist encontrada ou null se não existir.
     */
    public Playlist findByUsuarioIdAndNome(int usuarioId, String nome) {
        if (this.conn == null) {
            LOGGER.severe("Operação findByUsuarioIdAndNome não pode ser executada: conexão indisponível.");
            return null;
        }
        String sql = "SELECT id, usuario_id, nome FROM playlist WHERE usuario_id = ? AND nome ILIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setString(2, nome);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return converteParaPlaylist(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar Playlist por usuário e nome.", ex);
        }
        return null;
    }
    
     /**
     * Fecha a conexão com o banco de dados.
     */
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