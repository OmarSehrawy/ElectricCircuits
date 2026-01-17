package mna.components;

public class SineCS extends CurrentSource implements Source{
    private double frequency;
    private double phase;
    public SineCS(String name, int nodeA, int nodeB, double I, double frequency, double phase) {
        super(name, nodeA, nodeB, I);
        this.frequency = frequency;
        this.phase = phase;
    }
    @Override
    public double getValue(double time) {
        return I * Math.sin(2*Math.PI*frequency*time + phase);
    }
    public double getFrequency() {
        return frequency;
    }
    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }
    public double getPhase() {
        return phase;
    }
    public void setPhase(double phase) {
        this.phase = phase;
    }
}
