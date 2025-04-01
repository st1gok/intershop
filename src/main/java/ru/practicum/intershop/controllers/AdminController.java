package ru.practicum.intershop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.practicum.intershop.services.ProductsService;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private ProductsService productsService;

    @Autowired
    public AdminController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping("/import")
    public String importProducts(Model model) {
        return "import";
    }

    @PostMapping("/import")
    public String productsImport(@RequestParam("file") MultipartFile file,
                                 RedirectAttributes redirectAttributes) throws IOException {

        productsService.importProducts(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }
}
