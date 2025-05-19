package controller;

import DAO.UsuarioDAO;
import model.Usuario;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioController {
    protected final UsuarioDAO usuarioDAO;
    protected static final Logger LOGGER = Logger.getLogger(UsuarioController.class.getName());

    public UsuarioController() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public Usuario criarUsuario(int id, String email, int senha, String nome) {
        Usuario usuario = new Usuario(id, email, senha, nome);
        Usuario salvo = usuarioDAO.save(usuario);
        if (salvo == null) {
            LOGGER.warning("Usuário não pôde ser criado.");
        }
        return salvo;
    }

    public Usuario atualizarUsuario(int id, String nome, String email, int senha) {
        Usuario usuario = new Usuario(id, email, senha, nome);
        Usuario atualizado = usuarioDAO.update(usuario);
        if (atualizado == null) {
            LOGGER.warning("Usuário não pôde ser atualizado.");
        }
        return atualizado;
    }

    public boolean deletarUsuario(int id) {
        boolean sucesso = usuarioDAO.delete(id);
        if (!sucesso) {
            LOGGER.warning("Falha ao deletar usuário com ID: " + id);
        }
        return sucesso;
    }

    public Usuario buscarPorId(int id) {
        Usuario usuario = usuarioDAO.findById(id);
        if (usuario == null) {
            LOGGER.warning("Usuário com ID " + id + " não encontrado.");
        }
        return usuario;
    }

    public Usuario buscarPorEmail(String email) {
        Usuario usuario = usuarioDAO.findByEmail(email);
        if (usuario == null) {
            LOGGER.warning("Usuário com email " + email + " não encontrado.");
        }
        return usuario;
    }

    public List<Usuario> listarTodos() {
        return usuarioDAO.findAll();
        
    }

    public Usuario autenticarUsuario(String email, String senhaStr) {
        if (email == null || email.trim().isEmpty() || senhaStr == null || senhaStr.isEmpty()) {
            LOGGER.warning("Tentativa de email ou senha vazios.");
            return null;
        }
        if (!isValidEmail(email)) {
            LOGGER.warning("Tentativa de e-mail inválido: " + email);
            return null;
        }

        int senha;
        try {
            senha = Integer.parseInt(senhaStr);
        } catch (NumberFormatException e) {
            LOGGER.warning("Senha fornecida não é um número válido: " + senhaStr);
            return null; 
        }

       
        Usuario usuarioAutenticado = usuarioDAO.authenticate(email, senhaStr);

        if (usuarioAutenticado == null) {
            LOGGER.info("Falha na autenticação para o email: " + email + " (inválida ou usuário não encontrado).");
        } else {
            LOGGER.info("Usuário autenticado com sucesso: " + email);
        }
        return usuarioAutenticado;
    }

    private boolean isValidEmail(String email) {
        if (email == null) return false;
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    public void fecharConexao() {
        usuarioDAO.closeConnection();
    }
}