/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InstaGUI;

/**
 *
 * @author HP
 */
import Logica.GestorInsta;
import Logica.SesionManager;
import Logica.UsernameYaExiste;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;
import java.io.IOException;
import Logica.Usuario;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CrearCuenta extends JFrame {

    /*private JTextField txtNombre, txtUsername, txtPassword, txtEdad;
    private JRadioButton rbMasculino, rbFemenino;
    private JLabel rutaFoto;
    private String rutaFotoSeleccionada = "";
    private String extensionFoto = "";

    private final Color COLOR_FONDO = new Color(18, 18, 18);
    private final Color COLOR_CAMPO_FONDO = new Color(38, 38, 38);
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color COLOR_BORDE_CAMPO = new Color(50, 50, 50);
    private final Color COLOR_BOTON_DOMINANTE = new Color(193, 53, 132);

    private final int ANCHO_FOTO = 150;
    private final int ALTO_FOTO = 150;
    private final Font FONT_ETIQUETA = new Font("Arial", Font.PLAIN, 14);
    private JLabel labelFotoPerfilDisplay;

    public vtnRegistro() {
        setTitle("INSTA - Crear Cuenta");
        setSize(600, 800);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
        inicializarComponentes();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void inicializarComponentes() {
        JPanel panelContenedorCentral = new JPanel();
        panelContenedorCentral.setLayout(new BoxLayout(panelContenedorCentral, BoxLayout.Y_AXIS));
        panelContenedorCentral.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));
        panelContenedorCentral.setBackground(COLOR_FONDO);

        labelFotoPerfilDisplay = new JLabel();
        labelFotoPerfilDisplay.setPreferredSize(new Dimension(ANCHO_FOTO, ALTO_FOTO));
        labelFotoPerfilDisplay.setMinimumSize(new Dimension(ANCHO_FOTO, ALTO_FOTO));
        labelFotoPerfilDisplay.setMaximumSize(new Dimension(ANCHO_FOTO, ALTO_FOTO));

        labelFotoPerfilDisplay.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_CAMPO, 1));
        labelFotoPerfilDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        labelFotoPerfilDisplay.setText(null);
        labelFotoPerfilDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnSeleccionarFoto = new JButton("Seleccionar imagen");
        btnSeleccionarFoto.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSeleccionarFoto.addActionListener(e -> seleccionarFoto());

        btnSeleccionarFoto.setBackground(COLOR_FONDO);
        btnSeleccionarFoto.setForeground(COLOR_BOTON_DOMINANTE); 
        btnSeleccionarFoto.setBorderPainted(false);
        btnSeleccionarFoto.setFocusPainted(false);

        btnSeleccionarFoto.setPreferredSize(new Dimension(180, 30));
        btnSeleccionarFoto.setMaximumSize(new Dimension(180, 30));

        panelContenedorCentral.add(labelFotoPerfilDisplay);
        panelContenedorCentral.add(Box.createVerticalStrut(10));
        panelContenedorCentral.add(btnSeleccionarFoto);
        panelContenedorCentral.add(Box.createVerticalStrut(30));

        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelFormulario.setBackground(COLOR_FONDO);

        panelFormulario.add(crearFilaFormulario("Nombre", txtNombre = new JTextField()));
        panelFormulario.add(Box.createVerticalStrut(20));

        panelFormulario.add(crearFilaFormulario("Username", txtUsername = new JTextField()));
        panelFormulario.add(Box.createVerticalStrut(20));

        panelFormulario.add(crearFilaFormulario("Password", txtPassword = new JPasswordField()));
        panelFormulario.add(Box.createVerticalStrut(20));

        panelFormulario.add(crearFilaFormulario("Edad", txtEdad = new JTextField()));
        panelFormulario.add(Box.createVerticalStrut(20));

        JPanel panelGeneroHorizontal = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelGeneroHorizontal.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelGeneroHorizontal.setBackground(COLOR_FONDO);

        JLabel labelGenero = new JLabel("Genero (M/F)");
        labelGenero.setFont(FONT_ETIQUETA);
        labelGenero.setForeground(COLOR_TEXTO); 

        rbMasculino = new JRadioButton("M");
        rbFemenino = new JRadioButton("F");
        rbMasculino.setFont(FONT_ETIQUETA);
        rbFemenino.setFont(FONT_ETIQUETA);

        rbMasculino.setBackground(COLOR_FONDO);
        rbMasculino.setForeground(COLOR_TEXTO);
        rbFemenino.setBackground(COLOR_FONDO);
        rbFemenino.setForeground(COLOR_TEXTO);

        ButtonGroup grupoGenero = new ButtonGroup();
        grupoGenero.add(rbMasculino);
        grupoGenero.add(rbFemenino);

        panelGeneroHorizontal.add(labelGenero);
        panelGeneroHorizontal.add(Box.createHorizontalStrut(50));
        panelGeneroHorizontal.add(rbMasculino);
        panelGeneroHorizontal.add(rbFemenino);

        panelGeneroHorizontal.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelGeneroHorizontal.getPreferredSize().height));
        panelFormulario.add(panelGeneroHorizontal);

        panelContenedorCentral.add(panelFormulario);
        panelContenedorCentral.add(Box.createVerticalStrut(40));

        JButton btnRegistrar = new JButton("REGISTRAR CUENTA");
        btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegistrar.addActionListener(e -> registrarUsuario());

        btnRegistrar.setBackground(COLOR_BOTON_DOMINANTE);
        btnRegistrar.setForeground(Color.WHITE); 
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegistrar.setBorderPainted(false); 
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setOpaque(true); 
        btnRegistrar.setPreferredSize(new Dimension(250, 40));
        btnRegistrar.setMaximumSize(new Dimension(250, 40));

        panelContenedorCentral.add(btnRegistrar);

        add(panelContenedorCentral, BorderLayout.CENTER);
    }

    private JPanel crearFilaFormulario(String labelText, JTextField textField) {
        JPanel panelFila = new JPanel();
        panelFila.setLayout(new BoxLayout(panelFila, BoxLayout.Y_AXIS));
        panelFila.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelFila.setBackground(COLOR_FONDO);

        JLabel label = new JLabel(labelText);
        label.setFont(FONT_ETIQUETA);
        label.setForeground(COLOR_TEXTO);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelFila.add(label);
        panelFila.add(Box.createVerticalStrut(5));

        textField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 35));
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        textField.setBackground(COLOR_CAMPO_FONDO);
        textField.setForeground(COLOR_TEXTO);
        textField.setCaretColor(COLOR_TEXTO);

        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE_CAMPO, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        textField.setFont(FONT_ETIQUETA);
        textField.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelFila.add(textField);

        panelFila.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelFila.getPreferredSize().height));

        return panelFila;
    }

    private void seleccionarFoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes (JPG, PNG, JPEG)", "jpg", "jpeg", "png"));
        int resultado = fileChooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            rutaFotoSeleccionada = archivo.getAbsolutePath();

            try {
                ImageIcon iconoOriginal = new ImageIcon(rutaFotoSeleccionada);
                Image imagen = iconoOriginal.getImage();

                Image imagenRedimensionada = imagen.getScaledInstance(
                        ANCHO_FOTO,
                        ALTO_FOTO,
                        Image.SCALE_SMOOTH);

                labelFotoPerfilDisplay.setIcon(new ImageIcon(imagenRedimensionada));
                labelFotoPerfilDisplay.setText(null);

                String nombreArchivoOriginal = archivo.getName();
                int indicePunto = nombreArchivoOriginal.lastIndexOf('.');
                if (indicePunto > 0) {
                    extensionFoto = nombreArchivoOriginal.substring(indicePunto);
                } else {
                    extensionFoto = "";
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "No se pudo cargar la imagen seleccionada: " + e.getMessage(), "Error de Imagen", JOptionPane.ERROR_MESSAGE);
                labelFotoPerfilDisplay.setIcon(null);
                labelFotoPerfilDisplay.setText(null);
                rutaFotoSeleccionada = "";
            }
        }
    }

    private void registrarUsuario() {

        String nombre = txtNombre.getText();
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String edad = txtEdad.getText();

        char genero = 'N';
        if (rbMasculino.isSelected()) {
            genero = 'M';
        } else if (rbFemenino.isSelected()) {
            genero = 'F';
        }
        
        String bio = "";

        if (nombre.isEmpty() || username.isEmpty() || password.isEmpty() || genero == 'N' || edad.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe llenar todos los campos.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String mensajeError = validarContrasena(password);

        if (mensajeError != null) {
            JOptionPane.showMessageDialog(this, mensajeError, "Contraseña Insegura", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int Edad = Integer.parseInt(edad);
            Usuario nuevoUsuario = new Usuario(
                    nombre, genero, username, password, Edad, rutaFotoSeleccionada, bio
            );
            if (!rutaFotoSeleccionada.isEmpty()) {
                String rutaPermanente = GestorInsta.copiarFotoPerfil(username, rutaFotoSeleccionada, extensionFoto);

                if (rutaPermanente != null) {
                    nuevoUsuario.setRutaFotoPerfil(rutaPermanente);
                }
            }

            GestorInsta.crearNuevaCuenta(nuevoUsuario);
            SesionManager.setUsuarioActual(nuevoUsuario);
            JOptionPane.showMessageDialog(this, "Usuario " + username + " creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La Edad debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (UsernameYaExiste e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de Username", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error de E/S al crear archivos o copar ft: " + e.getMessage(), "Error de Sistema", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String validarContrasena(String password) {
    StringBuilder errores = new StringBuilder();
    
    if (password.length() < 5) {
        errores.append("- Debe tener al menos 5 caracteres.\n");
    }
    
    if (!password.matches(".*[0-9].*")) {
        errores.append("- Debe incluir al menos un número (0-9).\n");
    }

    if (!password.matches(".*[A-Z].*")) {
        errores.append("- Debe incluir al menos una letra mayúscula.\n");
    }
    
    
    if (!password.matches(".*[^a-zA-Z0-9].*")) {
        errores.append("- Debe incluir al menos un caracter especial (ej: !@#$%).\n");
    }
    
    if (errores.length() > 0) {
        return "La contraseña no cumple con los siguientes requisitos:\n" + errores.toString();
    }
    
    return null;
}*/
    
    
    
    
    
    
    private JTextField txtNombre, txtUsername, txtEdad;
    private JPasswordField txtPassword;
    private JTextArea txtBioArea;
    private JRadioButton rbMasculino, rbFemenino;
    private JComboBox<String> tipoCuenta;
    private String rutaFotoSeleccionada = "";
    private String extensionFoto = "";
    private JLabel labelFotoPerfilDisplay;
    
    private JLabel reqLongitud, reqMayus, reqMinus, reqNumero, reqEspecial;

    private final Color COLOR_FONDO = Color.BLACK;
    private final Color COLOR_CAMPO_FONDO = new Color(30, 30, 30);
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color INSTA_ROSA = new Color(193, 53, 132);
    private final Color INSTA_AZUL = new Color(0, 149, 246);
    private final Color COLOR_ERROR = new Color(255, 80, 80);
    private final Color COLOR_EXITO = new Color(0, 255, 127);

    public CrearCuenta() {
        setTitle("Instagram - CrearCuenta");
        setSize(390, 844); 
        setResizable(false);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
        inicializarComponentes();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBackground(COLOR_FONDO);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 0, 2, 0);
        gbc.gridx = 0;

        labelFotoPerfilDisplay = new JLabel("<html><center><font size='6'>+</font><br><b>AÑADIR FOTO</b></center></html>");
        labelFotoPerfilDisplay.setPreferredSize(new Dimension(120, 120));
        labelFotoPerfilDisplay.setOpaque(true);
        labelFotoPerfilDisplay.setBackground(new Color(45, 45, 45));
        labelFotoPerfilDisplay.setForeground(new Color(140, 140, 140));
        labelFotoPerfilDisplay.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 2));
        labelFotoPerfilDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        panelPrincipal.add(labelFotoPerfilDisplay, gbc);

        JButton btnFoto = new JButton("Seleccionar foto de perfil");
        btnFoto.setForeground(INSTA_AZUL);
        btnFoto.setFont(new Font("Arial", Font.BOLD, 12));
        btnFoto.setContentAreaFilled(false);
        btnFoto.setBorderPainted(false);
        btnFoto.addActionListener(e -> seleccionarFoto()); 
        gbc.gridy = 1;
        panelPrincipal.add(btnFoto, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 2; panelPrincipal.add(crearEtiqueta("Nombre completo"), gbc);
        txtNombre = crearCampo(); gbc.gridy = 3; panelPrincipal.add(txtNombre, gbc);

        gbc.gridy = 4; panelPrincipal.add(crearEtiqueta("Nombre de usuario"), gbc);
        txtUsername = crearCampo(); gbc.gridy = 5; panelPrincipal.add(txtUsername, gbc);

        JPanel fila = new JPanel(new GridLayout(1, 2, 15, 0));
        fila.setOpaque(false);
        JPanel pE = new JPanel(new BorderLayout()); pE.setOpaque(false);
        pE.add(crearEtiqueta("Edad"), BorderLayout.NORTH);
        txtEdad = crearCampo(); pE.add(txtEdad, BorderLayout.CENTER);
        
        JPanel pG = new JPanel(new BorderLayout()); pG.setOpaque(false);
        pG.add(crearEtiqueta("Género"), BorderLayout.NORTH);
        JPanel rbP = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0)); rbP.setOpaque(false);
        rbMasculino = new JRadioButton("M"); rbFemenino = new JRadioButton("F");
        rbMasculino.setForeground(Color.WHITE); rbFemenino.setForeground(Color.WHITE);
        rbMasculino.setOpaque(false); rbFemenino.setOpaque(false);
        ButtonGroup bg = new ButtonGroup(); bg.add(rbMasculino); bg.add(rbFemenino);
        rbP.add(rbMasculino); rbP.add(rbFemenino);
        pG.add(rbP, BorderLayout.CENTER);
        fila.add(pE); fila.add(pG);
        gbc.gridy = 6; panelPrincipal.add(fila, gbc);

        gbc.gridy = 7; panelPrincipal.add(crearEtiqueta("Biografía"), gbc);
        txtBioArea = new JTextArea(2, 20);
        txtBioArea.setBackground(COLOR_CAMPO_FONDO); txtBioArea.setForeground(Color.WHITE);
        txtBioArea.setLineWrap(true);
        JScrollPane sp = new JScrollPane(txtBioArea); sp.setPreferredSize(new Dimension(0, 45));
        gbc.gridy = 8; panelPrincipal.add(sp, gbc);

        gbc.gridy = 9; panelPrincipal.add(crearEtiqueta("Contraseña"), gbc);
        txtPassword = new JPasswordField();
        ((JPasswordField)txtPassword).setEchoChar('•');
        estiloCampoPassword((JPasswordField)txtPassword);
        gbc.gridy = 10; panelPrincipal.add(txtPassword, gbc);
        
        gbc.gridy = 11; panelPrincipal.add(crearEtiqueta("Privacidad de la cuenta"), gbc);
        tipoCuenta = new JComboBox<>(new String[]{"Publica", "Privada"});
        tipoCuenta.setBackground(COLOR_CAMPO_FONDO);
        tipoCuenta.setForeground(COLOR_TEXTO);
        tipoCuenta.setPreferredSize(new Dimension(0, 30));
        gbc.gridy = 12; panelPrincipal.add(tipoCuenta, gbc);

        JPanel pReq = new JPanel(new GridLayout(5, 1));
        pReq.setBackground(new Color(15, 15, 15));
        pReq.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        reqLongitud = crearLabelReq("- Minimo 5 caracteres");
        reqMayus = crearLabelReq("- Una mayúscula");
        reqMinus = crearLabelReq("- Una minúscula");
        reqNumero = crearLabelReq("- Un número");
        reqEspecial = crearLabelReq("- Un carácter especial");
        pReq.add(reqLongitud); pReq.add(reqMayus); pReq.add(reqMinus); pReq.add(reqNumero); pReq.add(reqEspecial);
        gbc.gridy = 13; gbc.insets = new Insets(10, 0, 10, 0);
        panelPrincipal.add(pReq, gbc);

        JButton btnRegistrar = new JButton("Crear Cuenta");
        btnRegistrar.setBackground(INSTA_ROSA); btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegistrar.setPreferredSize(new Dimension(0, 42));
        btnRegistrar.setOpaque(true); btnRegistrar.setBorderPainted(false);
        btnRegistrar.addActionListener(e -> registrarUsuario()); 
        gbc.gridy = 14; gbc.insets = new Insets(2, 0, 2, 0);
        panelPrincipal.add(btnRegistrar, gbc);

        JButton btnEntrar = new JButton("¿Ya tienes cuenta? Entrar");
        btnEntrar.setForeground(INSTA_AZUL); btnEntrar.setContentAreaFilled(false);
        btnEntrar.setBorderPainted(false);
        btnEntrar.addActionListener(e -> this.dispose());
        gbc.gridy = 15;
        panelPrincipal.add(btnEntrar, gbc);

        ((JPasswordField)txtPassword).getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarVisual(); }
            public void removeUpdate(DocumentEvent e) { validarVisual(); }
            public void changedUpdate(DocumentEvent e) { validarVisual(); }
        });

        add(panelPrincipal, BorderLayout.CENTER);
    }

    private JLabel crearEtiqueta(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(new Color(160, 160, 160));
        l.setFont(new Font("Arial", Font.BOLD, 11));
        return l;
    }

    private JLabel crearLabelReq(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(COLOR_ERROR);
        l.setFont(new Font("Arial", Font.PLAIN, 10));
        return l;
    }

    private JTextField crearCampo() {
        JTextField f = new JTextField();
        f.setBackground(COLOR_CAMPO_FONDO); f.setForeground(COLOR_TEXTO);
        f.setCaretColor(COLOR_TEXTO); f.setPreferredSize(new Dimension(0, 30));
        f.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)));
        return f;
    }

    private void estiloCampoPassword(JPasswordField f) {
        f.setBackground(COLOR_CAMPO_FONDO); f.setForeground(COLOR_TEXTO);
        f.setCaretColor(COLOR_TEXTO); f.setPreferredSize(new Dimension(0, 30));
        f.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)));
    }

    private void validarVisual() {
        String p = new String(((JPasswordField)txtPassword).getPassword());
        reqLongitud.setForeground(p.length() >= 5 ? COLOR_EXITO : COLOR_ERROR);
        reqMayus.setForeground(p.matches(".*[A-Z].*") ? COLOR_EXITO : COLOR_ERROR);
        reqMinus.setForeground(p.matches(".*[a-z].*") ? COLOR_EXITO : COLOR_ERROR);
        reqNumero.setForeground(p.matches(".*[0-9].*") ? COLOR_EXITO : COLOR_ERROR);
        reqEspecial.setForeground(p.matches(".*[!@#$%^&*].*") ? COLOR_EXITO : COLOR_ERROR);
    }

    private void seleccionarFoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes (JPG, PNG, JPEG)", "jpg", "jpeg", "png"));
        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            rutaFotoSeleccionada = archivo.getAbsolutePath();
            try {
                ImageIcon iconoOriginal = new ImageIcon(rutaFotoSeleccionada);
                Image imagen = iconoOriginal.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                labelFotoPerfilDisplay.setIcon(new ImageIcon(imagen));
                labelFotoPerfilDisplay.setText(null);
                String nombreArchivoOriginal = archivo.getName();
                int indicePunto = nombreArchivoOriginal.lastIndexOf('.');
                extensionFoto = (indicePunto > 0) ? nombreArchivoOriginal.substring(indicePunto) : "";
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void registrarUsuario() {
        String nombre = txtNombre.getText();
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        String edad = txtEdad.getText();
        String bio = txtBioArea.getText();
        
        boolean esPublica = tipoCuenta.getSelectedItem().toString().equalsIgnoreCase("Publica");
        
        char genero = rbMasculino.isSelected() ? 'M' : (rbFemenino.isSelected() ? 'F' : 'N');

        if (nombre.isEmpty() || username.isEmpty() || password.isEmpty() || genero == 'N' || edad.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe llenar todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int Edad = Integer.parseInt(edad);
            Usuario nuevoUsuario = new Usuario(nombre, genero, username, password, Edad, rutaFotoSeleccionada, bio, esPublica);
            
            if (!rutaFotoSeleccionada.isEmpty()) {
                String rutaPermanente = GestorInsta.copiarFotoPerfil(username, rutaFotoSeleccionada, extensionFoto);
                if (rutaPermanente != null) nuevoUsuario.setRutaFotoPerfil(rutaPermanente);
            }

            GestorInsta.crearNuevaCuenta(nuevoUsuario);
            SesionManager.setUsuarioActual(nuevoUsuario);
            JOptionPane.showMessageDialog(this, "¡Cuenta creada!");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
