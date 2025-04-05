package com.nexign.dmf.rss.rssvw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@ServletComponentScan
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan
//@Import({DBConnectConfig.class})
class RssvwApplication {

	public static void main(String[] args) {
		SpringApplication.run(RssvwApplication.class, args);
	}

}
