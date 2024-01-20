package com.concurrencysimulation.backend.API;

import org.springframework.http.HttpStatus;
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
        try{
            caretaker.push(systemMementoManager.saveSystem());
            return ResponseEntity.ok("System saved");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving system");
        }
    }
    
    @PostMapping("/restore")
    public ResponseEntity<String> restore(){

        try{
            systemMementoManager.restoreSystem(caretaker.undo());
            return ResponseEntity.ok("System restored");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error restoring system");
        }
    }
    
}
