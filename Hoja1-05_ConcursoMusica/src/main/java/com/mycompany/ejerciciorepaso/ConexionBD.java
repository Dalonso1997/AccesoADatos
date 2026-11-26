/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ejerciciorepaso;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author david
 */
public class ConexionBD {
    
    private static final Properties PROPS = cargarPropiedades();
    
    private static final String URL = PROPS.getProperty("db.url");
    private static final String USER = PROPS.getProperty("db.user");
    private static final String PASSWORD = PROPS.getProperty("db.password");
    
    
    private ConexionBD(){
        
    }
    
    private static Properties cargarPropiedades(){
        
        Properties p = new Properties();
        
        try (InputStream in = ConexionBD.class.getResourceAsStream("/db.properties")){
            
            if (in == null){
                throw new IllegalStateException("No se encuentra db.properties");
            }
            p.load(in);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error al leer properties", e);
        }
        return p;
    }
    
    
    public static Connection getConexion() throws SQLException{
        return DriverManager.getConnection(URL,USER,PASSWORD);
    }
    
}
