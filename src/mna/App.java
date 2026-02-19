package mna;

import mna.components.*;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class App {
    static NetList netList;
    static Analyzer analyzer;
    static Scanner scn;
    public static void main(String[] args) {
        System.out.println("Enter file path:");
        scn = new Scanner(System.in);
        netList = Parser.parse(scn.nextLine());
        analyzer = new Analyzer(netList);
        System.out.print(netList);
        System.out.println("Enter your choice:");
        System.out.println("1) DC Analysis. 2) DC Sweep. 3) Transient Analysis. 4) AC Analysis.");
        int choice = scn.nextInt();
        handleChoice(choice);
        scn.next();
    }
    public static void visualizeSweep(double[][] V,String r) {
        int steps = V.length;
        int nodes = netList.nodesCount();
        double[] range = getCol(V,V[0].length-1);
        double[] n1 = getCol(V,0);
        XYChart chart = QuickChart.getChart("DC Sweep",r,"Voltage","Node 1", range,n1);
        for (int i = 1; i < nodes; i++) chart.addSeries("Node "+(i+1),getCol(V,i));
        new SwingWrapper<>(chart).displayChart();
    }
    private static double[] getCol(double[][] M,int col) {
        double[] column = new double[M.length];
        for (int i = 0; i < M.length; i++) column[i] = M[i][col];
        return column;
    }
    public static void handleChoice(int choice) {
        switch (choice) {
            case 1:
                //DC
                System.out.println(Arrays.toString(analyzer.dcSolution()));
                System.out.println("Choose component");
                while (!scn.hasNext("")) {
                    System.out.println("Choose component");
                    System.out.println(Arrays.toString(getInfo(scn.next())));
                }
                break;
            case 2:
                //DC Sweep
                System.out.println("Choose component");
                handleSweep();
                break;
            case 3:
                //Transient
                System.out.println("Enter time step and duration");
                double step = scn.nextDouble();
                double duration = scn.nextDouble();
                break;
            case 4:
                //AC
            default:
                break;
        }
    }
    private static void handleSweep() {
        String c = scn.next();
        char type = c.charAt(0);
        System.out.printf("How many steps form []%c to []%c%n:",type,type);
        int steps = scn.nextInt(); double start = scn.nextDouble(); double end = scn.nextDouble();
        double[][] res = analyzer.dcSweep(c,steps,start,end);
        visualizeSweep(res,c);
    }
    public static double[] getInfo(String c) {
        char type = c.charAt(0);
        double[] info = new double[0];
        switch (type) {
            case 'R': info = netList.getResistors().get(c).getInfo(); break;
            case 'W': info = netList.getWires().get(c).getInfo(); break;
            case 'V': info = netList.getVoltageSources().get(c).getInfo(); break;
            case 'I': info = netList.getCurrentSources().get(c).getInfo(); break;
            default: break;
        }
        return info;
    }
}