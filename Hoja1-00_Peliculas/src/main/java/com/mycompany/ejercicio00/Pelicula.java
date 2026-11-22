/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ejercicio00;

/**
 *
 * @author david
 */
public class Pelicula {
    
    private int id;
    private String titulo;
    private String director;
    private int anioEstreno;
    private String genero;

    public Pelicula(int id, String titulo, String director, int anioEstreno, String genero) {
        this.id = id;
        this.titulo = titulo;
        this.director = director;
        this.anioEstreno = anioEstreno;
        this.genero = genero;
    }

    public Pelicula(String titulo, String director, int anioEstreno, String genero) {
        this.titulo = titulo;
        this.director = director;
        this.anioEstreno = anioEstreno;
        this.genero = genero;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getAnioEstreno() {
        return anioEstreno;
    }

    public void setAnioEstreno(int anioEstreno) {
        this.anioEstreno = anioEstreno;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    @Override
    public String toString() {
        return "Pelicula{" + "id=" + id + ", titulo=" + titulo + ", director=" + director + ", anioEstreno=" + anioEstreno + ", genero=" + genero + '}';
    }
    
    
}
