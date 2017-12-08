package com.bridgelabz.util.pojo;

public class UrlData {

	private String title;
	
	private String imageUrl;
	
	private String domain;
	
	public  UrlData(String title,String imageUrl, String domain){
		this.title=title;
		this.imageUrl=imageUrl;
		this.domain=domain;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	
}
