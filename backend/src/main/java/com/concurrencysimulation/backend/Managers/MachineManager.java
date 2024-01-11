package com.concurrencysimulation.backend.Managers;

import java.util.HashMap;
import java.util.Map;

import com.concurrencysimulation.backend.Models.Machine;

public class MachineManager {
    private static MachineManager instance = null;
    private int nextId = 0;
    private Map<Integer, Machine> machines = new HashMap<Integer, Machine>();

    private MachineManager() {
    }

    public static synchronized MachineManager getInstance() {
        if (instance == null) {
            instance = new MachineManager();
        }
        return instance;
    }

    public Machine createMachine(int time) {
        if (time < 1000) {
            time = getRandomTime();
        }
        Machine machine = new Machine(nextId, time);
        machines.put(nextId, machine);
        nextId++;
        return machine;
    }

    public Machine getMachine(int id) {
        if (machines.containsKey(id))
            return machines.get(id);
        return null;
    }

    public void removeMachine(int id) {
        if (machines.containsKey(id))
            machines.remove(id);
    }

    public Map<Integer, Machine> getMachines() {
        return machines;
    }

    public int getRandomTime() {
        int time = (int) (Math.random() * 1000);
        return time;
    }
}
