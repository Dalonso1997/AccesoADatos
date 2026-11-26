/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.ejerciciorepaso;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author david
 */
public class EjercicioRepaso {

    public static void main(String[] args) {
        
        try (Connection con = ConexionBD.getConexion()){
            
            
            System.out.println("COnexion establecida con exito.");
            
        } catch (SQLException e){
            System.out.println("No se pudo conectar a la base de datos: " +e.getMessage());
        }
        
    }
}
