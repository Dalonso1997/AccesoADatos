/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ejerciciorepaso;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author david
 */
public class UsuarioDAO {
    
    
    public boolean agregarUsuario(Usuario u) throws SQLException{
        
        String sql = "INSERT INTO usuarios (user, contrasena, nombre, apellidos, fechanac) VALUES(?,MD5(?),?,?,?)";
        
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setString(1, u.getUser());
            ps.setString(2, u.getContrasena());
            ps.setString(3, u.getNombre());
            ps.setString(4, u.getApellidos());
            ps.setDate(5, Date.valueOf(u.getFechaNac()));
            
            return ps.executeUpdate() == 1;
            
        }
    }
}
