package com.sk.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SpringBootApplication
@EnableEurekaClient
public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

}

@RestController
@RequestMapping("/api/v1/order/")
@Slf4j
class OrderController{

	private OrderService orderService;

	public OrderController(OrderService orderService){
		this.orderService = orderService;
	}

	@PostMapping("/save")
	public OrderDTO saveOrder(@RequestBody OrderDTO orderDTO) throws JsonProcessingException {
		log.info("entry>> {}",new ObjectMapper().writeValueAsString(orderDTO));
		OrderDTO responseDTO = orderService.persist(orderDTO);
		log.info("exit>> {}",responseDTO);
		return orderDTO;
	}

}

@Entity
@Table(name="ORDERS")
@Data
@AllArgsConstructor
@NoArgsConstructor
class Order{
	@Id
	private Long orderId;
	private String orderNumber;
	private Double amount;
	private String transactionId;
}
@Data
@AllArgsConstructor
@NoArgsConstructor
class OrderDTO{
	private Order order;
	private Payment payment;
}
@Data
@AllArgsConstructor
@NoArgsConstructor
class Payment{
	@Id
	private Long paymentId;
	private String cardNumber;
	private Long orderId;
	private String billingAddress;
	private String customerId;
	private Double amount;
	private String transactionId;
	private String paymentStatus;
}

@Service
@RefreshScope
@Slf4j
class OrderService{
	
	private OrderRepository orderRepository;

	@Lazy
	private RestTemplate restTemplate;

	public OrderService(OrderRepository orderRepository,RestTemplate restTemplate){
		this.orderRepository = orderRepository;
		this.restTemplate = restTemplate;
	}
	public OrderDTO persist(OrderDTO orderDTO){
		log.info("entry>> {}",orderDTO);
		
		String correctPaymentUrl = "http://PAYMENT-SERVICE/api/v1/payment/save";
		Payment payment = restTemplate.postForObject(correctPaymentUrl, orderDTO.getPayment(), Payment.class);
		log.info("PaymentService response>> {}",payment);
		orderDTO.setPayment(payment);
		Order order = orderRepository.save(orderDTO.getOrder());
		orderDTO.setOrder(order);
		return orderDTO;
	}
}

interface OrderRepository extends JpaRepository<Order, Long> {

}
