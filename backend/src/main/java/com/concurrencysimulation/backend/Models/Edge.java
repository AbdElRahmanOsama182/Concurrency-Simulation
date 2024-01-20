package com.concurrencysimulation.backend.Models;

import java.util.HashMap;
import java.util.Map;

public class Edge {
    // create properties for following example values:
    // { source: "node1", target: "node3", color: '#000000' },
    String source;
    String target;
    String color;

    public Edge(String source, String target, String color) {
        this.source = source;
        this.target = target;
        this.color = color;
    }

    public Edge() {
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Map<String,Object> serialize(){
        Map<String,Object> map = new HashMap<>();
        map.put("source", this.source);
        map.put("target", this.target);
        map.put("color", this.color);
        return map;

    }

}
