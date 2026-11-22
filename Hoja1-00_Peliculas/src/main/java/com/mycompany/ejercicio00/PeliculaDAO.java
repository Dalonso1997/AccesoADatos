/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ejercicio00;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author david
 */
public class PeliculaDAO {

    public List<Pelicula> listarPeliculas() throws SQLException {

        String sql = "SELECT id, title, director, release_year, genre FROM movies";
        List<Pelicula> peliculas = new ArrayList<>();
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Pelicula p = new Pelicula(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("director"),
                        rs.getInt("release_year"),
                        rs.getString("genre")
                );

                peliculas.add(p);

            }

        }

        return peliculas;

    }

    public boolean agregar(Pelicula p) throws SQLException {

        String sql = "INSERT INTO movies(title, director, release_year, genre) VALUES(?,?,?,?)";

        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getTitulo());
            ps.setString(2, p.getDirector());
            ps.setInt(3, p.getAnioEstreno());
            ps.setString(4, p.getGenero());

            return ps.executeUpdate() == 1;

        }

    }

    public boolean eliminar(int id) throws SQLException {

        String sql = "DELETE FROM movies where id = ?";

        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() == 1;

        }

    }

    public boolean actualizar(int id, String campo, Object valor) throws SQLException {

        String sql = "UPDATE movies SET " + campo + " = ? where id= ?";

        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setObject(1, valor);
            ps.setInt(2, id);

            return ps.executeUpdate() == 1;

        }

    }

    public boolean existeID(int id) throws SQLException {

        String sql = "SELECT id  FROM movies where id = ?";

        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

}
