package mna;

import mna.components.*;
import java.util.*;
import mna.exceptions.AnalysisException;
import mna.exceptions.InvalidComponentException;
import mna.exceptions.SweepException;
import mna.exceptions.ValueException;
import org.ejml.data.SingularMatrixException;
import org.ejml.simple.*;

public class Analyzer {
    private enum Mode {DC,T,AC}
    private Mode anaylsismode;
    private boolean sweepMode = false;
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
    private void assignNodeVoltage(double[] V) {
        for (int i = 0; i < m; i++) {
            int id = i + 1;
            netList.getNodes().get(id).setV(V[i]);
        }
    }
    private void assignVSCurrent(double[] V) {
        int index = m;
        for (VoltageSource vs : netList.getVoltageSources().values()) {
            vs.setI(V[index++]);
        }
    }
    public double[] dcSolution() throws AnalysisException {
        anaylsismode = Mode.DC;
        updateNetList(netList);
        prepMatrices();
        SimpleMatrix matrixG = new SimpleMatrix(G);
        SimpleMatrix vectorZ = new SimpleMatrix(size,1,true,Z);
        double[] solution = new double[size];
        try {
            SimpleMatrix resultX = matrixG.solve(vectorZ);
            for (int i = 0; i < size; i++) {
                solution[i] = resultX.get(i);
            }
            if (!sweepMode) {
                assignNodeVoltage(solution);
                assignVSCurrent(solution);
            }
        } catch (SingularMatrixException e) {
            throw new AnalysisException("Singular matrix");
        }
        return solution;
    }
    public double[][] dcSweep(String c,int steps,double start,double end) throws SweepException, InvalidComponentException {
        if (steps < 1) throw new SweepException("Invalid number of steps");
        anaylsismode = Mode.DC;
        sweepMode = true;
        updateNetList(netList);
        double[][] solution = new double[steps+1][size+1];
        char type = c.charAt(0);
        Component sweepComponent = switch (type) {
            case 'R' -> netList.getResistors().get(c);
            case 'V' -> netList.getVoltageSources().get(c);
            case 'I' -> netList.getCurrentSources().get(c);
            default -> throw new InvalidComponentException("Unknown component");
        };
        if(sweepComponent == null) throw new InvalidComponentException("Component doesn't exist");
        try {
            sweepComponent.setValue(start);
            sweepComponent.setValue(end);
        } catch (ValueException e) {
            throw new SweepException("Invalid values");
        }
        double delta = (end-start)/steps;
        for (int i = 0; i <= steps; i++) {
            double currentValue = start + i*delta;
            switch (type) {
                case 'R', 'V', 'I':
                    sweepComponent.setValue(currentValue);
                    break;
                default:
                    break;
            }
            try {
                double[] currentSolution = dcSolution();
                System.arraycopy(currentSolution, 0, solution[i], 0, size);
                solution[i][size] = currentValue;
            } catch (AnalysisException e) {
                throw new SweepException("Sweep failed at " + type + " = " + currentValue);
            }
        }
        sweepMode = false;
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
    private void prepMatrices(double step,double time) {}
    private void prepMatrices(double step) {}
    private void prepMatrices() {
        G = new double[size][size];
        setTo0(G);
        Z = new double[size];
        Arrays.fill(Z,0.0);
        addResistorStamp();
        addWireStamp();
        addCSStamp(0);
        addVSStamp(0);
    }
    private void addResistorStamp() {
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
    }
    private void addWireStamp() {
        for (Wire w : netList.getWires().values()) {
            int a = w.getNodeA().getId() - 1;
            int b = w.getNodeB().getId() - 1;
            if (w.getNodeA() != Node.GND) G[a][a] += w.getG();
            if (w.getNodeB() != Node.GND) G[b][b] += w.getG();
            if (w.getNodeA() != Node.GND && w.getNodeB() != Node.GND) {
                G[a][b] -= w.getG();
                G[b][a] -= w.getG();
            }
        }
    }
    private void addVSStamp(double time) {
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
            Z[m+vsIndex] = vs.getValue(time);
            vsIndex++;
        }
    }
    private void addCSStamp(double time) {
        for (CurrentSource cs : netList.getCurrentSources().values()) {
            int a = cs.getNodeA().getId() - 1;
            int b = cs.getNodeB().getId() - 1;
            if (cs.getNodeA() != Node.GND) Z[a] -= cs.getValue(time);
            if (cs.getNodeB() != Node.GND) Z[b] += cs.getValue(time);
        }
    }
    private void addCapacitorStamp() {
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
    private void addInductorStamp(double step) {
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
}