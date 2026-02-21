package mna.components;

import mna.Node;

public class Component {
    protected Node nodeA;
    protected Node nodeB;
    protected String name;
    public Node getNodeA() {
        return nodeA;
    }
    public void setNodeA(Node nodeA) {
        this.nodeA = nodeA;
    }
    public Node getNodeB() {
        return nodeB;
    }
    public void setNodeB(Node nodeB) {
        this.nodeB = nodeB;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double[] getInfo() {
        return null;
    }
    public void setValue(double value) {}
}