package tdtu.Proptech.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class HomeController {

    @GetMapping(value = { "/", "/home", "" })
    public String index() {
        return "index";
    }

    @GetMapping("/properties")
    public String properties() {
        return "properties";
    }
    @GetMapping("/properties/single")
    public String singleProperty() {
    	return "property-single";
    }

    @GetMapping("cart/checkout")
    public String checkout() {
        return "checkout";
    }

    @GetMapping("/thankyou")
    public String thankyouPage() {
        return "thankyou";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
