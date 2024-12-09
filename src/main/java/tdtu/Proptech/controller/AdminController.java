package tdtu.Proptech.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = {"/", ""})
    public String index() {
        return "dashboard";
    }
    @GetMapping("/review")
    public String review() {
    	return "review-property";
    }

}
