/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dubon.inventoryproject.models;

/**
 *
 * @author danie
 */
public class ComputadoraModel {
    
    private int    id;
    private String marca;
    private String modelo;
    private double precio;
    private int    stock;
    private String imagenRuta;

    // ── Constructores ──────────────────────────────────────────────────────────

    public ComputadoraModel() {}

    public ComputadoraModel(String marca, String modelo, double precio, int stock, String imagenRuta) {
        this.marca      = marca;
        this.modelo     = modelo;
        this.precio     = precio;
        this.stock      = stock;
        this.imagenRuta = imagenRuta;
    }

    public ComputadoraModel(int id, String marca, String modelo, double precio, int stock, String imagenRuta) {
        this.id         = id;
        this.marca      = marca;
        this.modelo     = modelo;
        this.precio     = precio;
        this.stock      = stock;
        this.imagenRuta = imagenRuta;
    }

    // ── Getters / Setters ──────────────────────────────────────────────────────

    public int    getId()         { return id; }
    public void   setId(int id)   { this.id = id; }

    public String getMarca()             { return marca; }
    public void   setMarca(String marca) { this.marca = marca; }

    public String getModelo()              { return modelo; }
    public void   setModelo(String modelo) { this.modelo = modelo; }

    public double getPrecio()              { return precio; }
    public void   setPrecio(double precio) { this.precio = precio; }

    public int  getStock()           { return stock; }
    public void setStock(int stock)  { this.stock = stock; }

    public String getImagenRuta()                  { return imagenRuta; }
    public void   setImagenRuta(String imagenRuta) { this.imagenRuta = imagenRuta; }

    @Override
    public String toString() {
        return marca + " " + modelo;
    }
}
