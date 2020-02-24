package stanislav.tun.novinomad.picasso;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import stanislav.tun.novinomad.picasso.persistance.pojos.*;
import stanislav.tun.novinomad.picasso.persistance.services.DriverService;
import stanislav.tun.novinomad.picasso.persistance.services.GuideService;
import stanislav.tun.novinomad.picasso.persistance.services.TourService;
import stanislav.tun.novinomad.picasso.persistance.services.UserService;
import stanislav.tun.novinomad.picasso.security.audit.AuditorAwareImpl;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class PicassoApp {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private GuideService guideService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private UserService userService;

    @Autowired
    private TourService tourService;

    Logger logger = LoggerFactory.getLogger(PicassoApp.class);

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAwareImpl();
    }

    public static void main(String[] args) {
        SpringApplication.run(PicassoApp.class, args);
    }

    private void createDriversLoop() {
        for (int i = 0; i <= 300; i++) {
            var d = new Driver();
            d.setFirstName("FirstName " + i);
            d.setMiddleName("MiddleName " + i);
            d.setLastName("LastName " + i);
            d.setCar(getRandomCar());
            driverService.createOrUpdateDriver(d);
        }
    }

    private void createGuidesLoop() {
        for (int i = 0; i <= 300; i++) {
            var g = new Guide();
            g.setFirstName("FirstName " + i);
            g.setMiddleName("MiddleName " + i);
            g.setLastName("LastName " + i);
            g.setLanguage(getRandomLanguage());
            guideService.createOrUpdateGuide(g);
        }
    }

    private void createToursLoop() {
        var allDrivers = driverService.getDriversList();
        var allGuides = guideService.getGuidesList();
        for (int i = 0; i <= 3000; i++) {
            var rnd = getRandomNumInRange(1, 3); // drivers count in tour
            var rnd2 = getRandomNumInRange(1, 3); // guides count in tour
            var tour1 = new Tour();
            tour1.setTittle("Tour "+i);
            tour1.setDescription("this is tour description "+i);
            tour1.setFileName("SomeFile.txt");
            var range = getRandomRange();
            tour1.setStartDate(range.getStart());
            tour1.setEndDate(range.getEnd());
            // add 1,2 or 3 drivers to tour
            switch (rnd) {
                case 1:
                    tour1.addDriver(java.util.Optional.of(allDrivers.get(getRandomNumInRange(1, 300))));
                    break;
                case 2:
                    tour1.addDriver(java.util.Optional.of(allDrivers.get(getRandomNumInRange(1, 300))));
                    tour1.addDriver(java.util.Optional.of(allDrivers.get(getRandomNumInRange(1, 300))));
                    break;
                case 3:
                    tour1.addDriver(java.util.Optional.of(allDrivers.get(getRandomNumInRange(1, 300))));
                    tour1.addDriver(java.util.Optional.of(allDrivers.get(getRandomNumInRange(1, 300))));
                    tour1.addDriver(java.util.Optional.of(allDrivers.get(getRandomNumInRange(1, 300))));
                    break;
            }

            switch (rnd2) {
                case 1:
                    tour1.addGuide(java.util.Optional.of(allGuides.get(getRandomNumInRange(1, 300))));
                    break;
                case 2:
                    tour1.addGuide(java.util.Optional.of(allGuides.get(getRandomNumInRange(1, 300))));
                    tour1.addGuide(java.util.Optional.of(allGuides.get(getRandomNumInRange(1, 300))));
                    break;
                case 3:
                    tour1.addGuide(java.util.Optional.of(allGuides.get(getRandomNumInRange(1, 300))));
                    tour1.addGuide(java.util.Optional.of(allGuides.get(getRandomNumInRange(1, 300))));
                    tour1.addGuide(java.util.Optional.of(allGuides.get(getRandomNumInRange(1, 300))));
                    break;
            }

            tour1.setCreatedBy("SYSTEM");
            tour1.setCreationDate(new Date());
        }
    }

    private DateTimeRange getRandomRange(){
        var start = LocalDateTime.of(2020, getRandomNumInRange(1,12), getRandomNumInRange(1,28), getRandomNumInRange(1,24), getRandomNumInRange(1,60), 0);
        var rnd = getRandomNumInRange(10, 15);
        var end = start.plusDays(rnd);
        try {
            return new DateTimeRange(start, end);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int getRandomNumInRange(int from, int to) {
        int rnd = from + (int) (Math.random() * to);
        return rnd;
    }

    private String getRandomCar() {
        int rnd = 1 + (int) (Math.random() * 10);
        switch (rnd) {
            case 1:
                return "Alfa Romeo";
            case 2:
                return "Mercedes Benz";
            case 3:
                return "BMW";
            case 4:
                return "Lamborghini";
            case 5:
                return "Porsche";
            case 6:
                return "Koenigsegg";
            case 7:
                return "VolksWagen";
            case 8:
                return "Audi";
            case 9:
                return "Nissan";
            case 10:
                return "Toyota";
            default:
                return "Mercedes Benz Sprinter";
        }
    }

    private String getRandomLanguage() {
        int rnd = 1 + (int) (Math.random() * 10);
        switch (rnd) {
            case 1:
                return "CHINESE";
            case 2:
                return "SPANISH";
            case 3:
                return "ENGLISH";
            case 4:
                return "HINDI";
            case 5:
                return "ARABIC";
            case 6:
                return "PORTUGUESE";
            case 7:
                return "BENGALI";
            case 8:
                return "RUSSIAN";
            case 9:
                return "JAPANESE";
            case 10:
                return "GERMAN";
            default:
                return "TURKISH";
        }
    }

    private void init() {
        var user1 = new User("visitor", "$2a$10$lnyXL7Jc.PlCMdrxSXyIu.5klIHkztPUaDwQBHoRdqdc20rjOJZHC");
        user1.addAuthority("VISITOR");
        user1.setEnabled(true);

        var user2 = new User("user", "$2a$10$YHyM3KAswilNcNbAUmZH9O28kBDhUX6Bz5CXCTzuBVX6ARJ3EpAjW");
        user2.addAuthority("USER");
        user2.setEnabled(true);

        var user3 = new User("testuser", "$2a$10$Z/.BLe3VelzXHnUKn9/.pOKKYnk9ctCW2WPj4wB7it/B9Q6gGbZtC");
        user3.addAuthority("TEST_USER");
        user3.setEnabled(true);

        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);

        var d1 = new Driver("Ryan", "Cooper");
        var d2 = new Driver("Michael", "Schumacher");
        driverService.createOrUpdateDriver(d1);
        driverService.createOrUpdateDriver(new Driver("Ken", "Miles"));
        driverService.createOrUpdateDriver(d2);
        driverService.createOrUpdateDriver(new Driver("Ken", "Block"));
        driverService.createOrUpdateDriver(new Driver("Carroll", "Shelby"));

        guideService.createOrUpdateGuide(new Guide("Roberto", "Strippoli"));
        guideService.createOrUpdateGuide(new Guide("Kennedy", "Omwenga"));
        guideService.createOrUpdateGuide(new Guide("Nic", "Polenakis"));
        guideService.createOrUpdateGuide(new Guide("Alfredo", "Meneses"));
        guideService.createOrUpdateGuide(new Guide("Peter", "Hillary"));
        guideService.createOrUpdateGuide(new Guide("Nikolai", "Drozdov"));

//        var tour1 = new Tour();
//        tour1.setStartDate(LocalDateTime.of(2020,2,1, 0,0,0));
//        tour1.setEndDate(LocalDateTime.of(2020,2,15, 0,0,0));
//        tour1.addDriver(java.util.Optional.of(d1));
//        tour1.setTittle("first tour");
//
//        var tour2 = new Tour();
//        tour2.setStartDate(LocalDateTime.of(2020,2,15, 0,0,0));
//        tour2.setEndDate(LocalDateTime.of(2020,2,28, 0,0,0));
//        tour2.addDriver(java.util.Optional.of(d1));
//        tour2.setTittle("second tour");
//
//        var tour3 = new Tour();
//        tour3.setStartDate(LocalDateTime.of(2020,3,1, 0,0,0));
//        tour3.setEndDate(LocalDateTime.of(2020,3,15, 0,0,0));
//        tour3.addDriver(java.util.Optional.of(d2));
//        tour3.setTittle("third tour");
//
//        var tour4 = new Tour();
//        tour4.setStartDate(LocalDateTime.of(2020,1,25, 0,0,0));
//        tour4.setEndDate(LocalDateTime.of(2020,2,1, 0,0,0));
//        tour4.addDriver(java.util.Optional.of(d2));
//        tour4.setTittle("fourth tour");
//
//        tourService.createOrUpdateTour(tour1);
//        tourService.createOrUpdateTour(tour2);
//        tourService.createOrUpdateTour(tour3);
//        tourService.createOrUpdateTour(tour4);

        logger.debug("initialize app data finished");
    }

    @PostConstruct
    private void init2(){
        createDriversLoop();
        createGuidesLoop();
        createToursLoop();
    }

}
