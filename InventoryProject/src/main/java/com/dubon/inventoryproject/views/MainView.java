/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dubon.inventoryproject.views;

import com.dubon.inventoryproject.controllers.InventarioController;
import com.dubon.inventoryproject.models.ComputadoraModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author danie
 * 
 *         VIEW — Ventana principal de la aplicación
 *
 *         Layout:
 *         HEADER → título + botones (Agregar, Editar, Eliminar, CSV, Cargar
 *         Seed)
 *         CENTRO → JTable izquierda | Panel detalle derecha (imagen + datos)
 *         STATUS → barra de estado inferior
 */

public class MainView extends JFrame {

    // Paleta Catppuccin Mocha
    private static final Color BASE = new Color(0x1E1E2E);
    private static final Color MANTLE = new Color(0x181825);
    private static final Color SURFACE = new Color(0x313244);
    private static final Color OVERLAY = new Color(0x45475A);
    private static final Color TEXT = new Color(0xCDD6F4);
    private static final Color SUBTEXT = new Color(0xA6ADC8);
    private static final Color BLUE = new Color(0x89B4FA);
    private static final Color GREEN = new Color(0xA6E3A1);
    private static final Color RED = new Color(0xF38BA8);
    private static final Color YELLOW = new Color(0xF9E2AF);
    private static final Color LAVENDER = new Color(0xB4BEFE);

    private final InventarioController controller = new InventarioController();
    private final ComputadoraTableModel tableModel = new ComputadoraTableModel();

    private JTable tabla;
    private JLabel lblImagen;
    private JLabel lblDetalleMarca;
    private JLabel lblDetalleModelo;
    private JLabel lblDetallePrecio;
    private JLabel lblDetalleStock;
    private JLabel statusLabel;
    private int lastSelectedRow;

    // ── Constructor ───────────────────────────────────────────────────────────

