package mna;

import mna.components.*;
import mna.exceptions.InvalidComponentException;
import mna.exceptions.ValueException;

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
    private static Component convert(String comp) throws InvalidComponentException {
        String name; int a; int b; double value;
        String[] fields = comp.split(" ");
        name = fields[0];
        a = Integer.parseInt(fields[1]);
        b = Integer.parseInt(fields[2]);
        value = (fields.length >= 4)? Double.parseDouble(fields[3]) : 0;
        char cType = fields[0].charAt(0);
        try {
            return switch (cType) {
                case 'R' -> new Resistor(name, a, b, value);
                case 'W' -> new Wire(name, a, b);
                case 'V' -> new VoltageSource(name, a, b, value);
                case 'I' -> new CurrentSource(name, a, b, value);
                case 'C' -> new Capacitor(name, a, b, value);
                case 'L' -> new Inductor(name, a, b, value);
                default -> throw new InvalidComponentException("Component type doesn't exist");
            };
        } catch (ValueException e) {
            System.out.println(e.getMessage());
            throw new InvalidComponentException("Invalid component's values");
        }
    }
}