package com.bridgelabz.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="notes")
public class Note {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator="custom")
	@GenericGenerator(name="custom", strategy="native")
	private int noteId;
	
	private String title;
	private String body;
	private String color;
	private Date CreateDate;
	User user;
	
}
