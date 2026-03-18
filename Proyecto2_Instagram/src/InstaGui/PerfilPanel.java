/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InstaGui;

import Logica.DatosPerfil;
import Logica.GestorInsta;
import Logica.Insta;
import Logica.PerfilNoEncontrado;
import Logica.SesionManager;
import Logica.Usuario;
import Logica.Comentario;
import Logica.Notificacion;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import java.text.SimpleDateFormat;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author HP
 */
public class PerfilPanel extends JPanel {

    private final Color COLOR_FONDO = new Color(18, 18, 18);
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color COLOR_SECUNDARIO_TEXTO = new Color(150, 150, 150);
    private final Color COLOR_BOTON_DOMINANTE = new Color(193, 53, 132);
    private final Color COLOR_BOTON_FONDO = new Color(38, 38, 38);
    private final Color COLOR_BORDE_POST = new Color(50, 50, 50);
    private final Color COLOR_UNFOLLOW = new Color(80, 80, 80);

    private String usernamePerfil;
    private JLabel username, nombre, edad, genero, fecha, bio;
    private JButton btnAccion;
    private JPanel panelPosts;
    private vtnInstaPrincipal vtnP;

    public PerfilPanel(String usernamePerfil, vtnInstaPrincipal vtnP) {
        this.usernamePerfil = usernamePerfil;
        this.vtnP = vtnP;

        setLayout(new BorderLayout());
        setBackground(COLOR_FONDO);

        try {
            cargarDatosYRenderizar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Fallo al crear PerfilPanel: " + ex.getMessage(), "Error Crítico de Carga", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private String toHex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }

    private boolean puedeVerContenido(Usuario dueñoPerfil) {
        if (dueñoPerfil.getUsuario().equalsIgnoreCase(SesionManager.getUsuarioActual().getUsuario())) {
            return true;
        }
        if (dueñoPerfil.isEsPublico()) {
            return true;
        }
        try {
            return GestorInsta.estaSiguiendo(SesionManager.getUsuarioActual().getUsuario(), dueñoPerfil.getUsuario());
        } catch (IOException e) {
            return false;
        }
    }

    public void cargarDatosYRenderizar() {
        removeAll();
        setLayout(new BorderLayout());
        setBackground(COLOR_FONDO);

        try {
            String usuarioLogueado = SesionManager.getUsuarioActual().getUsuario();

            if (usernamePerfil.equalsIgnoreCase(usuarioLogueado)) {
                Usuario refrescado = GestorInsta.buscarUsuarioPorUsername(usuarioLogueado);
                SesionManager.setUsuarioActual(refrescado);
            }
            DatosPerfil datos = GestorInsta.obtenerPefilCompleto(usernamePerfil, usuarioLogueado);

            Usuario usuarioObjetivo = datos.getDatosGenerales();

            add(crearEncabezadoPerfil(datos, usuarioLogueado), BorderLayout.NORTH);

            boolean esMiPropioPerfil = usernamePerfil.equalsIgnoreCase(usuarioLogueado);
            boolean esPublico = usuarioObjetivo.isEsPublico();
            boolean loSigo = datos.getloSigueElUsuarioActual();

            if (esMiPropioPerfil || esPublico || loSigo) {
                add(crearListaPublicaciones(datos.getInstasPropios()), BorderLayout.CENTER);
            } else {
                add(crearPanelCuentaPrivada(), BorderLayout.CENTER);
            }

        } catch (PerfilNoEncontrado e) {
            mostrarErrorVisual("Error: Perfil no encontrado", e.getMessage());
        } catch (IOException e) {
            mostrarErrorVisual("Error de Archivos", "No se pudieron cargar los datos del perfil");
        } catch (Exception e) {
            mostrarErrorVisual("Error Inesperado", e.getMessage());
        }

        revalidate();
        repaint();
    }

    private JPanel crearPanelCuentaPrivada() {
        JPanel panelPrivado = new JPanel(new GridBagLayout());
        panelPrivado.setBackground(COLOR_FONDO);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblCandado = new JLabel("🔒");
        lblCandado.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        lblCandado.setForeground(COLOR_TEXTO);

        JLabel lblTitulo = new JLabel("Esta cuenta es privada");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(COLOR_TEXTO);

        JLabel lblDesc = new JLabel("Síguela para ver sus fotos.");
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDesc.setForeground(COLOR_SECUNDARIO_TEXTO);

        gbc.gridy = 0;
        panelPrivado.add(lblCandado, gbc);
        gbc.gridy = 1;
        panelPrivado.add(lblTitulo, gbc);
        gbc.gridy = 2;
        panelPrivado.add(lblDesc, gbc);

        return panelPrivado;
    }

    private void mostrarErrorVisual(String titulo, String mensaje) {
        JLabel errorLabel = new JLabel("<html><center><h1 style='color:white;'>" + titulo + "</h1>"
                + "<p style='color:gray;'>" + mensaje + "</p></center></html>", SwingConstants.CENTER);
        add(errorLabel, BorderLayout.CENTER);
    }

    private JPanel crearEncabezadoPerfil(DatosPerfil datos, String usuarioLogueado) {

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        panelPrincipal.setBackground(COLOR_FONDO);

        JPanel panelInfoSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelInfoSuperior.setBackground(COLOR_FONDO);

        JLabel labelFotoPerfil = new JLabel();
        labelFotoPerfil.setPreferredSize(new Dimension(100, 100));
        labelFotoPerfil.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_POST, 1));
        labelFotoPerfil.setHorizontalAlignment(SwingConstants.CENTER);
        labelFotoPerfil.setVerticalAlignment(SwingConstants.CENTER);
        labelFotoPerfil.setForeground(COLOR_SECUNDARIO_TEXTO);

        String rutaFoto = datos.getDatosGenerales().getRutaFotoPerfil();
        System.out.println("DEBUG: Intentando cargar foto para @" + datos.getDatosGenerales().getUsuario());
        System.out.println("DEBUG: La ruta recuperada es: [" + rutaFoto + "]");
        labelFotoPerfil.setBorder(null);

        if (rutaFoto != null && !rutaFoto.isEmpty() && new java.io.File(rutaFoto).exists()) {
            try {
                ImageIcon icono = new ImageIcon(rutaFoto);
                Image img = icono.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                labelFotoPerfil.setIcon(new ImageIcon(img));
                labelFotoPerfil.setText(null);

            } catch (Exception ex) {
                labelFotoPerfil.setText("Error foto");
            }
        } else {
            labelFotoPerfil.setText("Sin foto");
            labelFotoPerfil.setBackground(COLOR_BOTON_FONDO);
            labelFotoPerfil.setOpaque(true);
        }
 
        panelInfoSuperior.add(labelFotoPerfil);

        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(new BoxLayout(panelDatos, BoxLayout.Y_AXIS));
        panelDatos.setBackground(COLOR_FONDO);

        username = new JLabel("@" + datos.getDatosGenerales().getUsuario());
        username.setFont(new Font("Arial", Font.BOLD, 16));
        username.setForeground(COLOR_TEXTO);

        nombre = new JLabel("Nombre: " + datos.getDatosGenerales().getNombre());
        nombre.setFont(new Font("Arial", Font.BOLD, 13));
        nombre.setForeground(COLOR_TEXTO);

        String textoBio = (datos.getDatosGenerales().getBio() != null ? datos.getDatosGenerales().getBio() : "Sin biografía");
        bio = new JLabel("<html><i>" + textoBio + "</i></html>");
        bio.setFont(new Font("Arial", Font.BOLD, 13));
        bio.setForeground(Color.LIGHT_GRAY);

        edad = new JLabel("Edad: " + datos.getDatosGenerales().getEdad());
        edad.setFont(new Font("Arial", Font.BOLD, 13));
        edad.setForeground(COLOR_TEXTO);

        genero = new JLabel("Genero: " + datos.getDatosGenerales().getGenero());
        genero.setFont(new Font("Arial", Font.BOLD, 13));
        genero.setForeground(COLOR_TEXTO);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechaTexto = sdf.format(datos.getDatosGenerales().getFechaIngreso());
        fecha = new JLabel("Desde: " + fechaTexto);
        fecha.setFont(new Font("Arial", Font.BOLD, 12));
        fecha.setForeground(COLOR_SECUNDARIO_TEXTO);

        username.setAlignmentX(Component.LEFT_ALIGNMENT);
        nombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        bio.setAlignmentX(Component.LEFT_ALIGNMENT);
        edad.setAlignmentX(Component.LEFT_ALIGNMENT);
        genero.setAlignmentX(Component.LEFT_ALIGNMENT);
        fecha.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelDatos.add(username);
        panelDatos.add(Box.createVerticalStrut(2));
        panelDatos.add(nombre);
        panelDatos.add(Box.createVerticalStrut(2));
        panelDatos.add(bio);
        panelDatos.add(Box.createVerticalStrut(5));
        panelDatos.add(edad);
        panelDatos.add(genero);
        panelDatos.add(fecha);

        panelInfoSuperior.add(panelDatos);

        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBackground(COLOR_FONDO);

        JSeparator separador = new JSeparator(SwingConstants.HORIZONTAL);
        separador.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        separador.setBackground(COLOR_FONDO);
        separador.setForeground(COLOR_BORDE_POST);
        panelCentral.add(separador);

        panelCentral.add(Box.createVerticalStrut(15));

        JPanel panelContadores = new JPanel(new GridLayout(1, 3, 20, 0));
        panelContadores.setBackground(COLOR_FONDO);
        panelContadores.add(crearContador("Posts", datos.getInstasPropios().size()));
        panelContadores.add(crearContador("Followers", datos.getTotalSeguidores()));
        panelContadores.add(crearContador("Following", datos.getTotalSeguidos()));
        panelContadores.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(panelContadores);

        panelCentral.add(Box.createVerticalStrut(15));

        JPanel panelBotonesAccion = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotonesAccion.setBackground(COLOR_FONDO);

        if (usernamePerfil.equalsIgnoreCase(usuarioLogueado)) {
            btnAccion = new JButton("EDITAR PERFIL");
            btnAccion.addActionListener(e -> mostrarOpcionesEdicion(datos.getDatosGenerales()));
            btnAccion.setBackground(COLOR_BOTON_FONDO);
            btnAccion.setForeground(COLOR_TEXTO);
            btnAccion.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_POST, 1));
            panelBotonesAccion.add(btnAccion);
        } else {
            if (datos.getloSigueElUsuarioActual()) {
                btnAccion = new JButton("SIGUIENDO");
                btnAccion.setBackground(COLOR_UNFOLLOW);
                btnAccion.setForeground(COLOR_TEXTO);
                btnAccion.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_POST, 1));
            } else {
                btnAccion = new JButton("SEGUIR");
                btnAccion.setBackground(COLOR_BOTON_DOMINANTE);
                btnAccion.setForeground(Color.WHITE);
                btnAccion.setFont(new Font("Arial", Font.BOLD, 12));
                btnAccion.setBorderPainted(false);
            }
            btnAccion.addActionListener(e -> manejarFollow(usuarioLogueado, usernamePerfil, datos.getloSigueElUsuarioActual()));
            btnAccion.setPreferredSize(new Dimension(140, 30));
            panelBotonesAccion.add(btnAccion);

            try {
                boolean elMeSigue = GestorInsta.estaSiguiendo(usernamePerfil, usuarioLogueado);
                if (datos.getloSigueElUsuarioActual() && elMeSigue) {
                    JButton btnMensaje = new JButton("MENSAJE");
                    btnMensaje.setBackground(COLOR_BOTON_FONDO);
                    btnMensaje.setForeground(COLOR_TEXTO);
                    btnMensaje.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_POST, 1));
                    btnMensaje.setPreferredSize(new Dimension(140, 30));
                    btnMensaje.setFocusPainted(false);
                    btnMensaje.addActionListener(e -> vtnP.abrirChatCon(usernamePerfil));
                    panelBotonesAccion.add(btnMensaje);
                }
            } catch (IOException ignored) {
            }
        }

        btnAccion.setFocusPainted(false);
        panelCentral.add(panelBotonesAccion);

        panelCentral.add(Box.createVerticalStrut(20));

        JSeparator separador2 = new JSeparator(SwingConstants.HORIZONTAL);
        separador2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        separador2.setBackground(COLOR_FONDO);
        separador2.setForeground(COLOR_BORDE_POST);
        panelCentral.add(separador2);

        panelPrincipal.add(panelInfoSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);

        JPanel contenedorFinal = new JPanel(new BorderLayout());
        contenedorFinal.add(panelPrincipal, BorderLayout.NORTH);
        contenedorFinal.setBackground(COLOR_FONDO);

        return contenedorFinal;
    }

    private JPanel crearContador(String titulo, int valor) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(COLOR_FONDO);

        JLabel labelValor = new JLabel(String.valueOf(valor));
        labelValor.setFont(new Font("Arial", Font.BOLD, 18));
        labelValor.setHorizontalAlignment(SwingConstants.CENTER);
        labelValor.setForeground(COLOR_TEXTO);

        JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        labelTitulo.setForeground(COLOR_TEXTO);

        p.add(labelValor, BorderLayout.CENTER);
        p.add(labelTitulo, BorderLayout.SOUTH);
        return p;
    }

    private JScrollPane crearListaPublicaciones(ArrayList<Insta> instas) {
        panelPosts = new JPanel();
        panelPosts.setLayout(new BoxLayout(panelPosts, BoxLayout.Y_AXIS));
        panelPosts.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPosts.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelPosts.setBackground(COLOR_FONDO);

        final int anchoPost = 500;

        if (instas.isEmpty()) {
            JLabel labelNoPosts = new JLabel("Aún no tiene posts publicados.");
            labelNoPosts.setForeground(COLOR_SECUNDARIO_TEXTO);
            labelNoPosts.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelPosts.add(labelNoPosts);
        } else {
            for (Insta post : instas) {
                JPanel panelPostCompleto = crearPanelPost(post);
                panelPostCompleto.setMaximumSize(new Dimension(anchoPost, Integer.MAX_VALUE));
                panelPostCompleto.setPreferredSize(new Dimension(anchoPost, panelPostCompleto.getPreferredSize().height));
                panelPostCompleto.setAlignmentX(Component.CENTER_ALIGNMENT);

                panelPosts.add(panelPostCompleto);
                panelPosts.add(Box.createVerticalStrut(20));
            }
        }

        JScrollPane scrollPane = new JScrollPane(panelPosts);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(COLOR_FONDO);
        return scrollPane;
    }

    private JPanel crearPanelPost(Insta post) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_POST, 1));
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(COLOR_FONDO);

        JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelHeader.setBackground(COLOR_FONDO);

        JLabel labelAutor = new JLabel("<html><b style='color:" + toHex(COLOR_TEXTO) + ";'>@" + post.getAutorUsername() + "</b></html>");
        panelHeader.add(labelAutor);

        JLabel labelFecha = new JLabel(" - " + post.getFechaPublicacion().toString());
        labelFecha.setForeground(COLOR_SECUNDARIO_TEXTO);
        panelHeader.add(labelFecha);

        panel.add(panelHeader, BorderLayout.NORTH);

        JPanel panelCuerpo = new JPanel();
        panelCuerpo.setLayout(new BoxLayout(panelCuerpo, BoxLayout.Y_AXIS));
        panelCuerpo.setBackground(COLOR_FONDO);

        JLabel labelImg = new JLabel();
        String rutaImg = post.getRutaImg();
        if (rutaImg != null && !rutaImg.isEmpty() && new File(rutaImg).exists()) {
            try {
                ImageIcon icono = new ImageIcon(rutaImg);
                Image img = icono.getImage();
                int anchoMaximo = 490;

                Image imagenEscalada = img.getScaledInstance(anchoMaximo, -1, Image.SCALE_SMOOTH);
                labelImg.setIcon(new ImageIcon(imagenEscalada));
                labelImg.setAlignmentX(Component.CENTER_ALIGNMENT);

            } catch (Exception e) {
                labelImg.setText("Error al cargar imagen");
                labelImg.setForeground(Color.RED);
            }
        } else {
            labelImg.setText("IMAGEN NO DISPONIBLE");
            labelImg.setForeground(COLOR_SECUNDARIO_TEXTO);
            labelImg.setBackground(COLOR_BOTON_FONDO);
            labelImg.setOpaque(true);
        }
        panelCuerpo.add(labelImg);
        panelCuerpo.add(Box.createVerticalStrut(5));

        if (post.getTexto() != null && !post.getTexto().trim().isEmpty()) {
            JTextArea areaTexto = new JTextArea(post.getTexto());
            areaTexto.setEditable(false);
            areaTexto.setLineWrap(true);
            areaTexto.setWrapStyleWord(true);

            areaTexto.setBackground(COLOR_FONDO);
            areaTexto.setForeground(COLOR_TEXTO);

            areaTexto.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            areaTexto.setAlignmentX(Component.CENTER_ALIGNMENT);
            areaTexto.setMaximumSize(new Dimension(480, areaTexto.getPreferredSize().height));
            panelCuerpo.add(areaTexto);
        }

        JPanel panelInteraccion = crearPanelInteraccion(post);

        panel.add(panelCuerpo, BorderLayout.CENTER);
        panel.add(panelInteraccion, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelInteraccion(Insta post) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        panel.setBackground(COLOR_FONDO);

        JTextArea areaComentarios = new JTextArea();
        areaComentarios.setEditable(false);
        areaComentarios.setWrapStyleWord(true);
        areaComentarios.setLineWrap(true);
        areaComentarios.setFont(new Font("Arial", Font.PLAIN, 10));

        areaComentarios.setBackground(COLOR_FONDO);
        areaComentarios.setForeground(COLOR_SECUNDARIO_TEXTO);

        JScrollPane scrollComentarios = new JScrollPane(areaComentarios);
        scrollComentarios.setMinimumSize(new Dimension(0, 70));
        scrollComentarios.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        scrollComentarios.setPreferredSize(new Dimension(490, 70));
        scrollComentarios.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_POST));
        scrollComentarios.getViewport().setBackground(COLOR_FONDO);

        JPanel panelAgregar = new JPanel(new BorderLayout(5, 5));
        panelAgregar.setBackground(COLOR_FONDO);

        JTextField txtComentario = new JTextField();
        JButton btnComentar = new JButton("Comentar");

        txtComentario.setBackground(COLOR_BOTON_FONDO);
        txtComentario.setForeground(COLOR_TEXTO);
        txtComentario.setCaretColor(COLOR_TEXTO);
        txtComentario.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_POST));

        btnComentar.setBackground(COLOR_FONDO);
        btnComentar.setForeground(COLOR_BOTON_DOMINANTE);
        btnComentar.setBorderPainted(false);

        btnComentar.addActionListener(e -> {
            agregarComentario(post, txtComentario.getText(), areaComentarios, txtComentario);
        });

        panelAgregar.add(txtComentario, BorderLayout.CENTER);
        panelAgregar.add(btnComentar, BorderLayout.EAST);

        panel.add(scrollComentarios, BorderLayout.NORTH);
        panel.add(panelAgregar, BorderLayout.SOUTH);

        cargarComentariosEnArea(post, areaComentarios);

        return panel;
    }

    private void cargarComentariosEnArea(Insta post, JTextArea area) {
        ArrayList<Comentario> comentarios = GestorInsta.leerComentarios(post);

        StringBuilder sb = new StringBuilder();
        if (comentarios.isEmpty()) {
            sb.append("Aún no hay comentarios.");
        } else {
            for (Comentario c : comentarios) {
                sb.append("@").append(c.getAutorUsername())
                        .append(": ").append(c.getTexto()).append("\n");
            }
        }
        area.setText(sb.toString());
    }

    private void agregarComentario(Insta post, String texto, JTextArea areaComentarios, JTextField txtComentario) {
        if (!texto.trim().isEmpty()) {
            try {
                String autor = SesionManager.getUsuarioActual().getUsuario(); 
                Comentario nuevoComentario = new Comentario(autor, texto);

                GestorInsta.guardarComentario(post, nuevoComentario);

                txtComentario.setText("");
                cargarComentariosEnArea(post, areaComentarios);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al guardar el comentario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "El comentario no puede estar vacío.");
        }
    }

    private JScrollPane crearGridPublicaciones(ArrayList<Insta> instas) {
        JPanel grid = new JPanel(new GridLayout(0, 3, 5, 5));
        grid.setBackground(COLOR_FONDO);

        for (Insta i : instas) {
            JPanel postPanel = new JPanel(new BorderLayout());
            postPanel.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_POST));
            postPanel.setBackground(COLOR_BOTON_FONDO);

            JLabel labelImg = new JLabel();
            labelImg.setPreferredSize(new Dimension(100, 200));
            labelImg.setHorizontalAlignment(SwingConstants.CENTER);
            labelImg.setForeground(COLOR_SECUNDARIO_TEXTO);

            String rutaImg = i.getRutaImg();
            if (rutaImg != null && !rutaImg.isEmpty() && new java.io.File(rutaImg).exists()) {
                try {
                    ImageIcon icono = new ImageIcon(rutaImg);
                    Image img = icono.getImage().getScaledInstance(100, 200, Image.SCALE_SMOOTH);
                    labelImg.setIcon(new ImageIcon(img));
                    labelImg.setText(null);
                } catch (Exception e) {
                    labelImg.setText("IMG ERROR");
                }
            } else {
                labelImg.setText("Sin Imagen");
            }

            postPanel.add(labelImg, BorderLayout.CENTER);

            JLabel labelContenido = new JLabel("<html><p style='width: 90px; color:" + toHex(COLOR_SECUNDARIO_TEXTO) + ";'>" + i.getTexto() + "</p></html>");
            postPanel.add(labelContenido, BorderLayout.SOUTH);

            grid.add(postPanel);
        }
        JScrollPane scrollPane = new JScrollPane(grid);
        scrollPane.getViewport().setBackground(COLOR_FONDO);
        return scrollPane;
    }

    private void mostrarOpcionesEdicion(Usuario usuario) {
        String[] opciones = {"Buscar Personas", "Desactivar/Activar Cuenta", "Agregar Foto de Perfil"};

        String estadoActual = usuario.isActivo() ? "(ACTIVA)" : "(DESACTIVA)";
        opciones[1] = opciones[1] + estadoActual;

        UIManager.put("OptionPane.background", COLOR_FONDO);
        UIManager.put("Panel.background", COLOR_FONDO);
        UIManager.put("OptionPane.messageForeground", COLOR_TEXTO);

        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione una opcion de Edicion:",
                "Editar Profile",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opciones,
                opciones[0]);

        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
        UIManager.put("OptionPane.messageForeground", null);

        if (seleccion != null) {
            if (seleccion.contains("Buscar Personas")) {
                System.out.println("abrir vtn de busqueda personas");
            } else if (seleccion.contains("Desactivar/Activar Cuenta")) {
                manejarDesactivacion(usuario);
            } else if (seleccion.contains("Agregar Foto de Perfil")) {
                System.out.println("abrir JFilechooser y llamar a guardarImagenEnSistema()");
            }
        }
    }

    private void manejarDesactivacion(Usuario usuario) {
        try {
            if (usuario.isActivo()) {
                int confirmacion = JOptionPane.showConfirmDialog(this,
                        "¿Estas seguro? Su cuenta no aparecera en busquedas.", "Confirmar Desactivacion", JOptionPane.YES_NO_OPTION);

                if (confirmacion == JOptionPane.YES_OPTION) {
                    GestorInsta.actualizarEstadoCuenta(usuario.getUsuario(), false);
                    SesionManager.cerrarSesion();
                    JOptionPane.showMessageDialog(this, "Cuenta desactivada. Volviendo al login.");

                    Window vtnActual = SwingUtilities.getWindowAncestor(this);
                    if (vtnActual != null) {
                        vtnActual.dispose();
                    }
                    new Login().setVisible(true);
                }
            } else {
                GestorInsta.actualizarEstadoCuenta(usuario.getUsuario(), true);
                JOptionPane.showMessageDialog(this, "Cuenta activada exitosamente.");
                cargarDatosYRenderizar();
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el estado de la cuenta: " + e.getMessage());
        }
    }

    private void manejarFollow(String seguidor, String seguido, boolean esSiguiendo) {
        try {
            Usuario usuarioObjetivo = GestorInsta.buscarUsuarioPorUsername(seguido);
            Usuario miUsuario = SesionManager.getUsuarioActual(); // El usuario que está usando la app

            if (esSiguiendo) {
                int confirmacion = JOptionPane.showConfirmDialog(this, "¿Desea dejar de seguir a " + seguido + "?", "Confirmar Unfollow", JOptionPane.YES_NO_OPTION);
                if (confirmacion == JOptionPane.YES_OPTION) {
                    GestorInsta.actualizarEstadoFollow(seguidor, seguido, false);

                    miUsuario.getSiguiendo().remove(seguido);
                    GestorInsta.actualizarUsuario(miUsuario);

                    JOptionPane.showMessageDialog(this, "Has dejado de seguir a " + seguido + ".");
                    cargarDatosYRenderizar();
                }
            } else {
                if (usuarioObjetivo.isEsPublico()) {
                    GestorInsta.actualizarEstadoFollow(seguidor, seguido, true);

                    if (!miUsuario.getSiguiendo().contains(seguido)) {
                        miUsuario.getSiguiendo().add(seguido);
                    }
                    GestorInsta.actualizarUsuario(miUsuario);

                    enviarSolicitudSeguimiento(seguidor, seguido, Notificacion.Tipo.SOLICITUD_ACEPTADA);

                    JOptionPane.showMessageDialog(this, "Ahora sigues a " + seguido + ".");
                    cargarDatosYRenderizar();
                } else {
                    enviarSolicitudSeguimiento(seguidor, seguido, Notificacion.Tipo.SOLICITUD_NUEVA);
                    JOptionPane.showMessageDialog(this, "Solicitud enviada.");

                    int resp = JOptionPane.showConfirmDialog(this,
                            "MODO PRUEBA: ¿Deseas que @" + seguido + " acepte la solicitud de @" + seguidor + " ahora?",
                            "Simular Aceptación", JOptionPane.YES_NO_OPTION);

                    if (resp == JOptionPane.YES_OPTION) {
                        ejecutarAceptacion(seguidor, seguido);
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al procesar el seguimiento: " + e.getMessage());
        }
    }

    private void enviarSolicitudSeguimiento(String emisor, String receptor, Notificacion.Tipo tipo) {
        try {
            if (vtnP.getSalidaSocket() != null) {
                Notificacion n = new Notificacion(emisor, receptor, tipo);
                vtnP.getSalidaSocket().writeObject(n);
                vtnP.getSalidaSocket().flush();
            }
        } catch (IOException e) {
            System.err.println("Error enviando notificación: " + e.getMessage());
        }
    }

    private void ejecutarAceptacion(String seguidor, String seguido) {
        try {
            GestorInsta.actualizarEstadoFollow(seguidor, seguido, true);

            enviarSolicitudSeguimiento(seguido, seguidor, Notificacion.Tipo.SOLICITUD_ACEPTADA);

            JOptionPane.showMessageDialog(this, "¡Has aceptado la solicitud de @" + seguidor + "!");

            cargarDatosYRenderizar();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al aceptar: " + ex.getMessage());
        }
    }

    
}
