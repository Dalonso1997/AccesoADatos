/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Ejercicio2;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

  

    public class MainEjercicio2 {

        public static void main(String[] args) {
            Scanner teclado = new Scanner(System.in);
            boolean seguir = true;

            while (seguir) {
                mostrarMenu();
                int eleccion = validarEntero(teclado);
                seguir = eleccionMenu(eleccion, teclado);
            }
        }

        private static boolean eleccionMenu(int eleccion, Scanner teclado) {
            switch (eleccion) {
                case 0 -> {
                    return false;
                }
                case 1 ->
                    opcion1();
                case 2 ->
                    opcion2();
                case 3 ->
                    opcion3();
                case 4 ->
                    opcion4(teclado);
                case 5 ->
                    opcion5();
                case 6 ->
                    opcion6();
                case 7 ->
                    opcion7();
                case 8 ->
                    opcion8();
                case 9 ->
                    opcion9();
                default ->
                    System.out.println("Opcion no valida");
            }
            return true;
        }

        private static int validarEntero(Scanner teclado) {
            int eleccion = -1;
            System.out.println("Introduce un numero");
            try {
                eleccion = Integer.parseInt(teclado.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("El numero debe ser un entero");
            }
            return eleccion;
        }

        private static void mostrarMenu() {
            System.out.println("Elige una opcion:");
            System.out.println("1. Listado de grupos");
            System.out.println("2. Listado de canciones");
            System.out.println("3. Numero de canciones por grupo");
            System.out.println("4. Canciones de un grupo");
            System.out.println("5. Las 5 canciones mas votadas");
            System.out.println("6. Grupos sin canciones");
            System.out.println("7. Los ultimos 5 votos");
            System.out.println("8. Eliminar canciones de un grupo");
            System.out.println("9. Modificar datos de un grupo");
            System.out.println("0. Salir");
        }

        private static void opcion1() {

            GrupoDAO dao = new GrupoDAO();
            try {
                List<Grupo> listaGrupo = dao.listarGrupos();
                for (Grupo grupo : listaGrupo) {
                    System.out.println("codGrupo: " + grupo.getCodGrupo()
                            + " nombre del grupo: " + grupo.getNombre()
                            + " localidad: " + grupo.getLocalidad()
                            + " estilo: " + grupo.getEstilo());
                }

            } catch (SQLException e) {
                System.out.println("Error al consultar la base de datos." + e.getMessage());
            }
        }

        private static void opcion2() {

            CancionDAO dao = new CancionDAO();

            try {
                dao.listarCanciones();
            } catch (SQLException e) {
                System.out.println("Error al consultar la base de datos: " + e.getMessage());
            }

        }

        private static void opcion3() {
            GrupoDAO dao = new GrupoDAO();

            try {
                dao.mostrarCancionesPorGrupo();
            } catch (SQLException e) {
                System.out.println("Error al consultar la base de datos: " + e.getMessage());
            }
        }

        private static boolean validarNombre(String nombre) {

            GrupoDAO dao = new GrupoDAO();
            try {
                return dao.existeGrupo(nombre);
            } catch (SQLException e) {
                System.out.println("Error al consultar la base de datos: " + e.getMessage());
                return false;
            }
        }

        private static String pedirYLeerNombre(Scanner teclado) {

            System.out.println("Introduce el nombre del grupo que quieres consultar:");
            String nombre = teclado.nextLine().trim();

            return nombre;

        }

        private static void opcion4(Scanner teclado) {
            CancionDAO dao = new CancionDAO();
            String nombre = pedirYLeerNombre(teclado);

            try {
                List<Cancion> listaCanciones = dao.listarCancionesPorGrupo(nombre);
                if (validarNombre(nombre)) {
                    if (listaCanciones.isEmpty()) {

                        System.out.println("El grupo: " + nombre + " no tiene canciones");
                    } else {
                        
                        DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm:ss");
                        
                        for (Cancion c : listaCanciones) {

                            System.out.println(c.getNumCancion() + "-" + c.getTitulo() + "-" + c.getDuracion().format(formato));

                        }
                    }
                } else {
                    System.out.println("No existe ningun grupo con ese nombre");
                }
            } catch (SQLException e) {
                System.out.println("Error al consultar la base de datos: " + e.getMessage());
            }
        }
    

    private static void opcion5() {
        System.out.println("Pendiente");
    }

    private static void opcion6() {
        System.out.println("Pendiente");
    }

    private static void opcion7() {
        System.out.println("Pendiente");
    }

    private static void opcion8() {
        System.out.println("Pendiente");
    }

    private static void opcion9() {
        System.out.println("Pendiente");
    }
}
