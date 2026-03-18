/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author HP
 */
public class Mensaje implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private String emisor;
    private String receptor;
    private String contenido;
    private Date fecha;
    private boolean esSticker;
    private boolean esImagen;
    
    public Mensaje(String emisor, String receptor, String contenido){
        this.emisor = emisor;
        this.receptor = receptor;
        this.contenido = contenido;
        this.fecha =  new Date();
        this.esSticker = false;
        this.esImagen = false;
    }
    
    public String getEmisor(){
        return emisor;
    }
    
    public String getReceptor(){
        return receptor;
    }
    
    public String getContenido(){
        return contenido;
    }
    
    public Date getFecha(){
        return fecha;
    }
    
    public boolean isEsSticker(){
        return esSticker;
    }
    
    public void setEsSticker(boolean esSticker){
        this.esSticker = esSticker;
    }
    
    public boolean isEsImagen(){
        return esImagen;
    }
    
    public void setEsImagen(boolean esImagen){
        this.esImagen = esImagen;
    }
}
