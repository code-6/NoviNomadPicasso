package stanislav.tun.novinomad.picasso.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import stanislav.tun.novinomad.picasso.persistance.pojos.Tour;

@Controller
@RequestMapping("/tours")
public class TourController {

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addTour(Model model) {
        Tour tour = new Tour();
        model.addAttribute("tour", tour);
        return "toursListPage.html";
    }

}
