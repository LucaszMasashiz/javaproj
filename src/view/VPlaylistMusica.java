package view;

import controller.ArtistaController;
import controller.MusicaController;
import controller.PlaylistMusicaController;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import manager.ManagerSession;
import model.Artista;
import model.Musica;
import model.Usuario;

/**
 * Tela responsável por exibir e gerenciar músicas de uma playlist específica.
 * Permite ao usuário listar, adicionar e remover músicas de uma playlist.
 * 
 * @author Masashi
 */
public class VPlaylistMusica extends javax.swing.JFrame {
    protected Usuario usuarioAutenticado;
    protected int usuarioId;
    protected int playlistId;
    protected PlaylistMusicaController playlistMusicaController = new PlaylistMusicaController();
    protected MusicaController musicaController = new MusicaController();
    protected ArtistaController artistaController = new ArtistaController(); 
    
    /**
     * Construtor da tela de músicas da playlist.
     * Inicializa os componentes, obtém o usuário autenticado e carrega as músicas da playlist.
     * 
     * @param playlistId ID da playlist cujas músicas serão gerenciadas.
     */
    public VPlaylistMusica(int playlistId) {
        initComponents();
        usuarioAutenticado = ManagerSession.getInstance().getCurrentUser();
        this.usuarioId = usuarioAutenticado != null ? usuarioAutenticado.getId() : -1;
        this.playlistId = playlistId;
        setTitle("Músicas da Playlist ID: " + playlistId);
        carregarMusicasDaPlaylist();
        getContentPane().setBackground(new java.awt.Color(70, 130, 180));
    }

    /**
     * Inicializa os componentes gráficos da tela.
     * Gerado automaticamente pelo editor visual.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaHome = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelaMusicas = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        excluirBotao = new javax.swing.JButton();
        voltarBotao = new javax.swing.JButton();
        escreveMusica = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        addMusica = new javax.swing.JButton();

        tabelaHome.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Artista", "Música", "Álbum", "Gênero", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tabelaHome);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel5.setFont(new java.awt.Font("Perpetua Titling MT", 3, 36)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("PLaylist");
        jLabel5.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        tabelaMusicas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Artista", "Música", "Álbum", "Gênero", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tabelaMusicas);

        jLabel1.setText("nome: ");

        excluirBotao.setText("Excluir");
        excluirBotao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                excluirBotaoActionPerformed(evt);
            }
        });

        voltarBotao.setText("Voltar");
        voltarBotao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                voltarBotaoActionPerformed(evt);
            }
        });

        jLabel2.setText("Adicionar música");

        addMusica.setText("adicionar");
        addMusica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addMusicaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(excluirBotao, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(47, 47, 47)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(escreveMusica, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(addMusica))
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(voltarBotao, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 522, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(199, 199, 199)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(49, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(221, 221, 221))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addGap(12, 12, 12)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(voltarBotao, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(excluirBotao, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(escreveMusica, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addMusica))))
                .addContainerGap(126, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Evento de ação para o botão "Voltar".
     * Retorna para a tela de listagem de playlists.
     * 
     * @param evt Evento de clique no botão "Voltar".
     */
    private void voltarBotaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_voltarBotaoActionPerformed
        VPlaylist tela = new VPlaylist();
            tela.setLocationRelativeTo(null); 
            tela.setVisible(true);
            this.dispose();
    }//GEN-LAST:event_voltarBotaoActionPerformed

    /**
     * Evento de ação para o botão "Excluir".
     * Remove a música selecionada da playlist, após confirmação do usuário.
     * 
     * @param evt Evento de clique no botão "Excluir".
     */
    private void excluirBotaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_excluirBotaoActionPerformed
        int selectedRow = tabelaMusicas.getSelectedRow();
            if (selectedRow == -1) {
                javax.swing.JOptionPane.showMessageDialog(this, "Selecione uma música para excluir.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            } 
//            sempre lembrar que a coluna da tabela começa contando do 0(0,1,2,3,4) o 4 = a quinta coluna tabela 
        int musicaId = (int) tabelaMusicas.getValueAt(selectedRow, 4);

        int confirm = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente remover a música da playlist?",
            "Confirmar remoção",
            javax.swing.JOptionPane.YES_NO_OPTION
        );
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                boolean sucesso = playlistMusicaController.removerMusica(playlistId, musicaId);
                if (sucesso) {
                    carregarMusicasDaPlaylist();
                    javax.swing.JOptionPane.showMessageDialog(this, "Música removida da playlist!");
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Erro ao remover música da playlist.", "Erro", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
    }//GEN-LAST:event_excluirBotaoActionPerformed

    /**
     * Evento de ação para o botão "Adicionar".
     * Adiciona a música digitada à playlist, caso encontrada.
     * 
     * @param evt Evento de clique no botão "Adicionar".
     */
    private void addMusicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addMusicaActionPerformed
        String nomeMusica = escreveMusica.getText().trim();

    if (nomeMusica.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(this, "Digite o nome da música para adicionar.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }
    List<Musica> musicas = musicaController.buscarPorNome(nomeMusica);
    Musica musica = (musicas != null && !musicas.isEmpty()) ? musicas.get(0) : null;
    
    if (musica == null) {
        javax.swing.JOptionPane.showMessageDialog(this, "Música não encontrada.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    boolean sucesso = playlistMusicaController.adicionarMusica(playlistId, musica.getId());
    if (sucesso) {
        carregarMusicasDaPlaylist(); 
        javax.swing.JOptionPane.showMessageDialog(this, "Música adicionada à playlist!");
        escreveMusica.setText(""); 
    } else {
        javax.swing.JOptionPane.showMessageDialog(this, "Erro ao adicionar música na playlist.", "Erro", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_addMusicaActionPerformed
    
    /**
     * Carrega e exibe as músicas da playlist atual na tabela.
     */
    private void carregarMusicasDaPlaylist() {
    List<Integer> musicasIds = playlistMusicaController.listarMusicasPorPlaylist(playlistId);
    DefaultTableModel model = (DefaultTableModel) tabelaMusicas.getModel();
    model.setRowCount(0);

    for (Integer musicaId : musicasIds) {
        Musica musica = musicaController.buscarPorId(musicaId);
        if (musica != null) {
        
            Artista artista = artistaController.buscarPorId(musica.getArtistaId());
            String nomeArtista = (artista != null) ? artista.getNomeArtistico() : "Desconhecido";

            model.addRow(new Object[] {
                nomeArtista,            
                musica.getNome(),
                musica.getAlbum(),
                musica.getGenero(),
                musica.getId()
            });
        }
    }
}
    
    /**
     * Método principal para iniciar a tela de músicas de uma playlist.
     * 
     * @param args Argumentos da linha de comando.
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
            java.util.logging.Logger.getLogger(VPlaylistMusica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VPlaylistMusica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VPlaylistMusica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VPlaylistMusica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VPlaylistMusica(1).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addMusica;
    private javax.swing.JTextField escreveMusica;
    private javax.swing.JButton excluirBotao;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tabelaHome;
    private javax.swing.JTable tabelaMusicas;
    private javax.swing.JButton voltarBotao;
    // End of variables declaration//GEN-END:variables
}
