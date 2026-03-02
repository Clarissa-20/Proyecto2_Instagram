/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InstaGui;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author HP
 */
public class Login extends JPanel{
    
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private MarcoMobile parent;
    
    private final Color COLOR_FONDO = new Color(18, 18, 18);
    private final Color COLOR_BOTON = new Color(193, 53, 132);
    
    public Login(MarcoMobile parent){
        this.parent = parent;
        this.setBackground(COLOR_FONDO);
        this.setLayout(new GridBagLayout());
        
        inicializarComponentes();
    }
    
    private void inicializarComponentes(){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        
        JLabel titulo = new JLabel("Instagram");
        titulo.setFont(new Font("Serif", Font.ITALIC | Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        gbc.gridy = 0;
        add(titulo, gbc);
        
        txtUsername = new JTextField("Username");
        txtUsername.setPreferredSize(new Dimension(280, 40));
        gbc.gridy = 1;
        add(txtUsername, gbc);
        
        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(280, 40));
        gbc.gridy = 2;
        add(txtPassword, gbc);
        
        JButton btnLogin = new JButton("Log In");
        btnLogin.setBackground(COLOR_BOTON);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setPreferredSize(new Dimension(280, 45));
        btnLogin.setFocusPainted(false);
        gbc.gridy = 3;
        add(btnLogin, gbc);
        
        JButton btnRegistro = new JButton("¿No tienes cuenta? Registrate");
        btnRegistro.setForeground(new Color(0, 149, 246));
        btnRegistro.setContentAreaFilled(false);
        btnRegistro.setBorderPainted(false);
        gbc.gridy = 4;
        add(btnRegistro, gbc);
        
        btnLogin.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Validando datos...");
        });
        
        btnRegistro.addActionListener(e -> {
            parent.mostrarVista("REGISTRO");
        });
    }
}
