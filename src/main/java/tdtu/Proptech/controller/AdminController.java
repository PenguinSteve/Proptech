package tdtu.Proptech.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping(value = {"/", ""})
    public String index() {
        return "admin";
    }

    @GetMapping("/product/create")
    public String createProduct() {
        return "create_product";
    }

    @GetMapping("/product/update/{id}")
    public String updateProduct() {
        return "update_product";
    }
}
