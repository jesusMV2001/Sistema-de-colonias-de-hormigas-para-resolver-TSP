/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prac3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author Xjesu
 */
public class Hormiga {
    Configurador config;
    Archivodedatos ciudades;
    private ArrayList<Integer> cVisitadas;
    private ArrayList<Integer> cNoVisitadas;
    private ArrayList<Double> probabilidades;
    
    private double coste;

    public Hormiga(int ciudadInicial, Archivodedatos ciudades, Configurador config) {
        this.ciudades = ciudades;
        this.config = config;
        cVisitadas = new ArrayList<>();
        cNoVisitadas = new ArrayList<>();
        probabilidades = new ArrayList<>();
        cVisitadas.add(ciudadInicial);
        cNoVisitadas = ciudades.getCiudades();
        cNoVisitadas.remove(ciudadInicial);
        coste = 0;
    }
    
    /** Añade la ciudad a ciudades visitadas y la elimina de ciudades no visitadas */
    public void addCiudad(Integer numCiudad) {
        cVisitadas.add(numCiudad);
        cNoVisitadas.remove(cNoVisitadas.indexOf(numCiudad));
    }
    
    /** Calculamos el coste de la solución obtenida */
    public double calculaCoste(double[][] distancias) {
        for (int i = 1; i < cVisitadas.size(); i++) {
            coste += distancias[cVisitadas.get(i-1)][cVisitadas.get(i)];
        }
        coste += distancias[cVisitadas.get(cVisitadas.size() - 1)][0];
        
        return coste;
    }
    
    /** Guarda en probabilidades las distintas probabilidades para moverse a cada
     una de las ciudades no visitadas */
    public void calculaProbTrans(double[][] feromonas, double[][] distancias) {
        probabilidades.clear();
        double sum = 0;
        
        for (int i = 0; i < cNoVisitadas.size(); i++) {
            sum += Math.pow(feromonas[(cVisitadas.get(cVisitadas.size() - 1))][cNoVisitadas.get(i)], config.getAlfa()) *
                    Math.pow((double)1/distancias[(cVisitadas.get(cVisitadas.size() - 1))][cNoVisitadas.get(i)], config.getBeta());
        }
        
        for (int i = 0; i < cNoVisitadas.size(); i++) {
            probabilidades.add((Math.pow(feromonas[(cVisitadas.get(cVisitadas.size() - 1))][cNoVisitadas.get(i)], config.getAlfa()) *
                    Math.pow((double)1/distancias[(cVisitadas.get(cVisitadas.size() - 1))][cNoVisitadas.get(i)], config.getBeta())) / sum);
        }
    }
    
    /** Devuelve una de la ciudades no visitadas teniendo en cuenta las
     probabilidades de transición */
    public Integer moverHormiga(Random ale) {
        double aux = ale.nextDouble();
        double sum = 0;
        for(int i=0; i<cNoVisitadas.size(); i++){
            double sumAnterior = sum;
            sum += probabilidades.get(i);
            if(aux <= sum && aux > sumAnterior){
                return cNoVisitadas.get(i);
            }
        }
        return null;
    }
    
    /** Devuelve la ciudad más prometedora teniendo en cuenta las probabilidades
     de transición */
    public Integer moverHormigaMax() {
        Double max = Collections.max(probabilidades);
        return cNoVisitadas.get(probabilidades.indexOf(max));
    }
    
    public Integer getNumCiudad(int index) {
        return cNoVisitadas.get(index);
    }

    public ArrayList<Integer> getcVisitadas() {
        return cVisitadas;
    }

    public double getCoste() {
        return coste;
    }
    
}
