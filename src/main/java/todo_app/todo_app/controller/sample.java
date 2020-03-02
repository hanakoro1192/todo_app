package todo_app.todo_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class sample{

    @RequestMapping("/sample")
    public String sample(){
        return "sample";
    }
}