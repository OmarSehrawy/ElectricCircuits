package mna;

import mna.components.*;
import java.util.Arrays;
import org.ejml.simple.SimpleMatrix;

public class Analyzer {
    enum Mode {DC,T,AC}
    Mode anaylsismode;
    private NetList netList;
    private int m;
    private int n;
    private int o;
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
        o = this.netList.lCount();
        switch (anaylsismode) {
            case DC -> size = m+n;
            case T -> size = m+n+o;
            case AC -> {}
            case null -> {}
        }
    }
    private void setTo0(double[][] a) {
        for(double[] b : a) Arrays.fill(b,0.0);
    }
    private void setGMatrix(double step) {
        G = new double[size][size];
        setTo0(G);
        for (Resistor r : netList.getResistors().values()) {
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
        for (VoltageSource vs : netList.getVoltageSources().values()) {
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
        if(anaylsismode == Mode.T) {
            int lIndex = 0;
            for (Inductor l : netList.getInductors().values()) {
                int a = l.getNodeA().getId() - 1;
                int b = l.getNodeB().getId() - 1;
                int row = lIndex + m + n;
                if (l.getNodeA() != Node.GND) {
                    G[a][row] = 1;
                    G[row][a] = 1;
                }
                if (l.getNodeB() != Node.GND) {
                    G[b][row] = -1;
                    G[row][b] = -1;
                }
                G[row][row] = -l.getL() / step;
                lIndex++;
            }
        }
    }
    private void setCMatrix() {
        C = new double[size][size];
        setTo0(C);
        for (Capacitor c : netList.getCapacitors().values()) {
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
    private void setZVector(double time) {
        Z = new double[size];
        Arrays.fill(Z,0.0);
        for (CurrentSource cs : netList.getCurrentSources().values()) {
            int a = cs.getNodeA().getId() - 1;
            int b = cs.getNodeB().getId() - 1;
            if (cs.getNodeA() != Node.GND) Z[a] -= cs.getI();
            if (cs.getNodeB() != Node.GND) Z[b] += cs.getI();
        }
        int vsIndex = 0;
        for (VoltageSource vs : netList.getVoltageSources().values()) {
            Z[m+vsIndex] = vs.getValue(time);
            vsIndex++;
        }
    }
    public double[] solution() {
        anaylsismode = Mode.DC;
        updateNetList(netList);
        prepMatrices();
        SimpleMatrix matrixG = new SimpleMatrix(G);
        SimpleMatrix vectorZ = new SimpleMatrix(size,1,true,Z);
        SimpleMatrix resultX = matrixG.solve(vectorZ);
        double[] solution = new double[size];
        for (int i = 0; i < size; i++) {
            solution[i] = resultX.get(i);
        }
        return solution;
    }
    public double[][] transientAnalysis(double step,double duration) {
        anaylsismode = Mode.T;
        updateNetList(netList);
        prepMatrices(step);
        double inv_h = 1.0/step;
        int steps = (int)(duration/step);
        SimpleMatrix matrixG = new SimpleMatrix(G);
        SimpleMatrix matrixC = new SimpleMatrix(C);
        SimpleMatrix initial = new SimpleMatrix(size,1);
        double[][] trans = new double[steps][size];
        SimpleMatrix C_h = matrixC.scale(inv_h);
        SimpleMatrix A = matrixG.plus(C_h);
        double[] staticZ = Z.clone();
        for (int i = 0; i < steps; i++) {
            System.arraycopy(staticZ,0,Z,0,size);
            int lIndex = 0;
            for(Inductor l : netList.getInductors().values()) {
                int row = m+n+lIndex;
                double prev_iL = initial.get(row);
                Z[row] -= (l.getL()/step) * prev_iL;
                lIndex++;
            }
            SimpleMatrix vectorZ = new SimpleMatrix(size,1,true,Z);
            SimpleMatrix B = vectorZ.plus(C_h.mult(initial));
            initial = A.solve(B);
            for (int j = 0; j < size; j++) {
                trans[i][j] = initial.get(j);
            }
        }
        return trans;
    }
    private void prepMatrices(double step,double time) { setGMatrix(step); setCMatrix(); setZVector(time); }
    private void prepMatrices(double step) { setGMatrix(step); setCMatrix(); setZVector(0); }
    private void prepMatrices() { setGMatrix(0); setCMatrix(); setZVector(0); }
}