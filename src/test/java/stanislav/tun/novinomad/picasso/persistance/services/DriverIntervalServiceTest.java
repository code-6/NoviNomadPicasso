//package stanislav.tun.novinomad.picasso.persistance.services;
//
//import org.junit.Before;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;
//import stanislav.tun.novinomad.picasso.persistance.pojos.DriverTourIntervals;
//import stanislav.tun.novinomad.picasso.persistance.pojos.MyInterval;
//import stanislav.tun.novinomad.picasso.persistance.pojos.Tour;
//import stanislav.tun.novinomad.picasso.util.JsonPrinter;
//
//import javax.xml.bind.ValidationException;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration
//class DriverIntervalServiceTest {
//
//    @Autowired
//    DriverIntervalService driverIntervalService;
//
//    @Autowired
//    DriverService driverService;
//
//    @Autowired
//    TourService tourService;
//
//    Driver driver1, driver2, driver3;
//
//    Tour tour1, tour2;
//
//    @BeforeEach
//    void setUp() throws ValidationException {
//        driver1 = new Driver("Test", "Test");
//        driver2 = new Driver("Test2", "Test2");
//        driver3 = new Driver("Test3", "Test3");
//
//        driverService.createOrUpdateDriver(driver1);
//        driverService.createOrUpdateDriver(driver2);
//        driverService.createOrUpdateDriver(driver3);
//
//        tour1 = new Tour();
//        tour2 = new Tour();
//
//        tour1.setTittle("Test tour");
//        tour1.setStartDate(LocalDate.now());
//        tour1.setEndDate(LocalDate.now().plusDays(10));
//        tour1.addDriver(Optional.of(driver2));
//
//        tour2.setTittle("Test tour 2");
//        tour2.setStartDate(LocalDate.now().plusDays(11));
//        tour2.setEndDate(LocalDate.now().plusDays(6));
//        tour2.addDriver(Optional.of(driver1));
//        tour2.addDriver(Optional.of(driver3));
//
//        tourService.createOrUpdateTour(tour1);
//        tourService.createOrUpdateTour(tour2);
//
//        var i1 = new MyInterval(LocalDate.now(), LocalDate.now().plusDays(3));
//        var di = new DriverTourIntervals(tour1, i1, driver2);
//        driverIntervalService.createOrUpdateInterval(di);
//
//        var i2 = new MyInterval(LocalDate.now().plusDays(11), LocalDate.now().plusDays(14));
//        var i3 = new MyInterval(LocalDate.now().plusDays(14), LocalDate.now().plusDays(17));
//        var di2 = new DriverTourIntervals(tour2, i2, driver1);
//        var di3 = new DriverTourIntervals(tour2, i3, driver3);
//
//        driverIntervalService.createOrUpdateInterval(di2);
//        driverIntervalService.createOrUpdateInterval(di3);
//
//    }
//
//    @Test
//    void getInterval() {
//        var di = driverIntervalService.getInterval(1);
//        assertNotNull(di);
//        System.out.println(JsonPrinter.getString(di.get()));
//    }
//
//    @Test
//    void getAllRelatedToDriver() {
//        var di = (List<DriverTourIntervals>) driverIntervalService.getAllRelatedToDriver(driver3);
//        var di2 = (List<DriverTourIntervals>) driverIntervalService.getAllRelatedToDriver(driver2);
//        var di3 = (List<DriverTourIntervals>) driverIntervalService.getAllRelatedToDriver(driver1);
//
//        System.out.println(JsonPrinter.getString(di));
//        System.out.println(JsonPrinter.getString(di2));
//        System.out.println(JsonPrinter.getString(di3));
//
//        assertEquals(1, di.size());
//        assertEquals(1, di2.size());
//        assertEquals(1, di3.size());
//
//        assertEquals(3, di.get(0).getId());
//    }
//
//    @Test
//    void getAllRelatedToTour() {
//        var di = (List<DriverTourIntervals>) driverIntervalService.getAllRelatedToTour(tour2);
//
//        System.out.println(JsonPrinter.getString(di));
//        assertEquals(2, di.size());
//        assertEquals(2, di.get(0).getId());
//        assertEquals(3, di.get(1).getId());
//    }
//
//    @Test
//    void getAllRelatedToTourAndDriver() {
//        var di = (List<DriverTourIntervals>) driverIntervalService.getAllRelatedToTourAndDriver(tour2,driver3);
//        assertEquals(1, di.size());
//        assertEquals(3, di.get(0).getId());
//    }
//
//    @Test
//    void getAllIntervals() {
//    }
//}