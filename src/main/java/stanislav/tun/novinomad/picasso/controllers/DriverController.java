package stanislav.tun.novinomad.picasso.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;
import stanislav.tun.novinomad.picasso.persistance.services.DriverService;
import stanislav.tun.novinomad.picasso.util.JsonPrinter;

@Controller
@RequestMapping("/drivers")
public class DriverController {

    @Autowired
    DriverService driverService;

    @RequestMapping("/list")
    public ModelAndView getDriversListView() {
        var drivers = driverService.getDriversList();
        var modelAndView = new ModelAndView();
        modelAndView.addObject("drivers", drivers);
        modelAndView.setViewName("driversListPage.html");
        return modelAndView;
    }

    @RequestMapping(value="/edit{id}")
    public ModelAndView getEditDriverView(@PathVariable(value = "id") Long driverId){
        var driver = driverService.getDriver(driverId);
        var mav = new ModelAndView();
        mav.addObject("driver", driver);
        mav.setViewName("addDriverPage.html");
        return mav;
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
    public String addOrUpdateDriverAction(Driver driver) {
        System.out.println("driver to be created or updated = "+JsonPrinter.getString(driver));
        driverService.createOrUpdateDriver(driver);

        return "redirect:/drivers/add";
    }
}
