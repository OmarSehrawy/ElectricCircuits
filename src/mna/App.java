package mna;

import mna.components.*;
import mna.exceptions.AnalysisException;
import mna.exceptions.InvalidComponentException;
import mna.exceptions.SweepException;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import java.io.*;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App {
    static NetList netList;
    static Analyzer analyzer;
    static Scanner scn;
    public static void main(String[] args) {
        System.out.println("Enter file path:");
        scn = new Scanner(System.in);
        try {
            netList = Parser.parse(scn.nextLine());
            analyzer = new Analyzer(netList);
            handleChoice();
        } catch (InvalidComponentException e) {
            System.out.println(e.getMessage());
        }
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
    public static void handleChoice() {
        System.out.print(netList);
        System.out.println("Enter your choice:");
        System.out.println("1) DC Analysis. 2) DC Sweep. 3) Transient Analysis. 4) AC Analysis.");
        try {
            int choice = scn.nextInt();
            scn.nextLine();
            switch (choice) {
                case 1:
                    //DC
                    try {
                        System.out.println(Arrays.toString(analyzer.dcSolution()));
                        while (true) {
                            System.out.println("Select component to view, or type 'exit'");
                            String input = scn.nextLine().trim();
                            if (input.equalsIgnoreCase("exit")) break;
                            if (input.isEmpty()) continue;
                            try {
                                System.out.println(Arrays.toString(getInfo(input)));
                            } catch (InvalidComponentException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    } catch (AnalysisException e) {
                        System.out.println("Can't solve: " + e.getMessage());
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
        } catch (InputMismatchException e) {
            scn.nextLine();
            System.out.println("Invalid choice");
        }
    }
    public static void handleSweep() {
        String c = scn.next();
        char type = c.charAt(0);
        System.out.printf("How many steps form []%c to []%c%n:",type,type);
        try {
            int steps = scn.nextInt();
            double start = scn.nextDouble();
            double end = scn.nextDouble();
            try {
                double[][] res = analyzer.dcSweep(c,steps,start,end);
                visualizeSweep(res,c);
            } catch (SweepException | InvalidComponentException e) {
                if(e instanceof SweepException) System.out.print("Sweep can't start: ");
                System.out.println(e.getMessage());
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid inputs");
        }
    }
    public static double[] getInfo(String c) throws InvalidComponentException {
        char type = c.charAt(0);
        try {
            return switch (type) {
                case 'R' -> netList.getResistors().get(c).getInfo();
                case 'W' -> netList.getWires().get(c).getInfo();
                case 'V' -> netList.getVoltageSources().get(c).getInfo();
                case 'I' -> netList.getCurrentSources().get(c).getInfo();
                default -> throw new InvalidComponentException("Component type doesn't exist");
            };
        } catch (NullPointerException e) {
            throw new InvalidComponentException("Component isn't in list");
        }
    }
}