package com.bridgelabz.service;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class Mailer {
	 private MailSender mailSender;  
	   
	    public void setMailSender(MailSender mailSender) {  
	        this.mailSender = mailSender;  
	    }  

	public  void  send(String to,String message){
	
		        //creating message  
		        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();  
		        simpleMailMessage.setFrom("");  
		        simpleMailMessage.setTo(to);  
		        simpleMailMessage.setSubject("Validate Email");  
		        simpleMailMessage.setText(message);  
		        //sending message  
		        mailSender.send(simpleMailMessage);     
		 
	}
}
