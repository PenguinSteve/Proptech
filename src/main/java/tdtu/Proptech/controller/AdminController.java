package tdtu.Proptech.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping(value = {"/", ""})
    public String index() {
        return "dashboard";
    }
    @GetMapping("/review")
    public String review() {
    	return "review-property";
    }

}
