/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dubon.inventoryproject.views;

import com.dubon.inventoryproject.models.ComputadoraModel;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author danie
 * 
 * VIEW — TableModel para la JTable del inventario
 *
 * Mapea la lista de ComputadoraModel a filas/columnas de Swing.
 */
public class ComputadoraTableModel extends AbstractTableModel {
    
    private static final String[] COLUMNAS = {"ID", "Marca", "Modelo", "Precio (Q)", "Stock"};

    private List<ComputadoraModel> datos = new ArrayList<>();

    // ── API pública ───────────────────────────────────────────────────────────

    public void setDatos(List<ComputadoraModel> lista) {
        this.datos = lista;
        fireTableDataChanged();
    }

    public ComputadoraModel getComputadoraEn(int fila) {
        return datos.get(fila);
    }

    // ── AbstractTableModel ────────────────────────────────────────────────────

    @Override public int    getRowCount()    { return datos.size(); }
    @Override public int    getColumnCount() { return COLUMNAS.length; }
    @Override public String getColumnName(int col) { return COLUMNAS[col]; }

    @Override
    public Object getValueAt(int row, int col) {
        ComputadoraModel c = datos.get(row);
        return switch (col) {
            case 0 -> c.getId();
            case 1 -> c.getMarca();
            case 2 -> c.getModelo();
            case 3 -> String.format("Q %.2f", c.getPrecio());
            case 4 -> c.getStock();
            default -> "";
        };
    }

    @Override
    public boolean isCellEditable(int row, int col) { return false; }
    
}
