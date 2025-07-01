package uahb.m1gl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "uahb.m1gl")
public class CustomerServiceAplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceAplication.class,args);
    }
}