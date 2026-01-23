/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

/**
 *
 * @author Disath Damsutha
 */
public class SplashScreen extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(SplashScreen.class.getName());

    /**
     * Creates new form SplashScreen
     */
    public SplashScreen() {
        initComponents();
        setLocationRelativeTo(null);
        load();

    }


    public void load() {
    new Thread(() -> {
        for (int i = 0; i <= 100; i++) {
            try {
                Thread.sleep(10);
                ProgressBar.setValue(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        
       new LoginFrame().setVisible(true);
     this.dispose();
    }).start();
}

    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ProgressBar = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Foodcity Grocery System");
        setUndecorated(true);
        setResizable(false);
        setSize(new java.awt.Dimension(500, 300));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ProgressBar.setBackground(new java.awt.Color(147, 202, 55));
        ProgressBar.setForeground(new java.awt.Color(147, 202, 55));
        ProgressBar.setBorder(null);
        ProgressBar.setStringPainted(true);
        getContentPane().add(ProgressBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, 460, 10));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/SplashScreen.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new SplashScreen().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar ProgressBar;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
