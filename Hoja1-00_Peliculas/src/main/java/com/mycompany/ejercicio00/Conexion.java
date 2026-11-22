/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ejercicio00;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author david
 */
public class Conexion {

    private static final String FICHERO = "/db.properties";
    private static final Properties PROPS = cargarPropiedades();

    private static final String URL = obligatoria(PROPS, "db.url");
    private static final String USUARIO = obligatoria(PROPS, "db.user");
    private static final String CONTRASENA = PROPS.getProperty("db.password", "");

    private Conexion() {
    }

    private static Properties cargarPropiedades() {

        Properties propiedades = new Properties();

        try (InputStream in = Conexion.class.getResourceAsStream(FICHERO)) {

            if (in == null) {
                throw new IllegalStateException("No se encuentra " + FICHERO + " en src/main/resources");
            }

            propiedades.load(new InputStreamReader(in, StandardCharsets.UTF_8));

        } catch (IOException ex) {
            throw new IllegalStateException("Error leyendo " + FICHERO , ex);
        }

        return propiedades;
    }
    
    private static String obligatoria(Properties props, String clave){
        
        String valor = props.getProperty(clave);
        
        if (valor == null || valor.isBlank()){
            throw new IllegalStateException("Error al leer la propiedad de la clave "+ clave + FICHERO);
        }
        
        return valor.trim();
        
    }
    
    public static Connection getConexion() throws SQLException{
        
        return DriverManager.getConnection(URL,USUARIO,CONTRASENA);
    
    }
    
    
}
