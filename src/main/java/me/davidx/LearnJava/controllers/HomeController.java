package me.davidx.LearnJava.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class HomeController {
    @RequestMapping("/home")
    public String home(Model model) {
        model.addAttribute("message", "hello, world");
        //return "WEB-INF/views/home.jsp";
        return "home";
    }
}