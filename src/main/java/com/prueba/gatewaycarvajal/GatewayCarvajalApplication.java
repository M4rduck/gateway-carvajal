package com.prueba.gatewaycarvajal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ GatewayCarvajalApplication.MAIN_PACKAGE })
public class GatewayCarvajalApplication {
	
	public static final String MAIN_PACKAGE = "com.prueba.gatewaycarvajal";

	public static void main(String[] args) {
		SpringApplication.run(GatewayCarvajalApplication.class, args);
	}

}
