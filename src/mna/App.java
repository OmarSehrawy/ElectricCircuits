package mna;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import java.util.Arrays;
import java.util.Scanner;

public class App {
    static NetList netList;
    static Analyzer analyzer;
    public static void main(String[] args) {
        System.out.println("Enter file path:");
        Scanner scn = new Scanner(System.in);
        netList = Parser.parse(scn.nextLine());
        analyzer = new Analyzer(netList);
        System.out.print(netList);
        System.out.println("Enter your choice:");
        System.out.println("1) DC Analysis.\t2) Transient Analysis.\t3) AC Analysis.");
        int choice = scn.nextInt();
        switch (choice) {
            case 1:
                System.out.print(Arrays.toString(analyzer.solution())); break;
            case 2:
                System.out.println("Enter time step and duration");
                double step = scn.nextDouble();
                double duration = scn.nextDouble();
                visualize(analyzer.transientAnalysis(step,duration),step);
                break;
            case 3:
            default:
                break;
        }
        scn.next();
    }
    public static void visualize(double[][] V, double step) {
        int steps = V.length;
        int nodes = netList.nodesCount();
        double[] time = new double[steps];
        for (int i = 0; i < steps; i++) time[i] = step * i;
        double[] n1 = getCol(V,0);
        XYChart chart = QuickChart.getChart("Transient Analysis","Time","Voltage","n1",time,n1);
        for (int i = 1; i < nodes; i++) chart.addSeries("n"+(i+1),getCol(V,i));
        new SwingWrapper<>(chart).displayChart();
    }
    private static double[] getCol(double[][] M,int col) {
        double[] column = new double[M.length];
        for (int i = 0; i < M.length; i++) column[i] = M[i][col];
        return column;
    }
}