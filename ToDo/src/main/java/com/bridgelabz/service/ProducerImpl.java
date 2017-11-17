package com.bridgelabz.service;

import java.io.Serializable;
import java.util.HashMap;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class ProducerImpl implements Producer{

	
	        @Autowired
		    private RabbitTemplate template;

		    @Autowired
		    private Queue queue;

		  
		    public void send(HashMap map) {
		        this.template.convertAndSend(queue.getName(), map);
		        System.out.println(" [x] Sent '" + map + "'");
		    }
	

}
