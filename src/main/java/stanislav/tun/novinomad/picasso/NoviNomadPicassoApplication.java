package stanislav.tun.novinomad.picasso;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
public class NoviNomadPicassoApplication {
	@Autowired
	DataSource dataSource;

	@Bean
	public ObjectMapper objectMapper(){
		return new ObjectMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(NoviNomadPicassoApplication.class, args);
	}

}
