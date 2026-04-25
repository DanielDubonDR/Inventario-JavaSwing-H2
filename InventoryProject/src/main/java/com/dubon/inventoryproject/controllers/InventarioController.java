/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dubon.inventoryproject.controllers;

import com.dubon.inventoryproject.database.ComputadoraDAO;
import com.dubon.inventoryproject.database.DatabaseManager;
import com.dubon.inventoryproject.models.ComputadoraModel;
import com.dubon.inventoryproject.utils.CsvExporter;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author danie
 */
public class InventarioController {

    private final ComputadoraDAO dao = new ComputadoraDAO();

    // ── CRUD ──────────────────────────────────────────────────────────────────

    public List<ComputadoraModel> obtenerTodas() throws SQLException {
        return dao.listarTodas();
    }

    public ComputadoraModel obtenerPorId(int id) throws SQLException {
        return dao.buscarPorId(id);
    }

    public void agregar(ComputadoraModel c) throws SQLException {
        validar(c);
        dao.insertar(c);
    }

    public void actualizar(ComputadoraModel c) throws SQLException {
        validar(c);
        dao.actualizar(c);
    }

    public void eliminar(int id) throws SQLException {
        dao.eliminar(id);
    }

    // ── SEED EXTERNO ──────────────────────────────────────────────────────────

    /**
     * Carga un seed .sql seleccionado por el usuario vía JFileChooser.
     * Delega a DatabaseManager que limpia la BD y ejecuta el script.
     *
     * @param sqlFile archivo .sql elegido por el usuario
     */
    public void cargarSeedDesdeArchivo(File sqlFile) throws SQLException, IOException {
        DatabaseManager.cargarSeedDesdeArchivo(sqlFile);
    }

    // ── EXPORTACIONES ────────────────────────────────────────────────────────

    /** Exporta el inventario a CSV y devuelve la ruta del archivo. */
    public String exportarCsv() throws SQLException, IOException {
        return CsvExporter.exportar(dao.listarTodas());
    }

    /** Exporta la BD completa a SQL (para persistencia entre sesiones). */
    public String exportarSql() throws SQLException, IOException {
        return DatabaseManager.exportDatabase();
    }

    // ── VALIDACIONES ─────────────────────────────────────────────────────────

    private void validar(ComputadoraModel c) {
        if (c.getMarca() == null || c.getMarca().isBlank())
            throw new IllegalArgumentException("La marca no puede estar vacía.");
        if (c.getModelo() == null || c.getModelo().isBlank())
            throw new IllegalArgumentException("El modelo no puede estar vacío.");
        if (c.getPrecio() < 0)
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        if (c.getStock() < 0)
            throw new IllegalArgumentException("El stock no puede ser negativo.");
    }

}
