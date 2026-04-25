package com.dubon.inventoryproject.views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

/**
 *
 * @author danie
 */
public class SeedChooserDialog extends JDialog {

    // ── Paleta ────────────────────────────────────────────────────────────────
    private static final Color BASE = new Color(0x1E1E2E);
    private static final Color MANTLE = new Color(0x181825);
    private static final Color SURFACE = new Color(0x313244);
    private static final Color OVERLAY = new Color(0x45475A);
    private static final Color SUBTEXT = new Color(0xA6ADC8);
    private static final Color BLUE = new Color(0x89B4FA);
    private static final Color GREEN = new Color(0xA6E3A1);
    private static final Color RED = new Color(0xF38BA8);
    private static final Color LAVENDER = new Color(0xB4BEFE);

    // ── Resultado ─────────────────────────────────────────────────────────────
    public enum Opcion {
        ARCHIVO, EXPORT, CANCELAR
    }

    private Opcion opcion = Opcion.CANCELAR;
    private File archivo = null;

    private final boolean hayExport;

    // ── Constructor ───────────────────────────────────────────────────────────

    public SeedChooserDialog(boolean hayExport) {
        super((Frame) null, "💻 Inventario de Computadoras — Inicio", true);
        this.hayExport = hayExport;
        buildUI();
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    // ── UI ────────────────────────────────────────────────────────────────────

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BASE);
        setContentPane(root);

        // ── Cabecera ─────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MANTLE);
        header.setBorder(new EmptyBorder(24, 28, 20, 28));

        JLabel icon = new JLabel("💻");
        icon.setFont(new Font("SansSerif", Font.PLAIN, 42));
        header.add(icon, BorderLayout.WEST);

        JPanel headerText = new JPanel(new GridLayout(2, 1, 0, 4));
        headerText.setOpaque(false);
        headerText.setBorder(new EmptyBorder(0, 14, 0, 0));

        JLabel titulo = new JLabel("Inventario de Computadoras");
        titulo.setFont(new Font("Monospaced", Font.BOLD, 17));
        titulo.setForeground(LAVENDER);

        JLabel sub = new JLabel("Selecciona cómo cargar los datos iniciales");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(SUBTEXT);

        headerText.add(titulo);
        headerText.add(sub);
        header.add(headerText, BorderLayout.CENTER);
        root.add(header, BorderLayout.NORTH);

        // ── Opciones ─────────────────────────────────────────────────────────
        JPanel opciones = new JPanel(new GridLayout(3, 1, 0, 10));
        opciones.setBackground(BASE);
        opciones.setBorder(new EmptyBorder(20, 24, 8, 24));

        opciones.add(buildOpcionCard(
                "📂",
                "Cargar seed desde archivo .sql",
                "Abre un explorador para seleccionar cualquier script SQL",
                GREEN,
                true,
                () -> accionArchivo()));

        JPanel cardExport = buildOpcionCard(
                "🔄",
                "Continuar sesión anterior",
                hayExport
                        ? "Se encontró inventario_export.sql — cargará los datos de la última sesión"
                        : "No hay sesión anterior guardada (inventario_export.sql no existe)",
                BLUE,
                hayExport,
                () -> {
                    opcion = Opcion.EXPORT;
                    dispose();
                });
        opciones.add(cardExport);

        root.add(opciones, BorderLayout.CENTER);

        // ── Pie ──────────────────────────────────────────────────────────────
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 24, 12));
        footer.setBackground(MANTLE);

        JButton btnCancelar = new JButton("Salir");
        btnCancelar.setBackground(SURFACE);
        btnCancelar.setForeground(RED);
        btnCancelar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(RED, 1),
                BorderFactory.createEmptyBorder(6, 18, 6, 18)));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnCancelar.addActionListener(e -> {
            opcion = Opcion.CANCELAR;
            dispose();
        });

        footer.add(btnCancelar);
        root.add(footer, BorderLayout.SOUTH);
    }

    /**
     * Construye una tarjeta de opción clicable.
     *
     * @param emoji       icono decorativo
     * @param titulo      texto principal
     * @param descripcion texto secundario
     * @param color       color del acento
     * @param habilitada  si false, la tarjeta aparece atenuada y no reacciona
     * @param accion      lambda a ejecutar al hacer clic
     */
    private JPanel buildOpcionCard(String emoji, String titulo, String descripcion,
            Color color, boolean habilitada, Runnable accion) {
        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(habilitada ? color : OVERLAY, 1),
                new EmptyBorder(14, 16, 14, 16)));

        // Icono
        JLabel lblEmoji = new JLabel(emoji);
        lblEmoji.setFont(new Font("SansSerif", Font.PLAIN, 26));
        lblEmoji.setOpaque(false);
        card.add(lblEmoji, BorderLayout.WEST);

        // Texto
        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 3));
        textos.setOpaque(false);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Monospaced", Font.BOLD, 13));
        lblTitulo.setForeground(habilitada ? color : OVERLAY);

        JLabel lblDesc = new JLabel("<html>" + descripcion + "</html>");
        lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblDesc.setForeground(habilitada ? SUBTEXT : OVERLAY);

        textos.add(lblTitulo);
        textos.add(lblDesc);
        card.add(textos, BorderLayout.CENTER);

        // Flecha indicadora
        if (habilitada) {
            JLabel flecha = new JLabel("→");
            flecha.setFont(new Font("Monospaced", Font.BOLD, 18));
            flecha.setForeground(color);
            card.add(flecha, BorderLayout.EAST);
        }

        // Interactividad
        if (habilitada) {
            card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            card.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    accion.run();
                }

                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    card.setBackground(new Color(
                            Math.min(SURFACE.getRed() + 20, 255),
                            Math.min(SURFACE.getGreen() + 20, 255),
                            Math.min(SURFACE.getBlue() + 20, 255)));
                    card.repaint();
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    card.setBackground(SURFACE);
                    card.repaint();
                }
            });
        }

        return card;
    }

    // ── Acción: seleccionar archivo ───────────────────────────────────────────

    private void accionArchivo() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Seleccionar script SQL de seed");
        fc.setFileFilter(new FileNameExtensionFilter("Scripts SQL (*.sql)", "sql"));
        fc.setAcceptAllFileFilterUsed(false);

        // Intentar abrir en el directorio de trabajo
        fc.setCurrentDirectory(new File(System.getProperty("user.dir")));

        int resultado = fc.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            archivo = fc.getSelectedFile();
            opcion = Opcion.ARCHIVO;
            dispose();
        }
        // Si canceló el JFileChooser, el diálogo sigue abierto para elegir otra opción
    }

    // ── Getters del resultado ─────────────────────────────────────────────────

    public Opcion getOpcion() {
        return opcion;
    }

    public File getArchivo() {
        return archivo;
    }

}
