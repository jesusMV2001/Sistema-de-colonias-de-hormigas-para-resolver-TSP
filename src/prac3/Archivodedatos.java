/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prac3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author jesus
 */
public class Archivodedatos {
    ArrayList<double[]> coordenadas;
    
    
    public Archivodedatos(String ruta){
        coordenadas = new ArrayList<>();
        String linea;
        FileReader f =null;
        try{
            f = new FileReader(ruta);
            BufferedReader b = new BufferedReader(f);
            while(!b.readLine().equals("NODE_COORD_SECTION")){}
            
            linea = b.readLine();
            while(!linea.equals("EOF")){
                String[] split = linea.split(" ");
                double valores[] = new double[2];
                valores[0] = Double.parseDouble( split[1] );
                valores[1] = Double.parseDouble( split[2] );
                
                coordenadas.add(valores);
                linea = b.readLine();
            }
            
        }catch(IOException e){
            System.out.println(e);
        }
    }
    
    public ArrayList<Integer> getCiudades() {
        ArrayList<Integer> aux = new ArrayList<>();
        for (int i = 0; i < coordenadas.size(); i++) {
            aux.add(i);
        }
        return aux;
    }

    public ArrayList<double[]> getCoordenadas() {
        return coordenadas;
    }
    
    public double[] get(int index) {
        return coordenadas.get(index);
    }
}
