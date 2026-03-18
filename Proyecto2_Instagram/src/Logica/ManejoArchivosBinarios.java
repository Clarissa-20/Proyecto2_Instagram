/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import Constantes.Constante;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 *
 * @author HP
 */

public class ManejoArchivosBinarios {

    private static final String RAIZ = "INSTA_RAIZ";
    public static final String ARCHIVO_USUARIOS = RAIZ + File.separator + "users.ins";

    public static void escribirUsuario(Usuario nuevoUsuario) throws IOException {
        ArrayList<Usuario> usuarios = leerTodosLosUsuarios();
        usuarios.add(nuevoUsuario);
        reescribirListaCompletaUsuarios(usuarios);

        File carpetaPersonal = new File(RAIZ + File.separator + nuevoUsuario.getUsuario());
        if (!carpetaPersonal.exists()) {
            carpetaPersonal.mkdirs();
        }

        System.out.println("usuario " + nuevoUsuario.getUsuario() + " escrito con exito");
    }

    public static void asegurarArchivoUsuario() {
        try {
            File carpeta = new File(RAIZ);
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            File archivo = new File(ARCHIVO_USUARIOS);
            if (!archivo.exists()) {
                System.out.println("Creando archivo users.ins por primera vez en " + RAIZ);
                archivo.createNewFile();

                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
                    oos.writeObject(new ArrayList<Usuario>());
                }
            }
        } catch (IOException e) {
            System.err.println("Advertencia: Fallo al asegurar la existencia de users.ins: " + e.getMessage());
        }
    }

    public static ArrayList<Usuario> leerTodosLosUsuarios() throws IOException {
        asegurarArchivoUsuario();
        ArrayList<Usuario> usuarios = new ArrayList<>();
        File archivo = new File(ARCHIVO_USUARIOS);

        if (!archivo.exists() || archivo.length() == 0) {
            return usuarios;
        }

        try (FileInputStream fis = new FileInputStream(archivo); ObjectInputStream ois = new ObjectInputStream(fis)) {

            Object obj = ois.readObject();
            if (obj instanceof ArrayList) {
                usuarios = (ArrayList<Usuario>) obj;
            }

        } catch (ClassNotFoundException e) {
            System.err.println("Error de clase durante la lectura: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error CRÍTICO de I/O/Serialización al leer users.ins.");
            throw new IOException("Fallo de lectura por posible corrupción del archivo binario.", e);
        } catch (Exception e) {
            throw new IOException("Error desconocido durante la deserialización.", e);
        }

        return usuarios;
    }

    public static boolean existeUsername(String username) throws IOException {
        ArrayList<Usuario> listaUsuarios = leerTodosLosUsuarios();
        for (Usuario u : listaUsuarios) {
            if (u.getUsuario().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    public static void escribirInsta(Insta nuevoInsta, String rutaArchivo) throws IOException {
        File archivo = new File(rutaArchivo);

        File directorioPadre = archivo.getParentFile();
        if (directorioPadre != null && !directorioPadre.exists()) {
            if (!directorioPadre.mkdirs()) {
                throw new IOException("Fallo al crear la estructura de directorios para el post.");
            }
        }

        ArrayList<Insta> instas = new ArrayList<>();
        if (archivo.exists() && archivo.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                while (true) {
                    try {
                        instas.add((Insta) ois.readObject());
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (Exception e) {
            }
        }
        instas.add(nuevoInsta);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            for (Insta i : instas) {
                oos.writeObject(i);
            }
        }
    }

    public static ArrayList<Insta> leerInstasDeUsuario(String username) throws IOException {
        ArrayList<Insta> instas = new ArrayList<>();
        String rutaArchivo = RAIZ + File.separator + username + File.separator + "instas.ins";
        File archivo = new File(rutaArchivo);

        if (!archivo.exists() || archivo.length() == 0) {
            return instas;
        }

        try (FileInputStream fis = new FileInputStream(archivo); ObjectInputStream ois = new ObjectInputStream(fis)) {

            while (true) {
                try {
                    Insta insta = (Insta) ois.readObject();
                    instas.add(insta);
                } catch (EOFException e) {
                    break;
                } catch (ClassNotFoundException e) {
                    throw new IOException("Error de formato al leer Insta: " + e.getMessage(), e);
                }
            }
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
        return instas;
    }

    public static ArrayList<Follow> leerListaFollows(String rutaArchivo) throws IOException {
        ArrayList<Follow> follows = new ArrayList();

        File archivo = new File(rutaArchivo);
        if (!archivo.exists() || archivo.length() == 0) {
            return follows;
        }

        try (FileInputStream fis = new FileInputStream(archivo); ObjectInputStream ois = new ObjectInputStream(fis)) {

            while (true) {
                try {
                    Follow follow = (Follow) ois.readObject();
                    follows.add(follow);
                } catch (EOFException e) {
                    break;
                } catch (ClassNotFoundException e) {
                    throw new IOException("Error de formato en archivo binario: " + e.getMessage(), e);
                }
            }

        } catch (IOException e) {
            System.err.println("Error de E/S al leer follows: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            throw new IOException("Error desconocido durante la deserialización de follows.", e);
        }
        return follows;
    }

    public static void reescribirFollows(String rutaArchivo, ArrayList<Follow> follows) throws IOException {
        File file = new File(rutaArchivo); 
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        try (FileOutputStream fos = new FileOutputStream(rutaArchivo, false); ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for (Follow f : follows) {
                oos.writeObject(f);
            }
        }
    }

    public static void reescribirListaCompletaUsuarios(ArrayList<Usuario> listaUsuarios) throws IOException {
        asegurarArchivoUsuario();
        try (FileOutputStream fos = new FileOutputStream(ARCHIVO_USUARIOS, false); ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(listaUsuarios);
            oos.flush();

        } catch (FileNotFoundException e) {
            System.err.println("Archivo users.ins no encontrado para reescritura: " + e.getMessage());
            throw e;
        }
    }

    public static void reescribirInstas(ArrayList<Insta> instas, String rutaArchivo) throws IOException {
        File archivo = new File(rutaArchivo);

        if (instas.isEmpty()) {
            if (archivo.exists()) {
                archivo.delete();
                System.out.println("Archivo de instas eliminado: " + rutaArchivo);
            }
            return;
        }

        try (FileOutputStream fos = new FileOutputStream(archivo); ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for (Insta post : instas) {
                oos.writeObject(post);
            }
            System.out.println("Archivo de instas reescrito exitosamente: " + rutaArchivo);

        } catch (IOException e) {
            System.err.println("Error al reescribir la lista de Instas en " + rutaArchivo + ": " + e.getMessage());
            throw e;
        }
    }

    public static void escribirComentario(Comentario nuevoComentario, String rutaArchivoComentarios) throws IOException {
        File archivo = new File(rutaArchivoComentarios);
        ArrayList<Comentario> comentarios = leerComentariosDePost(rutaArchivoComentarios);
        comentarios.add(nuevoComentario);

        if (archivo.getParentFile() != null) {
            archivo.getParentFile().mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            for (Comentario c : comentarios) {
                oos.writeObject(c);
            }
            System.out.println("Comentario escrito en: " + rutaArchivoComentarios);
        }
    }

    public static ArrayList<Comentario> leerComentariosDePost(String rutaArchivoComentarios) {
        ArrayList<Comentario> comentarios = new ArrayList<>();
        File archivo = new File(rutaArchivoComentarios);

        if (!archivo.exists() || archivo.length() == 0) {
            return comentarios;
        }

        try (FileInputStream fis = new FileInputStream(archivo); ObjectInputStream ois = new ObjectInputStream(fis)) {

            while (true) {
                try {
                    Comentario comentario = (Comentario) ois.readObject();
                    comentarios.add(comentario);
                } catch (EOFException e) {
                    break;
                }
            }
            System.out.println("Comentarios leídos exitosamente de: " + rutaArchivoComentarios);

        } catch (IOException e) {
            System.err.println("Error de E/S al leer comentarios: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Clase Comentario no encontrada: " + e.getMessage());
        }

        return comentarios;
    }

    public static ArrayList<Usuario> cargarUsuarios() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        File archivo = new File("users.ins");

        if (!archivo.exists()) {
            return usuarios;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            usuarios = (ArrayList<Usuario>) ois.readObject();
        } catch (Exception e) {
            System.err.println("Error al cargar lista de usuarios: " + e.getMessage());
        }
        return usuarios;
    }
}
