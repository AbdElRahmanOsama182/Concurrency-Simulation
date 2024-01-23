package com.concurrencysimulation.backend.API;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.concurrencysimulation.backend.Managers.Caretaker;
import com.concurrencysimulation.backend.Managers.SystemMementoManager;
import com.concurrencysimulation.backend.Models.Memento;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin(origins = { "http://localhost:8081" })
public class MementoControllar {

    private SystemMementoManager systemMementoManager = new SystemMementoManager();
    private Caretaker caretaker = Caretaker.getinstance();
    
    @PostMapping("/restore")
    public ResponseEntity<String> restore(){

        try{
            
           //TODO:stop machines and queues threads

           systemMementoManager.restoreSystem(caretaker.undo());

           //TODO:start machines and queues threads

           return ResponseEntity.ok("System is restored");
        } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error restoring system");
        }
    }
    
}