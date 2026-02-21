package mna.components;

import mna.Node;

public class CurrentSource extends ActiveElement implements Source {
    //Current flows from A to B
    protected double I;
    public CurrentSource(String name,int nodeA,int nodeB, double I) {
        this.I = I;
        this.name = name;
        this.nodeA = (nodeA == 0) ? Node.GND : new Node(nodeA);
        this.nodeB = (nodeB == 0) ? Node.GND : new Node(nodeB);
    }
    @Override
    public void setValue(double I) {
        this.I = I;
    }
    @Override
    public double getValue(double time) {
        return I;
    }
    @Override
    public String toString() {
        return String.format("%s %d %d %f%n",name,nodeA.getId(),nodeB.getId(),I);
    }
    @Override
    public double[] getInfo() {
        double V = nodeA.getV() - nodeB.getV();
        double P = I * V;
        return new double[]{V,I,P};
    }
}