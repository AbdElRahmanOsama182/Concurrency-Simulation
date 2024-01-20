package com.concurrencysimulation.backend.API;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.concurrencysimulation.backend.Managers.Caretaker;
import com.concurrencysimulation.backend.Managers.SystemMementoManager;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin
public class MementoControllar {

    private SystemMementoManager systemMementoManager = new SystemMementoManager();
    private Caretaker caretaker = Caretaker.getinstance();

    @PostMapping("/save")
    public ResponseEntity<String> save() {

        caretaker.push(systemMementoManager.saveSystem());
        
        return ResponseEntity.ok("System saved");
    }
    
    @PostMapping("/restore")
    public ResponseEntity<String> restore(){
        
        systemMementoManager.restoreSystem(caretaker.undo());

        return ResponseEntity.ok("System restored");
    }
    
}
