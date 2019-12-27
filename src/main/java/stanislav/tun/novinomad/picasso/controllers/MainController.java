package stanislav.tun.novinomad.picasso.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;
import stanislav.tun.novinomad.picasso.persistance.services.DriverService;

import java.util.List;

@Controller
@RequestMapping("/picasso")
public class MainController {

    @Autowired
    DriverService driverService;

    @RequestMapping("/drivers/list")
    public String getAllDrivers(Model model) {
        List<Driver> drivers = driverService.getDriversList();
        model.addAttribute("drivers", drivers);
        return "driversListPage";
    }

    @RequestMapping("/drivers/add")
    public String addDriver(Model model) {
        Driver driver = new Driver();
        model.addAttribute("driver", driver);
        return "addDriverPage";
    }

    @RequestMapping(value = "/drivers/save", method = RequestMethod.POST)
    public String addDriver(@ModelAttribute("driver") Driver driver, Model model){
        driverService.createDriver(driver);
        model.addAttribute("driver", driver);
        return "redirect:/drivers/add";
    }



}
