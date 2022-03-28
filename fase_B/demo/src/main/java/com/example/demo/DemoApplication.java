package com.example.demo;

import com.example.demo.metereology.Meteorology;
import com.example.demo.metereology.MeteorologyRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.Month;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(MeteorologyRepo meteorologyRepo) {
		return args -> {
			meteorologyRepo.deleteAll();

			var meteorology = new Meteorology(
					20.0,
					89.0,
					300,
					LocalDate.of(2022, Month.MARCH, 28));

			meteorologyRepo.save(meteorology);
			meteorologyRepo.findAll()
					.forEach(System.out::println);
		};
	}
}
