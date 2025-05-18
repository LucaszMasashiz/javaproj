/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import model.Artista;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import controller.ArtistaController;
import java.util.List;

/**
 *
 * @author Masashi
 */
public class ArtistaControllerTest {
    private ArtistaController controller;
    private Artista artistaCriado;

    @Before
    public void setUp() {
        controller = new ArtistaController();
    }

    @After
    public void tearDown() {
        if (artistaCriado != null && artistaCriado.getId() > 0) {
            controller.removerArtista(artistaCriado.getId());
        }
    }

    @Test
    public void testSalvarArtista() {
        artistaCriado = controller.salvarArtista("Nome Artístico Teste", "Pop", "Nome Real Teste");
        Assert.assertNotNull("Artista não foi salvo", artistaCriado);
        Assert.assertTrue("ID não foi atribuído", artistaCriado.getId() > 0);
        Assert.assertEquals("Nome artístico diferente", "Nome Artístico Teste", artistaCriado.getNomeArtistico());
    }

    @Test
    public void testBuscarPorId() {
        artistaCriado = controller.salvarArtista("Nome Artístico Buscar", "Rock", "Nome Real Buscar");
        Artista artista = controller.buscarPorId(artistaCriado.getId());
        Assert.assertNotNull("Artista não encontrado por ID", artista);
        Assert.assertEquals("Nome artístico incorreto", "Nome Artístico Buscar", artista.getNomeArtistico());
    }

    @Test
    public void testAtualizarArtista() {
        artistaCriado = controller.salvarArtista("Artista Antigo", "Jazz", "Nome Antigo");
        Artista atualizado = controller.atualizarArtista(
                artistaCriado.getId(),
                "Artista Novo",
                "Blues",
                "Nome Novo"
        );
        Assert.assertNotNull("Artista não foi atualizado", atualizado);
        Assert.assertEquals("Nome artístico não atualizado", "Artista Novo", atualizado.getNomeArtistico());
        Assert.assertEquals("Gênero não atualizado", "Blues", atualizado.getGenero());
        Assert.assertEquals("Nome real não atualizado", "Nome Novo", atualizado.getNome());
    }

    @Test
    public void testListarTodos() {
        artistaCriado = controller.salvarArtista("Listar Todos", "MPB", "Nome Real MPB");
        List<Artista> lista = controller.listarTodos();
        Assert.assertNotNull("Lista de artistas é nula", lista);
        Assert.assertTrue("Lista de artistas não contém o artista criado", 
            lista.stream().anyMatch(a -> "Listar Todos".equals(a.getNomeArtistico())));
    }

    @Test
    public void testBuscarPorGenero() {
        artistaCriado = controller.salvarArtista("Buscar Por Gênero", "Samba", "Nome Real Samba");
        List<Artista> lista = controller.buscarPorGenero("Samba");
        Assert.assertNotNull("Lista de artistas por gênero é nula", lista);
        Assert.assertTrue("Artista não foi encontrado pelo gênero",
            lista.stream().anyMatch(a -> "Buscar Por Gênero".equals(a.getNomeArtistico())));
    }

    @Test
    public void testBuscarPorNomeArtistico() {
        artistaCriado = controller.salvarArtista("Nome Unico Artistico", "Forró", "Nome Real Forró");
        Artista buscado = controller.buscarPorNomeArtistico("Nome Unico Artistico");
        Assert.assertNotNull("Artista não foi encontrado pelo nome artístico", buscado);
        Assert.assertEquals("Nome artístico diferente", "Nome Unico Artistico", buscado.getNomeArtistico());
    }

    @Test
    public void testRemoverArtista() {
        artistaCriado = controller.salvarArtista("Remover Artista", "Pagode", "Nome Real Pagode");
        boolean removido = controller.removerArtista(artistaCriado.getId());
        Assert.assertTrue("Artista não foi removido", removido);

        // Verifica se realmente foi removido
        Artista buscado = controller.buscarPorId(artistaCriado.getId());
        Assert.assertNull("Artista ainda existe após remoção", buscado);
        artistaCriado = null; // Para não tentar remover de novo no @After
    }
}

