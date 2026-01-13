package electric_circuit.components;

import electric_circuit.Node;

public class VoltageSource extends ActiveElement{
    //A is +V, B is -V
    private double V;
    public VoltageSource(String name, int nodeA, int nodeB, double V) {
        this.V = V;
        this.name = name;
        this.nodeA = (nodeA == 0) ? Node.GND : new Node(nodeA);
        this.nodeB = (nodeB == 0) ? Node.GND : new Node(nodeB);
    }
    public double getV() {
        return V;
    }
    public void setV(double V) {
        this.V = V;
    }
    @Override
    public String toString() {
        return String.format("%s %d %d %6.2f%n",name,nodeA.getId(),nodeB.getId(),V);
    }
}