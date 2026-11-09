package veterinaria;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import veterinaria.modelo.Cliente;
import veterinaria.modelo.Mascota;

public class PracticaVeterinaria {

    private static final LocalDate DIA_CITAS = LocalDate.of(2026, 11, 10);

    public static void main(String[] args) throws SQLException {

        comprobarConexion();

        ClinicaDAO dao = new ClinicaDAO();

        probarConsultas(dao);
        probarAltas(dao);
        probarRutinas(dao);
        probarTransaccion(dao);
        probarLotes(dao);
    }

    // ---------------------------------------------------------------
    // Parte 0. Comprobación de la conexión
    // ---------------------------------------------------------------

    private static void comprobarConexion() throws SQLException {

        String sql = "SELECT DATABASE(), CURRENT_USER(), VERSION(), (SELECT COUNT(*) FROM citas)";

        try (Connection con = ConexionBD.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            rs.next();
            System.out.println("Conexión correcta");
            System.out.println("  Base de datos: " + rs.getString(1));
            System.out.println("  Usuario:       " + rs.getString(2));
            System.out.println("  Servidor:      " + rs.getString(3));
            System.out.println("  Citas:         " + rs.getInt(4));
        }
    }

    // ---------------------------------------------------------------
    // Parte 2. Consultas
    // ---------------------------------------------------------------

    private static void probarConsultas(ClinicaDAO dao) throws SQLException {

        titulo("PARTE 2. Consultas");

        System.out.println("2.1 Veterinarios activos:");
        mostrarLista(dao.veterinariosActivos());

        mostrarMascotasDeCliente(dao, "12345678Z");
        mostrarMascotasDeCliente(dao, "45678901G");

        System.out.println("2.3 Citas pendientes de la veterinaria 1 el " + DIA_CITAS + ":");
        mostrarLista(dao.citasPendientes(1, DIA_CITAS));
    }

    private static void mostrarMascotasDeCliente(ClinicaDAO dao, String dni) throws SQLException {

        System.out.println("2.2 Mascotas del cliente " + dni + ":");
        mostrarLista(dao.mascotasDeCliente(dni));
    }

    // ---------------------------------------------------------------
    // Parte 3. Altas, modificaciones y control de errores
    // ---------------------------------------------------------------

    private static void probarAltas(ClinicaDAO dao) {

        titulo("PARTE 3. Altas y control de errores");

        System.out.println("3.1 Alta de 56789012B, Marta Vidal:");
        resultado(dao.altaCliente(new Cliente("56789012B", "Marta Vidal", "600999000", "marta.vidal@correo.es")));

        System.out.println("3.1 Alta de otro cliente con DNI 23456789D:");
        resultado(dao.altaCliente(new Cliente("23456789D", "Jorge Romero Copia", "600000001", "otro@correo.es")));

        System.out.println("3.2 Alta de Rocky, perro, cliente 2:");
        resultado(dao.altaMascota(new Mascota("Rocky", "perro", LocalDate.of(2024, 3, 1), 2)));

        System.out.println("3.2 Alta de Bolt, perro, cliente 99:");
        resultado(dao.altaMascota(new Mascota("Bolt", "perro", null, 99)));

        System.out.println("3.2 Alta de Nemo, pez, cliente 1:");
        resultado(dao.altaMascota(new Mascota("Nemo", "pez", null, 1)));

        System.out.println("3.3 Cambiar el teléfono de 34567890V:");
        resultado(dao.cambiarTelefono("34567890V", "611222333"));

        System.out.println("3.3 Cambiar el teléfono de 00000000T:");
        resultado(dao.cambiarTelefono("00000000T", "611222333"));
    }

    // ---------------------------------------------------------------
    // Parte 4. Procedimientos y funciones desde Java
    // ---------------------------------------------------------------

    private static void probarRutinas(ClinicaDAO dao) throws SQLException {

        titulo("PARTE 4. Procedimientos y funciones");

        probarPedirCita(dao);

        System.out.println("4.2 Edad de Luna: " + dao.edadMascota(1) + " | Edad de Nala: " + dao.edadMascota(6));

        System.out.println("4.3 Citas pendientes de Luna: " + dao.citasPendientesMascota(1));

        System.out.println("4.4 Ficha de Luna: " + dao.fichaMascota(1));
        System.out.println("    Ficha de la mascota 99: " + dao.fichaMascota(99));

        BigDecimal consulta = new BigDecimal("35");
        System.out.println("4.5 Consulta (35 €) para la clienta 1: " + dao.precioConDescuento(consulta, 1) + " €");
        System.out.println("    Consulta (35 €) para el cliente 2: " + dao.precioConDescuento(consulta, 2) + " €");

        System.out.println("4.6 Anular las citas de la veterinaria 1 el " + DIA_CITAS + ": " + dao.anularCitasDia(1, DIA_CITAS));
        System.out.println("    Citas pendientes que le quedan ese día: " + dao.citasPendientes(1, DIA_CITAS).size());
    }

