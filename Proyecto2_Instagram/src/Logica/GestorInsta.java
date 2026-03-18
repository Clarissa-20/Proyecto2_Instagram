/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import Constantes.Constante;
import static Logica.ManejoArchivosBinarios.leerTodosLosUsuarios;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author HP
 */

public class GestorInsta {

    private static final String RAIZ = "INSTA_RAIZ";

    public static void crearNuevaCuenta(Usuario nuevoUsuario) throws UsernameYaExiste, IOException {
        String username = nuevoUsuario.getUsuario();

        if (ManejoArchivosBinarios.existeUsername(username)) {
            throw new UsernameYaExiste("El username " + username + " no está disponible.");
        }

        File carpetaUsuario = new File(RAIZ + File.separator + username);

        if (!carpetaUsuario.exists() && !carpetaUsuario.mkdirs()) {
            throw new IOException("No se pudo crear el directorio del usuario en: " + carpetaUsuario.getAbsolutePath());
        }

        new File(carpetaUsuario, "imagenes").mkdirs();

        nuevoUsuario.setActivo(true);
        ManejoArchivosBinarios.escribirUsuario(nuevoUsuario);
    }

    public static ArrayList<Insta> generarTimeLine(String usuarioActual) throws IOException {
        ArrayList<Insta> timeLine = new ArrayList<>();

        Usuario usuarioLogueado = buscarUsuarioPorUsername(usuarioActual);
        if (usuarioLogueado == null) {
            return timeLine;
        }

        String rutaFollowing = RAIZ + File.separator + usuarioActual + File.separator + "following.ins";
        ArrayList<Follow> seguidos = ManejoArchivosBinarios.leerListaFollows(rutaFollowing);

        for (Follow f : seguidos) {
            if (f.isActivo()) {
                Usuario usuarioSeguido = buscarUsuarioPorUsername(f.getUsername());
                if (usuarioSeguido != null) {
                    String clavePosts = usuarioSeguido.getUsuario();
                    timeLine.addAll(ManejoArchivosBinarios.leerInstasDeUsuario(clavePosts));
                }
            }
        }

        ArrayList<Insta> instasPropios = ManejoArchivosBinarios.leerInstasDeUsuario(usuarioLogueado.getUsuario());
        timeLine.addAll(instasPropios);

        java.util.Collections.sort(timeLine, java.util.Collections.reverseOrder());
        return timeLine;
    }

