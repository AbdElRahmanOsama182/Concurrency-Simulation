package com.concurrencysimulation.backend.Models;

import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.crypto.Mac;

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

    public Product getTopProduct() {
        synchronized (this) {
            if (products.size() > 0) {
                System.out.println("Product size: " + products.size() + " of queue " + this.getQueueId());
                Product lastProduct = products.get(products.size() - 1);
                products.remove(products.size() - 1);
                return lastProduct;
            }
            return null;
        }
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
        // get queue thread state
        System.out.println("Thread state: " + this.getState()+" of queue "+this.getQueueId());
        synchronized (this) {
            products.add(product);
        }
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }
    
}
