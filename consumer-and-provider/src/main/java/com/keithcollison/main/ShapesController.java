package com.keithcollison.main;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/shape")
public class ShapesController {


    @RequestMapping(method = RequestMethod.GET, path = "/circle")
    public @ResponseBody Shape getShape() {
        Shape shape = new Shape();
        shape.setName("circle");
        return shape;
    }
}
