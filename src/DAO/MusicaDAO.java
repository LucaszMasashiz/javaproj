
package DAO;

import connection.ConnectionBD;
import model.Musica;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MusicaDAO {
    private Connection conn;
    private static final Logger LOGGER = Logger.getLogger(MusicaDAO.class.getName());

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
     * @param m O objeto Musica a ser salvo.
     * @return O objeto Musica com o ID populado (se auto-gerado) em caso de sucesso, ou null em caso de falha.
     */
    public Musica save(Musica m) {
        if (this.conn == null) {
            LOGGER.severe("Operação save (Musica) não pode ser executada: conexão com banco de dados indisponível.");
            return null;
        }

        // Se o ID da música NÃO é auto-incrementável e é fornecido pelo construtor:
        // String sql = "INSERT INTO musica (id, artista_id, nome, genero, album) VALUES (?, ?, ?, ?, ?)";
        // Se o ID da música É auto-incrementável:
        String sql = "INSERT INTO musica (artista_id, nome, genero, album) VALUES (?, ?, ?, ?)";


        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Se o ID da música NÃO é auto-incrementável:
            // ps.setInt(1, m.getId());
            // ps.setInt(2, m.getArtistaId());
            // ps.setString(3, m.getNome());
            // ps.setString(4, m.getGenero());
            // ps.setString(5, m.getAlbum());

            // Se o ID da música É auto-incrementável:
            ps.setInt(1, m.getArtistaId());
            ps.setString(2, m.getNome());
            ps.setString(3, m.getGenero());
            ps.setString(4, m.getAlbum());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                LOGGER.severe("Falha ao inserir Musica, nenhuma linha afetada.");
                return null;
            }

            // Se o ID da música É auto-incrementável, obter o ID gerado:
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    m.setId(rs.getInt(1));
                } else {
                    // Isso pode acontecer se a tabela não estiver configurada para retornar chaves
                    // ou se o ID for fornecido manualmente e não auto-gerado.
                    // Se o ID é fornecido manualmente, esta parte não é estritamente necessária
                    // a menos que você queira confirmar a inserção.
                    LOGGER.warning("Falha ao obter ID gerado para Musica (ou ID não é auto-gerado).");
                }
            }
            return m;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao salvar Musica: " + m.getNome(), ex);
            // Não há transação explícita aqui (setAutoCommit(false)), então não há rollback no DAO.
            // Se a operação falhar, a exceção é capturada e null é retornado.
            // Para operações em múltiplas tabelas, o rollback seria crucial.
            return null;
        }
        // Não há 'finally' para setAutoCommit(true) porque não foi alterado.
    }

    /**
     * Atualiza os dados de uma música existente no banco de dados.
     *
     * @param m O objeto Musica com os dados atualizados.
     * @return O objeto Musica atualizado em caso de sucesso, ou null em caso de falha.
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
                return null; // Ou retornar 'm' se preferir indicar que a operação não deu erro SQL, mas nada mudou.
                             // Retornar null é mais consistente com falha ou "não encontrado".
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar Musica com ID: " + m.getId(), ex);
            return null;
        }
    }

    /**
     * Remove uma música do banco de dados.
     *
     * @param id O ID da música a ser removida.
     * @return true em caso de sucesso, false em caso de falha ou se a música não foi encontrada.
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
            return affectedRows > 0; // Retorna true se alguma linha foi deletada
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao deletar Musica com ID: " + id, ex);
            return false;
        }
    }

    /**
     * Busca uma música pelo seu ID.
     *
     * @param id O ID da música.
     * @return O objeto Musica se encontrado, ou null caso contrário ou em caso de erro.
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
                return null; // Não encontrada
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar Musica por ID: " + id, ex);
            return null;
        }
    }

    /**
     * Lista todas as músicas cadastradas.
     *
     * @return Uma lista de Musicas, ou uma lista vazia em caso de erro ou se não houver músicas.
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
     * Lista todas as músicas de um artista específico.
     *
     * @param artistaId O ID do artista.
     * @return Uma lista de Musicas do artista, ou uma lista vazia.
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
     * Lista todas as músicas de um determinado gênero.
     *
     * @param genero O gênero das músicas a serem listadas.
     * @return Uma lista de Musicas do gênero especificado, ou uma lista vazia.
     */
    public List<Musica> findByGenero(String genero) {
        if (this.conn == null) {
            LOGGER.severe("Operação findByGenero (Musica) não pode ser executada: conexão indisponível.");
            return Collections.emptyList();
        }

        String sql = "SELECT id, artista_id, nome, genero, album FROM musica WHERE genero ILIKE ?"; // ILIKE para case-insensitive
        List<Musica> lista = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + genero + "%"); // Para buscar gêneros que contenham a string
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
     * Converte uma linha do ResultSet para um objeto Musica.
     *
     * @param rs O ResultSet posicionado na linha a ser convertida.
     * @return Um objeto Musica, ou null em caso de erro na conversão.
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
     * Fecha a conexão com o banco de dados.
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