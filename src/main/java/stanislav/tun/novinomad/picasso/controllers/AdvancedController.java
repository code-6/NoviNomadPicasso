package stanislav.tun.novinomad.picasso.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import stanislav.tun.novinomad.picasso.persistance.pojos.DTDays;
import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;

@Controller
@RequestMapping("/advanced")
public class AdvancedController {
    @RequestMapping(value = "/go", method = RequestMethod.POST)
    public String advanced(@ModelAttribute DTDays dtDays,
                           @RequestParam(name = "tourId") Long tourId, Model model){

        return "redirect:/tours/advanced";
    }
}
