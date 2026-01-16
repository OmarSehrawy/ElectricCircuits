package mna;

import mna.components.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Parser {
    public static NetList parse(String path) {
        NetList netList = new NetList();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while((line = br.readLine()) != null) {
                netList.addComponents(convert(line));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return netList;
    }
    private static Component convert(String comp) {
        Component c = null;
        String name; int a; int b; double value;
        String[] fields = comp.split(" ");
        name = fields[0];
        a = Integer.parseInt(fields[1]);
        b = Integer.parseInt(fields[2]);
        value = Double.parseDouble(fields[3]);
        char cType = fields[0].charAt(0);
        if (cType == 'R') {
            c = new Resistor(name,a,b,value);
        } else if (cType == 'C') {
            c = new Capacitor(name,a,b,value);
        } else if (cType == 'L') {
            c = new Inductor(name,a,b,value);
        } else if (cType == 'V') {
            c = new VoltageSource(name,a,b,value);
        } else if (cType == 'I') {
            c = new CurrentSource(name,a,b,value);
        }
        return c;
    }
}