package mna;

import mna.components.*;
import java.util.*;

public class NetList {
    private List<Resistor> resistors = new ArrayList<>();
    private List<Capacitor> capacitors = new ArrayList<>();
    private List<Inductor> inductors = new ArrayList<>();
    private List<CurrentSource> currentSources = new ArrayList<>();
    private List<VoltageSource> voltageSources = new ArrayList<>();
    private Map<Integer,Node> nodes = new HashMap<>();
    public NetList() {
        nodes.put(0,Node.GND);
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
        component.setNodeA(nodeManager(component.getNodeA().getId()));
        component.setNodeB(nodeManager(component.getNodeB().getId()));
    }
    private Node nodeManager(int id) {
        if(!nodes.containsKey(id)) nodes.put(id,new Node(id));
        return nodes.get(id);
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
    public Map<Integer, Node> getNodes() {
        return nodes;
    }
    @Override
    public String toString() {
        StringBuilder list = new StringBuilder();
        for(Component c : resistors) list.append(c);
        for(Component c : capacitors) list.append(c);
        for(Component c : inductors) list.append(c);
        for(Component c : currentSources) list.append(c);
        for(Component c : voltageSources) list.append(c);
        return list.toString();
    }
}