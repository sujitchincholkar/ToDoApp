package com.bridgelabz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;

public class MailerImpl implements Mailer {
	
	@Autowired
	private MailSender mailSender;  
	//@Async
	public  void  send(String to,String message){

		        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();  
		        simpleMailMessage.setFrom("chincholkarsujit12@gmail");  
		        simpleMailMessage.setTo(to);  
		        simpleMailMessage.setSubject("Validate Email");  
		        simpleMailMessage.setText(message);  
		       
		        mailSender.send(simpleMailMessage);     
		        System.out.println("mail send");
		 
	}
}
