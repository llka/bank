package ru.ilka.bank.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/greeting")
public class GreetingController {

    @GetMapping
    public ResponseEntity<String> greet() {
        return new ResponseEntity<>("Hello", HttpStatus.OK);
    }
}
