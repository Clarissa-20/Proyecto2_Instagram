/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InstaGui;

import Logica.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * S
 *
 * @author HP
 */
public class PanelInbox extends JPanel {

    private vtnInstaPrincipal vtnP;
    private JPanel contenedorLista;

    public PanelInbox(vtnInstaPrincipal vtnP) {
        this.vtnP = vtnP;
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel titulo = new JLabel("  Mensajes", JLabel.LEFT);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        titulo.setPreferredSize(new Dimension(390, 60));
        add(titulo, BorderLayout.NORTH);

        contenedorLista = new JPanel();
        contenedorLista.setLayout(new BoxLayout(contenedorLista, BoxLayout.Y_AXIS));
        contenedorLista.setBackground(Color.BLACK);

        JScrollPane scroll = new JScrollPane(contenedorLista);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        cargarListaDeChats();
    }

    public void cargarListaDeChats() {
        contenedorLista.removeAll();

        try {
            String nombreActual = vtnP.getUsuarioActual().getUsuario();
            ArrayList<Usuario> todosLosUsuarios = ManejoArchivosBinarios.leerTodosLosUsuarios();

            Usuario miUsuarioActualizado = null;
            for (Usuario u : todosLosUsuarios) {
                if (u.getUsuario().equalsIgnoreCase(nombreActual)) {
                    miUsuarioActualizado = u;
                    break;
                }
            }

            if (miUsuarioActualizado == null) {
                return;
            }

            List<String> seguidos = miUsuarioActualizado.getSiguiendo();
            System.out.println("DEBUG: Seguidos encontrados para: " + nombreActual + ": " + seguidos.size());

            for (String usernameContacto : seguidos) {
                for (Usuario contacto : todosLosUsuarios) {
                    if (contacto.getUsuario().equalsIgnoreCase(usernameContacto)) {
                        agregarFilaUsuario(usernameContacto);
                        System.out.println("DEBUG: Agregado a la lista visual: " + usernameContacto);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error crítico al leer datos de seguidos");
        }

        contenedorLista.revalidate();
        contenedorLista.repaint();
    }

    private void agregarFilaUsuario(String username) {
        JPanel fila = new JPanel(new BorderLayout());
        fila.setBackground(Color.BLACK);
        fila.setMaximumSize(new Dimension(390, 70));
        fila.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel foto = new JLabel();

        try {
            Usuario user = GestorInsta.buscarUsuarioPorUsername(username);

            if (user != null && user.getRutaFotoPerfil() != null) {
                ImageIcon icon = new ImageIcon(user.getRutaFotoPerfil());
                Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                foto.setIcon(new ImageIcon(img));
            }
        } catch (IOException e) {
            System.out.println("No se pudo cargar la foto de: " + username);
        }

        JLabel nombre = new JLabel("  " + username);
        nombre.setForeground(Color.WHITE);
        nombre.setFont(new Font("Arial", Font.PLAIN, 16));

        fila.add(foto, BorderLayout.WEST);
        fila.add(nombre, BorderLayout.CENTER);

        fila.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                vtnP.abrirChatCon(username);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                fila.setBackground(new Color(30, 30, 30));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                fila.setBackground(Color.BLACK);
            }
        });

        contenedorLista.add(fila);
    }
}
