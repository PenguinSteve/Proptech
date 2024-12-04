package tdtu.Proptech.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class ProductController {
    @GetMapping("/product/{id}")
    public String product() {
        return "product";
    }
}