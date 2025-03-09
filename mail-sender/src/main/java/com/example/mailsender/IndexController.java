package com.example.mailsender;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;

@Controller
@RequestMapping("/mail")
public class IndexController {

    private EnvioDeEmail envioDeEmail;

    public IndexController(EnvioDeEmail envioDeEmail) {
        this.envioDeEmail = envioDeEmail;
    }


    @GetMapping("/")
    String index(){
        System.out.printf("dddddddddddddddddddddddddddddddddddd");
        return "index";
    }

    @GetMapping("/send")
    String send(TemplateEngine templateEngine){
        System.out.printf("ddddddddddddddddddddddddddddddddddddxxxxxxxxxxxxxxxxxxxxxx");

        envioDeEmail.sendEmailNow();

        return "index";
    }

}
