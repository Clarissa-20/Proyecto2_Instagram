/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InstaGui;

import Logica.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 *
 * @author HP
 */
public class PanelChat extends JPanel {

    private String receptor;
    private Usuario usuarioLogueado;
    private JPanel contenedorMensajes;
    private JScrollPane scroll;
    private JTextField campoTexto;
    private ObjectOutputStream out;

    public PanelChat(Usuario actual, String receptor, ObjectOutputStream out) {
        this.usuarioLogueado = actual;
        this.receptor = receptor;
        this.out = out;

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        initHeader();
        initCuerpoChat();
        initInputArea();

        cargarHistorial();
        GestorChat.setPanelActivo(this);
    }

    private void initHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(new Color(20, 20, 20));
        JLabel nombre = new JLabel(" Chat con: @" + receptor);
        nombre.setFont(new Font("Arial", Font.BOLD, 16));
        header.add(nombre);
        add(header, BorderLayout.NORTH);
    }

    private void initCuerpoChat() {
        contenedorMensajes = new JPanel();
        contenedorMensajes.setLayout(new BoxLayout(contenedorMensajes, BoxLayout.Y_AXIS));
        contenedorMensajes.setBackground(Color.BLACK);

        scroll = new JScrollPane(contenedorMensajes);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private void initInputArea() {
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(new Color(30, 30, 30));

        campoTexto = new JTextField();
        JButton btnEnviar = new JButton("Enviar");
        JButton btnFoto = new JButton("📷");
        JButton btnStickers = new JButton("☺");

        btnEnviar.addActionListener(e -> enviarTexto());
        btnStickers.addActionListener(e -> abrirPanelStickers());

        btnFoto.addActionListener(e -> {
            JFileChooser selector = new JFileChooser();
            selector.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imagenes(JPG, PNG)", "jpg", "png", "jpeg"));

            int resultado = selector.showOpenDialog(this);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                File archivoElegido = selector.getSelectedFile();
                enviarFoto(archivoElegido.getAbsolutePath());
            }
        });

        JPanel botonesAccion = new JPanel(new GridLayout(1, 3));
        botonesAccion.add(btnStickers);
        botonesAccion.add(btnFoto);
        botonesAccion.add(btnEnviar);

        panelInferior.add(campoTexto, BorderLayout.CENTER);
        panelInferior.add(botonesAccion, BorderLayout.EAST);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void enviarTexto() {
        String texto = campoTexto.getText().trim();
        if (!texto.isEmpty()) {
            Mensaje nuevo = new Mensaje(usuarioLogueado.getUsuario(), receptor, texto);
            GestorChat.enviarMensaje(nuevo, out);
            agregarBurbuja(nuevo);
            campoTexto.setText("");
        }
    }

    private void enviarFoto(String rutaOriginal) {
        try {
            Mensaje msjFoto = new Mensaje(usuarioLogueado.getUsuario(), receptor, rutaOriginal);
            msjFoto.setEsImagen(true);
            GestorChat.enviarMensaje(msjFoto, out);
            agregarBurbuja(msjFoto);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al enviar la imagen");
        }
    }
    
    public void agregarBurbuja(Mensaje msj) {
        boolean esMio = msj.getEmisor().equals(usuarioLogueado.getUsuario());
        JPanel fila = new JPanel(new FlowLayout(esMio ? FlowLayout.RIGHT : FlowLayout.LEFT));
        fila.setOpaque(false);

        JLabel burbuja = new JLabel(); 
        burbuja.setOpaque(true);
        burbuja.setBackground(esMio ? new Color(55, 151, 240) : new Color(38, 38, 38));
        burbuja.setForeground(Color.WHITE);
        burbuja.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (msj.isEsImagen()) {
            ImageIcon original = new ImageIcon(msj.getContenido());
            Image imgEscalada = original.getImage().getScaledInstance(180, -1, Image.SCALE_SMOOTH);
            burbuja.setIcon(new ImageIcon(imgEscalada));
            burbuja.setText(""); 
        } 
        else if (msj.isEsSticker()) {
            try {
                ImageIcon stickerIcon = new ImageIcon(new java.net.URL(msj.getContenido()));
                Image img = stickerIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                burbuja.setIcon(new ImageIcon(img));
                burbuja.setText("");
                burbuja.setOpaque(false); 
                burbuja.setBorder(null);
            } catch (Exception e) {
                burbuja.setText("Error al cargar sticker");
            }
        } 
        else {
            burbuja.setText("<html><p style='width: 150px;'>" + msj.getContenido() + "</p></html>");
            burbuja.setIcon(null); 
        }

        fila.add(burbuja);
        contenedorMensajes.add(fila);

        SwingUtilities.invokeLater(() -> {
            contenedorMensajes.revalidate();
            contenedorMensajes.repaint();

            SwingUtilities.invokeLater(() -> {
                JScrollBar vertical = scroll.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            });
        });
    }

    private void cargarHistorial() {
        ArrayList<Mensaje> historial = GestorChat.leerConversacion(usuarioLogueado.getUsuario(), receptor);
        for (Mensaje m : historial) {
            agregarBurbuja(m);
        }
    }

    private void abrirPanelStickers() {
        JDialog ventanaStickers = new JDialog();
        ventanaStickers.setTitle("Selecciona un Sticker");
        ventanaStickers.setSize(300, 400);
        ventanaStickers.setLocationRelativeTo(this);

        JPanel gridStickers = new JPanel(new GridLayout(0, 3, 5, 5));
        gridStickers.setBackground(new Color(30, 30, 30));

        String[] nombresStickers = {"sticker_aplauso.png", "sticker_corazon.png", "sticker_feliz.png", "sticker_risa.png", "sticker_triste.png"};

        for (String nombre : nombresStickers) {
            String rutaEnPaquete = "/stickers/" + nombre;
            java.net.URL urlSticker = getClass().getResource(rutaEnPaquete);

            if (urlSticker != null) {
                ImageIcon icon = new ImageIcon(urlSticker);
                Image img = icon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
                JButton btnSticker = new JButton(new ImageIcon(img));

                btnSticker.setContentAreaFilled(false);
                btnSticker.setBorderPainted(false);
                btnSticker.setFocusPainted(false);
                btnSticker.setCursor(new Cursor(Cursor.HAND_CURSOR));

                btnSticker.addActionListener(e -> {
                    enviarSticker(urlSticker.toString());
                    ventanaStickers.dispose();
                });

                gridStickers.add(btnSticker);
            } else {
                System.err.println("No se pudo encontrar el sticker: " + rutaEnPaquete);
            }
        }

        JScrollPane scroll = new JScrollPane(gridStickers);
        scroll.setBorder(null);
        ventanaStickers.add(scroll);
        ventanaStickers.setVisible(true);

    }

    private void enviarSticker(String rutaSticker) {
        Mensaje msjSticker = new Mensaje(usuarioLogueado.getUsuario(), receptor, rutaSticker);
        msjSticker.setEsSticker(true);

        GestorChat.enviarMensaje(msjSticker, out);
        agregarBurbuja(msjSticker);
    }
    
    public String getReceptor(){
        return receptor;
    }
}
