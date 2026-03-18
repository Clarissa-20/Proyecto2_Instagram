/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import Constantes.Constante;
import InstaGui.PanelChat;
import java.io.*;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

/**
 *
 * @author HP
 */
public class GestorChat {

    private static PanelChat panelActivo = null;

    public static void setPanelActivo(PanelChat panel) {
        panelActivo = panel;
    }

    public static void iniciarEscucha(ObjectInputStream in) {
        new Thread(() -> {
            try {
                while (true) {
                    Object recibido = in.readObject();
                    if (recibido instanceof Mensaje) {
                        Mensaje msj = (Mensaje) recibido;

                        guardarMensajeEnArchivo(msj.getReceptor(), msj.getEmisor(), msj);

                        if (panelActivo != null && msj.getEmisor().equals(panelActivo.getReceptor())) {
                            SwingUtilities.invokeLater(() -> {
                                panelActivo.agregarBurbuja(msj);
                            });
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Hilo de escucha finalizado o error de conexión.");
            }
        }).start();
    }

    public static void enviarMensaje(Mensaje msj, ObjectOutputStream out) {
        try {
            guardarMensajeEnArchivo(msj.getEmisor(), msj.getReceptor(), msj);
            if (out != null) {
                out.writeObject(msj);
                out.flush();
            }
        } catch (IOException e) {
            System.err.println("Error al enviar/guardar mensaje: " + e.getMessage());
        }
    }

    public static void guardarMensajeEnArchivo(String dueñoDelInbox, String conQuienHabla, Mensaje msj) throws IOException {
        String rutaCarpeta = Constante.RUTA_BASE + dueñoDelInbox + "/inbox";
        File folder = new File(rutaCarpeta);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String rutaArchivo = rutaCarpeta + "/conversacion_con_" + conQuienHabla + ".ins";
        File archivo = new File(rutaArchivo);

        try (FileOutputStream fos = new FileOutputStream(archivo, true)) {
            if (archivo.length() == 0) {
                try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                    oos.writeObject(msj);
                }
            } else {
                try (AppendingObjectOutputStream aoos = new AppendingObjectOutputStream(fos)) {
                    aoos.writeObject(msj);
                }
            }
        }
    }

    public static ArrayList<Mensaje> leerConversacion(String usuarioActual, String conQuienHabla) {
        ArrayList<Mensaje> historial = new ArrayList<>();
        String rutaArchivo = Constante.RUTA_BASE + usuarioActual + "/inbox/conversacion_con_" + conQuienHabla + ".ins";
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            return historial;
        }

        try (FileInputStream fis = new FileInputStream(archivo); ObjectInputStream ois = new ObjectInputStream(fis)) {
            while (true) {
                try {
                    historial.add((Mensaje) ois.readObject());
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Error al leer mensajes: " + e.getMessage());
        }
        return historial;
    }
    
    public static void actualizarInterfazSiEsNecesario(Mensaje msj) {
        System.out.println("DEBUG: Intentando actualizar UI. ¿Hay panel activo?: " + (panelActivo != null));

        if (panelActivo != null) {
            System.out.println("DEBUG: Chat abierto con: " + panelActivo.getReceptor() + " | Mensaje viene de: " + msj.getEmisor());

            if (msj.getEmisor().equals(panelActivo.getReceptor())) {
                panelActivo.agregarBurbuja(msj);
            }
        }
    }
}
