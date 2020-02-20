package stanislav.tun.novinomad.picasso.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    Logger logger = LoggerFactory.getLogger(PicassoApp.class);

    @RequestMapping("/list")
    public ModelAndView getDriversListView() {
        var drivers = driverService.getDriversList();
        var modelAndView = new ModelAndView();
        modelAndView.addObject("drivers", drivers);
        modelAndView.setViewName("driversListPage.html");
        return modelAndView;
    }

    @RequestMapping(value = "/edit{id}")
    public ModelAndView getEditDriverView(@PathVariable(value = "id") Long driverId) {
        // test exception handler
        throw new NullPointerException();
//        var driver = driverService.getDriver(driverId);
//        logger.info("Start edit driver "+ JsonPrinter.getString(driver));
//        var mav = new ModelAndView();
//        mav.addObject("driver", driver);
//        mav.setViewName("addDriverPage.html");
//        return mav;
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
        driverService.createOrUpdateDriver(driver);
        logger.info("driver to be created or updated = " + JsonPrinter.getString(driver));
        return "redirect:/drivers/add";
    }
}
