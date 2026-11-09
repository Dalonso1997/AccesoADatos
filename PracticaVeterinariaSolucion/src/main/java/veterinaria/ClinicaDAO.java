package veterinaria;

import java.math.BigDecimal;
import java.sql.BatchUpdateException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import veterinaria.modelo.Cita;
import veterinaria.modelo.Cliente;
import veterinaria.modelo.Mascota;
import veterinaria.modelo.Veterinario;

public class ClinicaDAO {

    private static final int ERROR_DUPLICADO = 1062;
    private static final int ERROR_CLAVE_AJENA = 1452;
    private static final int ERROR_CLAVE_AJENA_SIN_DETALLE = 1216;
    private static final int ERROR_CHECK = 3819;

    // =====================================================================
    // PARTE 2. Consultas
    // =====================================================================

    public List<Veterinario> veterinariosActivos() throws SQLException {

        String sql = "SELECT id_veterinario, nombre, especialidad, activo "
                + "FROM veterinarios WHERE activo = TRUE ORDER BY nombre";

        List<Veterinario> veterinarios = new ArrayList<>();

        try (Connection con = ConexionBD.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                veterinarios.add(leerVeterinario(rs));
            }
        }

        return veterinarios;
    }

    public List<Mascota> mascotasDeCliente(String dni) throws SQLException {

        String sql = "SELECT m.id_mascota, m.nombre, m.especie, m.fecha_nacimiento, m.id_cliente "
                + "FROM mascotas m JOIN clientes c ON c.id_cliente = m.id_cliente "
                + "WHERE c.dni = ? ORDER BY m.nombre";

        List<Mascota> mascotas = new ArrayList<>();

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dni);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mascotas.add(leerMascota(rs));
                }
            }
        }

        return mascotas;
    }

    public List<Cita> citasPendientes(int idVeterinario, LocalDate dia) throws SQLException {

        String sql = "SELECT c.id_cita, c.id_mascota, m.nombre, c.id_veterinario, c.fecha_hora, "
                + "c.tipo, c.motivo, c.precio, c.estado "
                + "FROM citas c JOIN mascotas m ON m.id_mascota = c.id_mascota "
                + "WHERE c.id_veterinario = ? AND DATE(c.fecha_hora) = ? AND c.estado = 'PENDIENTE' "
                + "ORDER BY c.fecha_hora";

        List<Cita> citas = new ArrayList<>();

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idVeterinario);
            ps.setDate(2, Date.valueOf(dia));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    citas.add(leerCita(rs));
                }
            }
        }

        return citas;
    }

    private Veterinario leerVeterinario(ResultSet rs) throws SQLException {
        return new Veterinario(
                rs.getInt("id_veterinario"),
                rs.getString("nombre"),
                rs.getString("especialidad"),
                rs.getBoolean("activo"));
    }

    private Mascota leerMascota(ResultSet rs) throws SQLException {

        Date nacimiento = rs.getDate("fecha_nacimiento");
        LocalDate fechaNacimiento = (nacimiento == null) ? null : nacimiento.toLocalDate();

        return new Mascota(
                rs.getInt("id_mascota"),
                rs.getString("nombre"),
                rs.getString("especie"),
                fechaNacimiento,
                rs.getInt("id_cliente"));
    }

    private Cita leerCita(ResultSet rs) throws SQLException {
        return new Cita(
                rs.getInt("id_cita"),
                rs.getInt("id_mascota"),
                rs.getString("nombre"),
                rs.getInt("id_veterinario"),
                rs.getTimestamp("fecha_hora").toLocalDateTime(),
                rs.getString("tipo"),
                rs.getString("motivo"),
                rs.getBigDecimal("precio"),
                rs.getString("estado"));
    }

    // =====================================================================
    // PARTE 3. Altas, modificaciones y control de errores
    // =====================================================================

    public boolean altaCliente(Cliente c) {

        try (Connection con = ConexionBD.getConexion()) {

            c.setId(insertarCliente(con, c));
            return true;

        } catch (SQLException e) {

            if (e.getErrorCode() == ERROR_DUPLICADO) {
                System.out.println("   No se puede dar de alta: ya existe un cliente con ese DNI o email");
            } else {
                informarError("No se puede dar de alta", e);
            }
            return false;
        }
    }

    public boolean altaMascota(Mascota m) {

        try (Connection con = ConexionBD.getConexion()) {

            m.setId(insertarMascota(con, m));
            return true;

        } catch (SQLException e) {

            switch (e.getErrorCode()) {
                case ERROR_CLAVE_AJENA, ERROR_CLAVE_AJENA_SIN_DETALLE ->
                    System.out.println("   No se puede dar de alta: el cliente no existe");
                case ERROR_CHECK ->
                    System.out.println("   No se puede dar de alta: la especie no es válida");
                default ->
                    informarError("No se puede dar de alta", e);
            }
            return false;
        }
    }

    public boolean cambiarTelefono(String dni, String telefono) {

        String sql = "UPDATE clientes SET telefono = ? WHERE dni = ?";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, telefono);
            ps.setString(2, dni);

            int filasModificadas = ps.executeUpdate();
            return filasModificadas > 0;

        } catch (SQLException e) {
            informarError("No se puede cambiar el teléfono", e);
            return false;
        }
    }

    private int insertarCliente(Connection con, Cliente c) throws SQLException {

        String sql = "INSERT INTO clientes (dni, nombre, telefono, email) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getDni());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getEmail());
            ps.executeUpdate();

            return leerClaveGenerada(ps);
        }
    }

    private int insertarMascota(Connection con, Mascota m) throws SQLException {

        String sql = "INSERT INTO mascotas (nombre, especie, fecha_nacimiento, id_cliente) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            rellenarMascota(ps, m);
            ps.executeUpdate();

            return leerClaveGenerada(ps);
        }
    }

    private void rellenarMascota(PreparedStatement ps, Mascota m) throws SQLException {

        ps.setString(1, m.getNombre());
        ps.setString(2, m.getEspecie());

        if (m.getFechaNacimiento() == null) {
            ps.setNull(3, Types.DATE);
        } else {
            ps.setDate(3, Date.valueOf(m.getFechaNacimiento()));
        }

        ps.setInt(4, m.getIdCliente());
    }

    private int leerClaveGenerada(PreparedStatement ps) throws SQLException {

        try (ResultSet claves = ps.getGeneratedKeys()) {
            claves.next();
            return claves.getInt(1);
        }
    }

    // =====================================================================
    // PARTE 4. Procedimientos y funciones desde Java
    // =====================================================================

    public int pedirCita(int idMascota, int idVeterinario, LocalDateTime fechaHora, String tipo, String motivo) {

        try (Connection con = ConexionBD.getConexion()) {
            return llamarPedirCita(con, idMascota, idVeterinario, fechaHora, tipo, motivo);

        } catch (SQLException e) {
            informarError("Cita no concedida", e);
            return -1;
        }
    }

    public Integer edadMascota(int idMascota) throws SQLException {

        try (Connection con = ConexionBD.getConexion();
             CallableStatement cs = con.prepareCall("{? = call edad_mascota(?)}")) {

            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, idMascota);
            cs.execute();

            int edad = cs.getInt(1);
            if (cs.wasNull()) {
                return null;
            }
            return edad;
        }
    }

    public int citasPendientesMascota(int idMascota) throws SQLException {

        try (Connection con = ConexionBD.getConexion();
             CallableStatement cs = con.prepareCall("{? = call citas_pendientes(?)}")) {

            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, idMascota);
            cs.execute();

            return cs.getInt(1);
        }
    }

    public String fichaMascota(int idMascota) {

        try (Connection con = ConexionBD.getConexion();
             CallableStatement cs = con.prepareCall("{call ficha_mascota(?, ?, ?)}")) {

            cs.setInt(1, idMascota);
            cs.registerOutParameter(2, Types.VARCHAR);
            cs.registerOutParameter(3, Types.INTEGER);
            cs.execute();

            return "Dueño: " + cs.getString(2) + " | Citas: " + cs.getInt(3);

        } catch (SQLException e) {
            return "Sin ficha: " + describirError(e);
        }
    }

    public BigDecimal precioConDescuento(BigDecimal precio, int idCliente) throws SQLException {

        try (Connection con = ConexionBD.getConexion();
             CallableStatement cs = con.prepareCall("{call aplicar_descuento(?, ?)}")) {

            cs.setBigDecimal(1, precio);
            cs.registerOutParameter(1, Types.DECIMAL);
            cs.setInt(2, idCliente);
            cs.execute();

            return cs.getBigDecimal(1);
        }
    }

    public int anularCitasDia(int idVeterinario, LocalDate dia) {

        try (Connection con = ConexionBD.getConexion();
             CallableStatement cs = con.prepareCall("{call anular_citas_dia(?, ?, ?)}")) {

            cs.setInt(1, idVeterinario);
            cs.setDate(2, Date.valueOf(dia));
            cs.registerOutParameter(3, Types.INTEGER);
            cs.execute();

            return cs.getInt(3);

        } catch (SQLException e) {
            informarError("No se pueden anular las citas", e);
            return -1;
        }
    }

    private int llamarPedirCita(Connection con, int idMascota, int idVeterinario, LocalDateTime fechaHora,
                                String tipo, String motivo) throws SQLException {

        try (CallableStatement cs = con.prepareCall("{call pedir_cita(?, ?, ?, ?, ?, ?)}")) {

            cs.setInt(1, idMascota);
            cs.setInt(2, idVeterinario);
            cs.setTimestamp(3, Timestamp.valueOf(fechaHora));
            cs.setString(4, tipo);
            cs.setString(5, motivo);
            cs.registerOutParameter(6, Types.INTEGER);
            cs.execute();

            return cs.getInt(6);
        }
    }

    // =====================================================================
    // PARTE 5. Transacción
    // =====================================================================

    public int altaClienteConCita(Cliente cliente, Mascota mascota, int idVeterinario,
                                  LocalDateTime fechaHora, String tipo, String motivo) {

        try (Connection con = ConexionBD.getConexion()) {

            con.setAutoCommit(false);

            try {
                int idCliente = insertarCliente(con, cliente);

                mascota.setIdCliente(idCliente);
                int idMascota = insertarMascota(con, mascota);

                int idCita = llamarPedirCita(con, idMascota, idVeterinario, fechaHora, tipo, motivo);

                con.commit();

                cliente.setId(idCliente);
                mascota.setId(idMascota);
                return idCita;

            } catch (SQLException e) {
                con.rollback();
                informarError("Alta cancelada, se deshace todo", e);
                return -1;
            }

        } catch (SQLException e) {
            informarError("Error de conexión", e);
            return -1;
        }
    }

    // =====================================================================
    // PARTE 6. Lotes
    // =====================================================================

    public boolean altaMascotas(List<Mascota> mascotas) {

        String sql = "INSERT INTO mascotas (nombre, especie, fecha_nacimiento, id_cliente) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConexion()) {

            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                for (Mascota m : mascotas) {
                    rellenarMascota(ps, m);
                    ps.addBatch();
                }

                ps.executeBatch();
                con.commit();
                return true;

            } catch (BatchUpdateException e) {
                con.rollback();
                informarError("Lote cancelado, no se inserta ninguna", e);
                System.out.println("   Resultados del lote: " + Arrays.toString(e.getUpdateCounts()));
                return false;

            } catch (SQLException e) {
                con.rollback();
                informarError("Lote cancelado, no se inserta ninguna", e);
                return false;
            }

        } catch (SQLException e) {
            informarError("Error de conexión", e);
            return false;
        }
    }

    // =====================================================================
    // Errores
    // =====================================================================

    private void informarError(String contexto, SQLException e) {
        System.out.println("   " + contexto + ": " + describirError(e));
    }

    private String describirError(SQLException e) {
        return "[" + e.getSQLState() + " / " + e.getErrorCode() + "] " + e.getMessage();
    }
}
