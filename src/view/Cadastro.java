package view;

import DAO.UsuarioDAO;
import java.sql.Connection;
import connection.ConnectionBD;
import java.lang.System.Logger;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import model.Usuario;

/**
 *
 * @author Masashi
 */
public class Cadastro extends javax.swing.JFrame {
     
    /**
     * Construtor padrão. Inicializa componentes e define cor de fundo.
     */
    public Cadastro() {
        initComponents();
        getContentPane().setBackground(new java.awt.Color(70, 130, 180));
    }
    
    

    /**
     * Método gerado pelo editor de GUI do NetBeans para configurar todos os componentes gráficos da tela.
     * <p>
     * <b>Não editar manualmente.</b>
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        nomeCadastro = new javax.swing.JTextField();
        botaoCadastro = new javax.swing.JToggleButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        emailCadastro = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        senhaCadastro = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Perpetua Titling MT", 3, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("spotifei");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        nomeCadastro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nomeCadastroActionPerformed(evt);
            }
        });

        botaoCadastro.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        botaoCadastro.setText("Cadastrar");
        botaoCadastro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoCadastroActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel2.setText("Nome:");

        jLabel3.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("E-mail:");

        jLabel4.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Senha:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(221, 221, 221))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(220, 220, 220))))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(137, 137, 137)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(emailCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                                .addComponent(nomeCadastro))
                            .addComponent(senhaCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(183, 183, 183)
                        .addComponent(botaoCadastro))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(217, 217, 217)
                        .addComponent(jLabel4)))
                .addContainerGap(139, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nomeCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(emailCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(senhaCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)
                .addComponent(botaoCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(58, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Evento acionado quando o usuário pressiona Enter no campo de nome.
     * 
     * @param evt Evento de ação do campo de nome.
     */
    private void nomeCadastroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nomeCadastroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nomeCadastroActionPerformed

    /**
     * Evento acionado ao clicar no botão "Cadastrar".
     * Valida os campos, tenta cadastrar o usuário no banco e exibe mensagens de sucesso ou erro.
     *
     * @param evt Evento do botão de cadastro.
     */
    private void botaoCadastroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoCadastroActionPerformed
        String nome = nomeCadastro.getText().trim();
        String email = emailCadastro.getText().trim();
        char[] senhaChars = senhaCadastro.getPassword();
        String senhaStr = new String(senhaChars);

        if (nome.isEmpty() || email.isEmpty() || senhaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            java.util.Arrays.fill(senhaChars, ' '); 
            return;
        }

        int senha;
        try {
            senha = Integer.parseInt(senhaStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Senha deve ser um número válido (temporariamente).", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            java.util.Arrays.fill(senhaChars, ' '); 
            return;
        }

        Usuario novoUsuario = new Usuario(nome, senha, email);
        Connection conn = null;
        try {
            conn = ConnectionBD.getInstance().getConnection();
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            Usuario usuarioSalvo = usuarioDAO.save(novoUsuario);

            if (usuarioSalvo != null) {
                JOptionPane.showMessageDialog(this,
                        "Usuário '" + usuarioSalvo.getNome() + "' cadastrado com sucesso!\nID: " + usuarioSalvo.getId(),
                        "Cadastro Realizado",
                        JOptionPane.INFORMATION_MESSAGE);
                nomeCadastro.setText("");
                emailCadastro.setText("");
                senhaCadastro.setText("");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Falha ao cadastrar usuário. Verifique o console para mais detalhes, se houver.",
                        "Erro no Cadastro",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            
            JOptionPane.showMessageDialog(this,
                    "Erro de banco de dados: " + e.getMessage(),
                    "Erro Crítico",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); 
        } catch (Exception e) {
           
            JOptionPane.showMessageDialog(this,
                    "Ocorreu um erro inesperado: " + e.getMessage(),
                    "Erro Inesperado",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            java.util.Arrays.fill(senhaChars, ' ');

            if (conn != null) {
                try {
                    conn.close();
                    
                } catch (SQLException e) {
                   
                    System.err.println("Erro ao fechar a conexão com o banco: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
 
    }//GEN-LAST:event_botaoCadastroActionPerformed

    /**
     * Método principal. Inicia a interface de cadastro usando o tema Nimbus se disponível.
     * 
     * @param args argumentos da linha de comando (não utilizados)
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Cadastro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Cadastro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Cadastro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Cadastro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Cadastro().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton botaoCadastro;
    private javax.swing.JTextField emailCadastro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField nomeCadastro;
    private javax.swing.JPasswordField senhaCadastro;
    // End of variables declaration//GEN-END:variables
}
