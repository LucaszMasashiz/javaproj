package Controller;

import controller.UsuarioController;
import model.Usuario;

import java.util.List;

public class UsuarioControllerTest {
    public static void main(String[] args) {
        UsuarioController controller = new UsuarioController();

        System.out.println("== Criando usuário ==");
        Usuario novo = controller.criarUsuario(0, "teste@exemplo.com", 1234, "João Teste");
        System.out.println("Usuário criado: " + (novo != null ? novo.getEmail() : "Erro na criação"));

        if (novo != null) {
            System.out.println("\n== Buscando por ID ==");
            Usuario buscado = controller.buscarPorId(novo.getId());
            System.out.println("Encontrado: " + (buscado != null ? buscado.getNome() : "Não encontrado"));
        }

        if (novo != null) {
            System.out.println("\n== Atualizando usuário ==");
            Usuario atualizado = controller.atualizarUsuario(novo.getId(), "João Atualizado", "atualizado@exemplo.com", 5678);
            System.out.println("Atualizado: " + (atualizado != null ? atualizado.getNome() : "Erro na atualização"));
        }

        System.out.println("\n== Listando todos os usuários ==");
        List<Usuario> todos = controller.listarTodos();
        for (Usuario u : todos) {
            System.out.println("Usuário: " + u.getId() + " - " + u.getNome() + " - " + u.getEmail());
        }

        System.out.println("\n== Testando autenticação ==");
        Usuario autenticado = controller.autenticarUsuario("atualizado@exemplo.com", "5678");
        System.out.println("Autenticação: " + (autenticado != null ? "Sucesso" : "Falhou"));

        if (novo != null) {
            System.out.println("\n== Deletando usuário ==");
            boolean deletado = controller.deletarUsuario(novo.getId());
            System.out.println("Deletado: " + (deletado ? "Sim" : "Não"));
        }

        controller.fecharConexao();
    }
}

