package mna.components;

import mna.Node;
import mna.exceptions.ValueException;

public class Resistor extends PassiveElement {
    protected double R;
    public Resistor(String name, int nodeA, int nodeB,double R) throws ValueException {
        if (R <= 0) throw new ValueException("Resistance can't be 0 or less");
        this.R = R;
        this.name = name;
        this.nodeA = (nodeA == 0) ? Node.GND : new Node(nodeA);
        this.nodeB = (nodeB == 0) ? Node.GND : new Node(nodeB);
    }
    public double getG() throws ValueException {
        if (R < 0) throw new ValueException("Negative admittance");
        if (R == 0) throw new ValueException("Infinite admittance");
        return 1.0/R;
    }
    @Override
    public void setValue(double R) throws ValueException {
        if (R <= 0) throw new ValueException("Resistance can't be 0 or less");
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