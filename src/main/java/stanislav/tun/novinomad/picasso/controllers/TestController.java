package stanislav.tun.novinomad.picasso.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/test")
public class TestController {

    @GetMapping("/")
    public ModelAndView test(){
        var mav = new ModelAndView();
        mav.setViewName("index.html");

        return mav;
    }
}
