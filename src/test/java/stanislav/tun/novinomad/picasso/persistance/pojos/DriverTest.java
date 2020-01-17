package stanislav.tun.novinomad.picasso.persistance.pojos;

import org.junit.Before;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import stanislav.tun.novinomad.picasso.persistance.services.DriverService;
import stanislav.tun.novinomad.picasso.persistance.services.TourService;


@SpringBootTest
@ExtendWith(SpringExtension.class)
//@RunWith(SpringRunner.class)
@ContextConfiguration
class DriverTest {

    @Autowired
    DriverService driverService;

    @Autowired
    TourService tourService;

    Driver driver = new Driver("Test", "Test");
    Tour tour = new Tour();

    @Before
    void setUp() {

        driverService.createOrUpdateDriver(driver);

        tour.setTittle("Tour 1");
//        tour.setStartDate(new LocalDateTime());
//        tour.setEndDate(new LocalDateTime(2020, 01, 20, 10, 00));
        tour.addDriver(java.util.Optional.ofNullable(driver));

        tourService.createOrUpdateTour(tour);
    }


}