package com.sk.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
public class GatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}

}

@RestController
class FallBackController{
	@RequestMapping("/orderfallback")
	public String orderFallback(){
		return "Order Service is not available, please try again later....!";
	}

	@RequestMapping("/paymentfallback")
	public String paymentFallback(){
		return "Payment Service is not available, please try again later....!";
	}
}