    private static void probarPedirCita(ClinicaDAO dao) {

        System.out.println("4.1 Cita para Toby con el veterinario 2:");
        idCita(dao.pedirCita(4, 2, LocalDateTime.of(2026, 11, 20, 16, 30), "CONSULTA", "Revisión general"));

        System.out.println("    Cita con la veterinaria 3 (de baja):");
        idCita(dao.pedirCita(5, 3, LocalDateTime.of(2026, 11, 20, 10, 0), "CONSULTA", "Revisión de plumaje"));

        System.out.println("    Cita a una hora ocupada:");
        idCita(dao.pedirCita(2, 1, LocalDateTime.of(2026, 11, 10, 10, 0), "VACUNA", "Refuerzo"));

        System.out.println("    Cita fuera de horario (21:00):");
        idCita(dao.pedirCita(4, 2, LocalDateTime.of(2026, 11, 20, 21, 0), "CONSULTA", "Urgencia"));
    }

    // ---------------------------------------------------------------
    // Parte 5. Transacción
    // ---------------------------------------------------------------

    private static void probarTransaccion(ClinicaDAO dao) throws SQLException {

        titulo("PARTE 5. Transacción");

        System.out.println("5.1 Sergio Navas y su gata Bimba, cita con la veterinaria 1:");
        idCita(dao.altaClienteConCita(
                new Cliente("67890123B", "Sergio Navas", "622111222", "sergio.navas@correo.es"),
                new Mascota("Bimba", "gato", LocalDate.of(2022, 5, 8), 0),
                1, LocalDateTime.of(2026, 11, 25, 10, 0), "CONSULTA", "Primera visita"));

        System.out.println("5.2 Rosa Gil y su perra Lola, cita con la veterinaria 3 (de baja):");
        idCita(dao.altaClienteConCita(
                new Cliente("78901234X", "Rosa Gil", "633444555", "rosa.gil@correo.es"),
                new Mascota("Lola", "perro", LocalDate.of(2020, 1, 15), 0),
                3, LocalDateTime.of(2026, 11, 25, 11, 0), "CONSULTA", "Primera visita"));

        System.out.println("    Mascotas de 78901234X después del fallo:");
        mostrarLista(dao.mascotasDeCliente("78901234X"));

        System.out.println("    Alta de Rosa Gil otra vez:");
        resultado(dao.altaCliente(new Cliente("78901234X", "Rosa Gil", "633444555", "rosa.gil@correo.es")));
    }

    // ---------------------------------------------------------------
    // Parte 6. Lotes
    // ---------------------------------------------------------------

    private static void probarLotes(ClinicaDAO dao) throws SQLException {

        titulo("PARTE 6. Lotes");

        System.out.println("6.1 Thor (perro) y Pelusa (conejo) para el cliente 4:");
        resultado(dao.altaMascotas(List.of(
                new Mascota("Thor", "perro", LocalDate.of(2023, 7, 1), 4),
                new Mascota("Pelusa", "conejo", null, 4))));

        System.out.println("6.2 Rex (perro) y Dory (pez) para el cliente 4:");
        resultado(dao.altaMascotas(List.of(
                new Mascota("Rex", "perro", null, 4),
                new Mascota("Dory", "pez", null, 4))));

        System.out.println("    Mascotas del cliente 45678901G:");
        mostrarLista(dao.mascotasDeCliente("45678901G"));
    }

    // ---------------------------------------------------------------
    // Salida por pantalla
    // ---------------------------------------------------------------

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("===== " + texto + " =====");
    }

    private static void mostrarLista(List<?> lista) {

        if (lista.isEmpty()) {
            System.out.println("   (ninguna)");
            return;
        }

        for (Object elemento : lista) {
            System.out.println("   " + elemento);
        }
    }

    private static void resultado(boolean correcto) {
        System.out.println("   -> " + correcto);
    }

    private static void idCita(int id) {
        System.out.println("   -> id " + id);
    }
}
