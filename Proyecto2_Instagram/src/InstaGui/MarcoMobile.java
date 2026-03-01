/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InstaGui;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author HP
 */
public class MarcoMobile extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainContainer;

    public MarcoMobile() {
        setTitle("Insta - Simulacion Mobile");
        setSize(390, 844);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        mainContainer.add(new Login(this), "LOGIN");

        add(mainContainer);
    }

    public void mostrarVista(String nombreVista) {
        cardLayout.show(mainContainer, nombreVista);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MarcoMobile().setVisible(true);
        });
    }
}
