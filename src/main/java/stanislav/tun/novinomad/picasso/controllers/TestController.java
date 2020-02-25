package stanislav.tun.novinomad.picasso.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping
    public String getTableView(){
        return "testTableStaticheader";
    }
}
