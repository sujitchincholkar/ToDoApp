package com.bridgelabz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class MailerImpl implements Mailer {
	@Autowired
	 private MailSender mailSender;  
	   
	    public void setMailSender(MailSender mailSender) {  
	        this.mailSender = mailSender;  
	    }  
	public  void  send(String to,String message){
	
		        //creating message  
		        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();  
		        simpleMailMessage.setFrom("chincholkarsujit12@gmail");  
		        simpleMailMessage.setTo(to);  
		        simpleMailMessage.setSubject("Validate Email");  
		        simpleMailMessage.setText(message);  
		        //sending message  
		        mailSender.send(simpleMailMessage);     
		 
	}
}
