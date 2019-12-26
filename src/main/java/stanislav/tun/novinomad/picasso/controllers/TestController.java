package stanislav.tun.novinomad.picasso.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;

@Controller
@RequestMapping("/test")
public class TestController {

    @ResponseBody
    @GetMapping("/hi")
    public Driver sayHi(){
        var d = new Driver("Sanya");
        return d;
    }

}
