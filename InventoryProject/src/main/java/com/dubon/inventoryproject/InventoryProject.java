/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.dubon.inventoryproject;

import com.dubon.inventoryproject.database.DatabaseManager;
import com.dubon.inventoryproject.views.MainView;
import com.dubon.inventoryproject.views.SeedChooserDialog;
import javax.swing.*;
import java.io.File;
import java.sql.SQLException;

/**
 *
 * @author danie
 */
public class InventoryProject {

    public static void main(String[] args) {
        // ── 1. Look & Feel ───────────────────────────────────────────────────
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException
                | UnsupportedLookAndFeelException ignored) {
        }

        // ── 2. Conexión H2 (sin datos aún) ───────────────────────────────────
        try {
            DatabaseManager.getConnection(); // solo abre la conexión
            System.out.println("Conexion abierta");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "No se pudo conectar a la base de datos H2:\n" + e.getMessage(),
                    "Error Fatal", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // ── 3. Diálogo de selección de seed (en el EDT) ───────────────────────
        // Necesita correr en el EDT porque crea componentes Swing
        SwingUtilities.invokeLater(() -> {

            boolean hayExport = new File("inventario_export.sql").exists();

            SeedChooserDialog chooser = new SeedChooserDialog(hayExport);
            chooser.setVisible(true); // bloquea hasta que el usuario elige (es modal)

            // ── 4. Cargar datos según la opción elegida ───────────────────────
            switch (chooser.getOpcion()) {

                case ARCHIVO -> {
                    // Usuario seleccionó un .sql con JFileChooser
                    try {
                        DatabaseManager.cargarSeedDesdeArchivo(chooser.getArchivo());
                        System.out.println("[App] Seed externo cargado.");
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null,
                                "Error al cargar el archivo SQL:\n" + e.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                    }
                }

                case EXPORT -> {
                    // Cargar inventario_export.sql (sesión anterior)
                    try {
                        DatabaseManager.cargarSeedDesdeArchivo(new File("inventario_export.sql"));
                        System.out.println("[App] Sesión anterior cargada.");
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null,
                                "Error al cargar la sesión anterior:\n" + e.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                    }
                }

                case CANCELAR -> {
                    // El usuario cerró el diálogo o presionó Salir
                    System.out.println("[App] El usuario canceló el inicio.");
                    System.exit(0);
                }
            }

            // ── 5. Abrir ventana principal ────────────────────────────────────
            MainView ventana = new MainView();
            ventana.setVisible(true);
        });
    }
}
