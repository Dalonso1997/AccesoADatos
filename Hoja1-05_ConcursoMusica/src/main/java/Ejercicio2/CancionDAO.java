/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ejercicio2;

import com.mycompany.ejerciciorepaso.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author david
 */
public class CancionDAO {

    public void listarCanciones() throws SQLException {

        String sql = "SELECT c.titulo, g.nombre FROM canciones AS c INNER JOIN grupos AS g ON c.grupo = g.codGrupo ORDER BY g.nombre, c.numCancion";

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            String anterior = "";

            while (rs.next()) {

                if (!rs.getString("nombre").equals(anterior)) {
                    System.out.println(rs.getString("nombre"));
                    anterior = rs.getString("nombre");

                }

                System.out.println("\t" + rs.getString("titulo"));

            }
        }

    }

    public List<Cancion> listarCancionesPorGrupo(String nombre) throws SQLException {

        List<Cancion> listaCanciones = new ArrayList<>();
        String sql = "SELECT  numCancion, grupo, duracion, titulo, total_votos FROM canciones INNER JOIN grupos ON canciones.grupo = grupos.codGrupo WHERE grupos.nombre = ?";

        try (Connection con = ConexionBD.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);

            try (ResultSet rs = ps.executeQuery()) {
                
                
                
                while (rs.next()) {

                    listaCanciones.add(new Cancion(
                            rs.getInt("numCancion"),
                            rs.getInt("grupo"),
                            rs.getObject("duracion", LocalTime.class),
                            rs.getString("titulo"),
                            rs.getInt("total_votos")
                    ));
                    
                }

            }

        }
        
        return listaCanciones;

    }

}
