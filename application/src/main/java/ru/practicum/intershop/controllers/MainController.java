package ru.practicum.intershop.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

import java.net.URI;

@Controller
@RequestMapping("/")
public class MainController {

    @GetMapping
    public Mono<ResponseEntity<Void>> getRoot() {
            return Mono.just(ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("/main/items"))
                    .build());
    }
}
