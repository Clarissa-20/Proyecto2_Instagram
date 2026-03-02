/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InstaGui;

/*import Compartidas.Usuario;
import Insta.GestorInsta;*/
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;

/**
 *
 * @author HP
 */
public class CrearCuenta extends JPanel {

    private MarcoMobile parent;
    private JTextField txtUser, txtNombre, txtEdad;
    private JTextArea txtBio;
    private JPasswordField txtPass;
    private JRadioButton rbMasculino, rbFemenino;
    private JLabel reqLongitud, reqMayus, reqMinus, reqNumero, reqEspecial;
    private JLabel lblFotoPerfil;
    private String rutaFotoSeleccionada = "";

    private final Color INSTA_ROSA = new Color(193, 53, 132);
    private final Color INSTA_AZUL = new Color(0, 149, 246);
    private final Color COLOR_ERROR = new Color(255, 80, 80);
    private final Color COLOR_EXITO = new Color(0, 255, 127);
    private final Color COLOR_CAMPO = new Color(38, 38, 38);

    public CrearCuenta(MarcoMobile parent) {
        this.parent = parent;
        this.setBackground(Color.BLACK);
        this.setLayout(new GridBagLayout());

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0); 
        gbc.gridx = 0;

        lblFotoPerfil = new JLabel();
        Dimension dimCuadro = new Dimension(180, 180);
        lblFotoPerfil.setPreferredSize(dimCuadro);
        lblFotoPerfil.setMinimumSize(dimCuadro);
        lblFotoPerfil.setMaximumSize(dimCuadro);
        lblFotoPerfil.setOpaque(true);
        lblFotoPerfil.setBackground(new Color(45, 45, 45));
        lblFotoPerfil.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 3));
        lblFotoPerfil.setHorizontalAlignment(SwingConstants.CENTER);
        lblFotoPerfil.setText("<html><body style='text-align: center; color: #888888;'>"
                + "<h1 style='margin: 0; font-size: 35px;'>+</h1>"
                + "<p style='margin: 0; font-size: 9px; font-weight: bold;'>AÑADIR FOTO</p>"
                + "</body></html>");

        JButton btnSeleccionarFoto = new JButton("Seleccionar foto de perfil");
        btnSeleccionarFoto.setForeground(INSTA_AZUL);
        btnSeleccionarFoto.setContentAreaFilled(false);
        btnSeleccionarFoto.setBorderPainted(false);
        btnSeleccionarFoto.setFont(new Font("Arial", Font.BOLD, 13));

        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        add(lblFotoPerfil, gbc);

        gbc.gridy = 1;
        add(btnSeleccionarFoto, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 2;
        add(estiloEtiqueta("Nombre completo"), gbc);
        txtNombre = new JTextField();
        estiloCampoTexto(txtNombre);
        gbc.gridy = 3;
        add(txtNombre, gbc);

        gbc.gridy = 4;
        add(estiloEtiqueta("Nombre de usuario"), gbc);
        txtUser = new JTextField();
        estiloCampoTexto(txtUser);
        gbc.gridy = 5;
        add(txtUser, gbc);

        JPanel panelFila = new JPanel(new GridLayout(1, 2, 15, 0));
        panelFila.setOpaque(false);
        JPanel pEdad = new JPanel(new BorderLayout());
        pEdad.setOpaque(false);
        pEdad.add(estiloEtiqueta("Edad"), BorderLayout.NORTH);
        txtEdad = new JTextField();
        estiloCampoTexto(txtEdad);
        pEdad.add(txtEdad, BorderLayout.CENTER);
        JPanel pGenero = new JPanel(new BorderLayout());
        pGenero.setOpaque(false);
        pGenero.add(estiloEtiqueta("Género"), BorderLayout.NORTH);
        JPanel rbPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        rbPanel.setOpaque(false);
        rbMasculino = new JRadioButton("M");
        rbFemenino = new JRadioButton("F");
        rbMasculino.setForeground(Color.WHITE);
        rbFemenino.setForeground(Color.WHITE);
        rbMasculino.setOpaque(false);
        rbFemenino.setOpaque(false);
        ButtonGroup group = new ButtonGroup();
        group.add(rbMasculino);
        group.add(rbFemenino);
        rbPanel.add(rbMasculino);
        rbPanel.add(rbFemenino);
        pGenero.add(rbPanel, BorderLayout.CENTER);
        panelFila.add(pEdad);
        panelFila.add(pGenero);
        gbc.gridy = 6;
        add(panelFila, gbc);

        gbc.gridy = 7;
        add(estiloEtiqueta("Biografía"), gbc);
        txtBio = new JTextArea(3, 20);
        txtBio.setLineWrap(true);
        txtBio.setWrapStyleWord(true);
        txtBio.setBackground(COLOR_CAMPO);
        txtBio.setForeground(Color.WHITE);
        txtBio.setCaretColor(Color.WHITE);
        txtBio.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollBio = new JScrollPane(txtBio);
        scrollBio.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
        scrollBio.setPreferredSize(new Dimension(320, 70)); 
        gbc.gridy = 8;
        add(scrollBio, gbc);

        gbc.gridy = 9;
        add(estiloEtiqueta("Contraseña"), gbc);
        txtPass = new JPasswordField();
        estiloCampoTexto(txtPass);
        gbc.gridy = 10;
        add(txtPass, gbc);

        JPanel panelCajita = new JPanel(new GridLayout(5, 1, 1, 1));
        panelCajita.setBackground(new Color(15, 15, 15));
        panelCajita.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        reqLongitud = estiloReq("- Mínimo 5 caracteres");
        reqMayus = estiloReq("- Una mayúscula");
        reqMinus = estiloReq("- Una minúscula");
        reqNumero = estiloReq("- Un número");
        reqEspecial = estiloReq("- Un carácter especial");
        panelCajita.add(reqLongitud);
        panelCajita.add(reqMayus);
        panelCajita.add(reqMinus);
        panelCajita.add(reqNumero);
        panelCajita.add(reqEspecial);
        gbc.gridy = 11;
        gbc.insets = new Insets(10, 0, 10, 0);
        add(panelCajita, gbc);

        JButton btnCrear = new JButton("Registrarte");
        btnCrear.setBackground(INSTA_ROSA);
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFont(new Font("Arial", Font.BOLD, 16));
        btnCrear.setPreferredSize(new Dimension(320, 48));
        btnCrear.setBorder(null);

        JButton btnVolver = new JButton("¿Ya tienes cuenta? Entrar");
        btnVolver.setForeground(INSTA_AZUL);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setFont(new Font("Arial", Font.BOLD, 14));

        gbc.gridy = 12;
        add(btnCrear, gbc);
        gbc.gridy = 13;
        add(btnVolver, gbc);

        btnSeleccionarFoto.addActionListener(e -> seleccionarImagen());
        txtPass.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                validar();
            }

            public void removeUpdate(DocumentEvent e) {
                validar();
            }

            public void changedUpdate(DocumentEvent e) {
                validar();
            }
        });
        btnVolver.addActionListener(e -> parent.mostrarVista("LOGIN"));
    }

    private void seleccionarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            rutaFotoSeleccionada = selectedFile.getAbsolutePath();
            ImageIcon icon = new ImageIcon(new ImageIcon(rutaFotoSeleccionada)
                    .getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH));
            lblFotoPerfil.setIcon(icon);
            lblFotoPerfil.setText("");
            lblFotoPerfil.setBorder(BorderFactory.createLineBorder(INSTA_ROSA, 3));
        }
    }

    private void estiloCampoTexto(JTextField campo) {
        campo.setBackground(COLOR_CAMPO);
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        campo.setFont(new Font("Arial", Font.PLAIN, 15));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60)),
                BorderFactory.createEmptyBorder(0, 12, 0, 12)
        ));
        campo.setPreferredSize(new Dimension(320, 40));
    }

    private JLabel estiloEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(new Color(168, 168, 168));
        etiqueta.setFont(new Font("Arial", Font.BOLD, 12));
        return etiqueta;
    }

    private JLabel estiloReq(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.PLAIN, 10));
        label.setForeground(COLOR_ERROR);
        return label;
    }

    private void validar() {
        String pass = new String(txtPass.getPassword());
        actualizarColor(reqLongitud, pass.length() >= 5);
        actualizarColor(reqMayus, pass.matches(".*[A-Z].*"));
        actualizarColor(reqMinus, pass.matches(".*[a-z].*"));
        actualizarColor(reqNumero, pass.matches(".*[0-9].*"));
        actualizarColor(reqEspecial, pass.matches(".*[!@#$%^&+=.].*"));
    }

    private void actualizarColor(JLabel label, boolean cumple) {
        label.setForeground(cumple ? COLOR_EXITO : COLOR_ERROR);
    }

}
