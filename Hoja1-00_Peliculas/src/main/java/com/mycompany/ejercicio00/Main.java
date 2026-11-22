/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.ejercicio00;

import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author david
 */
public class Main {

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);
        int eleccion = -1;

        while (eleccion != 5) {

            System.out.println("Introduce una opcion:");
            System.out.println("1. Actualizar Pelicula");
            System.out.println("2. Eliminar por ID");
            System.out.println("3. Agregar pelicula");
            System.out.println("4. listar peliculas ");
            System.out.println("5. Salir");
            try {
                eleccion = Integer.parseInt(teclado.nextLine());
            } catch (NumberFormatException e) {
                System.err.println("Introduce un numero del 1 al 5.");
                continue;
            }

            switch (eleccion) {

                case 1 -> {
                    
                    actualizarPelicula(teclado);
                }

                case 2 -> {
                    
                    borrarPelicula(teclado);
                }

                case 3 -> {
                    agregarPelicula(teclado);
                }
                case 4 -> {
                    listarPeliculas();
                }
                case 5 ->{
                    System.out.println("Saliendo.......");
                }
                
                default -> System.out.println("Opción no válida, elige del 1 al 5.");
            }

        }

    }
    
    
    public static void listarPeliculas() {

        PeliculaDAO dao = new PeliculaDAO();
       

        try {

            for (Pelicula p : dao.listarPeliculas()) {

                System.out.println(p);
                
            }
            
        } catch (SQLException e) {
            System.out.println("Error al listar: " + e.getMessage());
        }

    }

    public static void agregarPelicula(Scanner teclado) {

        Pelicula p = crearPelicula(teclado);

        PeliculaDAO dao = new PeliculaDAO();

        try {
            dao.agregar(p);
        } catch (SQLException e) {
            System.out.println("Error al insertar: " + e.getMessage());
        }
    }

    public static void borrarPelicula(Scanner teclado) {
        PeliculaDAO dao = new PeliculaDAO();
        listarPeliculas();
        int idPelicula = comprobarId(teclado);
        try {
            if (dao.eliminar(idPelicula)) {
                System.out.println("Pelicula borrada correctamente.");
                listarPeliculas();
            }
        } catch (SQLException e) {
            System.out.println("Error al realizar el borrado." + e.getMessage());
        }

    }

    private static int comprobarId(Scanner teclado) {
        int idPelicula = 0;
        boolean validarId = true;
        PeliculaDAO dao = new PeliculaDAO();
        
        System.out.println();
        
        
        while (validarId) {
            System.out.println("Introduce el id de la pelicula: ");

            try {

                idPelicula = Integer.parseInt(teclado.nextLine());
                try {
                    
                    if (dao.existeID(idPelicula)){
                        validarId = false;
                    } else {
                        System.out.println("El id no existe.");
                    }
                    
                }catch (SQLException e){
                    System.out.println("Error en la base de datos: "+e.getMessage());
                }

            } catch (NumberFormatException e) {
                System.out.println("Introduce un numero valido (Empezando en 1)");
            }
        }
        return idPelicula;
    }
    
    private static Pelicula crearPelicula(Scanner teclado) {
        int anioEstreno = 0;
        boolean validacion = true;
        System.out.println("Introduce el nombre de la pelicula:");
        String nombre = teclado.nextLine();

        System.out.println("Introduce el director de la pelicula: ");
        String director = teclado.nextLine();

        while (validacion) {
            System.out.println("Introduce el anio de estreno:");
            try {
                anioEstreno = Integer.parseInt(teclado.nextLine().trim());
                validacion = false;
            } catch (NumberFormatException e) {
                System.err.println("Introduce un numero.");

            }
        }

        System.out.println("Introduce el genero de la pelicula");
        String genero = teclado.nextLine();

        Pelicula p = new Pelicula(nombre, director, anioEstreno, genero);

        return p;
    }

    public static void actualizarPelicula(Scanner teclado) {
        PeliculaDAO dao = new PeliculaDAO();

        listarPeliculas();
        System.out.println("");

        int id = comprobarId(teclado);
        String campo = validarCampo(teclado);
        Object valor = recogerValor(teclado, campo);
        
        try {
            if (dao.actualizar(id, campo, valor)) {

                System.out.println("Actualizado con exito.");
                listarPeliculas();

            }
        } catch (SQLException e) {
            System.out.println("Error en la actualizacion: " + e.getMessage());
        }
    }

    public static String validarCampo(Scanner teclado) {
        int eleccionCampo = -1;
        boolean validacionCampo = true;
        String campo = "";
        while (validacionCampo) {
            System.out.println("Introduce el campo que quieres cambiar:");
            System.out.println("1. titulo");
            System.out.println("2. Director");
            System.out.println("3. anio estreno");
            System.out.println("4. genero");
            try {
                eleccionCampo = Integer.parseInt(teclado.nextLine());
                if (eleccionCampo >= 1 && eleccionCampo <= 4) {
                    validacionCampo = false;
                }
            } catch (NumberFormatException e) {
                System.err.println("Introduce un numero valido.");
            }

        }

        switch (eleccionCampo) {

            case 1 -> {
                campo = "title";
            }
            case 2 -> {
                campo = "director";
            }
            case 3 -> {
                campo = "release_year";
            }

            case 4 -> {
                campo = "genre";
            }
        }

        return campo;

    }

    public static Object recogerValor(Scanner teclado, String campo) {

        if (campo.equals("release_year")) {
            while (true) {
                System.out.println("Introduce el nuevo anio:");
                try {
                    int nuevoAnio = Integer.parseInt(teclado.nextLine());
                    return nuevoAnio;
                } catch (NumberFormatException e) {
                    System.err.println("Introduce un numero valido.");
                }
            }
        }

        System.out.println("Introduce el nuevo valor");
        String valor = teclado.nextLine();

        return valor;

    }

}
