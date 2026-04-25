/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dubon.inventoryproject.database;

import com.dubon.inventoryproject.models.ComputadoraModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author danie
 */
public class ComputadoraDAO {
    // ── CREATE ────────────────────────────────────────────────────────────────

    public void insertar(ComputadoraModel c) throws SQLException {
        String sql = "INSERT INTO computadoras (marca, modelo, precio, stock, imagen_ruta) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getMarca());
            ps.setString(2, c.getModelo());
            ps.setDouble(3, c.getPrecio());
            ps.setInt(4, c.getStock());
            ps.setString(5, c.getImagenRuta());   // puede ser null → NULL en BD

            ps.executeUpdate();

            // Recuperar el ID generado y asignarlo al objeto
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    c.setId(keys.getInt(1));
                }
            }
        }
    }

    // ── READ ──────────────────────────────────────────────────────────────────

    public List<ComputadoraModel> listarTodas() throws SQLException {
        List<ComputadoraModel> lista = new ArrayList<>();
        String sql = "SELECT id, marca, modelo, precio, stock, imagen_ruta FROM computadoras ORDER BY id";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public ComputadoraModel buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, marca, modelo, precio, stock, imagen_ruta FROM computadoras WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    public void actualizar(ComputadoraModel c) throws SQLException {
        String sql = "UPDATE computadoras SET marca=?, modelo=?, precio=?, stock=?, imagen_ruta=? WHERE id=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getMarca());
            ps.setString(2, c.getModelo());
            ps.setDouble(3, c.getPrecio());
            ps.setInt(4, c.getStock());
            ps.setString(5, c.getImagenRuta());
            ps.setInt(6, c.getId());

            ps.executeUpdate();
        }
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM computadoras WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // ── HELPER ────────────────────────────────────────────────────────────────

    private ComputadoraModel mapear(ResultSet rs) throws SQLException {
        return new ComputadoraModel(
            rs.getInt("id"),
            rs.getString("marca"),
            rs.getString("modelo"),
            rs.getDouble("precio"),
            rs.getInt("stock"),
            rs.getString("imagen_ruta")   // puede ser null
        );
    }
}
