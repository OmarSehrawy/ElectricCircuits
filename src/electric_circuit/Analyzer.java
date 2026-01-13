package electric_circuit;

import electric_circuit.components.*;
import java.util.Arrays;

public class Analyzer {
    private NetList netList;
    private int m;
    private int n;
    private int size;
    private double[][] G;
    private double[][] C;
    private double[] Z;
    public Analyzer(NetList netList) {
        updateNetList(netList);
    }
    public void updateNetList(NetList netList) {
        this.netList = netList;
        m = this.netList.nodesCount();
        n = this.netList.vsCount();
        size = m+n;
        setGMatrix();
        setCMatrix();
        setZVector();
    }
    private void setTo0(double[][] a) {
        for(double[] b : a) Arrays.fill(b,0.0);
    }
    private void setGMatrix() {
        G = new double[size][size];
        setTo0(G);
        for (Resistor r : netList.getResistors()) {
            int a = r.getNodeA().getId() - 1;
            int b = r.getNodeB().getId() - 1;
            if (r.getNodeA() != Node.GND) G[a][a] += r.getG();
            if (r.getNodeB() != Node.GND) G[b][b] += r.getG();
            if (r.getNodeA() != Node.GND && r.getNodeB() != Node.GND) {
                G[a][b] -= r.getG();
                G[b][a] -= r.getG();
            }
        }
        int vsIndex = 0;
        for (VoltageSource vs : netList.getVoltageSources()) {
            int a = vs.getNodeA().getId() - 1;
            int b = vs.getNodeB().getId() - 1;
            if (vs.getNodeA() != Node.GND) {
                G[a][m+vsIndex] = 1;
                G[vsIndex+m][a] = 1;
            }
            if (vs.getNodeB() != Node.GND) {
                G[b][vsIndex+m] = -1;
                G[vsIndex+m][b] = -1;
            }
            vsIndex++;
        }
    }
    private void setCMatrix() {
        C = new double[size][size];
        setTo0(C);
        for (Capacitor c : netList.getCapacitors()) {
            int a = c.getNodeA().getId() - 1;
            int b = c.getNodeB().getId() - 1;
            if (c.getNodeA() != Node.GND) C[a][a] += c.getC();
            if (c.getNodeB() != Node.GND) C[b][b] += c.getC();
            if (c.getNodeA() != Node.GND && c.getNodeB() != Node.GND) {
                C[a][b] -= c.getC();
                C[b][a] -= c.getC();
            }
        }
    }
    private void setZVector() {
        Z = new double[size];
        Arrays.fill(Z,0.0);
        for (CurrentSource cs : netList.getCurrentSources()) {
            int a = cs.getNodeA().getId() - 1;
            int b = cs.getNodeB().getId() - 1;
            if (cs.getNodeA() != Node.GND) Z[a] -= cs.getI();
            if (cs.getNodeB() != Node.GND) Z[b] += cs.getI();
        }
        int vsIndex = 0;
        for (VoltageSource vs : netList.getVoltageSources()) {
            Z[m+vsIndex] = vs.getV();
            vsIndex++;
        }
    }
    public double[] solution() {
        Gauss();
        return Z;
    }
    private void Gauss() {
        Elimination.Solve(G,Z);
    }
}