package com.concurrencysimulation.backend.Models;

import java.awt.Color;

public class Machine {
    private int id;
    private int time;
    private Color color;

    public Machine(int id, int time) {
        this.id = id;
        this.time = time;
        Color defaultColor = new Color(94, 190, 247);
        this.color = defaultColor;
    }

    public int getId() {
        return id;
    }

    public int getTime() {
        return time;
    }

    public Color getColor() {
        return color;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
