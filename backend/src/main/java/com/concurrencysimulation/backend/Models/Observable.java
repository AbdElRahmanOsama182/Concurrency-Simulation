package com.concurrencysimulation.backend.Models;

public interface Observable {
    void notifyObserver(Boolean isFree);
}
