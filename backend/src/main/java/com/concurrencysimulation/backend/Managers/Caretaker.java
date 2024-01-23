package com.concurrencysimulation.backend.Managers;

import java.util.ArrayList;

import com.concurrencysimulation.backend.Models.Memento;

public class Caretaker {
    private static Caretaker instance = null;
    private ArrayList<Memento> history = new ArrayList<Memento>();

    private Caretaker(){
    }
    
    public static Caretaker getinstance(){
        if(instance == null){
            instance = new Caretaker();
        }
        return instance;
    }

    public void push(Memento memento){
        history.add(memento);
    }

    public Memento undo(){
        if(!history.isEmpty()){
            return history.get(history.size() - 1);
        }
        return null;
    }


}
