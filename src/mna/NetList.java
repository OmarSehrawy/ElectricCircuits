package mna;

import mna.components.*;
import mna.exceptions.InvalidComponentException;
import java.util.*;

public class NetList {
    private Map<String,Wire> wires = new HashMap<>();
    private Map<String,Resistor> resistors = new HashMap<>();
    private Map<String,Capacitor> capacitors = new HashMap<>();
    private Map<String,Inductor> inductors = new HashMap<>();
    private Map<String,CurrentSource> currentSources = new HashMap<>();
    private Map<String,VoltageSource> voltageSources = new HashMap<>();
    private Map<Integer,Node> nodes = new HashMap<>();
    public NetList() {
        nodes.put(0,Node.GND);
    }
    public void addComponents(Component comp) {
        linkNode(comp);
        switch (comp) {
            case Wire w -> wires.put(w.getName(),w);
            case Resistor r -> resistors.put(r.getName(),r);
            case Capacitor c -> capacitors.put(c.getName(),c);
            case Inductor l -> inductors.put(l.getName(),l);
            case CurrentSource i -> currentSources.put(i.getName(),i);
            case VoltageSource v -> voltageSources.put(v.getName(),v);
            default -> {}
        }
    }
    public void addComponents(Component... comps) {
        for (Component comp : comps) addComponents(comp);
    }
    private void linkNode(Component component) {
        try {
            component.setNodeA(nodeManager(component.getNodeA().getId()));
            component.setNodeB(nodeManager(component.getNodeB().getId()));
        } catch (InvalidComponentException e) {
            System.out.println(e.getMessage());
        }
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
    public int lCount() {return inductors.size();}
    public Map<String,Resistor> getResistors() {
        return resistors;
    }
    public Map<String, Wire> getWires() {
        return wires;
    }
    public Map<String,Capacitor> getCapacitors() {
        return capacitors;
    }
    public Map<String,Inductor> getInductors() {
        return inductors;
    }
    public Map<String,CurrentSource> getCurrentSources() {
        return currentSources;
    }
    public Map<String,VoltageSource> getVoltageSources() {
        return voltageSources;
    }
    public Map<Integer, Node> getNodes() {
        return nodes;
    }
    @Override
    public String toString() {
        StringBuilder list = new StringBuilder();
        for(Component c : resistors.values()) list.append(c);
        for(Component c : wires.values()) list.append(c);
        for(Component c : capacitors.values()) list.append(c);
        for(Component c : inductors.values()) list.append(c);
        for(Component c : currentSources.values()) list.append(c);
        for(Component c : voltageSources.values()) list.append(c);
        return list.toString();
    }
}