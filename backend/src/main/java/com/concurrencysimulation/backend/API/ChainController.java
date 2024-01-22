package com.concurrencysimulation.backend.API;

import java.awt.Color;
import java.time.temporal.ChronoUnit;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.concurrencysimulation.backend.Managers.MachineManager;
import com.concurrencysimulation.backend.Managers.ProductManager;
import com.concurrencysimulation.backend.Managers.QueueManager;
import com.concurrencysimulation.backend.Models.Edge;
import com.concurrencysimulation.backend.Models.Machine;
import com.concurrencysimulation.backend.Models.Node;
import com.concurrencysimulation.backend.Models.Product;
import com.concurrencysimulation.backend.Models.Queue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@CrossOrigin(origins = { "http://localhost:8081" })
public class ChainController {
    
    @PostMapping("/data")
    public String submitGraph(@RequestBody Map<String, Object> payload) {
        System.out.println("Payload:");
        System.out.println(payload);
        Map<String, Node> nodes = new HashMap<>();
        Map<String, Edge> edges = new HashMap<>();
        // add payload to nodes and edges maps 
        // ex edges payload    
        //  "edges":{
        //     "edge1":{ "source": "node1", "target": "node4", "color": "#000000" },
        //     "edge2":{ "source": "node1", "target": "node5", "color": "#000000" },
        //     "edge3":{ "source": "node4", "target": "node6", "color": "#000000" },
        //     "edge4":{ "source": "node5", "target": "node6", "color": "#000000" },
        //     "edge5":{ "source": "node6", "target": "node2", "color": "#000000" }
        // }
        
        // ex nodes payload
        // "nodes":{
        //     "node1":{ "name": "Start Node", "shape": "circle", "color": "green","main": true,"type": "machine","type":"queue"},
        //     "node2":{ "name": "Edge Node", "shape": "circle", "color": "green","main": true,"type": "machine","type":"queue"},
        //     "node3":{ "name": "Q1", "shape": "circle", "color": "green","main": true,"type": "machine","type":"queue"},
        //     "node4":{ "name": "M1", "shape": "square", "color": "red","main": true,"type": "machine","type":"machine"},
        //     "node5":{ "name": "M2", "shape": "square", "color": "red","main": true,"type": "machine","type":"machine"},
        //     "node6":{ "name": "M3", "shape": "square", "color": "red","main": true,"type": "machine","type":"machine"}
        // },
        
        // add nodes to nodes map
        for (Map.Entry<String, Object> entry : payload.entrySet()) {
            if (entry.getKey().equals("nodes")) {
                Map<String, Object> nodesPayload = (Map<String, Object>) entry.getValue();
                for (Map.Entry<String, Object> nodeEntry : nodesPayload.entrySet()) {
                    Map<String, Object> nodePayload = (Map<String, Object>) nodeEntry.getValue();
                    Node node = new Node(nodePayload.get("name").toString(), nodePayload.get("shape").toString(),
                            nodePayload.get("color").toString(), nodePayload.get("type").toString(),
                            Integer.parseInt(nodePayload.get("x").toString()),
                            Integer.parseInt(nodePayload.get("y").toString()));
                    nodes.put(nodeEntry.getKey(), node);
                }
            }
        }
        // add edges to edges map
        for (Map.Entry<String, Object> entry : payload.entrySet()) {
            if (entry.getKey().equals("edges")) {
                Map<String, Object> edgesPayload = (Map<String, Object>) entry.getValue();
                for (Map.Entry<String, Object> edgeEntry : edgesPayload.entrySet()) {
                    Map<String, Object> edgePayload = (Map<String, Object>) edgeEntry.getValue();
                    Edge edge = new Edge(edgePayload.get("source").toString(), edgePayload.get("target").toString(),
                            edgePayload.get("color").toString());
                    edges.put(edgeEntry.getKey(), edge);
                }
            }
        }

        // print nodes and edges
        System.out.println("Nodes:");
        for (Map.Entry<String, Node> entry : nodes.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue().getName());
        }
        System.out.println("Edges:");
        for (Map.Entry<String, Edge> entry : edges.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue().getSource() + " -> "
                    + entry.getValue().getTarget());
        }
        // clear machine manager
        MachineManager.getInstance().clear();
        // clear queues
        QueueManager.getInstance().clear();
        // clear products
        ProductManager.getInstance().clear();

        System.out.println("Managers are cleared");

        Map<String,Machine> mappingMachine= new HashMap<>();
        Map<String,Queue> mappingQueue= new HashMap<>();

        Queue startQueue=null;
        // create machines and queues
        for (Map.Entry<String, Node> entry : nodes.entrySet()) {
            Node node = entry.getValue();
            if (node.getType().equals("machine")) {
                Machine machine=MachineManager.getInstance().createMachine();
                machine.setNode(node);
                machine.setNodeKey(entry.getKey());
                mappingMachine.put(entry.getKey(),machine);
            }else if(node.getType().equals("queue")){
                Queue queue=QueueManager.getInstance().createQueue();
                queue.setNode(node);
                queue.setNodeKey(entry.getKey());
                mappingQueue.put(entry.getKey(),queue);
                if(node.getName().equals("Start Queue")){
                    startQueue=queue;
                }
            }
        }

        System.out.println("Machines and queues are created");
        // print mappings
        System.out.println("Mappings:");
        for (Map.Entry<String, Machine> entry : mappingMachine.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue().getMachineId() + " : "
                    + entry.getValue().getNode().getName());
        }
        System.out.println("Mappings Queue:");
        for (Map.Entry<String, Queue> entry : mappingQueue.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue().getQueueId() + " : "
                    + entry.getValue().getNode().getName());
        }

        // create edges
        for (Map.Entry<String, Edge> entry : edges.entrySet()) {
            Edge edge = entry.getValue();
            Machine machine;
            Queue queue;
            System.out.println("Edge: "+edge.getSource()+" -> "+edge.getTarget());
            if(mappingMachine.containsKey(edge.getSource()) && mappingQueue.containsKey(edge.getTarget())){
                machine=mappingMachine.get(edge.getSource());
                queue=mappingQueue.get(edge.getTarget());
                machine.setTargetQueue(queue);
            }else if(mappingMachine.containsKey(edge.getTarget()) && mappingQueue.containsKey(edge.getSource())){
                machine=mappingMachine.get(edge.getTarget());
                queue=mappingQueue.get(edge.getSource());
                machine.addQueue(queue);
                queue.addMachine(machine);
            }else{
                return "Error: Invalid edge";
            }
        }

        System.out.println("Edges are created");

        // create random number of products and add them to queue with name "Start Node"
        
        int numberOfProducts=(int)(Math.random()*10);
        // int numberOfProducts=6;
        System.out.println("Number of productssssssssssssssssss: "+numberOfProducts);

        for(int i=0;i<numberOfProducts;i++){
            ProductManager.getInstance().createProduct();
            Product product=ProductManager.getInstance().getProduct(ProductManager.getInstance().getProducts().size()-1);
            System.out.println("Product "+product.getId()+" is created");
            startQueue.addProduct(product);
        }
        // print all products
        for (Product product : startQueue.getProducts()) {
            System.out.println("Product " + product.getId() + " of queue Start Queue " );
        }

        System.out.println(numberOfProducts + " Products are created");


        for(Queue queue:QueueManager.getInstance().getQueues().values()){
            System.out.println("Queue " + queue.getQueueId() + " is running");
            queue.start();
        }

        // run machines
        for (Machine machine : MachineManager.getInstance().getMachines().values()) {
            System.out.println("Machine " + machine.getMachineId() + " is running");
            machine.start();
        }


        
        return "Machines are running";
    }
    

    @GetMapping("/data")
    public Map<String,Object> fetchGraph(){
        //iterate over machines and queues and create entry with key: node name and value: color of product
        // for queue key: node name, value: number of products
        Map<String,Object> data=new HashMap<>();

        for(Machine machine:MachineManager.getInstance().getMachines().values()){
            if(machine.getCurrentProduct()!=null){
                Color color=machine.getCurrentProduct().getColor();
                String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
                data.put(machine.getNode().getName(), hex);
                // System.out.println(machine.getNode().getName()+" : "+hex);
            }else{
                data.put(machine.getNode().getName(), "#386641");
                // System.out.println(machine.getNode().getName()+" : "+"#386641");
            }
        }

        for(Queue queue:QueueManager.getInstance().getQueues().values()){
            data.put(queue.getNode().getName(), queue.getProducts().size());
        }

        return data;
    }
    
    @GetMapping("/graph")
    public Map<String,Object> fetchStructure(){
        Map<String,Object> payload=new HashMap<>();
        Map<String, Object> nodes = new HashMap<>();
        Map<String, Object> edges = new HashMap<>();
        // add nodes to payload
        for (Map.Entry<Integer, Machine> entry : MachineManager.getInstance().getMachines().entrySet()) {
            Machine machine = entry.getValue();
            Node node = machine.getNode();
            nodes.put(machine.getNodeKey(), node.serialize());
        }
        for (Map.Entry<Integer, Queue> entry : QueueManager.getInstance().getQueues().entrySet()) {
            Queue queue = entry.getValue();
            Node node = queue.getNode();
            nodes.put(queue.getNodeKey(), node.serialize());
        }
        // add edges to payload
        for (Map.Entry<Integer, Machine> entry : MachineManager.getInstance().getMachines().entrySet()) {
            Machine machine = entry.getValue();
            for(Queue queue:machine.getQueues()){
                Edge edge=new Edge(queue.getNodeKey(),machine.getNodeKey(),"#000000");
                edges.put("edge"+machine.getMachineId()+queue.getQueueId(), edge.serialize());
            }
            if(machine.getTargetQueue()!=null){
                Edge edge=new Edge(machine.getNodeKey(),machine.getTargetQueue().getNodeKey(),"#000000");
                edges.put("edge"+machine.getMachineId()+machine.getTargetQueue().getQueueId(), edge.serialize());
            }
        }
        // add nodes and edges to payload
        payload.put("nodes", nodes);
        payload.put("edges", edges);
        return payload;
    }

}
