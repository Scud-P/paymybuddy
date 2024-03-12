package com.oc.paymybuddy;

import java.util.Scanner;

import com.oc.paymybuddy.service.MockDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PayMyBuddy implements CommandLineRunner {

	@Autowired
	private MockDBService mockDBService;

	@Autowired
	private ConfigurableApplicationContext context;

	public static void main(String[] args) {
		SpringApplication.run(PayMyBuddy.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Application started...");
		waitForExitInputInBackground();
	}

	private void waitForExitInputInBackground() {
		Thread thread = new Thread(() -> {
			Scanner scanner = new Scanner(System.in);
			System.out.println("Press 'q' to exit the program:");
			while (scanner.hasNext()) {
				String input = scanner.next();
				if ("q".equalsIgnoreCase(input)) {
					exitProgram();
					break;
				}
			}
			scanner.close();
		});
		thread.setDaemon(true);
		thread.start();
	}

	private void exitProgram() {
		System.out.println("Exiting program...");
		context.close();
	}
}
