package mna.components;

import mna.Node;

public class Capacitor extends PassiveElement {
    private double C;
    public Capacitor(String name, int nodeA, int nodeB, double C) {
        this.C = C;
        this.name = name;
        this.nodeA = (nodeA == 0) ? Node.GND : new Node(nodeA);
        this.nodeB = (nodeB == 0) ? Node.GND : new Node(nodeB);
    }
    public double getC() {
        return C;
    }
    @Override
    public void setValue(double C) {
        this.C = C;
    }
    @Override
    public String toString() {
        return String.format("%s %d %d %f%n",name,nodeA.getId(),nodeB.getId(),C);
    }
}