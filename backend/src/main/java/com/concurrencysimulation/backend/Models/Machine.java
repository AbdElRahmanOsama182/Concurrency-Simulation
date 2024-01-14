package com.concurrencysimulation.backend.Models;

import java.awt.Color;
import java.util.ArrayList;

public class Machine extends Thread{
    private int machineId;
    private Product currentProduct;   // Product that is currently being processed
    private Queue queue;
    private ArrayList<Queue> queues = new ArrayList<Queue>();

    public Machine(int id, Queue queue, ArrayList<Queue> queues) {
        this.machineId = id;
        this.queue = queue;
        this.queues = queues;
    }

    public int getMachineId() {
        return machineId;
    }

    public Color getColor() {
        return currentProduct.getColor();
    }

    public void setCurrentProduct(Product product) {
        this.currentProduct = product;
    }

    public void finished(){
        this.currentProduct = null;
        for(Queue queue : queues){
            synchronized(queue){
                if(queue.getProducts().size()>0){
                    Product product = queue.getProducts().get(0);
                    queue.removeProduct(product);
                    this.serve(product);
                    return;
                }
            }
        }
        // TODO: notify queues that machine is free
        for (Queue queue : queues) {
            // queue.notifyMachineFree(this);
        }
    }

    public void run() {
        // create random processing time
        int processingTime = (int) (Math.random() * 5000);
        try {
            Thread.sleep(processingTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        synchronized (queue) {
            // add product to queue
            queue.addProduct(currentProduct);
        }
        this.finished();

    }

    public void serve(Product product) {
        this.setCurrentProduct(product);
        this.start();
    }

}
