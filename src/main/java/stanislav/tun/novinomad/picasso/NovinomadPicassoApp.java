package stanislav.tun.novinomad.picasso;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import stanislav.tun.novinomad.picasso.controllers.TourController;
import stanislav.tun.novinomad.picasso.persistance.pojos.Driver;
import stanislav.tun.novinomad.picasso.persistance.pojos.Guide;
import stanislav.tun.novinomad.picasso.persistance.services.DriverService;
import stanislav.tun.novinomad.picasso.persistance.services.GuideService;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@SpringBootApplication
public class NovinomadPicassoApp {
	@Autowired
	DataSource dataSource;

	@Autowired
	private GuideService guideService;

	@Autowired
	private DriverService driverService;

	Logger logger = LoggerFactory.getLogger(NovinomadPicassoApp.class);

	@Bean
	public ObjectMapper objectMapper(){
		return new ObjectMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(NovinomadPicassoApp.class, args);
	}

	@PostConstruct
	private void init() {
		driverService.createOrUpdateDriver(new Driver("Ryan", "Cooper"));
		driverService.createOrUpdateDriver(new Driver("Ken", "Miles"));
		driverService.createOrUpdateDriver(new Driver("Michael", "Schumacher"));
		driverService.createOrUpdateDriver(new Driver("Ken", "Block"));
		driverService.createOrUpdateDriver(new Driver("Carroll", "Shelby"));

		guideService.createOrUpdateGuide(new Guide("Roberto", "Strippoli"));
		guideService.createOrUpdateGuide(new Guide("Kennedy", "Omwenga"));
		guideService.createOrUpdateGuide(new Guide("Nic", "Polenakis"));
		guideService.createOrUpdateGuide(new Guide("Alfredo", "Meneses"));
		guideService.createOrUpdateGuide(new Guide("Peter", "Hillary"));
		guideService.createOrUpdateGuide(new Guide("Nikolai", "Drozdov"));

		logger.debug("initialize app data finished");
	}

}
