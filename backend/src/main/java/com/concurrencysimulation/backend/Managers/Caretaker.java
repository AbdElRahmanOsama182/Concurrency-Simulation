package com.concurrencysimulation.backend.Managers;

import java.util.ArrayList;

import com.concurrencysimulation.backend.Models.Memento;

public class Caretaker {
    private static Caretaker instance = null;
    private ArrayList<Memento> history = new ArrayList<Memento>();
    private int currentIdx = -1;

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

    public Memento getSnap(){
        if(history.isEmpty()){
            return null;
        }
        currentIdx = (currentIdx + 1) % history.size();
            return history.get(currentIdx);
    }

    public void clear(){
        history.clear();
        currentIdx = -1;
    }


}
