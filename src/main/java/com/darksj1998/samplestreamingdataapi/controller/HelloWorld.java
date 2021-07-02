package com.darksj1998.samplestreamingdataapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HelloWorld {

    @GetMapping(path = "/helloWorld")
    public ResponseEntity helloWorld() {
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("message", "Hello World");
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping
    public String helloWorldString() {
        return "helloWorld";
    }
}
