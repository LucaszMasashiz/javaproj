package DAO;

import connection.ConnectionBD;
import model.Artista;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Masashi
 */
public class ArtistaDAO {
    protected Connection conn;
    protected static final Logger LOGGER = Logger.getLogger(ArtistaDAO.class.getName());

    public ArtistaDAO() {
        try {
            this.conn = ConnectionBD.getInstance().getConnection();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Falha ao conectar ao banco de dados no construtor.", e);
            this.conn = null; }
    }

    
    public Artista save(Artista a) {
        if (this.conn == null) {
            LOGGER.severe("Operação save não pode ser executada: conexão com banco de dados indisponível.");
            return null;
        }

        String sqlPessoa  = "INSERT INTO pessoa (nome) VALUES (?)";
        String sqlArtista = "INSERT INTO artista (id, nome_artistico, genero) VALUES (?, ?, ?)";

        try {
            conn.setAutoCommit(false);
            
            try (PreparedStatement ps = conn.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, a.getNome());
                int affectedRows = ps.executeUpdate();
                if (affectedRows == 0) {
                    LOGGER.severe("Falha ao inserir Pessoa, nenhuma linha afetada.");
                    conn.rollback();
                    return null;
                }
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        a.setId(rs.getInt(1));
                    } else {
                        LOGGER.severe("Falha ao obter ID gerado para Pessoa.");
                        conn.rollback();
                        return null;
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlArtista)) {
                ps.setInt(1, a.getId());
                ps.setString(2, a.getNomeArtistico());
                ps.setString(3, a.getGenero());
                ps.executeUpdate();
            }

            conn.commit();
            return a;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao salvar Artista: " + a.getNomeArtistico(), ex);
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException e2) {
                LOGGER.log(Level.SEVERE, "Erro ao tentar fazer rollback.", e2);
            }
            return null;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Erro ao restaurar autoCommit para true.", e);
            }
        }
    }

    public Artista update(Artista a) {
        if (this.conn == null) {
            LOGGER.severe("Operação update não pode ser executada: conexão com banco de dados indisponível.");
            return null;
        }

        String sqlPessoa  = "UPDATE pessoa  SET nome = ?             WHERE id = ?";
        String sqlArtista = "UPDATE artista SET nome_artistico = ?, genero = ? WHERE id = ?";
        try {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sqlPessoa)) {
                ps.setString(1, a.getNome());
                ps.setInt(2, a.getId());
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlArtista)) {
                ps.setString(1, a.getNomeArtistico());
                ps.setString(2, a.getGenero());
                ps.setInt(3, a.getId());
                ps.executeUpdate();
            }
            conn.commit();
            return a;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar Artista com ID: " + a.getId(), ex);
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException e2) {
                LOGGER.log(Level.SEVERE, "Erro ao tentar fazer rollback.", e2);
            }
            return null;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Erro ao restaurar autoCommit para true.", e);
            }
        }
    }

 
    public boolean delete(int id) {
        if (this.conn == null) {
            LOGGER.severe("Operação delete não pode ser executada: conexão com banco de dados indisponível.");
            return false;
        }

        String delArtista = "DELETE FROM artista WHERE id = ?";
        String delPessoa  = "DELETE FROM pessoa  WHERE id = ?";
        try {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(delArtista)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(delPessoa)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
            conn.commit();
            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao deletar Artista com ID: " + id, ex);
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException e2) {
                LOGGER.log(Level.SEVERE, "Erro ao tentar fazer rollback.", e2);
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Erro ao restaurar autoCommit para true.", e);
            }
        }
    }

    
    public Artista findById(int id) {
        if (this.conn == null) {
            LOGGER.severe("Operação findById não pode ser executada: conexão com banco de dados indisponível.");
            return null;
        }

        String sql = """
            SELECT p.id, p.nome,
                   a.nome_artistico, a.genero
              FROM pessoa p
              JOIN artista a ON p.id = a.id
             WHERE p.id = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return converteParaArtista(rs);
                }
                return null; 
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar Artista por ID: " + id, ex);
            return null;
        }
    }

    
    public List<Artista> findAll() {
        if (this.conn == null) {
            LOGGER.severe("Operação findAll não pode ser executada: conexão com banco de dados indisponível.");
            return Collections.emptyList();
        }

        String sql = """
            SELECT p.id, p.nome,
                   a.nome_artistico, a.genero
              FROM pessoa p
              JOIN artista a ON p.id = a.id
        """;
        List<Artista> lista = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Artista artista = converteParaArtista(rs);
                if (artista != null) { 
                    lista.add(artista);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao listar todos os Artistas.", ex);
            return Collections.emptyList(); // Retorna lista vazia em caso de erro
        }
        return lista;
    }

    
    public List<Artista> findByGenero(String genero) {
        if (this.conn == null) {
            LOGGER.severe("Operação findByGenero não pode ser executada: conexão com banco de dados indisponível.");
            return Collections.emptyList();
        }

        String sql = """
            SELECT p.id, p.nome,
                   a.nome_artistico, a.genero
              FROM pessoa p
              JOIN artista a ON p.id = a.id
             WHERE a.genero = ?
        """;
        List<Artista> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, genero);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                     Artista artista = converteParaArtista(rs);
                    if (artista != null) {
                        lista.add(artista);
                    }
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar Artistas por gênero: " + genero, ex);
            return Collections.emptyList();
        }
        return lista;
    }

   
    public Artista findByNomeArtistico(String nomeArtistico) {
        if (this.conn == null) {
            LOGGER.severe("Operação findByNomeArtistico não pode ser executada: conexão com banco de dados indisponível.");
            return null;
        }

        String sql = """
                SELECT p.id, p.nome,
                       a.nome_artistico, a.genero
                  FROM pessoa p
                  JOIN artista a ON p.id = a.id
                 WHERE unaccent(a.nome_artistico) ILIKE unaccent(?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + nomeArtistico + "%");
            ps.setString(1, nomeArtistico);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return converteParaArtista(rs);
                }
                return null; 
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar Artista por nome artístico: " + nomeArtistico, ex);
            return null;
        }
    }

   
    private Artista converteParaArtista(ResultSet rs) {
        try {
            int    id             = rs.getInt("id");
            String nomeReal       = rs.getString("nome");
            String nomeArtistico  = rs.getString("nome_artistico");
            String genero         = rs.getString("genero");
            return new Artista(nomeArtistico, genero, id, nomeReal);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao converter ResultSet para Artista.", ex);
            return null; 
        }
    }

    public void closeConnection() {
        if (this.conn != null) {
            try {
                this.conn.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Erro ao fechar a conexão com o banco de dados.", e);
            }
        }
    }
}