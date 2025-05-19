
package DAO;

import connection.ConnectionBD;
import model.Artista;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.AfterClass; 

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.*;

public class ArtistaDAOTeste {
    protected static ArtistaDAO dao;
    protected static Connection conn;

    @BeforeClass
    public static void setUpClass() throws SQLException { 
        conn = ConnectionBD.getInstance().getConnection();
        assertNotNull("A conexão com o banco de dados não pôde ser estabelecida.", conn);
        dao = new ArtistaDAO(); 

        try (Statement stmt = conn.createStatement()) {
           
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
    public void cleanUp() throws SQLException { 
        try (Statement stmt = conn.createStatement()) {
           
            stmt.execute("TRUNCATE TABLE artista CASCADE");
            stmt.execute("TRUNCATE TABLE pessoa  CASCADE");
        }
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("TRUNCATE TABLE artista CASCADE");
                stmt.execute("TRUNCATE TABLE pessoa CASCADE");
               
            }
            conn.close();
        }
      
    }

    @Test
    public void testSaveAndFindById() {
        Artista novo = new Artista("JDoe", "Rock", 0, "John"/*, "Doe", 30*/); 
                                                                               

        Artista salvo = dao.save(novo);
        assertNotNull("save() não deve retornar null em caso de sucesso", salvo);
        assertTrue("ID deve ser > 0 após salvar", salvo.getId() > 0);
        assertEquals("Nome do artista não corresponde", "John", salvo.getNome()); 
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
        
        System.out.println("Teste 'testSaveComFalhaPessoaIdNaoGerado' é conceitual para o DAO atual.");
    }


    @Test
    public void testFindByIdNaoExistente() {
        Artista naoExistente = dao.findById(9999); 
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

        List<Artista> jazz = dao.findByGenero("Jazz"); 
        assertNotNull("findByGenero() para gênero não existente não deve retornar null", jazz);
        assertTrue("findByGenero() deve retornar lista vazia para gênero não existente", jazz.isEmpty());
    }

    @Test
    public void testUpdate() {
        Artista artistaOriginal = new Artista("CJ","HipHop",0,"Carl"/*,"Johnson",28*/);
        Artista salvo = dao.save(artistaOriginal);
        assertNotNull("Artista original não foi salvo para o teste de update", salvo);

        
        salvo.setGenero("Rap Alternativo");
        salvo.setNome("Carlton Banks"); 
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
        
        Artista resultadoUpdate = dao.update(naoExistente);
        assertNotNull("Update em artista não existente não deveria lançar exceção SQL e retornar o objeto passado", resultadoUpdate);
       
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
        
        boolean resultadoDelete = dao.delete(8888); 
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