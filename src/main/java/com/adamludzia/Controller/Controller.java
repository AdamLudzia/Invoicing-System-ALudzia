package com.adamludzia.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/helloworld")
    public String helloWorldEndPoint() {
        return "Hello World!";
    }

    @GetMapping("/adam")
    public String myName() {
        return "My name is Adam";
    }
}

