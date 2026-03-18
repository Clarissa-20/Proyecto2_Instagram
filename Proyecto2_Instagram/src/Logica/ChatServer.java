/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author HP
 */
public class ChatServer {

    private static final int PUERTO = 5000;
    private static Map<String, ObjectOutputStream> clientesConectados = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Servidor de Inbox iniciado en puerto " + PUERTO + "...");

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {

        private Socket socket;
        private ObjectInputStream in;
        private String usernameCliente;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new ObjectInputStream(socket.getInputStream());
                usernameCliente = (String) in.readObject();

                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();

                synchronized (clientesConectados) {
                    clientesConectados.put(usernameCliente, out);
                }
                System.out.println("DEBUG: @" + usernameCliente + " se ha conectado.");

                while (true) {
                    Object recibido = in.readObject();

                    if (recibido instanceof Mensaje) {
                        reenviarMensaje((Mensaje) recibido);
                    } else if (recibido instanceof Notificacion) {
                        reenviarNotificacion((Notificacion) recibido);
                    }
                }
            } catch (EOFException | SocketException e) {
                System.out.println("INFO: " + usernameCliente + " cerró la conexión.");
            } catch (Exception e) {
                System.err.println("Error en la comunicación con " + usernameCliente + ": " + e.getMessage());
            } finally {
                desconectar();
            }
        }

        private void reenviarMensaje(Mensaje msj) {
            synchronized (clientesConectados) {
                ObjectOutputStream outDestino = clientesConectados.get(msj.getReceptor());
                if (outDestino != null) {
                    try {
                        outDestino.writeObject(msj);
                        outDestino.flush();
                        outDestino.reset();
                        System.out.println("Mensaje enviado de " + msj.getEmisor() + " a " + msj.getReceptor());
                    } catch (IOException e) {
                        System.err.println("Error al reenviar mensaje.");
                    }
                } else {
                    System.out.println("Usuario " + msj.getReceptor() + " no está en línea. El mensaje se guardará solo en archivos.");
                }
            }
        }

        private void reenviarNotificacion(Notificacion noti) {
            synchronized (clientesConectados) {
                ObjectOutputStream outDestino = clientesConectados.get(noti.getReceptor());
                if (outDestino != null) {
                    try {
                        outDestino.writeObject(noti);
                        outDestino.flush();
                        outDestino.reset();
                        System.out.println("Notificación de " + noti.getTipo() + " enviada a " + noti.getReceptor());
                    } catch (IOException e) {
                        System.err.println("Error al reenviar notificación.");
                    }
                }
            }
        }

        private void desconectar() {
            try {
                synchronized (clientesConectados) {
                    clientesConectados.remove(usernameCliente);
                }
                if (socket != null) {
                    socket.close();
                }
                System.out.println("DEBUG: Conexión de " + usernameCliente + " eliminada.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
