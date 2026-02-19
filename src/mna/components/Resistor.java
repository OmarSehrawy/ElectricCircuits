package mna.components;

import mna.Node;

public class Resistor extends PassiveElement {
    protected double R;
    public Resistor(String name, int nodeA, int nodeB,double R) {
        this.R = R;
        this.name = name;
        this.nodeA = (nodeA == 0) ? Node.GND : new Node(nodeA);
        this.nodeB = (nodeB == 0) ? Node.GND : new Node(nodeB);
    }
    public double getG() {
        return 1.0/R;
    }
    public void setR(double R) {
        this.R = R;
    }
    @Override
    public String toString() {
        return String.format("%s %d %d %f%n",name,nodeA.getId(),nodeB.getId(),R);
    }
    @Override
    public double[] getInfo() {
        double V = nodeA.getV() - nodeB.getV();
        double I = V / R;
        double P = I * V;
        return new double[]{R,V,I,P};
    }
}