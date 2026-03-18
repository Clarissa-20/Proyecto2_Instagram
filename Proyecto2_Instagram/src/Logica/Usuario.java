/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author HP
 */
public class Usuario implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private String nombre;
    private char genero;
    private String usuario;
    private String password;
    private Date fechaIngreso;
    private int edad;
    private boolean activo;
    private String rutaFotoPerfil;
    private String bio;
    private boolean esPublico;
    private ArrayList<String> siguiendo;
    
    public Usuario(String nombre, char genero, String usuario, String password, int edad, String rutaFoto, String bio, boolean esPublico){
        this.nombre = nombre;
        this.genero = genero;
        this.usuario = usuario;
        this.password = password;
        this.edad = edad;
        this.rutaFotoPerfil = rutaFoto;
        this.bio =  bio;
        this.fechaIngreso = new Date();
        this.activo = true;
        this.esPublico = esPublico;
        this.siguiendo = new ArrayList<>();
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    
    public char getGenero(){
        return genero;
    }
    
    public void setGenero(char genero){
        this.genero = genero;
    }
    
    public String getUsuario(){
        return usuario;
    }
    
    public String getPassword(){
        return password;
    }
    
    public void setPassword(String password){
        this.password = password;
    }
    
    public Date getFechaIngreso(){
        return fechaIngreso;
    }
    
    public int getEdad(){
        return edad;
    }
    
    public void setEdad(int edad){
        this.edad = edad;
    }
    
    public boolean isActivo(){
        return activo;
    }
    
    public void setActivo(boolean activo){
        this.activo = activo;
    }
    
    public String getRutaFotoPerfil(){
        return rutaFotoPerfil;
    }
    
    public void setRutaFotoPerfil(String ruta){
        this.rutaFotoPerfil = ruta;
    }
    
    public String getBio(){
        return bio;
    }
    
    public void setBio(String bio){
        this.bio = bio;
    }
    
    public boolean isEsPublico(){
        return esPublico;
    }
    
    public void setEsPublico(boolean esPublico){
        this.esPublico = esPublico;
    }
    
    public ArrayList<String> getSiguiendo(){
        if(this.siguiendo == null){
            this.siguiendo = new ArrayList<>();
        }
        return siguiendo;
    }
}
