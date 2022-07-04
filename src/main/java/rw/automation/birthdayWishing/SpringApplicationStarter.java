package rw.automation.birthdayWishing;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import rw.automation.birthdayWishing.v1.enums.ERole;
import rw.automation.birthdayWishing.v1.models.Role;
import rw.automation.birthdayWishing.v1.repositories.IRoleRepository;
import rw.automation.birthdayWishing.v1.serviceImpls.RoleServiceImpl;
import rw.automation.birthdayWishing.v1.services.IRoleService;

import java.util.Optional;

@SpringBootApplication
@EnableScheduling
public class SpringApplicationStarter {

	public static void main(String[] args) {
		SpringApplication.run(SpringApplicationStarter.class, args);
	}

}
