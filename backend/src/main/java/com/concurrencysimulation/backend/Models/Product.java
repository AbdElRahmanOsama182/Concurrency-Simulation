package com.concurrencysimulation.backend.Models;

import java.awt.Color;

public class Product {
    private Color color;
    private int id;

    public Product(Color color, int id) {
        this.color = color;
        this.id = id;
    }

    public Color getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setId(int id) {
        this.id = id;
    }
}
