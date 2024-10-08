package com.example.demo;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private/api/teste")
public class Api {

    @GetMapping
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello Mundão!");
    }

}
