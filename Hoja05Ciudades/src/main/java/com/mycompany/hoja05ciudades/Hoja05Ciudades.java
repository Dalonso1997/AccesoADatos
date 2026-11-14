/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.hoja05ciudades;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author daw2
 */
public class Hoja05Ciudades {

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);
        GestorFichero g = new GestorFichero();
        Path ficheroObjetos = Path.of("ciudades.obj");
        g.comprobarFichero(ficheroObjetos);

        System.out.println("Introduce el fichero con el que quieres trabajar:");
        String fichero = teclado.nextLine();
        Path ruta = Path.of(fichero);

        if (Files.exists(ruta)) {
            int eleccion = -1;
            boolean salir = false;
            while (!salir) {
                do {
                    mostrarMenu();

                    try {
                        eleccion = Integer.parseInt(teclado.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Error, debes introducir un numero entero");
                        eleccion = -1;
                    }

                } while (eleccion < 0 || eleccion > 5);

                switch (eleccion) {
                    case 0 -> {
                        salir = true;
                    }

                    case 1 -> {
                        opcion1(g, teclado, ruta, ficheroObjetos);
                    }

                    case 2 -> {
                        opcion2(g, teclado, ruta, ficheroObjetos);
                    }

                    case 3 -> {
                        opcion3(g, teclado, ruta, ficheroObjetos);
                    }
                    case 4 -> {
                        opcion4(g, teclado, ficheroObjetos);
                    }
                    case 5 -> {
                        opcion5(g, teclado, ficheroObjetos);
                    }
                    default -> {

                    }
                }
            }
        } else {
            System.out.println("El fichero no existe.");
        }

    }

    public static void mostrarMenu() {

        System.out.println("Elige una opcion");
        System.out.println("1. Anadir ciudad");
        System.out.println("2. Leer contenido");
        System.out.println("3. Obtener repetidas");
        System.out.println("4. ELiminar ciudad");
        System.out.println("5. Obtener mas poblada");
        System.out.println("0. Salir");

    }

    public static void opcion1(GestorFichero g, Scanner teclado, Path ruta, Path ficheroObjetos) {

        try {
            List<String> lineas = new ArrayList<String>(g.leerLineasFichero(ruta));

            System.out.println("Elige el numero de linea quieres escribir en el fichero: ");

            int linea = Integer.parseInt(teclado.nextLine());

            if (linea < 2 || linea > lineas.size()) {
                System.out.println("Esa linea no existe, no se anadira nada.");
            } else {

                City ciudad = g.crearCiudad(lineas.get(linea - 1));
                g.escribirCiudadEnFichero(ciudad, ficheroObjetos);

            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    public static void opcion2(GestorFichero g, Scanner teclado, Path ruta, Path ficheroObjeto) {
        try {
            List<City> listaCiudades = g.listarCiudades(ficheroObjeto);

            if (listaCiudades.isEmpty()) {
                System.out.println("La lista esta vacia");
            } else {
                listaCiudades.sort((a, b) -> g.compararCiudades(a, b));

                for (City ciudad : listaCiudades) {

                    System.out.println("Nombre: " + ciudad.getNombre()
                            + " | Codigo de pais: " + ciudad.getCodPais()
                            + " | provincia: " + ciudad.getProvincia()
                            + " | Poblacion: " + ciudad.getPoblacion() + "\n");

                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void opcion3(GestorFichero g, Scanner teclado, Path ruta, Path ficheroObjetos) {

        boolean hayEntradas = false;

        try {
            Map<String, Integer> mapaCiudades = new HashMap<>();
            List<City> listaCiudades = g.listarCiudades(ficheroObjetos);

            for (City ciudad : listaCiudades) {

                String nombre = ciudad.getNombre();
                String codigoPais = ciudad.getCodPais();
                String clave = nombre + "-" + codigoPais;

                mapaCiudades.put(clave, mapaCiudades.getOrDefault(clave, 0) + 1);

            }

            for (Map.Entry<String, Integer> entrada : mapaCiudades.entrySet()) {

                if (entrada.getValue() > 1) {

                    System.out.println("Ciudad: " + entrada.getKey() + "veces repetida: " + entrada.getValue());
                    hayEntradas = true;
                }

            }

            if (!hayEntradas) {
                System.out.println("No hay ciudades repetidas");
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void opcion4(GestorFichero g, Scanner teclado, Path ficheroObjeto) {

        int eliminadas = 0;

        System.out.println("Escribe el nombre de la ciudad que quieres borrar");
        String nombreCiudad = teclado.nextLine();
        System.out.println("Introduce el codigo de pais");
        String codigoDelPais = teclado.nextLine();

        try {
            List<City> listaCiudades = g.listarCiudades(ficheroObjeto);

            Iterator<City> it = listaCiudades.iterator();

            while (it.hasNext()) {

                City ciudad = it.next();

                if (ciudad.getNombre().equals(nombreCiudad.toUpperCase()) && ciudad.getCodPais().equals(codigoDelPais.toUpperCase())) {
                    it.remove();
                    eliminadas += 1;
                }
            }

            if (eliminadas > 0) {
                System.out.println("Completado. Se han eliminado: " + eliminadas + " ciudades.");
                g.reescribirFichero(listaCiudades, ficheroObjeto);
            } else {
                System.out.println("No hay ciudades que eliminar.");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void opcion5(GestorFichero g, Scanner teclado, Path ficheroObjetos) {

        int mayorPoblacion = 0;

        try {

            List<City> listaCiudades = g.listarCiudades(ficheroObjetos);

            for (City ciudad : listaCiudades) {

                if (mayorPoblacion < ciudad.getPoblacion()) {
                    mayorPoblacion = ciudad.getPoblacion();
                }

            }

            for (City ciudad : listaCiudades) {

                if (mayorPoblacion == ciudad.getPoblacion()) {
                    System.out.println(ciudad.getNombre() + "-" + ciudad.getCodPais() + "-" + mayorPoblacion);
                }

            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

}
