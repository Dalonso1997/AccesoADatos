/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ejercicio2;

import com.mycompany.ejerciciorepaso.ConexionBD;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author david
 */
public class GrupoDAO {

    public List<Grupo> listarGrupos() throws SQLException {

        List<Grupo> listaGrupos = new ArrayList<>();
        String sql = "SELECT codgrupo, nombre, localidad, estilo, esgrupo, annoGrab, fechaEstreno, compania FROM grupos ORDER BY codgrupo";

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Integer anno = rs.getObject("annoGrab", Integer.class);
                Date fechaEstreno = rs.getDate("fechaEstreno");
                LocalDate fecha = fechaEstreno == null ? null : fechaEstreno.toLocalDate();

                listaGrupos.add(new Grupo(
                        rs.getInt("codgrupo"),
                        rs.getString("nombre"),
                        rs.getString("localidad"),
                        rs.getString("estilo"),
                        rs.getBoolean("esgrupo"),
                        anno,
                        fecha,
                        rs.getString("compania")
                ));   
            }
        }
        
        return listaGrupos;
        
    }
    
    
    public void mostrarCancionesPorGrupo() throws SQLException{
        
        String sql = "SELECT g.nombre, count(c.numCancion) AS total FROM grupos AS g LEFT JOIN canciones AS c ON g.codGrupo = c.grupo GROUP BY g.codGrupo ORDER BY g.nombre;";
        
        try (Connection con = ConexionBD.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()){
            
            
            while (rs.next()){
                System.out.println(rs.getInt("total")+" "+rs.getString("nombre"));
            }
            
            
        }
        
        
        
    }
    
    
    public boolean existeGrupo(String nombre) throws SQLException{
        
        String sql = "SELECT nombre from grupos WHERE nombre = ?";
        
       try (Connection con = ConexionBD.getConexion();
               PreparedStatement ps = con.prepareStatement(sql)){
           
           ps.setString(1, nombre);
           
           try (ResultSet rs = ps.executeQuery()){
               
               return rs.next();
               
           }
           
           
       }
        
    }
    
}
