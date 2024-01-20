package com.concurrencysimulation.backend.Models;

import java.util.HashMap;
import java.util.Map;

import com.concurrencysimulation.backend.Models.Machine;
import com.concurrencysimulation.backend.Models.Product;
import com.concurrencysimulation.backend.Models.Queue;

public class Memento{

    private Map<Integer, Machine> machines = new HashMap<Integer, Machine>();
    private Map<Integer, Product> products = new HashMap<Integer, Product>();
    private Map<Integer, Queue> queues = new HashMap<Integer, Queue>();

    //System connections as a graph
    /*
     * 
     */

    public Memento(Map<Integer, Machine> machines, Map<Integer, Product> products, Map<Integer, Queue> queues){
        this.machines = machines;
        this.products = products;
        this.queues = queues;
    }

    public Map<Integer, Machine> getMachines() {
        return new HashMap<>(machines);
    }

    public Map<Integer, Product> getProducts() {
        return new HashMap<>(products);
    }

    public Map<Integer, Queue> getQueues() {
        return new HashMap<>(queues);
    }

}