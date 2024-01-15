package com.concurrencysimulation.backend.Managers;

import java.util.HashMap;
import java.util.Map;

import com.concurrencysimulation.backend.Models.Machine;
import com.concurrencysimulation.backend.Models.Memento;
import com.concurrencysimulation.backend.Models.Product;
import com.concurrencysimulation.backend.Models.Queue;

public class SystemMementoManager {
    
    MachineManager machineManager = MachineManager.getInstance();
    ProductManager productManager = ProductManager.getInstance();
    QueueManager queueManager = QueueManager.getInstance();

    private Map<Integer, Machine> machines = machineManager.getMachines();
    private Map<Integer, Product> products = productManager.getProducts();
    private Map<Integer, Queue> queues = queueManager.getQueues();

    public Memento saveSystem(){
        return new Memento(machines, products, queues);
    }

    public void restoreSystem(Memento memento){
        machineManager.setMachines(memento.getMachines());
        productManager.setProducts(memento.getProducts());
        queueManager.setQueues(memento.getQueues());
        
    }

}
