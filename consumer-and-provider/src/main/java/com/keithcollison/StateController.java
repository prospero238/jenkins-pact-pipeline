package com.keithcollison;

import javax.xml.ws.Response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class StateController {

    @RequestMapping(method = RequestMethod.GET, path = "/state")
    public ResponseEntity<String> acceptState() {
        return new ResponseEntity<String>(HttpStatus.OK);
    }

}
