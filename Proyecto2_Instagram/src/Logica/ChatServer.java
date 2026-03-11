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

                synchronized (clientesConectados) {
                    clientesConectados.put(usernameCliente, out);
                }
                System.out.println("DEBUG: " + usernameCliente + " se ha conectado.");

                while (true) {
                    Mensaje msj = (Mensaje) in.readObject();
                    reenviarMensaje(msj);
                }
            } catch (Exception e) {
                System.out.println(usernameCliente + " se ha desconectado.");
            } finally {
                synchronized (clientesConectados) {
                    clientesConectados.remove(usernameCliente);
                }
            }
        }

        private void reenviarMensaje(Mensaje msj) {
            synchronized (clientesConectados) {
                ObjectOutputStream outDestino = clientesConectados.get(msj.getReceptor());
                if (outDestino != null) {
                    try {
                        outDestino.writeObject(msj);
                        outDestino.flush();
                        System.out.println("Mensaje enviado de " + msj.getEmisor() + " a " + msj.getReceptor());
                    } catch (IOException e) {
                        System.err.println("Error al reenviar mensaje.");
                    }
                } else {
                    System.out.println("Usuario " + msj.getReceptor() + " no está en línea. El mensaje se guardará solo en archivos.");
                }
            }
        }
    }
}
