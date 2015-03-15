/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minority.game.GUI;
import jade.core.Agent;
import minority.game.Engine;

/**
 *
 * @author antonio
 */
public class EngineGUI extends javax.swing.JFrame {

    /**
     * Creates new form EngineGUI
     * @param engine_agent
     */
    public EngineGUI(Agent engine_agent) {
        this.engine = engine_agent;
        initComponents();
    }

  
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        combo_n_player = new javax.swing.JComboBox();
        combo_n_giocate = new javax.swing.JComboBox();
        combo_bit = new javax.swing.JComboBox();
        combo_spg = new javax.swing.JComboBox();
        button_start = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel5.setText("#Giocatori");

        jLabel6.setText("#Giocate");

        jLabel7.setText("Bit Memoria");

        jLabel8.setText("SPG");
        jLabel8.setToolTipText("Strategie per giocatore");

        combo_n_player.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"3", "11", "51","71","91","101", "1001" }));

        combo_n_giocate.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "10", "100", "1000" }));

        combo_bit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "3", "6", "8", "10","16" }));

        combo_spg.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2","3","4" ,"5","6","7","8","9"}));

        button_start.setText("Start");
        button_start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_startActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(button_start, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(29, 29, 29)
                        .addComponent(combo_n_player, 0, 137, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(combo_spg, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(combo_n_giocate, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(combo_bit, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(combo_n_player, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(combo_n_giocate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(combo_bit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(combo_spg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(button_start)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        engine.doDelete();
    }//GEN-LAST:event_formWindowClosing

    private void button_startActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_startActionPerformed
        int n_giocatori = Integer.parseInt((String)combo_n_player.getSelectedItem());
        int n_giocate = Integer.parseInt((String)combo_n_giocate.getSelectedItem());
        int n_bit = Integer.parseInt((String)combo_bit.getSelectedItem());
        int spg = Integer.parseInt((String)combo_spg.getSelectedItem());
        ((Engine)engine).startGame(n_giocatori, n_bit, spg, n_giocate);
    }//GEN-LAST:event_button_startActionPerformed

   
    
    private Agent engine;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_start;
    private javax.swing.JComboBox combo_bit;
    private javax.swing.JComboBox combo_n_giocate;
    private javax.swing.JComboBox combo_n_player;
    private javax.swing.JComboBox combo_spg;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}