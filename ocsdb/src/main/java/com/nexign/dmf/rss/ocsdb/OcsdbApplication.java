package com.nexign.dmf.rss.ocsdb;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OcsdbApplication {
	public static void main(String[] args) {
		SpringApplication.run(OcsdbApplication.class, args);
	}

}
