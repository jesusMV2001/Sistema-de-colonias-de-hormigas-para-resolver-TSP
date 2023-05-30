/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prac3;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author Xjesu
 */
public class TablaDatos {
    private Configurador config;
    private ArrayList<Hormiga> datos;
    private ArrayList<Float> tiempos;
    private FileWriter fichero;
    private PrintWriter pw;

    public TablaDatos(Configurador config) {
        this.config = config;
        datos = new ArrayList<>();
        tiempos = new ArrayList<>();
    }
    
    public void insertarDato(Hormiga dato, float tiempo){
        datos.add(dato);
        tiempos.add(tiempo);
    }
    
    public void escribeInfoLog(Algoritmo_SCH algoritmo, int numArchivo, int numEjec){
        try
        {            
            
            ArrayList<Hormiga> soluciones = algoritmo.getSalida();
            
            String semilla = String.valueOf(config.getSemillas().get(numEjec));
            //Generación del fichero con todo el log de la ejecución
                String nombre = config.getArchivos().get(numArchivo);
                fichero = new FileWriter("logs/"+"SCH_"+nombre.substring(0, nombre.lastIndexOf('.'))+"_"+
                       config.getSemillas().get(numEjec)+"_log.txt");
            pw = new PrintWriter(fichero);

            pw.println("Archivo " + config.getArchivos().get(numArchivo));
            pw.println("Semilla " + config.getSemillas().get(numEjec));
            pw.println("\nFeromona Inicial: " + algoritmo.getFeromonaInicial());
            for (int i = 0; i < soluciones.size() / config.getTamPoblacion(); i++) {
                pw.println();             
                pw.println("----------- Iteracion " + (i + 1) + " -----------");
                for(int j=0; j<config.getTamPoblacion(); j++){
                    pw.println("Hormiga " + (j+1) + ": " + soluciones.get(j + (config.getTamPoblacion()*i)).getcVisitadas() 
                            + " Coste: " + soluciones.get(j + (config.getTamPoblacion()*i)).getCoste());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
    }
    
    public void generarFichero(){
        try
        {
            // Generacion del fichero con los datos en formato CSV
            fichero = new FileWriter("salida.txt");
            pw = new PrintWriter(fichero);

            pw.print("Datos,"); // Nombre tabla
            for (int j = 0; j < config.getSemillas().size(); j++) pw.print("Coste,Tiempo,"); // Cabecera tabla
            //Datos
            pw.println();
            for (int j = 0; j < config.getArchivos().size(); j++){
                pw.print("Archivo_" + (j+1) + ",");
                for (int k = 0; k < config.getSemillas().size(); k++) {
                    pw.print((float)datos.get(k+(config.getArchivos().size()*j)).getCoste() + "," +
                             tiempos.get(k+(config.getArchivos().size()*j)) + ",");
                }
                pw.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
    }    
}
