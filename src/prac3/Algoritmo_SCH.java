/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prac3;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Xjesu
 */
public class Algoritmo_SCH {    
    Configurador config;
    TablaDatos datos;
    Archivodedatos ciudades;
    private double[][] distancias, feromonas;
    private double feromonaInicial;
    private long tiempoInicio;
    private Random aleatorio;
    private Hormiga mejorGlobal;
    float diffTiempo;
    ArrayList<Hormiga> salida;

    public Algoritmo_SCH(long semilla, Configurador config, Archivodedatos ciudades, TablaDatos datos) {
        tiempoInicio = 0;
        diffTiempo = 0;
        salida = new ArrayList<>();
        feromonaInicial = 0;
        this.ciudades = ciudades;
        this.config = config;
        this.datos = datos;
        this.aleatorio = new Random(semilla);
        mejorGlobal = null;
        distancias = new double[ciudades.getCoordenadas().size()][ciudades.getCoordenadas().size()]; // Matriz numCiudades x numCiudades
        feromonas = new double[ciudades.getCoordenadas().size()][ciudades.getCoordenadas().size()]; // Matriz numCiudades x numCiudades
        
        // Inicializamos la matriz de distancias
        for (int i = 0; i < distancias.length; i++) {
            for (int j = i; j < distancias.length; j++) {
                if(i==j) distancias[i][j] = -1;
                else {
                    double distancia = Math.sqrt(Math.pow(ciudades.get(i)[0] - ciudades.get(j)[0], 2) 
                        + Math.pow(ciudades.get(i)[1] - ciudades.get(j)[1], 2));
                    distancias[i][j] = distancia;
                    distancias[j][i] = distancia;
                }
            }
        }
        
        // Inicializamos la matriz de feromonas
        for (int i = 0; i < feromonas.length; i++) {
            for (int j = 0; j < feromonas.length; j++) {
                feromonas[i][j] = config.getInicialMatrizFeromonas();
            }
        }
        
        // Aplicamos un greedy para obtener la feromona inicial
        Hormiga inicial = new Hormiga(aleatorio.nextInt(0, ciudades.getCoordenadas().size()), ciudades, config);
        
        for (int i = 0; i < ciudades.getCoordenadas().size() - 1; i++) {
            inicial.calculaProbTrans(feromonas, distancias); // Calculamos las probabilidades de transición
            inicial.addCiudad(inicial.moverHormigaMax()); // Nos movemos a la mejor
        }
        
        feromonaInicial = 1 / (config.getTamPoblacion() * inicial.calculaCoste(distancias)); // Calculamos el valor de la feromona inicial       
    }
            
    public Hormiga aplicaAlgoritmo(){
        int iteraciones = 0;
        tiempoInicio = System.currentTimeMillis();
        diffTiempo = 0;
        
        while(iteraciones < config.getMaxIteraciones() && diffTiempo < config.getMaxDuracion()){
            
            // Inicializamos la hormigas en una ciudad
            ArrayList<Hormiga> pobHormigas = new ArrayList<>();
            for (int i = 0; i < config.getTamPoblacion(); i++) {
                pobHormigas.add(new Hormiga(aleatorio.nextInt(0, ciudades.getCoordenadas().size()), ciudades, config));
            }
            
            // Construimos el camino de las hormigas y realizamos las actualizaciones locales
            for (int i = 0; i < ciudades.getCoordenadas().size() - 1; i++) {
                for (int j = 0; j < config.getTamPoblacion(); j++) {
                    pobHormigas.get(j).calculaProbTrans(feromonas, distancias); // Calculamos las probabilidades de transición
                    int indexCiudad;
                    if(aleatorio.nextDouble() >= config.getQ0()){
                        indexCiudad = pobHormigas.get(j).moverHormigaMax();
                    }
                    else indexCiudad = pobHormigas.get(j).moverHormiga(aleatorio);
                    
                    pobHormigas.get(j).addCiudad(indexCiudad);
                    
                    // Aplicamos actualización local
                    Integer ciudadPrimera = pobHormigas.get(j).getcVisitadas().get(pobHormigas.get(j).getcVisitadas().size() - 2);
                    Integer ciudadSegunda = pobHormigas.get(j).getcVisitadas().get(pobHormigas.get(j).getcVisitadas().size() - 1);
                    feromonas[ciudadPrimera][ciudadSegunda] = 
                            (1 - config.getFi()) * feromonas[ciudadPrimera][ciudadSegunda] + config.getFi() * feromonaInicial; 
                }
            }
            
            for (Hormiga h : pobHormigas) salida.add(h); // Guardamos las hormigas generadas para los logs
            
            // Aplicamos la actualización global
            Hormiga mejorHormiga = pobHormigas.get(obtenerMejorHormiga(pobHormigas));
            ArrayList<Integer> mejorCamino = mejorHormiga.getcVisitadas();
            for (int i = 1; i < mejorCamino.size(); i++) {
                feromonas[mejorCamino.get(i-1)][mejorCamino.get(i)] = 
                    (1 - config.getP()) * feromonas[mejorCamino.get(i-1)][mejorCamino.get(i)] + config.getP() * (1 / mejorHormiga.getCoste());
            }
            feromonas[mejorCamino.get(mejorCamino.size()-1)][0] = 
                (1 - config.getP()) * feromonas[mejorCamino.get(mejorCamino.size()-1)][0] + config.getP() * (1 / mejorHormiga.getCoste());
            
            // Comprobamos si la mejor actual es mejor que la global
            if(iteraciones == 0) mejorGlobal = mejorHormiga;
            else if(mejorHormiga.getCoste() < mejorGlobal.getCoste()){
                mejorGlobal = mejorHormiga;
            }
                        
            iteraciones++;
            diffTiempo = ((float)(System.currentTimeMillis() - tiempoInicio) / 1000); // Caculamos el tiempo que ha pasado en segundos
        }
        return mejorGlobal;
    }
 
    /** Devuelve la posición de la mejor hormiga dentro de la población */
    private int obtenerMejorHormiga(ArrayList<Hormiga> pobHormigas) {
        int mejorCamino = 0;
        double costeMejorCamino = pobHormigas.get(0).calculaCoste(distancias);
        for (int i = 1; i < pobHormigas.size(); i++) {
            double costeCaminoActual = pobHormigas.get(i).calculaCoste(distancias);
            if(costeCaminoActual < costeMejorCamino)
                mejorCamino = i;
        }
        return mejorCamino;
    }
    
    public ArrayList<Hormiga> getSalida() {
        return salida;
    }
    
    public float getTiempoEjecucion() {
        return diffTiempo;
    }

    public double[][] getDistancias() {
        return distancias;
    }

    public double[][] getFeromonas() {
        return feromonas;
    }

    public double getFeromonaInicial() {
        return feromonaInicial;
    }

    public Hormiga getMejorGlobal() {
        return mejorGlobal;
    }
    
}
