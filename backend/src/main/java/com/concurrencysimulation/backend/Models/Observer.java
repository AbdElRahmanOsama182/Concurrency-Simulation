package com.concurrencysimulation.backend.Models;

public interface Observer {
    void update(Boolean isFree, Machine machine);
}
