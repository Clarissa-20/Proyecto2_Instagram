/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InstaGui;

import Logica.GestorInsta;
import Logica.Usuario;
import Logica.CredencialesInvalidas;
import Logica.SesionManager;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author HP
 */

public class Login extends JFrame {
    
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    
    private final Color COLOR_FONDO = new Color(10, 10, 10); 
    private final Color COLOR_CAMPO_FONDO = Color.WHITE;     
    private final Color COLOR_TEXTO_INPUT = Color.BLACK;
    private final Color COLOR_LINK = new Color(0, 149, 246); 
    private final Color COLOR_BOTON = new Color(193, 53, 132); 
    
    private final Font FONT_LOGO = new Font("Serif", Font.BOLD + Font.ITALIC, 48);

    public Login() {
        setTitle("Instagram - Login");
        setSize(390, 844); 
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
        inicializarComponentes();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void inicializarComponentes() {
        JPanel panelContenedor = new JPanel(new GridBagLayout());
        panelContenedor.setBackground(COLOR_FONDO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 30, 10, 30); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel labelLogo = new JLabel("Instagram");
        labelLogo.setFont(FONT_LOGO);
        labelLogo.setForeground(Color.WHITE);
        labelLogo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 40, 0); 
        panelContenedor.add(labelLogo, gbc);

        txtUsername = crearCampoTexto("Username");
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 30, 15, 30);
        panelContenedor.add(txtUsername, gbc);

        txtPassword = crearCampoPassword("Password");
        gbc.gridy = 2;
        panelContenedor.add(txtPassword, gbc);

        JButton btnLogin = new JButton("Log In");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(COLOR_BOTON);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setOpaque(true);
        btnLogin.setPreferredSize(new Dimension(0, 45)); 
        
        btnLogin.addActionListener(e -> iniciarSesion());
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 30, 25, 30);
        panelContenedor.add(btnLogin, gbc);

        JLabel labelRegistro = new JLabel("¿No tienes cuenta? Regístrate");
        labelRegistro.setForeground(COLOR_LINK);
        labelRegistro.setFont(new Font("Arial", Font.BOLD, 13));
        labelRegistro.setHorizontalAlignment(SwingConstants.CENTER);
        labelRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        labelRegistro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                abrirRegistro();
            }
        });
        
        gbc.gridy = 4;
        panelContenedor.add(labelRegistro, gbc);

        add(panelContenedor, BorderLayout.CENTER);
    }

    private JTextField crearCampoTexto(String placeholder) {
        JTextField campo = new JTextField(placeholder);
        campo.setPreferredSize(new Dimension(0, 40));
        campo.setBackground(COLOR_CAMPO_FONDO);
        campo.setForeground(Color.GRAY);
        campo.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        campo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(COLOR_TEXTO_INPUT);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (campo.getText().isEmpty()) {
                    campo.setText(placeholder);
                    campo.setForeground(Color.GRAY);
                }
            }
        });
        return campo;
    }

    private JPasswordField crearCampoPassword(String placeholder) {
        JPasswordField campo = new JPasswordField();
        campo.setEchoChar((char) 0); 
        campo.setText(placeholder);
        campo.setPreferredSize(new Dimension(0, 40));
        campo.setBackground(COLOR_CAMPO_FONDO);
        campo.setForeground(Color.GRAY);
        campo.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        campo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (new String(campo.getPassword()).equals(placeholder)) {
                    campo.setText("");
                    campo.setEchoChar('•');
                    campo.setForeground(COLOR_TEXTO_INPUT);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (new String(campo.getPassword()).isEmpty()) {
                    campo.setEchoChar((char) 0);
                    campo.setText(placeholder);
                    campo.setForeground(Color.GRAY);
                }
            }
        });
        return campo;
    }
    
    private void iniciarSesion() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        
        if (username.equals("Nombre de usuario")) {
             username = "";
        }
        if (password.equals("Contraseña")) {
             password = "";
        }

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar usuario y contraseña", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Usuario usuarioLogueado = GestorInsta.logIn(username, password);

            SesionManager.setUsuarioActual(usuarioLogueado);

            JOptionPane.showMessageDialog(this, "¡Sesion iniciada como " + username + "!", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            vtnInstaPrincipal p = new vtnInstaPrincipal(usuarioLogueado);
            p.setVisible(true);
            this.dispose();

        } catch (CredencialesInvalidas e) {
            JOptionPane.showMessageDialog(this, "Credenciales inválidas: Usuario no encontrado o contraseña incorrecta.", "Error de Inicio de Sesión", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error de sistema al cargar usuarios: " + e.getMessage(), "Error del Sistema", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirRegistro() {
        CrearCuenta r = new CrearCuenta();
        r.setVisible(true);
    }
}
