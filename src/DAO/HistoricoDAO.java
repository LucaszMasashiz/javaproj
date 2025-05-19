package DAO;
import connection.ConnectionBD;
import model.Historico;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HistoricoDAO {
    protected Connection conn;
    protected static final Logger LOGGER = Logger.getLogger(HistoricoDAO.class.getName());

    public HistoricoDAO() {
        try {
            this.conn = ConnectionBD.getInstance().getConnection();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao conectar ao banco de dados no HistoricoDAO.", e);
            this.conn = null;
        }
    }

    public Historico save(Historico historico) {
        if (this.conn == null) {
            LOGGER.severe("Conexão com o banco indisponível (save historico).");
            return null;
        }
        String sql = "INSERT INTO historico_busca (usuario_id, termo_busca) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, historico.getUsuarioId());
            ps.setString(2, historico.getTermoBusca());
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                LOGGER.severe("Nenhum histórico salvo.");
                return null;
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    historico.setId(rs.getInt(1));
                }
            }
            return historico;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao salvar histórico.", ex);
            return null;
        }
    }
    
    public boolean delete(int id) {
    if (this.conn == null) {
        LOGGER.severe("Conexão com o banco indisponível (delete historico).");
        return false;
    }
    String sql = "DELETE FROM historico_busca WHERE id = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, id);
        int affectedRows = ps.executeUpdate();
        return affectedRows > 0;
    } catch (SQLException ex) {
        LOGGER.log(Level.SEVERE, "Erro ao deletar histórico por ID.", ex);
        return false;
    }
}


    public List<Historico> findByUsuarioId(int usuarioId) {
        if (this.conn == null) {
            LOGGER.severe("Conexão com o banco indisponível (buscar historico).");
            return new ArrayList<>();
        }
        String sql = "SELECT id, usuario_id, termo_busca FROM historico_busca WHERE usuario_id = ?";
        List<Historico> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Historico h = new Historico(
                        rs.getInt("id"),
                        rs.getInt("usuario_id"),
                        rs.getString("termo_busca")
                    );
                    lista.add(h);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar históricos por usuário.", ex);
        }
        return lista;
    }
}
