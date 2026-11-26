/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.ejerciciorepaso;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 *
 * @author david
 */
public class Ejercicio1Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        Usuario u = generarUsuario(teclado);

        agregarUsuario(u);

    }

    private static Usuario generarUsuario(Scanner teclado) {
        System.out.println("Introduce el user del usuario");
        String user = teclado.nextLine();
        System.out.println("Introduce la contrasena del usuario");
        String contrasena = teclado.nextLine();
        System.out.println("Introduce el nombre del usuario");
        String nombre = teclado.nextLine();
        System.out.println("Introduce los apellidos del usuario");
        String apellidos = teclado.nextLine();
        LocalDate fechaNac = recogerYValidarFechaNac(teclado);
        Usuario u = new Usuario(user, contrasena, nombre, apellidos, fechaNac);

        return u;
    }

    private static void agregarUsuario(Usuario u) {
        UsuarioDAO dao = new UsuarioDAO();

        try {
            if (dao.agregarUsuario(u)) {
                System.out.println("Usuario agregado con exito");
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("El identificador '" + u.getUser() + "' ya esta en uso.");
            } else {
                System.out.println("Error al insertar usuario: " + e.getMessage());
            }
        }
    }

    public static LocalDate recogerYValidarFechaNac(Scanner teclado) {

        LocalDate fechaNac = null;

        while (fechaNac == null) {
            System.out.println("Introduce la fecha de nacimiento (AAAA-MM-DD)");

            try {
                fechaNac = LocalDate.parse(teclado.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println("Formato no valido. Ejemplo: 2000-05-14");
            }

        }

        return fechaNac;

    }
}
