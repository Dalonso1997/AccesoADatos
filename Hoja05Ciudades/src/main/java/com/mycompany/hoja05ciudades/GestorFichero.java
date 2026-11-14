/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.hoja05ciudades;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author daw2
 */
public class GestorFichero {

    public boolean comprobarFichero(Path ruta) {

        if (Files.exists(ruta) && !Files.isDirectory(ruta)) {
            return true;
        } else if (!Files.exists(ruta)) {
            try {
                crearFichero(ruta);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return false;

    }

    public void crearFichero(Path ruta) throws IOException {
        Files.createFile(ruta);
    }

    public List<String> leerLineasFichero(Path ruta) throws IOException {

        return Files.readAllLines(ruta, StandardCharsets.UTF_8);

    }

    public City crearCiudad(String ciudad) {

        String[] datosCiudad = ciudad.split(",");

        City ciudadCreada = new City(Integer.parseInt(datosCiudad[0]), datosCiudad[1], datosCiudad[2], datosCiudad[3], Integer.parseInt(datosCiudad[4]));
        return ciudadCreada;
    }

    public void escribirCiudadEnFichero(City ciudad, Path ruta) throws IOException {

        if (Files.size(ruta) > 0) {

            try (ObjectOutputStream os = new SinCabecera(Files.newOutputStream(ruta, StandardOpenOption.APPEND))) {

                os.writeObject(ciudad);
                
            }

        } else {
            try (ObjectOutputStream os = new ObjectOutputStream(Files.newOutputStream(ruta))) {

                os.writeObject(ciudad);

            }
        }

    }
    
    
    
    public List<City> listarCiudades(Path ruta) throws IOException, ClassNotFoundException{
        
        List<City> listaCiudades = new ArrayList<>();
        
        try (ObjectInputStream is = new ObjectInputStream(Files.newInputStream(ruta))){
            
            while (true){
                
                City ciudad = (City) is.readObject();
                listaCiudades.add(ciudad);
            }
            
        } catch (EOFException e){
            
        }
        
        return listaCiudades;
  
    }
    
    
    public int compararCiudades(City a, City b){
        
        int resultado = a.getNombre().compareTo(b.getNombre());
        
        if ( resultado != 0){
            
            return resultado;
            
        } else {
            
            return a.getCodPais().compareTo(b.getCodPais());
            
        }
        
    }
    
    public void reescribirFichero(List<City> listaCiudades, Path ficheroObjeto) throws IOException{
        
        try (ObjectOutputStream os = new ObjectOutputStream(Files.newOutputStream(ficheroObjeto, StandardOpenOption.TRUNCATE_EXISTING))){
            
            for (City ciudad : listaCiudades) {
                
                os.writeObject(ciudad);
                
            }
            
        }
        
        
    }
    
}
