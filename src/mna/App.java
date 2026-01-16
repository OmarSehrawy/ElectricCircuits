package mna;

import java.util.Arrays;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        System.out.println("Enter file path:");
        Scanner scn = new Scanner(System.in);
        NetList netList = Parser.parse(scn.nextLine());
        Analyzer analyzer = new Analyzer(netList);
        System.out.print(netList);
        System.out.println("Enter your choice:");
        System.out.println("1) DC Analysis.\t2) Transient Analysis.\t3) AC Analysis.");
        int choice = scn.nextInt();
        switch (choice) {
            case 1:
                System.out.print(Arrays.toString(analyzer.solution())); break;
            case 2:
            case 3:
            default:
                break;
        }
        scn.next();
    }
}