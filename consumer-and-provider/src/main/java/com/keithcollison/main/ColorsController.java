package com.keithcollison.main;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/color")
public class ColorsController {


    @RequestMapping(method = RequestMethod.GET, path = "/red")
    public @ResponseBody
    Color getColor() {
        Color color = new Color();
        color.setName("red");
        return color;
    }
}
