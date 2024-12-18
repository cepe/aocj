package pl.kaq.aoc.y24.day17;

import pl.kaq.Solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

record MachineData(int a, int b, int c, List<Integer> mem) {

}

class MachineRunner {
    private final int a;
    private final int b;
    private final int c;
    private final List<Integer> mem;

    public MachineRunner(int a, int b, int c, List<Integer> mem) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.mem = mem;
    }

    public void run() {
        var machine = new Machine();
        machine.setRegisters(a, b, c);
        machine.loadProgram(mem);
        machine.run();
        machine.print();
    }

}

class Machine {
    private int a = 0;
    private int b = 0;
    private int c = 0;
    private int ip = 0;
    private List<Integer> mem = List.of();
    private final List<Integer> output = new ArrayList<>();

    public void setRegisters(int a, int b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public void loadProgram(List<Integer> mem) {
        this.mem = mem;
    }

    public void reset() {
        this.ip = 0;
        this.output.clear();
    }

    public List<Integer> getOutput() {
        return output;
    }

    public void run() {
        var memSize = mem.size();
        while (ip < memSize) {
//            print();
//            System.out.printf("operand: %d%n", mem.get(ip));
            switch (mem.get(ip)) {
                case 0 -> a = a / (1 << combo(mem.get(ip + 1)));
                case 1 -> b = b ^ mem.get(ip + 1);
                case 2 -> b = combo(mem.get(ip + 1)) % 8;
                case 3 -> {
                    if (a != 0) {
                        ip = mem.get(ip + 1) - 2;
                    }
                }
                case 4 -> b = b ^ c;
                case 5 -> output.add(combo(mem.get(ip + 1)) % 8);
                case 6 -> b = a / (1 << combo(mem.get(ip + 1)));
                case 7 -> c = a / (1 << combo(mem.get(ip + 1)));
                default -> throw new IllegalStateException();
            }
            ip += 2;
        }
    }

    private int combo(int operand) {
        return switch (operand) {
            case 0, 1, 2, 3 -> operand;
            case 4 -> a;
            case 5 -> b;
            case 6 -> c;
            default -> throw new IllegalStateException();
        };
    }

    public void print() {
        System.out.printf("Registers: a = %d, b = %d, c = %d%n", a, b, c);
        System.out.println(output);
    }
}

public class Day17 extends Solution {

    @Override
    public String firstStar(String input) {
        var machineData = parseMachineData(input);
        var machine = new Machine();
        machine.setRegisters(machineData.a(), machineData.b(), machineData.c());
        machine.loadProgram(machineData.mem());
        machine.run();
        return machine.getOutput().stream()
                .map(i -> Integer.toString(i))
                .collect(Collectors.joining(","));
    }

    private MachineData parseMachineData(String input) {
        var split = input.split("\n\n");
        var registersData = split[0].split("\n");
        var a = parseRegister(registersData[0]);
        var b = parseRegister(registersData[1]);
        var c = parseRegister(registersData[2]);
        var mem = Arrays.stream(split[1].split(": ")[1].split(","))
                .map(Integer::parseInt)
                .toList();
        return new MachineData(a, b, c, mem);
    }

    private int parseRegister(String registerStr) {
        return Integer.parseInt(registerStr.split(": ")[1]);
    }

    @Override
    public String secondStar(String input) {
        var machineData = parseMachineData(input);
        for (int i = 0; i < 1000000000; i++) {
            var machine = new Machine();
            machine.setRegisters(i, machineData.b(), machineData.c());
            machine.loadProgram(machineData.mem());
            machine.run();
            if (machine.getOutput().equals(machineData.mem())) {
                return Integer.toString(i);
            }
        }
        return "Not Found";
    }


    public static void main(String[] args) {
        new Day17().run("input.in");
    }
}
