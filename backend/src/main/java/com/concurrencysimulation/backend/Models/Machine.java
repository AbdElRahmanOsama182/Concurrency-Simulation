package com.concurrencysimulation.backend.Models;

import java.awt.Color;
import java.util.ArrayList;

import com.concurrencysimulation.backend.Models.Queue;
import com.concurrencysimulation.backend.Models.Product;

public class Machine extends Thread{
    private int machineId;
    private Product currentProduct;   // Product that is currently being processed
    private Queue queue;
    private ArrayList<Queue> queues = new ArrayList<Queue>();

    public Machine(int id) {
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
                    System.out.println("Product " + product.getId() + " is being served in machine " + this.machineId);
                    
                    this.setCurrentProduct(product);
                    this.run();
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
        System.out.println("Processing time: " + processingTime + " in machine " + this.machineId);
        try {
            Thread.sleep(processingTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Product " + currentProduct.getId() + " is finished"+ " in machine " + this.machineId);

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

    public void setTargetQueue(Queue queue){
        this.queue = queue;
    }

    public void addQueue(Queue queue){
        this.queues.add(queue);
    }


    public static void main(String[] args) {
        Queue queue = new Queue(1);
        Queue queue2 = new Queue(2);
        
        Machine machine = new Machine(1);
        machine.addQueue(queue);
        machine.setTargetQueue(queue2);
        Product product = new Product(Color.RED,1);
        machine.serve(product);

        Machine machine2 = new Machine(2);
        machine2.addQueue(queue2);
        machine2.setTargetQueue(queue);
        Product product2 = new Product(Color.BLUE,2);
        machine2.serve(product2);


    }

}