    public static ArrayList<Insta> buscarPorMencion(String usuarioActual) throws IOException {
        ArrayList<Insta> instasMencionados = new ArrayList<>();
        String mencionBuscada = "@" + usuarioActual.toLowerCase();

        try {
            ArrayList<Usuario> todosLosUsuarios = ManejoArchivosBinarios.leerTodosLosUsuarios();

            for (Usuario u : todosLosUsuarios) {
                if (!u.isActivo()) {
                    continue;
                }
                ArrayList<Insta> instasDelUsuario = ManejoArchivosBinarios.leerInstasDeUsuario(u.getUsuario());
                for (Insta i : instasDelUsuario) {
                    if (i.getTexto().toLowerCase().contains(mencionBuscada)) {
                        instasMencionados.add(i);
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Error al acceder a los archivos para buscar menciones: " + e.getMessage());
        }
        java.util.Collections.sort(instasMencionados, java.util.Collections.reverseOrder());
        return instasMencionados;
    }

    public static ArrayList<Insta> buscarPorHashtag(String hashtag) throws IOException {
        ArrayList<Insta> instasEncontrados = new ArrayList<>();
        String hashtagBuscado = hashtag.toLowerCase();
        if (!hashtagBuscado.startsWith("#")) {
            hashtagBuscado = "#" + hashtagBuscado;
        }

        try {
            ArrayList<Usuario> todosLosUsuarios = ManejoArchivosBinarios.leerTodosLosUsuarios();

            for (Usuario u : todosLosUsuarios) {
                if (!u.isActivo()) {
                    continue;
                }
                ArrayList<Insta> instasDelUsuario = ManejoArchivosBinarios.leerInstasDeUsuario(u.getUsuario());
                for (Insta i : instasDelUsuario) {
                    if (i.getContenido().toLowerCase().contains(hashtagBuscado)) {
                        instasEncontrados.add(i);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al acceder a los archivos para buscar hashtags: " + e.getMessage());
        }
        java.util.Collections.sort(instasEncontrados, java.util.Collections.reverseOrder());
        return instasEncontrados;
    }

    public static boolean actualizarEstadoCuenta(String username, boolean nuevoEstado) throws IOException {
        ArrayList<Usuario> listaUsuarios = ManejoArchivosBinarios.leerTodosLosUsuarios();
        if (listaUsuarios.isEmpty()) {
            throw new IOException("La lista de usuarios está vacía. No se puede actualizar el estado.");
        }

        boolean estadoCambiado = false;
        boolean estadoFinal = false;

        for (Usuario u : listaUsuarios) {
            if (u.getUsuario().equalsIgnoreCase(username)) {
                boolean estadoActual = u.isActivo();
                estadoFinal = !estadoActual;
                u.setActivo(estadoFinal);
                estadoCambiado = true;
                break;
            }
        }

        if (!estadoCambiado) {
            throw new IOException("Usuario no encontrado para la actualizacion.");
        }

        ManejoArchivosBinarios.reescribirListaCompletaUsuarios(listaUsuarios);
        return estadoFinal;
    }

    public static int contarFollows(String username, boolean esFollowing) throws IOException {
        String nombreArchivo = esFollowing ? "following.ins" : "followers.ins";
        String rutaArchivo = RAIZ + File.separator + username + File.separator + nombreArchivo;
        ArrayList<Follow> lista = ManejoArchivosBinarios.leerListaFollows(rutaArchivo);

        int contador = 0;
        for (Follow f : lista) {
            if (f.isActivo()) {
                contador++;
            }
        }
        return contador;
    }

    public static void actualizarEstadoFollow(String seguidor, String seguido, boolean estado) throws IOException {
        String rutaFollowing = RAIZ + File.separator + seguidor + File.separator + "following.ins";
        ArrayList<Follow> followsExistentes = ManejoArchivosBinarios.leerListaFollows(rutaFollowing);
        actualizarListaDeFollows(followsExistentes, seguido, estado);
        ManejoArchivosBinarios.reescribirFollows(rutaFollowing, followsExistentes);

        String rutaFollowers = RAIZ + File.separator + seguido + File.separator + "followers.ins";
        ArrayList<Follow> followersExistentes = ManejoArchivosBinarios.leerListaFollows(rutaFollowers);
        actualizarListaDeFollows(followersExistentes, seguidor, estado);
        ManejoArchivosBinarios.reescribirFollows(rutaFollowers, followersExistentes);

        ArrayList<Usuario> todosLosUsuarios = ManejoArchivosBinarios.leerTodosLosUsuarios();
        for (Usuario u : todosLosUsuarios) {
            if (u.getUsuario().equalsIgnoreCase(seguidor)) {
                if (estado) {
                    if (!u.getSiguiendo().contains(seguido)) {
                        u.getSiguiendo().add(seguido);
                    }
                } else {
                    u.getSiguiendo().remove(seguido);
                }
                break;
            }
        }
        ManejoArchivosBinarios.reescribirListaCompletaUsuarios(todosLosUsuarios);
    }

    private static void actualizarListaDeFollows(ArrayList<Follow> lista, String user, boolean estado) {
        boolean encontrado = false;
        for (Follow f : lista) {
            if (f.getUsername().equalsIgnoreCase(user)) {
                f.setActivo(estado);
                encontrado = true;
                break;
            }
        }
        if (!encontrado && estado) {
            lista.add(new Follow(user));
        }
    }

    public static ArrayList<Usuario> buscarPersonas(String textoBusqueda, String usuarioLogueado) {
        ArrayList<Usuario> resultados = new ArrayList<>();
        String busqueda = textoBusqueda.toLowerCase();

        try {
            ArrayList<Usuario> todosLosUsuarios = ManejoArchivosBinarios.leerTodosLosUsuarios();
            for (Usuario u : todosLosUsuarios) {
                boolean contieneTexto = u.getUsuario().toLowerCase().contains(busqueda);
                boolean estaActivo = u.isActivo();
                boolean diferenteUser = !u.getUsuario().equalsIgnoreCase(usuarioLogueado);

                if (contieneTexto && estaActivo && diferenteUser) {
                    resultados.add(u);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer los usuarios para la busqueda: " + e.getMessage());
        }
        return resultados;
    }

    public static boolean estaSiguiendo(String seguidor, String seguido) throws IOException {
        String rutaFollowing = RAIZ + File.separator + seguidor + File.separator + "following.ins";
        if (!new File(rutaFollowing).exists()) {
            return false;
        }

        ArrayList<Follow> listaFollowing = ManejoArchivosBinarios.leerListaFollows(rutaFollowing);
        for (Follow f : listaFollowing) {
            if (f.getUsername().equalsIgnoreCase(seguido) && f.isActivo()) {
                return true;
            }
        }
        return false;
    }

    public static DatosPerfil obtenerPefilCompleto(String perfilUsername, String usuarioLogueado) throws PerfilNoEncontrado, IOException {
        Usuario perfil = null;
        ArrayList<Usuario> todos = ManejoArchivosBinarios.leerTodosLosUsuarios();

        for (Usuario u : todos) {
            if (u.getUsuario().trim().equalsIgnoreCase(perfilUsername.trim()) && u.isActivo()) {
                perfil = u;
                break;
            }
        }

        if (perfil == null) {
            throw new PerfilNoEncontrado("El perfil de @" + perfilUsername + " no existe o esta desactivado");
        }

        int seguidores = contarFollows(perfilUsername, false);
        int seguidos = contarFollows(perfilUsername, true);

        boolean loSigue = false;
        if (!perfilUsername.equalsIgnoreCase(usuarioLogueado)) {
            loSigue = estaSiguiendo(usuarioLogueado, perfilUsername);
        }

        ArrayList<Insta> instas = ManejoArchivosBinarios.leerInstasDeUsuario(perfil.getUsuario());
        java.util.Collections.sort(instas);

        return new DatosPerfil(perfil, seguidores, seguidos, loSigue, instas);
    }

    public static final int MAX_CARACTERES_INSTA = 140;

    public static void publicarInsta(String username, String contenido) throws LongitudInstaInvalida, IOException {
        if (contenido.length() > MAX_CARACTERES_INSTA) {
            throw new LongitudInstaInvalida("El contenido excede el maximo permitido.", MAX_CARACTERES_INSTA);
        }

        Insta nuevoInsta = new Insta(username, contenido, "");
        String rutaCompletaArchivo = RAIZ + File.separator + username + File.separator + "insta.ins";
        ManejoArchivosBinarios.escribirInsta(nuevoInsta, rutaCompletaArchivo);
    }

    public static Usuario logIn(String username, String password) throws CredencialesInvalidas, IOException {
        ArrayList<Usuario> lista = ManejoArchivosBinarios.leerTodosLosUsuarios();
        for (Usuario u : lista) {
            if (u.getUsuario().equals(username) && u.getPassword().equals(password)) {
                if (!u.isActivo()) {
                    u.setActivo(true);
                    ManejoArchivosBinarios.reescribirListaCompletaUsuarios(lista);
                }
                return u;
            }
        }
        throw new CredencialesInvalidas();
    }

    public static String copiarFotoPerfil(String username, String rutaOriginal, String extension) throws IOException {
        if (rutaOriginal == null || rutaOriginal.isEmpty()) {
            return null;
        }

        String rutaDestinoCarpeta = RAIZ + File.separator + username + File.separator + "imagenes";
        File carpeta = new File(rutaDestinoCarpeta);

        if (!carpeta.exists() && !carpeta.mkdirs()) {
            throw new IOException("Fallo al crear carpeta de imágenes.");
        }

        String nombreFinal = "perfil" + extension;
        Path origen = new File(rutaOriginal).toPath();
        Path destino = new File(rutaDestinoCarpeta + File.separator + nombreFinal).toPath();

        Files.copy(origen, destino, StandardCopyOption.REPLACE_EXISTING);

        return destino.toString();
    }

    public static void crearInsta(Insta nuevoPost) throws LongitudInstaInvalida, IOException {
        if (nuevoPost.getTexto().length() > 140) {
            throw new LongitudInstaInvalida("El texto es demasiado largo.", nuevoPost.getTexto().length());
        }

        String autor = nuevoPost.getAutorUsername();
        String rutaArchivoInstas = RAIZ + File.separator + autor + File.separator + "insta.ins";
        ManejoArchivosBinarios.escribirInsta(nuevoPost, rutaArchivoInstas);
    }

    public static void eliminarInsta(Insta postAEliminar) throws IOException {
        String username = postAEliminar.getAutorUsername();
        ArrayList<Insta> instasUsuario = ManejoArchivosBinarios.leerInstasDeUsuario(username);

        boolean eliminado = instasUsuario.removeIf(i
                -> i.getFechaPublicacion().equals(postAEliminar.getFechaPublicacion())
                && i.getRutaImg().equals(postAEliminar.getRutaImg()));

        if (!eliminado) {
            throw new IOException("No se encontró el post para eliminar.");
        }

        String rutaArchivoInstas = RAIZ + File.separator + username + File.separator + "insta.ins";
        ManejoArchivosBinarios.reescribirInstas(instasUsuario, rutaArchivoInstas);
    }

    public static void guardarComentario(Insta postComentado, Comentario nuevoComentario) throws IOException {
        String rutaArchivoComentarios = RAIZ + File.separator
                + postComentado.getAutorUsername()
                + File.separator + "comentarios_" + postComentado.getIdPost() + ".ins";

        ManejoArchivosBinarios.escribirComentario(nuevoComentario, rutaArchivoComentarios);
    }

    public static ArrayList<Comentario> leerComentarios(Insta postComentado) {
        String rutaArchivoComentarios = RAIZ + File.separator
                + postComentado.getAutorUsername()
                + File.separator + "comentarios_" + postComentado.getIdPost() + ".ins";

        return ManejoArchivosBinarios.leerComentariosDePost(rutaArchivoComentarios);
    }

    public static Usuario buscarUsuarioPorUsername(String username) throws IOException {
        ArrayList<Usuario> listaUsuarios = ManejoArchivosBinarios.leerTodosLosUsuarios();
        for (Usuario u : listaUsuarios) {
            if (u.getUsuario().equalsIgnoreCase(username)) {
                return u;
            }
        }
        return null;
    }

    public static void actualizarUsuario(Usuario userModificado) throws IOException {
        ArrayList<Usuario> todos = leerTodosLosUsuarios();
        for (int i = 0; i < todos.size(); i++) {
            if (todos.get(i).getUsuario().equalsIgnoreCase(userModificado.getUsuario())) {
                todos.set(i, userModificado);
                break;
            }
        }
        ManejoArchivosBinarios.reescribirListaCompletaUsuarios(todos);
    }

    public static void guardarListaUsuarios(ArrayList<Usuario> todos) throws IOException {
        ManejoArchivosBinarios.reescribirListaCompletaUsuarios(todos);
    }
}