    public MainView() {
        setTitle("💻 Inventario de Computadoras");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1150, 700);
        setMinimumSize(new Dimension(950, 600));
        setLocationRelativeTo(null);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                cerrarAplicacion();
            }
        });

        buildUI();
        cargarDatos();
    }

    // ── Construcción UI ───────────────────────────────────────────────────────

    private void buildUI() {
        getContentPane().setBackground(BASE);
        setLayout(new BorderLayout());
        add(buildHeader(), BorderLayout.NORTH);
        add(buildCentro(), BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);
    }

    // HEADER ──────────────────────────────────────────────────────────────────

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MANTLE);
        header.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, OVERLAY),
                new EmptyBorder(14, 20, 14, 20)));

        JLabel titulo = new JLabel("💻  Inventario de Computadoras");
        titulo.setFont(new Font("Monospaced", Font.BOLD, 20));
        titulo.setForeground(LAVENDER);
        header.add(titulo, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setOpaque(false);

        JButton btnAgregar = accionBoton("➕ Agregar", GREEN);
        JButton btnEditar = accionBoton("✏️ Editar", BLUE);
        JButton btnEliminar = accionBoton("🗑 Eliminar", RED);
        JButton btnCsv = accionBoton("📊 Exportar CSV", YELLOW);

        btnAgregar.addActionListener(e -> accionAgregar());
        btnEditar.addActionListener(e -> accionEditar());
        btnEliminar.addActionListener(e -> accionEliminar());
        btnCsv.addActionListener(e -> accionExportarCsv());

        btnPanel.add(btnAgregar);
        btnPanel.add(btnEditar);
        btnPanel.add(btnEliminar);
        btnPanel.add(new JSeparator(SwingConstants.VERTICAL));
        btnPanel.add(btnCsv);
        btnPanel.add(new JSeparator(SwingConstants.VERTICAL));

        header.add(btnPanel, BorderLayout.EAST);
        return header;
    }

    private JButton accionBoton(String texto, Color accentColor) {
        JButton b = new JButton(texto);
        b.setBackground(SURFACE);
        b.setForeground(accentColor);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentColor, 1),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                b.setBackground(accentColor);
                b.setForeground(BASE);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                b.setBackground(SURFACE);
                b.setForeground(accentColor);
            }
        });
        return b;
    }

    // CENTRO ──────────────────────────────────────────────────────────────────

    private JSplitPane buildCentro() {
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildPanelTabla(), buildPanelDetalle());
        split.setDividerLocation(660);
        split.setDividerSize(4);
        split.setBackground(OVERLAY);
        split.setBorder(null);
        return split;
    }

    private JPanel buildPanelTabla() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BASE);
        p.setBorder(new EmptyBorder(12, 16, 12, 8));

        tabla = new JTable(tableModel);
        tabla.setBackground(SURFACE);
        tabla.setForeground(TEXT);
        tabla.setGridColor(OVERLAY);
        tabla.setSelectionBackground(new Color(0x45475A));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setRowHeight(34);
        tabla.setFont(new Font("Monospaced", Font.PLAIN, 13));
        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(false);
        tabla.setIntercellSpacing(new Dimension(0, 1));
        tabla.setFillsViewportHeight(true);

        tabla.getTableHeader().setBackground(MANTLE);
        tabla.getTableHeader().setForeground(SUBTEXT);
        tabla.getTableHeader().setFont(new Font("Monospaced", Font.BOLD, 13));
        tabla.getTableHeader().setBorder(new MatteBorder(0, 0, 1, 0, OVERLAY));

        tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(130);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(70);

        tabla.getSelectionModel().addListSelectionListener(this::onSeleccion);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(OVERLAY, 1));
        scroll.getViewport().setBackground(SURFACE);
        scroll.setBackground(BASE);

        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildPanelDetalle() {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(MANTLE);
        p.setBorder(new EmptyBorder(16, 10, 16, 16));

        lblImagen = new JLabel("Sin imagen", SwingConstants.CENTER);
        lblImagen.setPreferredSize(new Dimension(300, 200));
        lblImagen.setBackground(SURFACE);
        lblImagen.setForeground(SUBTEXT);
        lblImagen.setFont(new Font("SansSerif", Font.ITALIC, 13));
        lblImagen.setOpaque(true);
        lblImagen.setBorder(BorderFactory.createLineBorder(OVERLAY, 1));
        p.add(lblImagen, BorderLayout.NORTH);

        JPanel datos = new JPanel(new GridLayout(4, 1, 0, 8));
        datos.setOpaque(false);
        datos.setBorder(new EmptyBorder(8, 4, 0, 4));

        lblDetalleMarca = detailLabel("—");
        lblDetalleModelo = detailLabel("—");
        lblDetallePrecio = detailLabel("—");
        lblDetalleStock = detailLabel("—");

        datos.add(detalleRow("🏷  Marca:", lblDetalleMarca));
        datos.add(detalleRow("📦 Modelo:", lblDetalleModelo));
        datos.add(detalleRow("💰 Precio:", lblDetallePrecio));
        datos.add(detalleRow("📊 Stock:", lblDetalleStock));

        p.add(datos, BorderLayout.CENTER);
        return p;
    }

    private JLabel detailLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(BLUE);
        l.setFont(new Font("Monospaced", Font.BOLD, 13));
        return l;
    }

    private JPanel detalleRow(String titulo, JLabel valor) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setOpaque(false);
        JLabel lbl = new JLabel(titulo + "  ");
        lbl.setForeground(SUBTEXT);
        lbl.setFont(new Font("Monospaced", Font.PLAIN, 12));
        row.add(lbl);
        row.add(valor);
        return row;
    }

    // STATUS BAR ──────────────────────────────────────────────────────────────

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(MANTLE);
        bar.setBorder(new CompoundBorder(
                new MatteBorder(1, 0, 0, 0, OVERLAY),
                new EmptyBorder(6, 16, 6, 16)));
        statusLabel = new JLabel("Listo.");
        statusLabel.setForeground(SUBTEXT);
        statusLabel.setFont(new Font("Monospaced", Font.PLAIN, 12));
        bar.add(statusLabel, BorderLayout.WEST);

        JLabel version = new JLabel("Java 25 • H2 in-memory • MVC");
        version.setForeground(OVERLAY);
        version.setFont(new Font("Monospaced", Font.PLAIN, 11));
        bar.add(version, BorderLayout.EAST);
        return bar;
    }

    // ── Lógica UI ─────────────────────────────────────────────────────────────

    private void cargarDatos() {
        try {
            List<ComputadoraModel> lista = controller.obtenerTodas();
            tableModel.setDatos(lista);
            estado("Inventario cargado: " + lista.size() + " registro(s).");
        } catch (SQLException e) {
            error("Error al cargar datos: " + e.getMessage());
        }
    }

    private void onSeleccion(ListSelectionEvent e) {
        if (e.getValueIsAdjusting())
            return;
        int fila = tabla.getSelectedRow();
        if (fila < 0)
            return;
        lastSelectedRow = fila;
        mostrarDetalle(tableModel.getComputadoraEn(fila));
    }

    private void mostrarDetalle(ComputadoraModel c) {
        lblDetalleMarca.setText(c.getMarca());
        lblDetalleModelo.setText(c.getModelo());
        lblDetallePrecio.setText(String.format("Q %.2f", c.getPrecio()));
        lblDetalleStock.setText(String.valueOf(c.getStock()));

        if (c.getImagenRuta() != null && !c.getImagenRuta().isBlank()) {
            File imgFile = new File(c.getImagenRuta());
            if (imgFile.exists()) {
                try {
                    BufferedImage original = ImageIO.read(imgFile);
                    int w = lblImagen.getWidth() > 0 ? lblImagen.getWidth() : 300;
                    int h = lblImagen.getHeight() > 0 ? lblImagen.getHeight() : 200;
                    Image scaled = original.getScaledInstance(w, h, Image.SCALE_SMOOTH);
                    lblImagen.setIcon(new ImageIcon(scaled));
                    lblImagen.setText(null);
                } catch (IOException ex) {
                    lblImagen.setIcon(null);
                    lblImagen.setText("⚠ No se pudo leer la imagen");
                }
            } else {
                lblImagen.setIcon(null);
                lblImagen.setText("⚠ Archivo no encontrado");
            }
        } else {
            lblImagen.setIcon(null);
            lblImagen.setText("Sin imagen");
        }
    }

    private void limpiarDetalle() {
        lblImagen.setIcon(null);
        lblImagen.setText("Sin imagen");
        lblDetalleMarca.setText("—");
        lblDetalleModelo.setText("—");
        lblDetallePrecio.setText("—");
        lblDetalleStock.setText("—");
    }

    // ── Acciones ──────────────────────────────────────────────────────────────

    private void accionAgregar() {
        FormDialog dlg = new FormDialog(this);
        dlg.setVisible(true);
        if (dlg.isConfirmado()) {
            try {
                controller.agregar(dlg.getComputadora());
                cargarDatos();
                estado("ComputadoraModel agregada correctamente.");
            } catch (Exception ex) {
                error(ex.getMessage());
            }
        }
    }

    private void accionEditar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            aviso("Selecciona una ComputadoraModel para editar.");
            return;
        }
        FormDialog dlg = new FormDialog(this, tableModel.getComputadoraEn(fila));
        dlg.setVisible(true);
        if (dlg.isConfirmado()) {
            try {
                controller.actualizar(dlg.getComputadora());
                cargarDatos();
                mostrarDetalle(tableModel.getComputadoraEn(lastSelectedRow));
                estado("ComputadoraModel actualizada.");
            } catch (Exception ex) {
                error(ex.getMessage());
            }
        }
    }

    private void accionEliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            aviso("Selecciona una ComputadoraModel para eliminar.");
            return;
        }
        ComputadoraModel c = tableModel.getComputadoraEn(fila);
        int resp = JOptionPane.showConfirmDialog(this,
                "¿Eliminar \"" + c + "\"?", "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (resp == JOptionPane.YES_OPTION) {
            try {
                controller.eliminar(c.getId());
                cargarDatos();
                limpiarDetalle();
                estado("ComputadoraModel eliminada.");
            } catch (SQLException ex) {
                error(ex.getMessage());
            }
        }
    }

    private void accionExportarCsv() {
        try {
            String ruta = controller.exportarCsv();
            JOptionPane.showMessageDialog(this,
                    "Reporte exportado exitosamente:\n" + ruta,
                    "Exportar CSV", JOptionPane.INFORMATION_MESSAGE);
            estado("CSV exportado: " + ruta);
        } catch (Exception ex) {
            error("Error al exportar CSV: " + ex.getMessage());
        }
    }

    // ── Cierre ────────────────────────────────────────────────────────────────

    private void cerrarAplicacion() {
        try {
            String ruta = controller.exportarSql();
            JOptionPane.showMessageDialog(this,
                    "Base de datos guardada en:\n" + ruta +
                            "\n\nSe cargará automáticamente en la próxima ejecución.",
                    "Datos guardados", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo exportar la base de datos:\n" + ex.getMessage(),
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
        } finally {
            dispose();
            System.exit(0);
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void estado(String msg) {
        statusLabel.setText(msg);
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void aviso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }

}
