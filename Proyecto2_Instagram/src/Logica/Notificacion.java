/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.io.Serializable;

/**
 *
 * @author HP
 */
public class Notificacion implements Serializable{
    public enum Tipo{
        SOLICITUD_NUEVA, SOLICITUD_ACEPTADA
    }
    
    private String emisor;
    private String receptor;
    private Tipo tipo;
    
    public Notificacion(String emisor, String receptor, Tipo tipo){
        this.emisor = emisor;
        this.receptor = receptor;
        this.tipo = tipo;
    }
    
    public String getEmisor(){
        return emisor;
    }
    
    public String getReceptor(){
        return receptor;
    }
    
    public Tipo getTipo(){
        return tipo;
    }
}
