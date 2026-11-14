package com.mycompany.hoja05ciudades;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author daw2
 */
public class City implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    
  
    private int id;
    private String nombre;
    private String codPais;
    private String provincia;
    private int poblacion;

    public City(int id, String nombre, String codPais, String provincia, int poblacion) {
        this.id = id;
        this.nombre = nombre.toUpperCase();
        this.codPais = codPais.toUpperCase();
        this.provincia = provincia.toUpperCase();
        this.poblacion = poblacion;
        
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCodPais() {
        return codPais;
    }

    public String getProvincia() {
        return provincia;
    }

    public int getPoblacion() {
        return poblacion;
    }
    
    
    @Override
    public String toString() {
        return "id: "+id+"\nnombre: "+nombre+"\ncodPais: "+codPais+"\nProvincia: "+provincia+"\npoblacion: "+poblacion+"\n";
    }
    
    
    
    
}
