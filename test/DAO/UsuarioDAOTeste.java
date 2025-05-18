/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import connection.ConnectionBD;
import model.Usuario;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.AfterClass;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.*;

/**                                                                           
 *
 * @author Masashi
 */
public class UsuarioDAOTeste {
    private static UsuarioDAO dao;
    private static Connection conn;

    @BeforeClass
    public static void setUpClass() throws SQLException {
        conn = ConnectionBD.getInstance().getConnection();
        assertNotNull("A conexão com o banco de dados não pôde ser estabelecida.", conn);
        dao = new UsuarioDAO(); 

        try (Statement stmt = conn.createStatement()) {
           
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS pessoa (
                  id SERIAL PRIMARY KEY,
                  nome VARCHAR(100) NOT NULL
                )
            """);
           
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS usuario (
                  id INT PRIMARY KEY,
                  email VARCHAR(100) NOT NULL UNIQUE,
                  senha INT NOT NULL, -- Lembre-se do alerta de segurança sobre isso
                  FOREIGN KEY (id) REFERENCES pessoa(id) ON DELETE CASCADE
                )
            """);
        }
    }

    @Before
    public void cleanUp() throws SQLException {
        try (Statement stmt = conn.createStatement()) {
           
            stmt.execute("TRUNCATE TABLE usuario CASCADE"); 
            stmt.execute("TRUNCATE TABLE pessoa CASCADE"); 
        }
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        if (conn != null && !conn.isClosed()) {
           
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("TRUNCATE TABLE usuario CASCADE");
                stmt.execute("TRUNCATE TABLE pessoa CASCADE");
                
            }
            conn.close();
        }
        if (dao != null) {
             dao.closeConnection(); 
        }
    }

    @Test
    public void testSaveAndFindById() {
        Usuario novoUsuario = new Usuario(4, "teste@exemplo.com", 12345, "Usuario Teste");
        
        Usuario salvo = dao.save(novoUsuario);
        assertNotNull("save() não deve retornar null em caso de sucesso", salvo);
        assertTrue("ID do usuário deve ser > 0 após salvar", salvo.getId() > 0);
        assertEquals("Email não corresponde ao esperado", "teste@exemplo.com", salvo.getEmail());
        assertEquals("Nome não corresponde ao esperado", "Usuario Teste", salvo.getNome());
        assertEquals("Senha não corresponde à esperada", 12345, salvo.getSenha());

        Usuario encontrado = dao.findById(salvo.getId());
        assertNotNull("findById() deve encontrar o usuário salvo", encontrado);
        assertEquals("ID do usuário encontrado não corresponde", salvo.getId(), encontrado.getId());
        assertEquals("Email do usuário encontrado não corresponde", "teste@exemplo.com", encontrado.getEmail());
        assertEquals("Nome do usuário encontrado não corresponde", "Usuario Teste", encontrado.getNome());
        assertEquals("Senha do usuário encontrado não corresponde", 12345, encontrado.getSenha());
    }

    @Test
    public void testSaveComEmailDuplicadoDeveFalhar() {
        Usuario usuario1 = new Usuario(23, "duplicado@exemplo.com", 111,"Usuario Um");
        Usuario salvo1 = dao.save(usuario1);
        assertNotNull("Primeiro usuário com email duplicado deveria ser salvo", salvo1);

        Usuario usuario2 = new Usuario(21, "duplicado@exemplo.com", 222, "Usuario Dois");
        Usuario salvo2 = dao.save(usuario2); // Deve falhar devido à constraint UNIQUE no email
        assertNull("save() deve retornar null ao tentar salvar usuário com email duplicado", salvo2);
    }

    @Test
    public void testFindByIdNaoExistente() {
        Usuario naoExistente = dao.findById(99999); 
        assertNull("findById() deve retornar null para usuário não existente", naoExistente);
    }
    
    @Test
    public void testFindByEmail() {
        Usuario usuario = new Usuario(2, "busca.email@exemplo.com", 789,"Buscador Email");
        Usuario salvo = dao.save(usuario);
        assertNotNull("Usuário para teste de findByEmail não foi salvo", salvo);

        Usuario encontrado = dao.findByEmail("busca.email@exemplo.com");
        assertNotNull("findByEmail() deve encontrar o usuário", encontrado);
        assertEquals("ID do usuário encontrado não corresponde", salvo.getId(), encontrado.getId());
        assertEquals("Email não corresponde", "busca.email@exemplo.com", encontrado.getEmail());

        Usuario naoEncontrado = dao.findByEmail("nao.existe@exemplo.com");
        assertNull("findByEmail() deve retornar null para email não existente", naoEncontrado);
    }

    @Test
    public void testFindAll() {
        assertTrue("findAll() deve retornar lista vazia inicialmente", dao.findAll().isEmpty());

        Usuario usuario1 = new Usuario(15, "user1@exemplo.com", 124, "User Um");
        Usuario usuario2 = new Usuario(18, "user2@exemplo.com", 456, "User Dois");
        
        assertNotNull(dao.save(usuario1));
        assertNotNull(dao.save(usuario2));

        List<Usuario> todos = dao.findAll();
        assertNotNull("findAll() não deve retornar null", todos);
        assertEquals("findAll() deve retornar 2 usuários", 2, todos.size());
    }

    @Test
    public void testUpdate() {
        Usuario original = new Usuario(8, "original@exemplo.com", 111, "Original Nome");
        Usuario salvo = dao.save(original);
        assertNotNull("Usuário original não foi salvo para o teste de update", salvo);

        salvo.setEmail("atualizado@exemplo.com");
        salvo.setNome("Nome Atualizado");
        salvo.setSenha(999);

        Usuario atualizado = dao.update(salvo);
        assertNotNull("update() não deve retornar null em caso de sucesso", atualizado);
        assertEquals("Email não foi atualizado corretamente", "atualizado@exemplo.com", atualizado.getEmail());
        assertEquals("Nome não foi atualizado corretamente", "Nome Atualizado", atualizado.getNome());
        assertEquals("Senha não foi atualizada corretamente", 999, atualizado.getSenha());

        Usuario buscadoAposUpdate = dao.findById(salvo.getId());
        assertNotNull("Não foi possível buscar o usuário após o update", buscadoAposUpdate);
        assertEquals("Email buscado não corresponde ao atualizado", "atualizado@exemplo.com", buscadoAposUpdate.getEmail());
        assertEquals("Nome buscado não corresponde ao atualizado", "Nome Atualizado", buscadoAposUpdate.getNome());
        assertEquals("Senha buscada não corresponde à atualizada", 999, buscadoAposUpdate.getSenha());
    }

    @Test
    public void testUpdateParaEmailDuplicadoDeveFalhar() {
        Usuario usuario1 = new Usuario(54, "email1.update@exemplo.com", 101, "Update Um");
        Usuario usuario2 = new Usuario(73, "email2.update@exemplo.com", 102, "Update Dois");

        Usuario salvo1 = dao.save(usuario1);
        Usuario salvo2 = dao.save(usuario2);
        assertNotNull(salvo1);
        assertNotNull(salvo2);

        
        salvo2.setEmail("email1.update@exemplo.com");
        Usuario resultadoUpdate = dao.update(salvo2);
        assertNull("Update que resultaria em email duplicado deveria retornar null", resultadoUpdate);

        
        Usuario usuario2OriginalDB = dao.findById(salvo2.getId());
        assertNotNull(usuario2OriginalDB);
        assertEquals("Email do usuario2 não deveria ter sido alterado no DB", "email2.update@exemplo.com", usuario2OriginalDB.getEmail());
    }


    @Test
    public void testDelete() {
        Usuario usuarioParaDeletar = new Usuario(66, "deletar@exemplo.com", 777, "Para Deletar");
        Usuario salvo = dao.save(usuarioParaDeletar);
        assertNotNull("Usuário não foi salvo para o teste de delete", salvo);

        int idParaDeletar = salvo.getId();
        assertTrue("delete() deve retornar true em caso de sucesso", dao.delete(idParaDeletar));

        Usuario deletado = dao.findById(idParaDeletar);
        assertNull("findById() após delete deve retornar null", deletado);

       
    }

    @Test
    public void testDeleteNaoExistente() {
        boolean resultadoDelete = dao.delete(77777);
        assertTrue("delete() para ID não existente deve retornar true (sem exceção SQL)", resultadoDelete);
    }
}

