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
public class vtnOtroPerfil extends JPanel {

    private final vtnInstaPrincipal vtnP;
    private final PerfilPanel panelContenido;

    public vtnOtroPerfil(String usernameOtro, vtnInstaPrincipal vtnP) {
        super(new BorderLayout());
        this.vtnP = vtnP;
        this.panelContenido = new PerfilPanel(usernameOtro, vtnP);

        add(this.panelContenido, BorderLayout.CENTER);
    }

    public void recargarPerfil() {
        this.panelContenido.cargarDatosYRenderizar();
    }
}
