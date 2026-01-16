package mna;

public class Node {
    private int id;
    private double v; //Voltage
    public static final Node GND = new Node();
    private Node() {
        this(0);
        this.v = 0;
    }
    public Node(int id) {
        this.id = id;
    }
    public double getV() {
        return v;
    }
    public void setV(double v) {
        this.v = v;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return String.format("n%d(V) = %6.2fV%n",id,v);
    }
}