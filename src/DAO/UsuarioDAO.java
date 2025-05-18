/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import connection.ConnectionBD;
import model.Usuario; 
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
public class UsuarioDAO {
    private Connection conn;
    private static final Logger LOGGER = Logger.getLogger(UsuarioDAO.class.getName());

    public UsuarioDAO() {
        try {
            this.conn = ConnectionBD.getInstance().getConnection();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Falha ao conectar ao banco de dados no construtor do UsuarioDAO.", e);
            this.conn = null;
        }
    }

    
    public Usuario save(Usuario u) {
        if (this.conn == null) {
            LOGGER.severe("Operação save (Usuario) não pode ser executada: conexão com banco de dados indisponível.");
            return null;
        }

        String sqlPessoa = "INSERT INTO pessoa (nome) VALUES (?)";
        String sqlUsuario = "INSERT INTO usuario (id, email, senha) VALUES (?, ?, ?)";

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement psPessoa = conn.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS)) {
                psPessoa.setString(1, u.getNome()); 
                int affectedRows = psPessoa.executeUpdate();
                if (affectedRows == 0) {
                    LOGGER.severe("Falha ao inserir Pessoa para o Usuario, nenhuma linha afetada.");
                    conn.rollback();
                    return null;
                }
                try (ResultSet rs = psPessoa.getGeneratedKeys()) {
                    if (rs.next()) {
                        u.setId(rs.getInt(1)); 
                    } else {
                        LOGGER.severe("Falha ao obter ID gerado para Pessoa (Usuario).");
                        conn.rollback();
                        return null;
                    }
                }
            }

            try (PreparedStatement psUsuario = conn.prepareStatement(sqlUsuario)) {
                psUsuario.setInt(1, u.getId());
                psUsuario.setString(2, u.getEmail());
                psUsuario.setInt(3, u.getSenha()); 
                psUsuario.executeUpdate();
            }

            conn.commit();
            return u;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao salvar Usuario: " + u.getEmail(), ex);
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException e2) {
                LOGGER.log(Level.SEVERE, "Erro ao tentar fazer rollback ao salvar Usuario.", e2);
            }
            return null;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Erro ao restaurar autoCommit para true após salvar Usuario.", e);
            }
        }
    }

    public Usuario update(Usuario u) {
        if (this.conn == null) {
            LOGGER.severe("Operação update (Usuario) não pode ser executada: conexão indisponível.");
            return null;
        }

        String sqlPessoa = "UPDATE pessoa SET nome = ? WHERE id = ?";
        String sqlUsuario = "UPDATE usuario SET email = ?, senha = ? WHERE id = ?";

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement psPessoa = conn.prepareStatement(sqlPessoa)) {
                psPessoa.setString(1, u.getNome());
                psPessoa.setInt(2, u.getId());
                psPessoa.executeUpdate();
            }

            try (PreparedStatement psUsuario = conn.prepareStatement(sqlUsuario)) {
                psUsuario.setString(1, u.getEmail());
                psUsuario.setInt(2, u.getSenha()); 
                psUsuario.setInt(3, u.getId());
                psUsuario.executeUpdate();
            }

            conn.commit();
            return u;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar Usuario com ID: " + u.getId(), ex);
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException e2) {
                LOGGER.log(Level.SEVERE, "Erro ao tentar fazer rollback ao atualizar Usuario.", e2);
            }
            return null;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Erro ao restaurar autoCommit para true após atualizar Usuario.", e);
            }
        }
    }

    public boolean delete(int id) {
        if (this.conn == null) {
            LOGGER.severe("Operação delete (Usuario) não pode ser executada: conexão indisponível.");
            return false;
        }

        String delUsuario = "DELETE FROM usuario WHERE id = ?";
        String delPessoa = "DELETE FROM pessoa  WHERE id = ?"; 

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement psUsuario = conn.prepareStatement(delUsuario)) {
                psUsuario.setInt(1, id);
                psUsuario.executeUpdate();
            }

            try (PreparedStatement psPessoa = conn.prepareStatement(delPessoa)) {
                psPessoa.setInt(1, id);
                psPessoa.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao deletar Usuario com ID: " + id, ex);
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException e2) {
                LOGGER.log(Level.SEVERE, "Erro ao tentar fazer rollback ao deletar Usuario.", e2);
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Erro ao restaurar autoCommit para true após deletar Usuario.", e);
            }
        }
    }

    public Usuario findById(int id) {
        if (this.conn == null) {
            LOGGER.severe("Operação findById (Usuario) não pode ser executada: conexão indisponível.");
            return null;
        }

        String sql = """
            SELECT p.id, p.nome, u.email, u.senha
            FROM pessoa p
            JOIN usuario u ON p.id = u.id
            WHERE p.id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return converteParaUsuario(rs);
                }
                return null; 
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar Usuario por ID: " + id, ex);
            return null;
        }
    }

    public Usuario findByEmail(String email) {
        if (this.conn == null) {
            LOGGER.severe("Operação findByEmail (Usuario) não pode ser executada: conexão indisponível.");
            return null;
        }

        String sql = """
            SELECT p.id, p.nome, u.email, u.senha
            FROM pessoa p
            JOIN usuario u ON p.id = u.id
            WHERE u.email = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return converteParaUsuario(rs);
                }
                return null; 
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar Usuario por email: " + email, ex);
            return null;
        }
    }

    public List<Usuario> findAll() {
        if (this.conn == null) {
            LOGGER.severe("Operação findAll (Usuario) não pode ser executada: conexão indisponível.");
            return Collections.emptyList();
        }

        String sql = """
            SELECT p.id, p.nome, u.email, u.senha
            FROM pessoa p
            JOIN usuario u ON p.id = u.id
        """;
        List<Usuario> lista = new ArrayList<>();

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Usuario usuario = converteParaUsuario(rs);
                if (usuario != null) {
                    lista.add(usuario);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao listar todos os Usuarios.", ex);
            return Collections.emptyList();
        }
        return lista;
    }

    private Usuario converteParaUsuario(ResultSet rs) {
        try {
            int id = rs.getInt("id");
            String nome = rs.getString("nome");
            String email = rs.getString("email");
            int senha = rs.getInt("senha"); 
            return new Usuario(id, email, senha, nome);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao converter ResultSet para Usuario.", ex);
            return null;
        }
    }

    public void closeConnection() {
        if (this.conn != null) {
            try {
                this.conn.close();
                LOGGER.info("Conexão do UsuarioDAO fechada.");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Erro ao fechar a conexão do UsuarioDAO.", e);
            }
        }
    }
}