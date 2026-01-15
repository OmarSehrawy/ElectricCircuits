package com.mna.simulator.model.components;

import com.mna.simulator.model.Node;

public class Resistor extends PassiveElement {
    private double R;
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
        return String.format("%s %d %d %6.2f%n",name,nodeA.getId(),nodeB.getId(),R);
    }
}