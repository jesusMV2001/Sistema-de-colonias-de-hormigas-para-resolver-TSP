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
public class Configurador {
    private ArrayList<String> archivos;
    private ArrayList<Long> semillas;
    private int maxIteraciones, maxDuracion, tamPoblacion, alfa, beta, inicialMatrizFeromonas;
    private double q0, p, fi;
    
    public Configurador(String ruta){
        archivos = new ArrayList<>();
        semillas = new ArrayList<>();
        String linea;
        FileReader f =null;
        try{
            f = new FileReader(ruta);
            BufferedReader b = new BufferedReader(f);
            while( (linea=b.readLine()) != null){
                String[] split = linea.split("=");
                switch(split[0]){
                    case "Archivos":
                        String[] v= split[1].split(" ");
                        for (int i = 0; i < v.length; i++) 
                            archivos.add(v[i]);
                        break;
                    case "Semillas":
                        String[] vsemillas= split[1].split(" ");
                        for (int i = 0; i < vsemillas.length; i++) 
                            semillas.add(Long.parseLong(vsemillas[i]));
                        break;
                    case "MaxIteraciones":
                        maxIteraciones = Integer.parseInt(split[1]);
                        break;
                    case "MaxDuracion":
                        maxDuracion = Integer.parseInt(split[1]);
                        break;
                    case "TamPoblacion":
                        tamPoblacion = Integer.parseInt(split[1]);
                        break;
                    case "alfa":
                        alfa = Integer.parseInt(split[1]);
                        break;
                    case "beta":
                        beta = Integer.parseInt(split[1]);
                        break;
                    case "q0":
                        q0 = (double)Integer.parseInt(split[1]) / 100;
                        break;
                    case "p":
                        p = (double)Integer.parseInt(split[1]) / 100;
                        break;
                    case "fi":
                        fi = (double)Integer.parseInt(split[1]) / 100;
                        break;
                    case "inicialMatrizFeromonas":
                        inicialMatrizFeromonas = Integer.parseInt(split[1]);
                        break;
                }
            }
            
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public ArrayList<String> getArchivos() {
        return archivos;
    }

    public ArrayList<Long> getSemillas() {
        return semillas;
    }

    public int getMaxIteraciones() {
        return maxIteraciones;
    }

    public int getMaxDuracion() {
        return maxDuracion;
    }

    public int getTamPoblacion() {
        return tamPoblacion;
    }

    public int getAlfa() {
        return alfa;
    }

    public int getBeta() {
        return beta;
    }

    public double getQ0() {
        return q0;
    }

    public double getP() {
        return p;
    }

    public double getFi() {
        return fi;
    }

    public int getInicialMatrizFeromonas() {
        return inicialMatrizFeromonas;
    }    
}
