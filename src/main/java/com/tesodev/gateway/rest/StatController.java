package com.tesodev.gateway.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class StatController {

	@GetMapping("/stat")
    public String stat() {
        return "OK";
    }
}
