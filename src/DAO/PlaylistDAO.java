
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
 * DAO para a entidade Playlist.
 *
 * @author Seu Nome (ou Masashi)
 */
public class PlaylistDAO {
    private Connection conn;
    private static final Logger LOGGER = Logger.getLogger(PlaylistDAO.class.getName());

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
     * @param p O objeto Playlist a ser salvo.
     * @return O objeto Playlist com o ID populado (se auto-gerado) em caso de sucesso, ou null em caso de falha.
     */
    public Playlist save(Playlist p) {
        if (this.conn == null) {
            LOGGER.severe("Operação save (Playlist) não pode ser executada: conexão com banco de dados indisponível.");
            return null;
        }

        // Assumindo que o ID da playlist é auto-incrementável
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
     * Atualiza os dados de uma playlist existente no banco de dados.
     *
     * @param p O objeto Playlist com os dados atualizados.
     * @return O objeto Playlist atualizado em caso de sucesso, ou null em caso de falha.
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
     * Remove uma playlist do banco de dados.
     * NOTA: Isso NÃO remove as músicas associadas através de uma tabela de junção,
     * a menos que a FK na tabela de junção tenha ON DELETE CASCADE.
     *
     * @param id O ID da playlist a ser removida.
     * @return true em caso de sucesso, false em caso de falha ou se a playlist não foi encontrada.
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
     * @param id O ID da playlist.
     * @return O objeto Playlist se encontrado, ou null caso contrário ou em caso de erro.
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
                return null; // Não encontrada
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar Playlist por ID: " + id, ex);
            return null;
        }
    }

    /**
     * Lista todas as playlists cadastradas.
     *
     * @return Uma lista de Playlists, ou uma lista vazia em caso de erro ou se não houver playlists.
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
     * Lista todas as playlists de um usuário específico.
     *
     * @param usuarioId O ID do usuário.
     * @return Uma lista de Playlists do usuário, ou uma lista vazia.
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
     * Converte uma linha do ResultSet para um objeto Playlist.
     *
     * @param rs O ResultSet posicionado na linha a ser convertida.
     * @return Um objeto Playlist, ou null em caso de erro na conversão.
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

    // --- Métodos para gerenciar músicas em playlists (Exemplo - Requer tabela de junção) ---
    // Para implementar estes, você precisaria de uma tabela como:
    // CREATE TABLE playlist_musica (
    //     playlist_id INT NOT NULL,
    //     musica_id INT NOT NULL,
    //     PRIMARY KEY (playlist_id, musica_id),
    //     FOREIGN KEY (playlist_id) REFERENCES playlist(id) ON DELETE CASCADE,
    //     FOREIGN KEY (musica_id) REFERENCES musica(id) ON DELETE CASCADE
    // );

    /**
     * Adiciona uma música a uma playlist. (Exemplo, requer tabela de junção)
     * @param playlistId O ID da playlist.
     * @param musicaId O ID da música.
     * @return true se a música foi adicionada com sucesso, false caso contrário.
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
            // Pode dar erro de PK duplicada se já existir, ou FK se IDs não existirem.
            LOGGER.log(Level.SEVERE, "Erro ao adicionar musica " + musicaId + " à playlist " + playlistId, ex);
            return false;
        }
    }

    /**
     * Remove uma música de uma playlist. (Exemplo, requer tabela de junção)
     * @param playlistId O ID da playlist.
     * @param musicaId O ID da música.
     * @return true se a música foi removida com sucesso, false caso contrário.
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
     * Lista os IDs de todas as músicas de uma playlist específica. (Exemplo, requer tabela de junção)
     * Para obter os objetos Musica completos, seria necessário um JOIN com a tabela musica ou um MusicaDAO.
     * @param playlistId O ID da playlist.
     * @return Uma lista de IDs de músicas.
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