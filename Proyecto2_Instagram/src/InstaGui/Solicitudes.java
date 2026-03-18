/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InstaGui;

import Logica.GestorInsta;
import Logica.Notificacion;
import Logica.GestorSolicitudes;
import java.awt.*;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author HP
 */

public class Solicitudes extends JPanel {

    private final Color COLOR_FONDO = new Color(18, 18, 18);
    private final Color COLOR_TEXTO = Color.WHITE;
    private JPanel contenedorLista;
    private vtnInstaPrincipal vtnP;

    public Solicitudes(vtnInstaPrincipal vtnP) {
        this.vtnP = vtnP;
        setLayout(new BorderLayout());
        setBackground(COLOR_FONDO);

        JLabel titulo = new JLabel("Solicitudes de seguimiento", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(COLOR_TEXTO);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        contenedorLista = new JPanel();
        contenedorLista.setLayout(new BoxLayout(contenedorLista, BoxLayout.Y_AXIS));
        contenedorLista.setBackground(COLOR_FONDO);

        JScrollPane scroll = new JScrollPane(contenedorLista);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(COLOR_FONDO);
        add(scroll, BorderLayout.CENTER);

        actualizarLista();
    }

    public void actualizarLista() {
        contenedorLista.removeAll();

        String usuarioActual = vtnP.getUsuarioActual().getUsuario();
        ArrayList<Notificacion> pendientes = GestorSolicitudes.leerSolicitudesDisco(usuarioActual);

        if (pendientes.isEmpty()) {
            JLabel vacio = new JLabel("No tienes solicitudes pendientes.");
            vacio.setForeground(Color.GRAY);
            vacio.setAlignmentX(Component.CENTER_ALIGNMENT);
            contenedorLista.add(Box.createVerticalStrut(20));
            contenedorLista.add(vacio);
        } else {
            for (Notificacion n : pendientes) {
                contenedorLista.add(crearFilaSolicitud(n));
                contenedorLista.add(Box.createVerticalStrut(10));
            }
        }
        revalidate();
        repaint();
    }

    private JPanel crearFilaSolicitud(Notificacion n) {
        JPanel fila = new JPanel(new BorderLayout(15, 0));
        fila.setBackground(new Color(30, 30, 30));
        fila.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel lblInfo = new JLabel("@" + n.getEmisor() + " quiere seguirte.");
        lblInfo.setForeground(COLOR_TEXTO);
        fila.add(lblInfo, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setOpaque(false);

        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.setBackground(new Color(0, 149, 246));
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.addActionListener(e -> {
            aceptarSolicitud(n);
        });

        JButton btnRechazar = new JButton("Eliminar");
        btnRechazar.setBackground(new Color(54, 54, 54));
        btnRechazar.setForeground(Color.WHITE);

        btnRechazar.addActionListener(e -> {
            rechazarSolicitud(n);
        });

        panelBotones.add(btnAceptar);
        panelBotones.add(btnRechazar);
        fila.add(panelBotones, BorderLayout.EAST);

        return fila;
    }

    private void aceptarSolicitud(Notificacion n) {
        try {
            GestorInsta.actualizarEstadoFollow(n.getEmisor(), vtnP.getUsuarioActual().getUsuario(), true);

            GestorSolicitudes.borrarSolicitudDisco(vtnP.getUsuarioActual().getUsuario(), n.getEmisor());
            vtnP.getListaSolicitudesPendientes().removeIf(notif -> notif.getEmisor().equals(n.getEmisor()));

            Notificacion respuesta = new Notificacion(
                    vtnP.getUsuarioActual().getUsuario(), n.getEmisor(), Notificacion.Tipo.SOLICITUD_ACEPTADA
            );

            ObjectOutputStream salida = vtnP.getSalidaSocket();
            if (salida != null) {
                salida.writeObject(respuesta);
                salida.flush();
                System.out.println("LOG: Aviso de aceptación enviado a @" + n.getEmisor());
            }

            vtnP.actualizarIconoSolicitudes();
            actualizarLista();

            JOptionPane.showMessageDialog(this, "¡Ahora @" + n.getEmisor() + " puede ver tu perfil!");

        } catch (Exception e) {
            System.err.println("Error al aceptar solicitud: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void rechazarSolicitud(Notificacion n) {
        GestorSolicitudes.borrarSolicitudDisco(vtnP.getUsuarioActual().getUsuario(), n.getEmisor());
        vtnP.getListaSolicitudesPendientes().removeIf(notif -> notif.getEmisor().equals(n.getEmisor()));

        vtnP.actualizarIconoSolicitudes();
        actualizarLista();

        System.out.println("LOG: Solicitud de @" + n.getEmisor() + " rechazada y eliminada del disco.");
    }
}
