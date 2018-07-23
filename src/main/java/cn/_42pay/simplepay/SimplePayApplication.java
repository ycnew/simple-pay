package cn._42pay.simplepay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@EnableAutoConfiguration
@ServletComponentScan
@SpringBootApplication
public class SimplePayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimplePayApplication.class, args);
	}
}
