/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import Constantes.Constante;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author HP
 */
public class GestorChat {

    /*
     Guarda el mensaje en el archivo binario del usuario. Ruta:
     INSTA_RAIZ/usuario/inbox/conversacion_con_X.ins
     */
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
}
