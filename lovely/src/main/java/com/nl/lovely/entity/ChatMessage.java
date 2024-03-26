package com.nl.lovely.entity;

public class ChatMessage {

	private String message;
	private String user;
	
	
	
	public ChatMessage() {
		super();
	}

	public ChatMessage(String message, String user) {
		super();
		this.message = message;
		this.user = user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
}
