package ru.practicum.intershop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.services.ProductsService;

import java.io.IOException;
import java.net.URI;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private ProductsService productsService;

    @Autowired
    public AdminController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping("/import")
    public Mono<String> importProducts(Model model) {
        return Mono.just("import");
    }

    @PostMapping("/import")
    public Mono<ResponseEntity<Void>> productsImport(@RequestPart("file") FilePart file) throws IOException {
        return productsService.importProducts(file).then(
         Mono.just(ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/main/items"))
                .build()));
    }
}
