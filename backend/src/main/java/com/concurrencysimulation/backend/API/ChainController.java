package com.concurrencysimulation.backend.API;

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
                            Boolean.parseBoolean(nodePayload.get("main").toString()),
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
        // create machines
        for (Map.Entry<String, Node> entry : nodes.entrySet()) {
            Node node = entry.getValue();
            if (node.getType().equals("machine")) {
                Machine machine=MachineManager.getInstance().createMachine();
                mappingMachine.put(entry.getKey(),machine);
            }else if(node.getType().equals("queue")){
                Queue queue=QueueManager.getInstance().createQueue();
                mappingQueue.put(entry.getKey(),queue);
                if(node.getName().equals("Start Node")){
                    startQueue=queue;
                }
            }
        }

        System.out.println("Machines and queues are created");
        // print mappings
        System.out.println("Mappings:");
        for (Map.Entry<String, Machine> entry : mappingMachine.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue().getMachineId());
        }
        System.out.println("Mappings Queue:");
        for (Map.Entry<String, Queue> entry : mappingQueue.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue().getQueueId());
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
        
        // int numberOfProducts=(int)(Math.random()*10);
        int numberOfProducts=6;

        for(int i=0;i<numberOfProducts;i++){
            ProductManager.getInstance().createProduct();
            Product product=ProductManager.getInstance().getProduct(ProductManager.getInstance().getProducts().size()-1);
            startQueue.addProduct(product);
        }

        System.out.println(numberOfProducts + " Products are created");

        // run machines
        for (Machine machine : MachineManager.getInstance().getMachines().values()) {
            machine.start();
        }


        
        return "Machines are running";
    }
    

}