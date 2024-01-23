package com.concurrencysimulation.backend.Managers;

import java.util.HashMap;
import java.util.Map;

import com.concurrencysimulation.backend.Models.Machine;
import com.concurrencysimulation.backend.Models.Memento;
import com.concurrencysimulation.backend.Models.Product;
import com.concurrencysimulation.backend.Models.Queue;

public class SystemMementoManager {
    
   /*  private MachineManager machineManager = MachineManager.getInstance();
    private ProductManager productManager = ProductManager.getInstance();
    private QueueManager queueManager = QueueManager.getInstance();

    public Memento saveSystem(){
        Map<Integer, Machine> machines = new HashMap<>(machineManager.getMachines());
        Map<Integer, Product> products = new HashMap<>(productManager.getProducts());
        Map<Integer, Queue> queues = new HashMap<>(queueManager.getQueues());

        return new Memento(machines, products, queues);
    }

    public void restoreSystem(Memento memento){
        if(memento == null){
            throw new IllegalArgumentException("Cannot restore system with a null Memento.");
        }

        machineManager.setMachines(memento.getMachines());
        productManager.setProducts(memento.getProducts());
        queueManager.setQueues(memento.getQueues());
        
    }*/

}
