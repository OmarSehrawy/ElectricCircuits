package mna.components;

public class SineVS extends VoltageSource implements Source{
    private double frequency;
    public SineVS(String name, int nodeA, int nodeB, double V, double f) {
        super(name, nodeA, nodeB, V);
        frequency = f;
    }
    @Override
    public double getValue(double time) {
        return V * Math.sin(2*Math.PI*frequency*time);
    }
    public double getFrequency() {
        return frequency;
    }
    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }
}