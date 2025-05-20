package controller;

import DAO.UsuarioDAO;
import model.Usuario;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller responsável por gerenciar as operações de Usuário,
 * incluindo cadastro, atualização, exclusão, autenticação e consulta.
 */
public class UsuarioController {
    protected final UsuarioDAO usuarioDAO;
    protected static final Logger LOGGER = Logger.getLogger(UsuarioController.class.getName());

    /**
     * Construtor padrão. Inicializa o DAO de Usuário.
     */
    public UsuarioController() {
        this.usuarioDAO = new UsuarioDAO();
    }

    /**
     * Cria um novo usuário no sistema.
     *
     * @param id    ID do usuário (geralmente 0 para auto-incremento).
     * @param email E-mail do usuário.
     * @param senha Senha numérica do usuário.
     * @param nome  Nome do usuário.
     * @return O usuário criado, ou null se falhar.
     */
    public Usuario criarUsuario(int id, String email, int senha, String nome) {
        Usuario usuario = new Usuario(id, email, senha, nome);
        Usuario salvo = usuarioDAO.save(usuario);
        if (salvo == null) {
            LOGGER.warning("Usuário não pôde ser criado.");
        }
        return salvo;
    }

    /**
     * Atualiza os dados de um usuário existente.
     *
     * @param id    ID do usuário.
     * @param nome  Novo nome.
     * @param email Novo e-mail.
     * @param senha Nova senha numérica.
     * @return O usuário atualizado, ou null se falhar.
     */
    public Usuario atualizarUsuario(int id, String nome, String email, int senha) {
        Usuario usuario = new Usuario(id, email, senha, nome);
        Usuario atualizado = usuarioDAO.update(usuario);
        if (atualizado == null) {
            LOGGER.warning("Usuário não pôde ser atualizado.");
        }
        return atualizado;
    }

    /**
     * Remove um usuário do sistema pelo ID.
     *
     * @param id ID do usuário a ser removido.
     * @return true se a operação for bem-sucedida, false caso contrário.
     */
    public boolean deletarUsuario(int id) {
        boolean sucesso = usuarioDAO.delete(id);
        if (!sucesso) {
            LOGGER.warning("Falha ao deletar usuário com ID: " + id);
        }
        return sucesso;
    }

    /**
     * Busca um usuário pelo seu ID.
     *
     * @param id ID do usuário.
     * @return Usuário encontrado ou null se não existir.
     */
    public Usuario buscarPorId(int id) {
        Usuario usuario = usuarioDAO.findById(id);
        if (usuario == null) {
            LOGGER.warning("Usuário com ID " + id + " não encontrado.");
        }
        return usuario;
    }

    /**
     * Busca um usuário pelo seu e-mail.
     *
     * @param email E-mail do usuário.
     * @return Usuário encontrado ou null se não existir.
     */
    public Usuario buscarPorEmail(String email) {
        Usuario usuario = usuarioDAO.findByEmail(email);
        if (usuario == null) {
            LOGGER.warning("Usuário com email " + email + " não encontrado.");
        }
        return usuario;
    }

    /**
     * Lista todos os usuários cadastrados.
     *
     * @return Lista de usuários.
     */
    public List<Usuario> listarTodos() {
        return usuarioDAO.findAll();
        
    }

    /**
     * Autentica um usuário usando e-mail e senha.
     *
     * @param email   E-mail do usuário.
     * @param senhaStr Senha do usuário (em formato String).
     * @return O usuário autenticado ou null se falhar.
     */
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

    /**
     * Verifica se o e-mail está em um formato válido.
     *
     * @param email E-mail a ser verificado.
     * @return true se o e-mail é válido, false caso contrário.
     */
    private boolean isValidEmail(String email) {
        if (email == null) return false;
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    /**
     * Fecha a conexão do DAO de usuário com o banco de dados.
     */
    public void fecharConexao() {
        usuarioDAO.closeConnection();
    }
}