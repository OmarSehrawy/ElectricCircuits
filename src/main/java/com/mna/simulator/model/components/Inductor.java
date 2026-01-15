package com.mna.simulator.model.components;

import com.mna.simulator.model.Node;

public class Inductor extends PassiveElement {
    private double L;
    public Inductor(String name, int nodeA, int nodeB, double L) {
        this.L = L;
        this.name = name;
        this.nodeA = (nodeA == 0) ? Node.GND : new Node(nodeA);
        this.nodeB = (nodeB == 0) ? Node.GND : new Node(nodeB);
    }
    public double getL() {
        return L;
    }
    public void setL(double L) {
        this.L = L;
    }
    @Override
    public String toString() {
        return String.format("%s %d %d %6.2f%n",name,nodeA.getId(),nodeB.getId(),L);
    }
}