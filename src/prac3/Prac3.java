/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package prac3;

/**
 *
 * @author Xjesu
 */
public class Prac3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Configurador config = new Configurador(args[0]);
        TablaDatos resultados = new TablaDatos(config);
                
        for (int i = 0; i < config.getArchivos().size(); i++) {
            System.out.println("------------- Archivo " + config.getArchivos().get(i) + " -------------");
            Archivodedatos a = new Archivodedatos(config.getArchivos().get(i));
            for (int j = 0; j < config.getSemillas().size(); j++) {
                System.out.println("Semilla " + config.getSemillas().get(j));
                Algoritmo_SCH algoritmo = new Algoritmo_SCH(config.getSemillas().get(j), config, a, resultados);
                Hormiga h = algoritmo.aplicaAlgoritmo();
                System.out.println("Solucion Final: " + h.getcVisitadas());
                System.out.println("Coste: " + (float)h.getCoste());
                System.out.println("Tiempo: " + algoritmo.getTiempoEjecucion());
                System.out.println();
                
                resultados.insertarDato(h, algoritmo.getTiempoEjecucion());
                resultados.escribeInfoLog(algoritmo, i, j);
            }
            System.out.println();
        }
        
        resultados.generarFichero();
    } 
}
