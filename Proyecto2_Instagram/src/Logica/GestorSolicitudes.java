/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import Constantes.Constante;
/**
 *
 * @author HP
 */
public class GestorSolicitudes {
    
    public static void guardarSolicitudDisco(Notificacion n) {
        String ruta = Constante.RUTA_BASE + n.getReceptor() + "/solicitudes.dat";
        File archivo = new File(ruta);

        ArrayList<Notificacion> lista = leerSolicitudesDisco(n.getReceptor());

        boolean existe = lista.stream().anyMatch(sol -> sol.getEmisor().equals(n.getEmisor()));

        if (!existe) {
            lista.add(n);
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
                oos.writeObject(lista);
            } catch (IOException e) {
                System.err.println("Error al guardar solicitud en disco: " + e.getMessage());
            }
        }
    }

    public static ArrayList<Notificacion> leerSolicitudesDisco(String usuario) {
        String ruta = Constante.RUTA_BASE + usuario + "/solicitudes.dat";
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (ArrayList<Notificacion>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static void borrarSolicitudDisco(String dueñoPerfil, String emisorAEliminar) {
        ArrayList<Notificacion> lista = leerSolicitudesDisco(dueñoPerfil);
        lista.removeIf(n -> n.getEmisor().equals(emisorAEliminar));

        String ruta = Constante.RUTA_BASE + dueñoPerfil + "/solicitudes.dat";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))) {
            oos.writeObject(lista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
