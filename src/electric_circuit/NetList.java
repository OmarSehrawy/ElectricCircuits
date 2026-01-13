package electric_circuit;

import electric_circuit.components.*;
import java.util.ArrayList;
import java.util.List;

public class NetList {
    private List<Resistor> resistors = new ArrayList<>();
    private List<Capacitor> capacitors = new ArrayList<>();
    private List<Inductor> inductors = new ArrayList<>();
    private List<CurrentSource> currentSources = new ArrayList<>();
    private List<VoltageSource> voltageSources = new ArrayList<>();
    private List<Node> nodes = new ArrayList<>();
    public NetList() {
        nodes.add(Node.GND);
    }
    public void addComponents(Component comp) {
        linkNode(comp);
        switch (comp) {
            case Resistor r -> resistors.add(r);
            case Capacitor c -> capacitors.add(c);
            case Inductor i -> inductors.add(i);
            case CurrentSource cs -> currentSources.add(cs);
            case VoltageSource vs -> voltageSources.add(vs);
            default -> {}
        }
    }
    public void addComponents(Component... comps) {
        for (Component comp : comps) addComponents(comp);
    }
    private void linkNode(Component component) {
        component.setNodeA(nodeManager(component.getNodeA()));
        component.setNodeB(nodeManager(component.getNodeB()));
    }
    private Node nodeManager(Node n) {
        if(searchNode(n.getId()) == null) nodes.add(n);
        return searchNode(n.getId());
    }
    private Node searchNode(int id) {
        Node n = null;
        for (Node i : nodes) {
            if (i.getId() == id) n = i;
        }
        return n;
    }
    public int nodesCount() {
        return nodes.size()-1;
    }
    public int vsCount() {
        return voltageSources.size();
    }
    public List<Resistor> getResistors() {
        return resistors;
    }
    public List<Capacitor> getCapacitors() {
        return capacitors;
    }
    public List<Inductor> getInductors() {
        return inductors;
    }
    public List<CurrentSource> getCurrentSources() {
        return currentSources;
    }
    public List<VoltageSource> getVoltageSources() {
        return voltageSources;
    }
    public List<Node> getNodes() {
        return nodes;
    }
}