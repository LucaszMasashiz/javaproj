package DAO;

import connection.ConnectionBD;
import model.Musica;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO responsável pelas operações de persistência relacionadas à entidade Musica.
 * Permite salvar, atualizar, remover, buscar e listar músicas no banco de dados.
 * 
 * @author Masashi
 */
public class MusicaDAO {
    protected Connection conn;
    protected static final Logger LOGGER = Logger.getLogger(MusicaDAO.class.getName());

    /**
     * Construtor padrão. Inicializa a conexão com o banco de dados.
     */
    public MusicaDAO() {
        try {
            this.conn = ConnectionBD.getInstance().getConnection();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Falha ao conectar ao banco de dados no construtor do MusicaDAO.", e);
            this.conn = null;
        }
    }

    /**
     * Salva uma nova música no banco de dados.
     * 
     * @param m Objeto Musica a ser salvo.
     * @return O objeto Musica salvo (com ID atualizado), ou null em caso de erro.
     */
    public Musica save(Musica m) {
        if (this.conn == null) {
            LOGGER.severe("Operação save (Musica) não pode ser executada: conexão com banco de dados indisponível.");
            return null;
        }

       
        String sql = "INSERT INTO musica (artista_id, nome, genero, album) VALUES (?, ?, ?, ?)";


        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, m.getArtistaId());
            ps.setString(2, m.getNome());
            ps.setString(3, m.getGenero());
            ps.setString(4, m.getAlbum());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                LOGGER.severe("Falha ao inserir Musica, nenhuma linha afetada.");
                return null;
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    m.setId(rs.getInt(1));
                } else {
                 
                    LOGGER.warning("Falha ao obter ID gerado para Musica (ou ID não é auto-gerado).");
                }
            }
            return m;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao salvar Musica: " + m.getNome(), ex);
            return null;
        }
      
    }

    /**
     * Atualiza uma música existente no banco de dados.
     * 
     * @param m Objeto Musica com as informações a serem atualizadas.
     * @return O objeto Musica atualizado, ou null em caso de erro.
     */
    public Musica update(Musica m) {
        if (this.conn == null) {
            LOGGER.severe("Operação update (Musica) não pode ser executada: conexão indisponível.");
            return null;
        }

        String sql = "UPDATE musica SET artista_id = ?, nome = ?, genero = ?, album = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, m.getArtistaId());
            ps.setString(2, m.getNome());
            ps.setString(3, m.getGenero());
            ps.setString(4, m.getAlbum());
            ps.setInt(5, m.getId());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                return m;
            } else {
                LOGGER.warning("Nenhuma música atualizada. Verificar se o ID " + m.getId() + " existe.");
                return null; 
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar Musica com ID: " + m.getId(), ex);
            return null;
        }
    }

    /**
     * Remove uma música do banco de dados.
     * 
     * @param id ID da música a ser removida.
     * @return true se a música foi removida, false caso contrário.
     */
    public boolean delete(int id) {
        if (this.conn == null) {
            LOGGER.severe("Operação delete (Musica) não pode ser executada: conexão indisponível.");
            return false;
        }

        String sql = "DELETE FROM musica WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0; 
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao deletar Musica com ID: " + id, ex);
            return false;
        }
    }

    /**
     * Busca uma música pelo seu ID.
     * 
     * @param id ID da música.
     * @return Objeto Musica encontrado, ou null se não existir.
     */
    public Musica findById(int id) {
        if (this.conn == null) {
            LOGGER.severe("Operação findById (Musica) não pode ser executada: conexão indisponível.");
            return null;
        }

        String sql = "SELECT id, artista_id, nome, genero, album FROM musica WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return converteParaMusica(rs);
                }
                return null; 
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar Musica por ID: " + id, ex);
            return null;
        }
    }

    /**
     * Lista todas as músicas do banco de dados.
     * 
     * @return Lista de objetos Musica encontrados.
     */
    public List<Musica> findAll() {
        if (this.conn == null) {
            LOGGER.severe("Operação findAll (Musica) não pode ser executada: conexão indisponível.");
            return Collections.emptyList();
        }

        String sql = "SELECT id, artista_id, nome, genero, album FROM musica";
        List<Musica> lista = new ArrayList<>();

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Musica musica = converteParaMusica(rs);
                if (musica != null) {
                    lista.add(musica);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao listar todas as Musicas.", ex);
            return Collections.emptyList();
        }
        return lista;
    }

    /**
     * Busca músicas por ID do artista.
     * 
     * @param artistaId ID do artista.
     * @return Lista de músicas associadas ao artista.
     */
    public List<Musica> findByArtistaId(int artistaId) {
        if (this.conn == null) {
            LOGGER.severe("Operação findByArtistaId (Musica) não pode ser executada: conexão indisponível.");
            return Collections.emptyList();
        }

        String sql = "SELECT id, artista_id, nome, genero, album FROM musica WHERE artista_id = ?";
        List<Musica> lista = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, artistaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Musica musica = converteParaMusica(rs);
                    if (musica != null) {
                        lista.add(musica);
                    }
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar Musicas por artistaId: " + artistaId, ex);
            return Collections.emptyList();
        }
        return lista;
    }
     
    /**
     * Busca músicas por gênero.
     * 
     * @param genero Gênero da música (busca parcial, case-insensitive).
     * @return Lista de músicas do gênero informado.
     */
    public List<Musica> findByGenero(String genero) {
        if (this.conn == null) {
            LOGGER.severe("Operação findByGenero (Musica) não pode ser executada: conexão indisponível.");
            return Collections.emptyList();
        }

        String sql = "SELECT id, artista_id, nome, genero, album FROM musica WHERE genero ILIKE ?"; 
        List<Musica> lista = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + genero + "%"); 
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Musica musica = converteParaMusica(rs);
                    if (musica != null) {
                        lista.add(musica);
                    }
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar Musicas por genero: " + genero, ex);
            return Collections.emptyList();
        }
        return lista;
    }
    
    /**
     * Busca músicas por nome (busca parcial, case-insensitive).
     * 
     * @param nome Nome ou parte do nome da música.
     * @return Lista de músicas que correspondem ao nome informado.
     */
    public List<Musica> findByNome(String nome) {
    if (this.conn == null) {
        LOGGER.severe("Operação findByNome (Musica) não pode ser executada: conexão indisponível.");
        return Collections.emptyList();
    }
    String sql = "SELECT id, artista_id, nome, genero, album FROM musica WHERE nome ILIKE ?"; 
    List<Musica> lista = new ArrayList<>();
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, "%" + nome + "%");
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Musica musica = converteParaMusica(rs);
                if (musica != null) {
                    lista.add(musica);
                }
            }
        }
    } catch (SQLException ex) {
        LOGGER.log(Level.SEVERE, "Erro ao buscar Musicas por nome: " + nome, ex);
        return Collections.emptyList();
    }
    return lista;
}
    
/**
     * Converte um ResultSet em um objeto Musica.
     * 
     * @param rs ResultSet contendo os dados da música.
     * @return Objeto Musica populado, ou null em caso de erro.
     */
    private Musica converteParaMusica(ResultSet rs) {
        try {
            int id = rs.getInt("id");
            int artistaId = rs.getInt("artista_id");
            String nome = rs.getString("nome");
            String genero = rs.getString("genero");
            String album = rs.getString("album");
            return new Musica(id, artistaId, nome, genero, album);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao converter ResultSet para Musica.", ex);
            return null;
        }
    }
    
    
    /**
     * Fecha a conexão do DAO com o banco de dados.
     */
    public void closeConnection() {
        if (this.conn != null) {
            try {
                this.conn.close();
                LOGGER.info("Conexão do MusicaDAO fechada.");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Erro ao fechar a conexão do MusicaDAO.", e);
            }
        }
    }
}