package com.mna.simulator.service;

import com.mna.simulator.model.*;
import com.mna.simulator.model.components.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AnalyzerTest {
    @Test
    void testVD() {
        NetList netList = new NetList();
        netList.addComponents(
                new VoltageSource("V1",1,0,10),
                new Resistor("R1",1,2,5),
                new Resistor("R2",2,0,5)
        );
        Analyzer analyzer = new Analyzer(netList);
        double[] solution = analyzer.solution();
        assertEquals(5.0,solution[1],0.01,"Voltage divider works properly");
    }
}
