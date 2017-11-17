package com.bridgelabz.angular;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
@ComponentScan(basePackages="com.bridgelabz.service")
public class RabbitConfiguration {

}
