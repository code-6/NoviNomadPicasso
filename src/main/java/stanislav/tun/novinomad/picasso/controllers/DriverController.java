package stanislav.tun.novinomad.picasso.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;
import stanislav.tun.novinomad.picasso.persistance.services.DriverService;

import java.util.List;

@Controller
@RequestMapping("/drivers")
public class DriverController {

    @Autowired
    DriverService driverService;

    @RequestMapping("/list")
    public ModelAndView getAllDrivers() {
        var drivers = driverService.getDriversList();
        var modelAndView = new ModelAndView();
        modelAndView.addObject("drivers", drivers);
        modelAndView.setViewName("driversListPage.html");
        return modelAndView;
    }

    // get view
    @RequestMapping("/add")
    public ModelAndView addDriverView() {
        var driver = new Driver();
        var modelAndView = new ModelAndView();
        modelAndView.addObject("driver", driver);
        modelAndView.setViewName("addDriverPage.html");
        return modelAndView;
    }

    // action todo: make single method for adding driver to db
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String addDriverAction(@ModelAttribute("driver") Driver driver, Model model) {
        driverService.createDriver(driver);
        model.addAttribute("driver", driver);
        return "redirect:/drivers/add";
    }


}
