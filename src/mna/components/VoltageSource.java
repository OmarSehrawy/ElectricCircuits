package mna.components;

import mna.Node;

public class VoltageSource extends ActiveElement implements Source{
    //A is +V, B is -V
    protected double V;
    public VoltageSource(String name, int nodeA, int nodeB, double V) {
        this.V = V;
        this.name = name;
        this.nodeA = (nodeA == 0) ? Node.GND : new Node(nodeA);
        this.nodeB = (nodeB == 0) ? Node.GND : new Node(nodeB);
    }
    @Override
    public double getValue(double time) {
        return V;
    }
    public void setV(double V) {
        this.V = V;
    }
    @Override
    public String toString() {
        return String.format("%s %d %d %f%n",name,nodeA.getId(),nodeB.getId(),V);
    }
}