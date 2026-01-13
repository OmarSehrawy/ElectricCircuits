package electric_circuit.components;

import electric_circuit.Node;

public class Resistor extends PassiveElement {
    private double G; //admittance
    public Resistor(String name, int nodeA, int nodeB,double G) {
        this.G = G;
        this.name = name;
        this.nodeA = (nodeA == 0) ? Node.GND : new Node(nodeA);
        this.nodeB = (nodeB == 0) ? Node.GND : new Node(nodeB);
    }
    public double getG() {
        return G;
    }
    public void setG(double G) {
        this.G = G;
    }
    @Override
    public String toString() {
        return String.format("%s %d %d %6.2f%n",name,nodeA.getId(),nodeB.getId(),1.0/G);
    }
}