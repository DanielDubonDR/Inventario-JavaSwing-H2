/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dubon.inventoryproject.utils;

import com.dubon.inventoryproject.models.ComputadoraModel;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author danie
 */
public class CsvExporter {
    // Separador de columnas — cambiar a ';' si tu Excel regional lo requiere
    private static final char SEPARADOR = ',';
 
    /**
     * Exporta la lista a un archivo CSV en el directorio de trabajo.
     *
     * @param computadoras lista de computadoras a exportar
     * @return ruta absoluta del archivo generado
     * @throws IOException si no se puede escribir el archivo
     */
    public static String exportar(List<ComputadoraModel> computadoras) throws IOException {
 
        // Nombre con timestamp para evitar sobreescribir reportes previos
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = "reporte_inventario_" + timestamp + ".csv";
 
        try (PrintWriter pw = new PrintWriter(
                new FileWriter(fileName, StandardCharsets.UTF_8))) {
 
            // BOM UTF-8: necesario para que Excel detecte la codificación correctamente
            pw.print('\uFEFF');
 
            // ── Cabecera ──────────────────────────────────────────────────────
            pw.println(String.join(String.valueOf(SEPARADOR),
                "ID",
                "Marca",
                "Modelo",
                "Precio (Q)",
                "Stock",
                "Ruta Imagen"
            ));
 
            // ── Filas de datos ────────────────────────────────────────────────
            for (ComputadoraModel c : computadoras) {
                pw.println(String.join(String.valueOf(SEPARADOR),
                    String.valueOf(c.getId()),
                    escapar(c.getMarca()),
                    escapar(c.getModelo()),
                    String.format("%.2f", c.getPrecio()),
                    String.valueOf(c.getStock()),
                    escapar(c.getImagenRuta() != null ? c.getImagenRuta() : "")
                ));
            }
        }
 
        System.out.println("[CSV] Reporte generado: " + new java.io.File(fileName).getAbsolutePath());
        return new java.io.File(fileName).getAbsolutePath();
    }
 
    // ── Helpers ───────────────────────────────────────────────────────────────
 
    /**
     * Escapa un valor para CSV según RFC 4180:
     *   - Si contiene el separador, comillas dobles o salto de línea
     *     → encierra todo el valor entre comillas dobles.
     *   - Las comillas dobles internas se duplican ("" representa una sola ").
     *   - Los valores null se convierten en cadena vacía.
     */
    private static String escapar(String valor) {
        if (valor == null) return "";
 
        boolean necesitaComillas = valor.indexOf(SEPARADOR) >= 0
                || valor.contains("\"")
                || valor.contains("\n")
                || valor.contains("\r");
 
        if (necesitaComillas) {
            // Duplicar comillas internas y envolver con comillas dobles
            return "\"" + valor.replace("\"", "\"\"") + "\"";
        }
        return valor;
    }
}
