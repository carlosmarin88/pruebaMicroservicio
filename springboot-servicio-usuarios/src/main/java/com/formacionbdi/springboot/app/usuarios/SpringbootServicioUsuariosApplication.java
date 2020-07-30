package com.formacionbdi.springboot.app.usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EntityScan({"com.formacionbdi.springboot.app.commons.usuarios.models.entity"})
@EnableEurekaClient
//@SpringBootApplication(scanBasePackages =
//"com.formacionbdi.springboot.app.usuarios.commons.models.entity")
public class SpringbootServicioUsuariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServicioUsuariosApplication.class, args);
	}
}
