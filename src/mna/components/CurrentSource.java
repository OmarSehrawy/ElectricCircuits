package mna.components;

import mna.Node;

public class CurrentSource extends ActiveElement {
    //Current flows from A to B
    protected double I;
    public CurrentSource(String name,int nodeA,int nodeB, double I) {
        this.I = I;
        this.name = name;
        this.nodeA = (nodeA == 0) ? Node.GND : new Node(nodeA);
        this.nodeB = (nodeB == 0) ? Node.GND : new Node(nodeB);
    }
    public double getI() {
        return I;
    }
    public void setI(double I) {
        this.I = I;
    }
    @Override
    public String toString() {
        return String.format("%s %d %d %f%n",name,nodeA.getId(),nodeB.getId(),I);
    }
}