package dev.vietis.nampd.employee.achievement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EmployeeAchievementApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeAchievementApplication.class, args);
	}

}
