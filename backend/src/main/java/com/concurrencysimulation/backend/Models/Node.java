package com.concurrencysimulation.backend.Models;

import java.util.Map;

public class Node {
    // create properties for following example values:
    // { name: "Start Node", shape: 'circle', color: 'green',main: true,type: 'machine'}
    String name;
    String shape;
    String color;
    String type;
    boolean main;
    int x;
    int y;

    public Node(String name, String shape, String color, String type, boolean main, int x, int y) {
        this.name = name;
        this.shape = shape;
        this.color = color;
        this.type = type;
        this.main = main;
        this.x = x;
        this.y = y;
    }

    public Node() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }

    

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Map<String,Object> serialize(){
        Map<String,Object> node = Map.of(
            "name",this.name,
            "shape",this.shape,
            "color",this.color,
            "type",this.type,
            "main",this.main
        );
        return node;
    }


}
