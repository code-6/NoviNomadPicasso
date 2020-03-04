package stanislav.tun.novinomad.picasso.controllers;

import org.dom4j.rule.Mode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import stanislav.tun.novinomad.picasso.PicassoApp;
import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;
import stanislav.tun.novinomad.picasso.persistance.services.DriverService;
import stanislav.tun.novinomad.picasso.util.JsonPrinter;

@Controller
@RequestMapping("/drivers")
public class DriverController {

    @Autowired
    DriverService driverService;

    Logger logger = LoggerFactory.getLogger(DriverController.class);

    @RequestMapping("/list")
    public ModelAndView getDriversListView() {
        var drivers = driverService.getDriversList();
        var modelAndView = new ModelAndView();
        modelAndView.addObject("drivers", drivers);
        modelAndView.setViewName("driversListPage.html");
        modelAndView.addObject("activeDrivers", true);
        return modelAndView;
    }

    private void throwException() throws Exception {
        throw new Exception("This is an exception!");
    }

    @RequestMapping(value = "/edit{id}")
    public ModelAndView getEditDriverView(@PathVariable(value = "id") Long driverId) {
        var driver = driverService.getDriver(driverId);
        var mav = new ModelAndView();
        mav.addObject("driver", driver);
        mav.setViewName("addDriverPage.html");
        return mav;
    }

    @RequestMapping(value = "/delete{id}")
    public String deleteDriver(@PathVariable(value = "id") Long driverId){
        var driver = driverService.getDriver(driverId);

        if(!driver.isEmpty() && driver.isPresent()){
            var d = driver.get();
            d.setDeleted(true);
            driverService.createOrUpdateDriver(d);
        }
        return "redirect:/drivers/list";
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
    public ModelAndView addOrUpdateDriverAction(Driver driver) {
        var modelAndView = new ModelAndView();
//        modelAndView.setViewName("redirect:/drivers/add");
        modelAndView.setViewName("redirect:/drivers/list");

        driverService.createOrUpdateDriver(driver);
        return modelAndView;
    }
}
