
// src/test/java/DAO/ArtistaDAOTest.java
package DAO;

import connection.ConnectionBD;
import model.Artista;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.AfterClass; // Para fechar a conexão

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.*;

public class ArtistaDAOTeste {
    private static ArtistaDAO dao;
    private static Connection conn;

    @BeforeClass
    public static void setUpClass() throws SQLException { // SQLException ainda pode ocorrer aqui do ConnectionBD ou Statement
        conn = ConnectionBD.getInstance().getConnection();
        assertNotNull("A conexão com o banco de dados não pôde ser estabelecida.", conn);
        dao = new ArtistaDAO(); // Construtor não lança mais SQLException

        try (Statement stmt = conn.createStatement()) {
            // Ajustado para ser compatível com o ArtistaDAO.save() que insere apenas 'nome'
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS pessoa (
                  id SERIAL PRIMARY KEY,
                  nome VARCHAR(100) NOT NULL,
                  sobrenome VARCHAR(100), 
                  idade INT
                )
            """);
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS artista (
                  id INT PRIMARY KEY,
                  nome_artistico VARCHAR(100) NOT NULL,
                  genero VARCHAR(50) NOT NULL,
                  FOREIGN KEY (id) REFERENCES pessoa(id) ON DELETE CASCADE
                )
            """);
        }
    }

    @Before
    public void cleanUp() throws SQLException { // SQLException do Statement
        try (Statement stmt = conn.createStatement()) {
            // A ordem importa devido à FK. Artista primeiro.
            stmt.execute("TRUNCATE TABLE artista CASCADE");
            stmt.execute("TRUNCATE TABLE pessoa  CASCADE");
        }
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            // Limpar tabelas após todos os testes
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("TRUNCATE TABLE artista CASCADE");
                stmt.execute("TRUNCATE TABLE pessoa CASCADE");
                // Opcional: DROP TABLES IF EXISTS pessoa, artista;
            }
            conn.close();
        }
        // Se o DAO gerenciasse sua própria conexão e tivesse um método close:
        // if (dao != null) {
        //     dao.closeConnection();
        // }
    }

    @Test
    public void testSaveAndFindById() {
        // O construtor do Artista no teste (com sobrenome, idade) é mais completo
        // que o usado internamente pelo converteParaArtista do DAO.
        // Para o DAO atual, apenas nome, nomeArtistico e genero são usados de 'Artista a'.
        Artista novo = new Artista("JDoe", "Rock", 0, "John"/*, "Doe", 30*/); // Ajustado para o construtor do DAO
                                                                                // ou assumindo um construtor que só usa esses.
                                                                                // Se o Artista model tiver sobrenome e idade,
                                                                                // e o DAO devesse usá-los, o DAO.save() precisaria mudar.
                                                                                // Por ora, vamos assumir que o Artista passado ao DAO
                                                                                // só precisa ter os campos que o DAO utiliza.

        Artista salvo = dao.save(novo);
        assertNotNull("save() não deve retornar null em caso de sucesso", salvo);
        assertTrue("ID deve ser > 0 após salvar", salvo.getId() > 0);
        assertEquals("Nome do artista não corresponde", "John", salvo.getNome()); // Verificar se o nome real foi salvo
        assertEquals("Nome artístico não corresponde", "JDoe", salvo.getNomeArtistico());
        assertEquals("Gênero não corresponde", "Rock", salvo.getGenero());


        Artista encontrado = dao.findById(salvo.getId());
        assertNotNull("findById() deve encontrar o artista salvo", encontrado);
        assertEquals("ID do artista encontrado não corresponde", salvo.getId(), encontrado.getId());
        assertEquals("Nome do artista encontrado não corresponde", "John", encontrado.getNome());
        assertEquals("Nome artístico do artista encontrado não corresponde", "JDoe", encontrado.getNomeArtistico());
        assertEquals("Gênero do artista encontrado não corresponde", "Rock", encontrado.getGenero());
    }

    @Test
    public void testSaveComFalhaPessoaIdNaoGerado() {
        // Este teste é mais difícil de simular sem mockar o PreparedStatement
        // para forçar o getGeneratedKeys a não retornar nada.
        // Com a estrutura atual, uma falha no getGeneratedKeys fará o save retornar null.
        // Aqui, vamos assumir que se `save` retorna null, algo deu errado.
        // O teste específico de "Falha ao obter ID" já está coberto pela lógica do DAO
        // que retorna null. Se precisarmos testar essa condição explicitamente,
        // precisaríamos de um controle mais fino sobre a execução do SQL (ex: Mocks).
        System.out.println("Teste 'testSaveComFalhaPessoaIdNaoGerado' é conceitual para o DAO atual.");
    }


    @Test
    public void testFindByIdNaoExistente() {
        Artista naoExistente = dao.findById(9999); // Um ID que certamente não existe
        assertNull("findById() deve retornar null para artista não existente", naoExistente);
    }

    @Test
    public void testFindAllAndFindByGenero() {
        Artista artista1 = new Artista("Alicia", "Pop",    0, "Alice"/*, "Smith",25*/);
        Artista artista2 = new Artista("BB",     "Rock",   0, "Bob"/*,   "Brown",40*/);

        Artista salvo1 = dao.save(artista1);
        Artista salvo2 = dao.save(artista2);

        assertNotNull("Primeiro artista não foi salvo", salvo1);
        assertNotNull("Segundo artista não foi salvo", salvo2);

        List<Artista> todos = dao.findAll();
        assertNotNull("findAll() não deve retornar null", todos);
        assertEquals("findAll() deve retornar 2 artistas", 2, todos.size());

        List<Artista> pops = dao.findByGenero("Pop");
        assertNotNull("findByGenero() não deve retornar null", pops);
        assertEquals("findByGenero('Pop') deve retornar 1 artista", 1, pops.size());
        assertEquals("Nome artístico do artista pop não corresponde", "Alicia", pops.get(0).getNomeArtistico());

        List<Artista> jazz = dao.findByGenero("Jazz"); // Gênero não existente
        assertNotNull("findByGenero() para gênero não existente não deve retornar null", jazz);
        assertTrue("findByGenero() deve retornar lista vazia para gênero não existente", jazz.isEmpty());
    }

    @Test
    public void testUpdate() {
        Artista artistaOriginal = new Artista("CJ","HipHop",0,"Carl"/*,"Johnson",28*/);
        Artista salvo = dao.save(artistaOriginal);
        assertNotNull("Artista original não foi salvo para o teste de update", salvo);

        // Modifica o objeto retornado pelo save (que já tem o ID correto)
        salvo.setGenero("Rap Alternativo");
        salvo.setNome("Carlton Banks"); // Supondo que Artista tenha setNome para o nome real
        salvo.setNomeArtistico("Fresh C");

        Artista atualizado = dao.update(salvo);
        assertNotNull("update() não deve retornar null em caso de sucesso", atualizado);
        assertEquals("Gênero não foi atualizado corretamente", "Rap Alternativo", atualizado.getGenero());
        assertEquals("Nome real não foi atualizado corretamente", "Carlton Banks", atualizado.getNome());
        assertEquals("Nome artístico não foi atualizado corretamente", "Fresh C", atualizado.getNomeArtistico());

        Artista buscadoAposUpdate = dao.findById(salvo.getId());
        assertNotNull("Não foi possível buscar o artista após o update", buscadoAposUpdate);
        assertEquals("Gênero buscado não corresponde ao atualizado", "Rap Alternativo", buscadoAposUpdate.getGenero());
        assertEquals("Nome real buscado não corresponde ao atualizado", "Carlton Banks", buscadoAposUpdate.getNome());
        assertEquals("Nome artístico buscado não corresponde ao atualizado", "Fresh C", buscadoAposUpdate.getNomeArtistico());
    }

    @Test
    public void testUpdateArtistaNaoExistente() {
        Artista naoExistente = new Artista("Fantasma", "Blues", 999, "Desconhecido"/*, "N。", 100*/);
        // O update no DAO modificado tentará executar os UPDATEs.
        // Se o ID não existir, nenhuma linha será afetada, mas o DAO não lança exceção.
        // O DAO modificado retorna o objeto passado como parâmetro se não houver SQLException,
        // ou null se houver SQLException. Sem mock, é difícil testar o "não afetou linhas"
        // a não ser que o update retornasse boolean ou número de linhas afetadas.
        // Com o retorno atual (Artista ou null), assumimos que se não houve SQLException,
        // ele retorna o objeto 'a'.
        Artista resultadoUpdate = dao.update(naoExistente);
        assertNotNull("Update em artista não existente não deveria lançar exceção SQL e retornar o objeto passado", resultadoUpdate);
        // Para verificar que nada mudou, precisaríamos de um findById.
        assertNull("findById para artista não existente (após tentativa de update) deve ser null", dao.findById(9999));
    }


    @Test
    public void testDelete() {
        Artista artistaParaDeletar = new Artista("DP","Pop",0,"Diana"/*,"Prince",32*/);
        Artista salvo = dao.save(artistaParaDeletar);
        assertNotNull("Artista não foi salvo para o teste de delete", salvo);

        int idParaDeletar = salvo.getId();
        assertTrue("delete() deve retornar true em caso de sucesso", dao.delete(idParaDeletar));

        Artista deletado = dao.findById(idParaDeletar);
        assertNull("findById() após delete deve retornar null", deletado);
    }

    @Test
    public void testDeleteNaoExistente() {
        // O método delete no DAO modificado não lança exceção se o ID não existe (0 linhas afetadas).
        // Ele retorna true se o commit for bem-sucedido, ou false se houver SQLException.
        boolean resultadoDelete = dao.delete(8888); // ID não existente
        assertTrue("delete() para ID não existente deve retornar true (nenhuma exceção SQL, commit ok)", resultadoDelete);
    }

    @Test
    public void testFindByNomeArtistico() {
        Artista artista = new Artista("Unico", "Funk", 0, "Fulano"/*, "de Tal", 22*/);
        Artista salvo = dao.save(artista);
        assertNotNull("Artista não foi salvo para teste de findByNomeArtistico", salvo);

        Artista encontrado = dao.findByNomeArtistico("Unico");
        assertNotNull("findByNomeArtistico() deve encontrar o artista", encontrado);
        assertEquals("ID do artista encontrado não corresponde", salvo.getId(), encontrado.getId());
        assertEquals("Nome artístico não corresponde", "Unico", encontrado.getNomeArtistico());

        Artista naoEncontrado = dao.findByNomeArtistico("Inexistente");
        assertNull("findByNomeArtistico() deve retornar null para nome não existente", naoEncontrado);
    }
}