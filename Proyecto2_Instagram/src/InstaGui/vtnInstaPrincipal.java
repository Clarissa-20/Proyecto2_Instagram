/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InstaGui;

import Logica.GestorChat;
import Logica.GestorInsta;
import Logica.Insta;
import Logica.Mensaje;
import Logica.Notificacion;
import Logica.Usuario;
import Logica.GestorSolicitudes;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author HP
 */

public class vtnInstaPrincipal extends JFrame implements PostListener {

    private final Color COLOR_FONDO = new Color(18, 18, 18);
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color COLOR_BARRA_NAVEGACION = new Color(30, 30, 30);
    private final Color COLOR_ICONO_INACTIVO = new Color(150, 150, 150);
    private final Font FONT_LOGO = new Font("Arial", Font.BOLD, 28);

    private final Usuario usuarioActual;
    private JPanel panelContenidoCentral;
    private CardLayout cardLayout;
    private TimeLine timeLine;
    private Buscar buscar;
    private vtnPerfil perfil;

    private ObjectOutputStream salidaSocket;
    private ObjectInputStream entradaSocket;
    private Socket socket;
    private PanelInbox panelMensajes;

    private ArrayList<Notificacion> listaSolicitudesPendientes = new ArrayList<>();
    private Solicitudes panelSolicitudes;
    private JButton btnSolis;

    public vtnInstaPrincipal(Usuario usuario) {
        this.usuarioActual = usuario;
        setTitle("INSTA - @" + usuario.getUsuario());
        setSize(390, 844);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(COLOR_FONDO);

        inicializarVistas();
        inicializarComponentes();

        this.listaSolicitudesPendientes = GestorSolicitudes.leerSolicitudesDisco(usuario.getUsuario());
        actualizarIconoSolicitudes();

        mostrarVista("Perfil");
        iniciarConexionChat();
    }

    private void inicializarVistas() {
        cardLayout = new CardLayout();
        panelContenidoCentral = new JPanel(cardLayout);
        panelContenidoCentral.setBackground(COLOR_FONDO);

        timeLine = new TimeLine(usuarioActual);
        buscar = new Buscar(usuarioActual, this);
        perfil = new vtnPerfil(usuarioActual, this);
        panelMensajes = new PanelInbox(this);

        try {
            Logica.DatosPerfil misDatos = Logica.GestorInsta.obtenerPefilCompleto(
                    usuarioActual.getUsuario(),
                    usuarioActual.getUsuario()
            );
            perfil.actualizarContenido(misDatos);
        } catch (Exception e) {
            System.err.println("Error al precargar datos del perfil: " + e.getMessage());
        }

        panelContenidoCentral.add(timeLine, "TimeLine");
        panelContenidoCentral.add(buscar, "Buscar");
        panelContenidoCentral.add(panelMensajes, "Inbox");

        JLabel labelPublicar = new JLabel("PUBLICAR", SwingConstants.CENTER);
        labelPublicar.setForeground(COLOR_TEXTO);
        labelPublicar.setBackground(COLOR_FONDO);
        panelContenidoCentral.add(labelPublicar, "Publicar");

        panelContenidoCentral.add(perfil, "Perfil");
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        add(crearBarraSuperior(), BorderLayout.NORTH);
        add(panelContenidoCentral, BorderLayout.CENTER);
        add(crearBarraNavegacionInferior(), BorderLayout.SOUTH);
    }

