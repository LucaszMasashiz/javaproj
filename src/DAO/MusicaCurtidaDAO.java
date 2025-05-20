package DAO;

import connection.ConnectionBD;
import model.MusicaCurtida;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe DAO responsável pelas operações de persistência relacionadas à entidade MusicaCurtida.
 * Permite salvar, remover, buscar e verificar curtidas de músicas por usuários.
 * 
 * @author Masashi
 */
public class MusicaCurtidaDAO {
    protected Connection conn;
    protected static final Logger LOGGER = Logger.getLogger(MusicaCurtidaDAO.class.getName());

    /**
     * Construtor padrão. Inicializa a conexão com o banco de dados.
     */
    public MusicaCurtidaDAO() {
        try {
            this.conn = ConnectionBD.getInstance().getConnection();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao conectar ao banco de dados.", e);
            this.conn = null;
        }
    }

     /**
     * Salva uma nova curtida de música para um usuário no banco de dados.
     *
     * @param curtida Objeto MusicaCurtida a ser salvo.
     * @return O objeto MusicaCurtida salvo (com ID atualizado), ou null em caso de erro.
     */
    public MusicaCurtida save(MusicaCurtida curtida) {
        if (this.conn == null) {
            LOGGER.severe("Conexão indisponível.");
            return null;
        }
        String sql = "INSERT INTO musica_curtida (usuario_id, musica_id) VALUES (?, ?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, curtida.getUsuarioId());
            ps.setInt(2, curtida.getMusicaId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                curtida.setId(rs.getInt(1));
            }
            return curtida;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao curtir música.", ex);
            return null;
        }
    }

 /**
     * Remove uma curtida de música de um usuário no banco de dados.
     *
     * @param usuarioId ID do usuário.
     * @param musicaId ID da música.
     * @return true se a curtida foi removida com sucesso, false caso contrário.
     */
    public boolean delete(int usuarioId, int musicaId) {
        if (this.conn == null) {
            LOGGER.severe("Conexão indisponível.");
            return false;
        }
        String sql = "DELETE FROM musica_curtida WHERE usuario_id = ? AND musica_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, musicaId);
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao descurtir música.", ex);
            return false;
        }
    }

/**
     * Busca todas as músicas curtidas por um determinado usuário.
     *
     * @param usuarioId ID do usuário.
     * @return Lista de objetos MusicaCurtida associados ao usuário.
     */
    public List<MusicaCurtida> findByUsuarioId(int usuarioId) {
        if (this.conn == null) {
            LOGGER.severe("Conexão indisponível.");
            return new ArrayList<>();
        }
        String sql = "SELECT id, usuario_id, musica_id FROM musica_curtida WHERE usuario_id = ?";
        List<MusicaCurtida> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MusicaCurtida curtida = new MusicaCurtida(
                    rs.getInt("id"),
                    rs.getInt("usuario_id"),
                    rs.getInt("musica_id")
                );
                lista.add(curtida);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar músicas curtidas por usuário.", ex);
        }
        return lista;
    }

 /**
     * Verifica se um usuário já curtiu uma determinada música.
     *
     * @param usuarioId ID do usuário.
     * @param musicaId ID da música.
     * @return true se o usuário já curtiu a música, false caso contrário.
     */
    public boolean isCurtida(int usuarioId, int musicaId) {
        if (this.conn == null) {
            LOGGER.severe("Conexão indisponível.");
            return false;
        }
        String sql = "SELECT 1 FROM musica_curtida WHERE usuario_id = ? AND musica_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, musicaId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao checar curtida.", ex);
            return false;
        }
    }
}