/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dubon.inventoryproject.database;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author danie
 */
public class DatabaseManager {

    private static final String URL = "jdbc:h2:mem:inventariodb;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.h2.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver H2 no encontrado: " + e.getMessage());
            }
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    /**
     * Limpia la BD y ejecuta el script .sql seleccionado por el usuario
     * (ya sea un seed externo o el inventario_export.sql de sesión anterior).
     */
    public static void cargarSeedDesdeArchivo(File sqlFile) throws SQLException, IOException {
        Connection conn = getConnection();
        limpiarTablas(conn);
        String sql = new String(java.nio.file.Files.readAllBytes(sqlFile.toPath()), StandardCharsets.UTF_8);
        executeSqlScript(conn, sql);
        System.out.println("[DB] Seed cargado desde: " + sqlFile.getAbsolutePath());
    }

    private static void limpiarTablas(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS computadoras");
        }
    }

    /**
     * Parser de scripts SQL con comentarios.
     *
     */
    private static void executeSqlScript(Connection conn, String script) throws SQLException {
        // Paso 1: eliminar líneas de comentario antes de hacer cualquier split
        StringBuilder limpio = new StringBuilder();
        for (String linea : script.split("\r?\n")) {
            String t = linea.trim();
            // Saltar líneas vacías y líneas que son puro comentario
            if (t.isEmpty() || t.startsWith("--") || t.startsWith("//"))
                continue;
            limpio.append(linea).append("\n");
        }

        // Paso 2: dividir el SQL limpio por ";" y ejecutar cada sentencia
        try (Statement stmt = conn.createStatement()) {
            for (String raw : limpio.toString().split(";")) {
                String s = raw.trim();
                if (s.isEmpty())
                    continue;
                try {
                    stmt.execute(s);
                } catch (SQLException e) {
                    System.err.println("[DB] Error en sentencia: " + e.getMessage());
                    System.err.println("[DB] Sentencia: " + s.substring(0, Math.min(s.length(), 120)));
                }
            }
        }
    }

    public static String exportDatabase() throws SQLException, IOException {
        String fileName = "inventario_export.sql";
        Connection conn = getConnection();
        StringBuilder sb = new StringBuilder();
        sb.append("-- INVENTORY APP - EXPORT AUTOMATICO\n");
        sb.append("-- Generado: ")
                .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n\n");
        sb.append("DROP TABLE IF EXISTS computadoras;\n\n");
        sb.append("CREATE TABLE IF NOT EXISTS computadoras (\n");
        sb.append("    id INT AUTO_INCREMENT PRIMARY KEY,\n");
        sb.append("    marca VARCHAR(100) NOT NULL,\n");
        sb.append("    modelo VARCHAR(100) NOT NULL,\n");
        sb.append("    precio DECIMAL(10,2) NOT NULL,\n");
        sb.append("    stock INT NOT NULL DEFAULT 0,\n");
        sb.append("    imagen_ruta VARCHAR(500)\n);\n\n");
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(
                        "SELECT marca,modelo,precio,stock,imagen_ruta FROM computadoras ORDER BY id")) {
            while (rs.next()) {
                String img = rs.getString("imagen_ruta");
                String imgVal = (img == null) ? "NULL" : "'" + img.replace("'", "''") + "'";
                sb.append(String.format(
                        "INSERT INTO computadoras (marca,modelo,precio,stock,imagen_ruta) VALUES ('%s','%s',%.2f,%d,%s);\n",
                        rs.getString("marca").replace("'", "''"),
                        rs.getString("modelo").replace("'", "''"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        imgVal));
            }
        }
        try (FileWriter fw = new FileWriter(fileName, StandardCharsets.UTF_8)) {
            fw.write(sb.toString());
        }
        System.out.println("[DB] BD exportada a: " + new File(fileName).getAbsolutePath());
        return new File(fileName).getAbsolutePath();
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("[DB] Error al cerrar: " + e.getMessage());
        }
    }
}
