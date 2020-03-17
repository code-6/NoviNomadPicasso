package stanislav.tun.novinomad.picasso.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import stanislav.tun.novinomad.picasso.persistance.pojos.Tour;
import stanislav.tun.novinomad.picasso.util.ConcurrentHolder;
import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;
import stanislav.tun.novinomad.picasso.persistance.pojos.User;
import stanislav.tun.novinomad.picasso.persistance.services.DriverService;
import stanislav.tun.novinomad.picasso.persistance.services.UserService;

@Controller
@RequestMapping("/drivers")
public class DriverController {

    @Autowired
    DriverService driverService;

    @Autowired
    ConcurrentHolder holder;

    @Autowired
    AuditorAware auditor;

    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(DriverController.class);

    @RequestMapping("/list")
    public ModelAndView getDriversListView() {
        User user = userService.getUser(auditor.getCurrentAuditor().get().toString()).get();
        holder.release(user);
        var drivers = driverService.getDriversList();
        var modelAndView = new ModelAndView();
        modelAndView.addObject("drivers", drivers);
        modelAndView.setViewName("driversListPage.html");
        modelAndView.addObject("activeDrivers", true);
        return modelAndView;
    }

    @RequestMapping(value = "/edit{id}")
    public ModelAndView getEditDriverView(@PathVariable(value = "id") Long driverId) {
        var driver = driverService.getDriver(driverId);
        var mav = new ModelAndView();
        mav.addObject("driver", driver);
        mav.setViewName("addDriverPage");
        User user = userService.getUser(auditor.getCurrentAuditor().get().toString()).get();
        if (holder.isHold(driver.get())) {
            mav = getDriversListView();
            mav.addObject("error", "Edit is not available!");
            var desc = "This entity currently edited by user: " + holder.getHolderOf(driver.get()).getUserName()
                    + ". Try again later or request to release this entity";
            mav.addObject("errorDesc", desc);
            return mav;
        } else {
            holder.hold(driver.get(), user);
        }

        return mav;
    }

    @RequestMapping(value = "/delete{id}")
    public ModelAndView deleteDriver(@PathVariable(value = "id") Long driverId) {
        var mav = new ModelAndView();
        var driver = driverService.getDriver(driverId);
        if (!driver.isEmpty() && driver.isPresent()) {
            var d = driver.get();
            if (holder.isHold(d)) {
                mav = getDriversListView();
                mav.addObject("error", "Delete rejected!");
                var desc = "This entity currently edited by user: " + holder.getHolderOf(d).getUserName();
                mav.addObject("errorDesc", desc);
                return mav;
            } else {
                // todo : check if driver attached to future tours.
                if (!driverService.hasFutureTours(driver.get())){
                    d.setDeleted(true);
                    driverService.createOrUpdateDriver(d);
                }else{
                    // todo : return error view.
                    mav = getDriversListView();
                    mav.addObject("error", "Delete rejected!");
                    var desc = "Driver: "+d.getFullName()+" included in tours: ";
                    var futureTours = driverService.getDriverFutureTours(d);
                    for (Tour t : futureTours) {
                        desc += t.getTittle()+";\n";
                    }
                    desc += "Please exclude driver from tours above and try again.";
                    mav.addObject("errorDesc", desc);
                    return mav;
                }
            }
        }
        mav.addObject("drivers", driverService.getDriversList());
        mav.addObject("activeDrivers", true);
        mav.setViewName("driversListPage");
        return mav;
    }

    // get view
    @RequestMapping("/add")
    public ModelAndView addDriverView() {
        var driver = new Driver();
        var modelAndView = new ModelAndView();
        modelAndView.addObject("driver", driver);
        modelAndView.setViewName("addDriverPage");
        return modelAndView;
    }

    // action todo: make single method for adding driver to db
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView addOrUpdateDriverAction(Driver driver) {
        var modelAndView = new ModelAndView();
//        modelAndView.setViewName("redirect:/drivers/add");
        modelAndView.setViewName("redirect:/drivers/list");
        // if exist => edit => hold
        if (driverService.exist(driver))
            holder.release(driver);

        driverService.createOrUpdateDriver(driver);

        return modelAndView;
    }
}
