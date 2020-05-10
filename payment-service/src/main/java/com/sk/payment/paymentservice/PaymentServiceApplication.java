package com.sk.payment.paymentservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Random;
import java.util.UUID;

@SpringBootApplication
@EnableEurekaClient
public class PaymentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentServiceApplication.class, args);
	}

}

@RestController
@RequestMapping("/api/v1/payment/")
@Slf4j
class PaymentController{

	private PaymentService paymentService;

	public PaymentController(PaymentService paymentService){
		this.paymentService = paymentService;
	}

	@PostMapping("/save")
	public Payment savePayment(@RequestBody Payment payment){
		log.info("entry>> {}",payment);
		Payment response = paymentService.persist(payment);
		log.info("exit>> {}",payment);
		return response;
	}

	@GetMapping("/{orderId}")
	public Payment getPayment(@PathVariable Long orderId){
		log.info("entry>> {}",orderId);
		Payment payment = paymentService.getPayment(orderId);
		log.info("exit>> {}",payment);
		return payment;
	}

}

@Entity
@Table(name="Payments")
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
@Slf4j
class PaymentService{
	private PaymentRepository paymentRepository;

	public PaymentService(PaymentRepository paymentRepository){
		this.paymentRepository = paymentRepository;
	}
	public Payment persist(Payment payment){
		log.info("entry>> {}",payment);
		payment.setPaymentStatus(new Random().nextBoolean()? "SUCCESS": "FAILURE");
		payment.setTransactionId(UUID.randomUUID().toString());
		Payment persistedPayment = paymentRepository.save(payment);
		log.info("exit>> {}",persistedPayment);
		return persistedPayment;
	}

	public Payment getPayment(Long orderId) {
		log.info("entry>> {}",orderId);
		return paymentRepository.findByOrderId(orderId);
	}
}

interface PaymentRepository extends JpaRepository<Payment, Long> {
	public Payment findByOrderId(Long orderId);
}
