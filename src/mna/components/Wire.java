package mna.components;

public class Wire extends Resistor{
    private static final double WIRE_R = 1e-6;
    public Wire(String name, int nodeA, int nodeB) {
        super(name, nodeA, nodeB, WIRE_R);
    }
    @Override
    public String toString() {
        return String.format("%s %d %d%n",name,nodeA.getId(),nodeB.getId());
    }
    @Override
    public double[] getInfo() {
        double Vd = nodeA.getV() - nodeB.getV();
        double V = (nodeA.getV() + nodeB.getV())/2;
        double I = (R != 0)? Vd / R : 0;
        return new double[]{V,I};
    }
}
