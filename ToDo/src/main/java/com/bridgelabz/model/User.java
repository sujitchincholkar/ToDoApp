package com.bridgelabz.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name="user")
public class User {
	@Id
	@GeneratedValue
	private int userId;
	
	@Column(name="first_name")
	private String fullName;
	
	@Column(name="email")
	private String email;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(name="contact_no")
	private String contactNo;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(name="password")
	private String password;
	
	@Column(name="isActivated")
	private boolean isActivated;
	
	@Column(name="profile_url",columnDefinition="LONGBLOB")
	private String profileUrl;
	
	@JsonIgnore
	@OneToMany(mappedBy="user")
	Set<Note> notes;
	
	
	@OneToMany(fetch=FetchType.EAGER,mappedBy="user")
	Set<Label> labels;
	
	
	//=========================================//
	public Set<Note> getNotes() {
		return notes;
	}
	public void setNotes(Set<Note> notes) {
		this.notes = notes;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String firstName) {
		this.fullName = firstName;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getContactNo() {
		return contactNo;
	}
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isActivated() {
		return isActivated;
	}
	public void setActivated(boolean isActivated) {
		this.isActivated = isActivated;
	}
	
	public String getProfileUrl() {
		return profileUrl;
	}
	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}
	
	public Set<Label> getLabels() {
		return labels;
	}
	public void setLabels(Set<Label> labels) {
		this.labels = labels;
	}
}
