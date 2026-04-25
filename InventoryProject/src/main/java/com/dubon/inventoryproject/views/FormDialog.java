/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dubon.inventoryproject.views;

import com.dubon.inventoryproject.models.ComputadoraModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

/**
 *
 * @author danie
 * 
 * VIEW — Diálogo de alta / edición de una ComputadoraModel
 *
 * Muestra un formulario modal con los 4 atributos + imagen.
 * Devuelve una ComputadoraModel con los datos capturados, o null si el
 * usuario canceló.
 */

public class FormDialog extends JDialog {
    
    // Campos del formulario
    private final JTextField txtMarca  = new JTextField(20);
    private final JTextField txtModelo = new JTextField(20);
    private final JTextField txtPrecio = new JTextField(20);
    private final JTextField txtStock  = new JTextField(20);
    private final JTextField txtImagen = new JTextField(20);

    private boolean confirmado = false;
    private ComputadoraModel computadoraEditada = null;   // null → alta nueva

    // ── Constructores ─────────────────────────────────────────────────────────

    /** Constructor para ALTA (formulario vacío).
     * @param owner */
    public FormDialog(Frame owner) {
        this(owner, null);
    }

    /** Constructor para EDICIÓN (pre-carga los datos).
     * @param owner
     * @param existente */
    public FormDialog(Frame owner, ComputadoraModel existente) {
        super(owner, existente == null ? "➕ Agregar ComputadoraModel" : "✏️ Editar ComputadoraModel", true);
        this.computadoraEditada = existente;

        if (existente != null) {
            txtMarca.setText(existente.getMarca());
            txtModelo.setText(existente.getModelo());
            txtPrecio.setText(String.valueOf(existente.getPrecio()));
            txtStock.setText(String.valueOf(existente.getStock()));
            if (existente.getImagenRuta() != null) txtImagen.setText(existente.getImagenRuta());
        }

        buildUI();
        pack();
        setLocationRelativeTo(owner);
    }

    // ── UI ────────────────────────────────────────────────────────────────────

    private void buildUI() {
        setResizable(false);

        // Panel principal con padding
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(new EmptyBorder(20, 24, 16, 24));
        main.setBackground(new Color(0x1E1E2E));
        setContentPane(main);

        // Formulario
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(6, 4, 6, 4);
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.HORIZONTAL;

        agregarFila(form, gbc, 0, "Marca:",   txtMarca);
        agregarFila(form, gbc, 1, "Modelo:",  txtModelo);
        agregarFila(form, gbc, 2, "Precio:",  txtPrecio);
        agregarFila(form, gbc, 3, "Stock:",   txtStock);

        // Fila imagen (campo + botón explorador)
        JLabel lblImg = label("Imagen:");
        gbc.gridx = 0; gbc.gridy = 4;
        form.add(lblImg, gbc);

        JPanel imgRow = new JPanel(new BorderLayout(6, 0));
        imgRow.setOpaque(false);
        estilizarCampo(txtImagen);
        txtImagen.setEditable(false);
        imgRow.add(txtImagen, BorderLayout.CENTER);

        JButton btnExplorer = boton("📂 Buscar");
        btnExplorer.addActionListener(e -> seleccionarImagen());
        imgRow.add(btnExplorer, BorderLayout.EAST);

        gbc.gridx = 1;
        form.add(imgRow, gbc);

        main.add(form, BorderLayout.CENTER);

        // Botones OK / Cancelar
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botones.setOpaque(false);

        JButton btnOk     = boton("✔ Guardar");
        JButton btnCancel = boton("✘ Cancelar");

        btnOk.setBackground(new Color(0x89B4FA));
        btnOk.setForeground(new Color(0x1E1E2E));

        btnOk.addActionListener(e -> {
            if (validar()) {
                confirmado = true;
                dispose();
            }
        });
        btnCancel.addActionListener(e -> dispose());

        botones.add(btnCancel);
        botones.add(btnOk);
        main.add(botones, BorderLayout.SOUTH);
    }

    private void agregarFila(JPanel form, GridBagConstraints gbc, int row, String texto, JTextField campo) {
        gbc.gridx = 0; gbc.gridy = row;
        form.add(label(texto), gbc);
        gbc.gridx = 1;
        estilizarCampo(campo);
        form.add(campo, gbc);
    }

    private JLabel label(String texto) {
        JLabel l = new JLabel(texto);
        l.setForeground(new Color(0xCDD6F4));
        l.setFont(new Font("Monospaced", Font.PLAIN, 13));
        return l;
    }

    private void estilizarCampo(JTextField campo) {
        campo.setBackground(new Color(0x313244));
        campo.setForeground(new Color(0xCDD6F4));
        campo.setCaretColor(new Color(0x89B4FA));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0x45475A), 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        campo.setFont(new Font("Monospaced", Font.PLAIN, 13));
    }

    private JButton boton(String texto) {
        JButton b = new JButton(texto);
        b.setBackground(new Color(0x313244));
        b.setForeground(new Color(0xCDD6F4));
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0x45475A), 1),
            BorderFactory.createEmptyBorder(6, 14, 6, 14)
        ));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return b;
    }

    // ── Lógica ────────────────────────────────────────────────────────────────

    private void seleccionarImagen() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Seleccionar imagen de la ComputadoraModel");
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Imágenes (PNG, JPG, GIF)", "png", "jpg", "jpeg", "gif"));

        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            txtImagen.setText(f.getAbsolutePath());
        }
    }

    private boolean validar() {
        if (txtMarca.getText().isBlank()) {
            error("El campo Marca es obligatorio."); return false;
        }
        if (txtModelo.getText().isBlank()) {
            error("El campo Modelo es obligatorio."); return false;
        }
        try {
            double p = Double.parseDouble(txtPrecio.getText().trim());
            if (p < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            error("El precio debe ser un número positivo."); return false;
        }
        try {
            int s = Integer.parseInt(txtStock.getText().trim());
            if (s < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            error("El stock debe ser un número entero positivo."); return false;
        }
        return true;
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validación", JOptionPane.WARNING_MESSAGE);
    }

    // ── Resultado ─────────────────────────────────────────────────────────────

    /** Devuelve true si el usuario presionó Guardar.
     * @return  */
    public boolean isConfirmado() { return confirmado; }

    /**
     * Construye y devuelve una ComputadoraModel con los datos del formulario.
     * Si es edición, reutiliza el ID original.
     * @return 
     */
    public ComputadoraModel getComputadora() {
        int    id     = (computadoraEditada != null) ? computadoraEditada.getId() : 0;
        String marca  = txtMarca.getText().trim();
        String modelo = txtModelo.getText().trim();
        double precio = Double.parseDouble(txtPrecio.getText().trim());
        int    stock  = Integer.parseInt(txtStock.getText().trim());
        String imagen = txtImagen.getText().isBlank() ? null : txtImagen.getText().trim();

        return new ComputadoraModel(id, marca, modelo, precio, stock, imagen);
    }
}