    private JPanel crearBarraSuperior() {
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_BARRA_NAVEGACION);
        panelHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(50, 50, 50)));

        JLabel labelLogo = new JLabel("Instagram");
        labelLogo.setFont(FONT_LOGO);
        labelLogo.setForeground(new Color(255, 100, 180));
        labelLogo.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 0));
        panelHeader.add(labelLogo, BorderLayout.WEST);

        JPanel panelIconos = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelIconos.setOpaque(false);

        JButton btnLikes = new JButton("❤️");
        styleTopButton(btnLikes);

        btnSolis = new JButton("👤+");
        styleTopButton(btnSolis);
        btnSolis.addActionListener(e -> {
            if (panelSolicitudes == null) {
                panelSolicitudes = new Solicitudes(this);
                panelContenidoCentral.add(panelSolicitudes, "Solicitudes");
            }
            panelSolicitudes.actualizarLista();
            mostrarVista("Solicitudes");
        });

        panelIconos.add(btnLikes);
        panelIconos.add(btnSolis);

        panelHeader.add(panelIconos, BorderLayout.EAST);

        return panelHeader;
    }

    private void styleTopButton(JButton btn) {
        btn.setFont(new Font("SansSerif", Font.PLAIN, 18));
        btn.setForeground(COLOR_TEXTO);
        btn.setBackground(COLOR_BARRA_NAVEGACION);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JPanel crearBarraNavegacionInferior() {
        JPanel panelBarraNavegacion = new JPanel(new GridLayout(1, 4));
        panelBarraNavegacion.setBackground(COLOR_BARRA_NAVEGACION);
        panelBarraNavegacion.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(50, 50, 50)));

        JButton btnTimeLine = crearBotonNavegacion("🏠", "TimeLine");
        btnTimeLine.addActionListener(e -> {
            timeLine.cargarTimeLine();
            mostrarVista("TimeLine");
        });

        JButton btnBuscar = crearBotonNavegacion("🔍", "Buscar");
        btnBuscar.addActionListener(e -> {
            mostrarVista("Buscar");
        });

        JButton btnPublicar = crearBotonNavegacion("➕", "Publicar");
        btnPublicar.addActionListener(e -> {
            vtnCrearInsta crearInsta = new vtnCrearInsta(this);
            crearInsta.setVisible(true);
        });

        JButton btnPerfil = crearBotonNavegacion("👤", "Perfil");
        btnPerfil.addActionListener(e -> {
            try {
                Logica.DatosPerfil misDatos = Logica.GestorInsta.obtenerPefilCompleto(
                        usuarioActual.getUsuario(),
                        usuarioActual.getUsuario()
                );

                perfil.actualizarContenido(misDatos);

                mostrarVista("Perfil");

            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(this, "Error al cargar el perfil: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        JButton btnInbox = crearBotonNavegacion("✉", "Inbox");
        btnInbox.addActionListener(e -> {
            panelMensajes.cargarListaDeChats();
            mostrarVista("Inbox");
        });

        panelBarraNavegacion.add(btnTimeLine);
        panelBarraNavegacion.add(btnBuscar);
        panelBarraNavegacion.add(btnPublicar);
        panelBarraNavegacion.add(btnInbox);
        panelBarraNavegacion.add(btnPerfil);

        return panelBarraNavegacion;
    }

    private JButton crearBotonNavegacion(String icono, String comando) {
        JButton btn = new JButton(icono);
        btn.setFont(new Font("SansSerif", Font.BOLD, 24));
        btn.setBackground(COLOR_BARRA_NAVEGACION);
        btn.setForeground(COLOR_ICONO_INACTIVO);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setActionCommand(comando);
        btn.setOpaque(true);
        return btn;
    }

    public void mostrarVista(String nombreVista) {
        cardLayout.show(panelContenidoCentral, nombreVista);
    }

    public void mostrarOtroPerfil(String username) {
        final String VISTA_PERFIL_OTRO = "PERFIL_AJENO_" + username.toUpperCase();
        try {
            vtnOtroPerfil panelOtroPerfil = new vtnOtroPerfil(username, this);
            panelContenidoCentral.add(panelOtroPerfil, VISTA_PERFIL_OTRO);
            cardLayout.show(panelContenidoCentral, VISTA_PERFIL_OTRO);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar el perfil: " + e.getMessage());
        }
        panelContenidoCentral.revalidate();
        panelContenidoCentral.repaint();
    }

    @Override
    public void postPublicadoExitosamente() {
        perfil.cargarDatosPerfil();
        mostrarVista("Perfil");
    }

    public void refrescarVistas() {
        SwingUtilities.invokeLater(() -> {
            perfil.cargarDatosPerfil();
            timeLine.cargarTimeLine();
            for (Component comp : panelContenidoCentral.getComponents()) {
                if (comp.isVisible() && comp instanceof vtnOtroPerfil) {
                    ((vtnOtroPerfil) comp).recargarPerfil();
                }
            }
            panelContenidoCentral.revalidate();
            panelContenidoCentral.repaint();
        });
    }

    private void iniciarConexionChat() {
        new Thread(() -> {
            try {
                socket = new Socket("localhost", 5000);
                salidaSocket = new ObjectOutputStream(socket.getOutputStream());
                salidaSocket.flush();
                salidaSocket.writeObject(usuarioActual.getUsuario());
                salidaSocket.flush();
                entradaSocket = new ObjectInputStream(socket.getInputStream());

                while (true) {
                    Object recibido = entradaSocket.readObject();

                    if (recibido instanceof Mensaje) {
                        recibirMensajeEnTiempoReal((Mensaje) recibido);

                    } else if (recibido instanceof Notificacion) {
                        Notificacion n = (Notificacion) recibido;

                        if (n.getTipo() == Notificacion.Tipo.SOLICITUD_NUEVA) {

                            GestorSolicitudes.guardarSolicitudDisco(n);
                            listaSolicitudesPendientes = GestorSolicitudes.leerSolicitudesDisco(usuarioActual.getUsuario());

                            SwingUtilities.invokeLater(() -> {
                                actualizarIconoSolicitudes();
                                if (panelSolicitudes != null) {
                                    panelSolicitudes.actualizarLista();
                                }
                            });

                        } else if (n.getTipo() == Notificacion.Tipo.SOLICITUD_ACEPTADA) {

                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(this,
                                        "<html><body style='padding: 5px;'>"
                                        + "<b style='color: #0095F6;'>@" + n.getEmisor() + "</b> aceptó tu solicitud.<br>"
                                        + "¡Ahora puedes ver su perfil y enviarle mensajes!</body></html>",
                                        "¡Nueva amistad!",
                                        JOptionPane.INFORMATION_MESSAGE);

                                refrescarVistas();

                                if (panelMensajes != null) {
                                    panelMensajes.cargarListaDeChats();
                                }
                            });

                            System.out.println("LOG: @" + n.getEmisor() + " te ha aceptado.");
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Desconectado del servidor de chat.");
            }
        }).start();
    }

    private void recibirMensajeEnTiempoReal(Mensaje msj) {
        try {
            GestorChat.guardarMensajeEnArchivo(usuarioActual.getUsuario(), msj.getEmisor(), msj);
            javax.swing.SwingUtilities.invokeLater(() -> {
                GestorChat.actualizarInterfazSiEsNecesario(msj);
            });
        } catch (IOException e) {
            System.err.println("Error al guardar mensaje.");
        }
    }

    public ObjectOutputStream getSalidaSocket() {
        return this.salidaSocket;
    }

    public Usuario getUsuarioActual() {
        return this.usuarioActual;
    }

    public void abrirChatCon(String usernameDestino) {
        PanelChat chat = new PanelChat(this.usuarioActual, usernameDestino, this.salidaSocket);
        panelContenidoCentral.add(chat, "CHAT_ACTIVO");
        cardLayout.show(panelContenidoCentral, "CHAT_ACTIVO");
        panelContenidoCentral.revalidate();
        panelContenidoCentral.repaint();
    }

    public ArrayList<Notificacion> getListaSolicitudesPendientes() {
        return this.listaSolicitudesPendientes;
    }

    public void actualizarIconoSolicitudes() {
        if (btnSolis != null) {
            int cantidad = listaSolicitudesPendientes.size();
            if (cantidad > 0) {
                btnSolis.setForeground(new Color(255, 50, 50));
                btnSolis.setText("👤+ (" + cantidad + ") ●");
            } else {
                btnSolis.setForeground(COLOR_TEXTO);
                btnSolis.setText("👤+");
            }
        }
    }

    public void cargarPerfilAjeno(Usuario usuario) {
        vtnPerfil vistaPerfilAjeno = new vtnPerfil(usuario, this);

        String nombreVista = "PERFIL_" + usuario.getUsuario().toUpperCase();
        panelContenidoCentral.add(vistaPerfilAjeno, nombreVista);

        cardLayout.show(panelContenidoCentral, nombreVista);

        panelContenidoCentral.revalidate();
        panelContenidoCentral.repaint();
    }
}
