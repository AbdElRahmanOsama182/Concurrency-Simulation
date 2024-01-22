package com.concurrencysimulation.backend.Models;

import java.awt.Color;
import java.util.ArrayList;

import com.concurrencysimulation.backend.Models.Queue;
import com.concurrencysimulation.backend.Models.Product;
import com.concurrencysimulation.backend.Models.Node;


public class Machine extends Thread implements Observable {
    private int machineId;
    private Product currentProduct; // Product that is currently being processed
    private Queue queue;
    private ArrayList<Queue> queues = new ArrayList<Queue>();
    private Node node;
    private String nodeKey;

    public Machine(int id) {
        this.machineId = id;
    }

    public int getMachineId() {
        return machineId;
    }

    public Product getCurrentProduct() {
        return currentProduct;
    }

    public Color getColor() {
        return currentProduct.getColor();
    }

    public void setCurrentProduct(Product product) {
        this.currentProduct = product;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }

    public String getNodeKey() {
        return nodeKey;
    }

    public void finished() {

        synchronized (queue) {
            // add product to queue
            queue.addProduct(currentProduct);
            System.out.println("Product " + currentProduct.getId() + " is added to queue " + queue.getQueueId());
            queue.notify();
        }
        this.run();

    }

    public void run() {

        this.currentProduct = null;
        while (this.currentProduct == null) {
            System.out.println("Machine " + this.machineId + " is looking for product");
            for (Queue queue : queues) {
                synchronized (queue) {
                    Product product = queue.getTopProduct();
                    System.out.println("Product " + product + " is taken from queue " + queue.getQueueId());
                    if (product == null) {
                        System.out.println(
                                "Product is null for machine " + this.machineId + " in queue " + queue.getQueueId());
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    System.out.println("Product " + product.getId() + " is being served in machine " + this.machineId
                            + "from machine");
                    this.setCurrentProduct(product);
                }

            }

        }
        // create random processing time
        int processingTime = (int) (Math.random() * 5000);
        System.out.println("Processing time: " + processingTime + " in machine " + this.machineId);
        try {
            Thread.sleep(processingTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Product " + currentProduct.getId() + " is finished" + " in machine " + this.machineId);
        if (this.machineId == 3) {
            System.out.println("Finisheddddddddddddddddddddddddddddddddd");
        }
        this.finished();

    }

    public void setTargetQueue(Queue queue) {
        this.queue = queue;
    }

    public Queue getTargetQueue() {
        return queue;
    }

    public void addQueue(Queue queue) {
        this.queues.add(queue);
    }
  
    public void notifyObserver(Boolean isFree) {
        for (Queue queue : queues) {
            queue.update(isFree, this);
        }
    }

    public void removeQueue(Queue queue) {
        this.queues.remove(queue);
    }

    public ArrayList<Queue> getQueues() {
        return queues;
    }


    public static void main(String[] args) {

        Queue queue1 = new Queue(1);
        Queue queue2 = new Queue(2);
        Queue queue3 = new Queue(3);

        Machine machine1 = new Machine(1);
        Machine machine2 = new Machine(2);
        Machine machine3 = new Machine(3);

        machine1.addQueue(queue1);
        machine1.setTargetQueue(queue2);

        machine2.addQueue(queue1);
        machine2.setTargetQueue(queue2);

        machine3.addQueue(queue2);
        machine3.setTargetQueue(queue3);

        queue1.addMachine(machine1);
        queue1.addMachine(machine2);

        queue2.addMachine(machine3);

        Product product1 = new Product(Color.RED, 1);
        Product product2 = new Product(Color.BLUE, 2);
        Product product3 = new Product(Color.GREEN, 3);
        Product product4 = new Product(Color.YELLOW, 4);
        Product product5 = new Product(Color.PINK, 5);
        Product product6 = new Product(Color.ORANGE, 6);
        Product product7 = new Product(Color.MAGENTA, 7);
        Product product8 = new Product(Color.CYAN, 8);
        Product product9 = new Product(Color.GRAY, 9);
        Product product10 = new Product(Color.BLACK, 10);

        queue1.addProduct(product1);
        queue1.addProduct(product2);
        queue1.addProduct(product3);
        queue1.addProduct(product4);
        queue1.addProduct(product5);
        queue1.addProduct(product6);
        queue1.addProduct(product7);
        queue1.addProduct(product8);
        queue1.addProduct(product9);
        queue1.addProduct(product10);

        // queue1.start();
        // queue2.start();
        // queue3.start();

        machine1.start();
        machine2.start();
        machine3.start();

    }

}
