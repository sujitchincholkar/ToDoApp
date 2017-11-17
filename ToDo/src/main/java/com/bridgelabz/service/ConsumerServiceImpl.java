package com.bridgelabz.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.SerializationUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;


public class ConsumerServiceImpl implements MessageListener{
		 
		@Autowired
		Mailer mailer;


		public void onMessage(Message messsage) {
			
			byte body[]=messsage.getBody();
			Map map = (HashMap)SerializationUtils.deserialize(body);
			mailer.send(map.get("to")+"", map.get("message")+"");
		}

}
