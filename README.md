# Acceso a Datos — DAM2

![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8-4479A1?logo=mysql&logoColor=white)
![JDBC](https://img.shields.io/badge/JDBC-DAO-6DB33F)
![Maven](https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=white)

Prácticas del módulo **Acceso a Datos** de 2º de DAM (curso 2026/27): persistencia en ficheros y acceso a bases de datos relacionales con JDBC.

## 📂 Proyectos

| Proyecto | Qué trabaja |
|---|---|
| [`PracticaVeterinariaSolucion`](PracticaVeterinariaSolucion) | Gestión de una clínica veterinaria sobre MySQL con **JDBC** y patrón **DAO** |
| [`Hoja05Ciudades`](Hoja05Ciudades) | Lectura de un CSV y serialización de objetos en fichero (`ObjectOutputStream` sin cabecera) |

## 🐾 Práctica Veterinaria

Aplicación de consola que trabaja con una base de datos de clientes, mascotas, veterinarios y citas.

**Qué incluye:**
- Conexión centralizada en `ConexionBD`, que lee la configuración de `db.properties`
- `ClinicaDAO` con consultas parametrizadas (`PreparedStatement`) y mapeo a clases del modelo
- Altas y modificaciones, con control de errores
- Llamadas a **funciones y procedimientos almacenados** (`CallableStatement`)
- **Transacción** manual (`commit` / `rollback`): alta de cliente + mascota + cita en un único bloque
- **Inserción por lotes** (`addBatch` / `executeBatch`)

```
PracticaVeterinariaSolucion/
├── sql/
│   ├── veterinaria.sql     # Crea la BD, las tablas, datos de prueba y el usuario
│   └── parte1.sql          # Funciones y procedimientos almacenados
└── src/main/java/veterinaria/
    ├── ConexionBD.java
    ├── ClinicaDAO.java
    ├── PracticaVeterinaria.java   # main: prueba cada parte
    └── modelo/             # Cliente, Mascota, Veterinario, Cita
```

### Cómo ejecutarla

```bash
# 1. Crear la base de datos (crea también un usuario local de pruebas)
mysql -u root -p < PracticaVeterinariaSolucion/sql/veterinaria.sql
mysql -u root -p < PracticaVeterinariaSolucion/sql/parte1.sql

# 2. Compilar y ejecutar
cd PracticaVeterinariaSolucion
mvn clean package
mvn exec:java
```

> La contraseña de `db.properties` es la del usuario local de pruebas que crea `veterinaria.sql`, no una credencial real.

## 🏙️ Hoja 05 — Ciudades

Lee `ciudades.csv`, crea objetos `City` y los guarda y recupera de un fichero binario. Incluye una subclase de `ObjectOutputStream` que no escribe la cabecera, para poder añadir objetos a un fichero que ya existe.

```bash
cd Hoja05Ciudades
mvn clean package
mvn exec:java   # pide por teclado el CSV a cargar (ciudades.csv)
```

---

👤 **David Alonso Casaiz** · [GitHub](https://github.com/Dalonso1997)
