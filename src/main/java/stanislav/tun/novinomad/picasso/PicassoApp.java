package stanislav.tun.novinomad.picasso;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;
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
@EnableTransactionManagement
@EnableCaching
public class PicassoApp {

    private Faker faker = new Faker();

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

    private void createDriversLoop(int count) {
        for (int i = 0; i < count; i++) {
            var d = new Driver();
            d.setFirstName(faker.name().firstName());
            d.setLastName(faker.name().lastName());
            d.setCar(getRandomCar());
            driverService.createOrUpdateDriver(d);
        }
    }

    private void createGuidesLoop(int count) {
        for (int i = 0; i < count; i++) {
            var g = new Guide();
            g.setFirstName(faker.name().firstName());
            g.setLastName(faker.name().lastName());
            g.setLanguage(getRandomLanguage());
            guideService.createOrUpdateGuide(g);
        }
    }

    private void createToursLoop(int count, int entityInTourCount) {
        var allDrivers = driverService.getDriversList();
        var allGuides = guideService.getGuidesList();
        for (int i = 0; i < count; i++) {
            var rnd = getRandomNumInRange(1, entityInTourCount); // drivers count in tour
            var rnd2 = getRandomNumInRange(1, entityInTourCount); // guides count in tour
            var tour = new Tour();
            tour.setTittle(faker.book().title());
            tour.setDescription(faker.company().catchPhrase());
            tour.setFileName(faker.file().fileName());
            var range = getRandomRange();
            tour.setStartDate(range.getStart());
            tour.setEndDate(range.getEnd());
            // add 1,2 or 3 drivers to tour
            switch (rnd) {
                case 1:
                    tour.addDriver(java.util.Optional.of(allDrivers.get(getRandomNumInRange(0, allDrivers.size()-1))));
                    break;
                case 2:
                    tour.addDriver(java.util.Optional.of(allDrivers.get(getRandomNumInRange(0, allDrivers.size()-1))));
                    tour.addDriver(java.util.Optional.of(allDrivers.get(getRandomNumInRange(0, allDrivers.size()-1))));
                    break;
                case 3:
                    tour.addDriver(java.util.Optional.of(allDrivers.get(getRandomNumInRange(0, allDrivers.size()-1))));
                    tour.addDriver(java.util.Optional.of(allDrivers.get(getRandomNumInRange(0, allDrivers.size()-1))));
                    tour.addDriver(java.util.Optional.of(allDrivers.get(getRandomNumInRange(0, allDrivers.size()-1))));
                    break;
            }

            switch (rnd2) {
                case 1:
                    tour.addGuide(java.util.Optional.of(allGuides.get(getRandomNumInRange(0, allDrivers.size()-1))));
                    break;
                case 2:
                    tour.addGuide(java.util.Optional.of(allGuides.get(getRandomNumInRange(0, allDrivers.size()-1))));
                    tour.addGuide(java.util.Optional.of(allGuides.get(getRandomNumInRange(0, allDrivers.size()-1))));
                    break;
                case 3:
                    tour.addGuide(java.util.Optional.of(allGuides.get(getRandomNumInRange(0, allDrivers.size()-1))));
                    tour.addGuide(java.util.Optional.of(allGuides.get(getRandomNumInRange(0, allDrivers.size()-1))));
                    tour.addGuide(java.util.Optional.of(allGuides.get(getRandomNumInRange(0, allDrivers.size()-1))));
                    break;
            }

            tour.setCreatedBy("SYSTEM");
            tour.setCreationDate(new Date());

            tourService.createOrUpdateTour(tour);
        }
    }

    private DateTimeRange getRandomRange(){
        var start = LocalDateTime.of(2020, getRandomNumInRange(1,12), getRandomNumInRange(1,28), getRandomNumInRange(0,23), getRandomNumInRange(0,59), 0);
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

    @PostConstruct
    private void init(){
        var user1 = new User("visitor", "$2a$10$lnyXL7Jc.PlCMdrxSXyIu.5klIHkztPUaDwQBHoRdqdc20rjOJZHC");
        user1.addAuthority("VISITOR");
        user1.setEnabled(true);

        var user2 = new User("user", "$2a$10$YHyM3KAswilNcNbAUmZH9O28kBDhUX6Bz5CXCTzuBVX6ARJ3EpAjW");
        user2.addAuthority("USER");
        user2.setEnabled(true);

        var user3 = new User("testuser", "$2a$10$Z/.BLe3VelzXHnUKn9/.pOKKYnk9ctCW2WPj4wB7it/B9Q6gGbZtC");
        user3.addAuthority("USER");
        user3.setEnabled(true);

        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);

        createDriversLoop(30);
        createGuidesLoop(30);
        createToursLoop(500, 2);
    }

}
