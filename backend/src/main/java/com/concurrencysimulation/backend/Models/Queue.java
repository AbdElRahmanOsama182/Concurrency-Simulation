package com.concurrencysimulation.backend.Models;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Queue extends Thread {
    private int queueId;
    private ArrayList<Machine> machines = new ArrayList<Machine>();
    private ArrayList<Product> products = new ArrayList<Product>();

    public Queue(int id) {
        this.queueId = id;
    }

    public int getQueueId() {
        return queueId;
    }

    public ArrayList<Machine> getMachines() {
        return machines;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setMachines(ArrayList<Machine> machines) {
        this.machines = machines;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public void addMachine(Machine machine) {
        machines.add(machine);
    }

    public void removeMachine(Machine machine) {
        machines.remove(machine);
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

}
