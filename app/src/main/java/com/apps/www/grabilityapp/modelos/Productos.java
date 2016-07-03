package com.apps.www.grabilityapp.modelos;

import java.io.Serializable;

/**
 * Created by gustavo morales on 3/07/2016.
 * tavomorales88@gmail.com
 **/
public class Productos implements Serializable{

    private String nombre;
    private String descripcion;
    private String precio;
    private String tipo;
    private String categoria;
    private String categoriaId;
    private String urlImage53;
    private String urlImage75;
    private String urlImage100;

    @Override
    public String toString() {
        return nombre + " " + descripcion + " " + precio + " " + tipo + " " + categoria +
                " " + categoriaId + " " + urlImage53 + " " + urlImage75 + " " + urlImage100;
    }

    public Productos(){}

    public Productos(String nombre, String descripcion, String precio, String tipo, String categoria,
                     String categoriaId, String urlImage53, String urlImage75, String urlImage100) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.tipo = tipo;
        this.categoria = categoria;
        this.categoriaId = categoriaId;
        this.urlImage53 = urlImage53;
        this.urlImage75 = urlImage75;
        this.urlImage100 = urlImage100;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(String categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getUrlImage53() {
        return urlImage53;
    }

    public void setUrlImage53(String urlImage53) {
        this.urlImage53 = urlImage53;
    }

    public String getUrlImage75() {
        return urlImage75;
    }

    public void setUrlImage75(String urlImage75) {
        this.urlImage75 = urlImage75;
    }

    public String getUrlImage100() {
        return urlImage100;
    }

    public void setUrlImage100(String urlImage100) {
        this.urlImage100 = urlImage100;
    }
}
