package com.management.venue.pojo;

public class ResponseData<T> {

	private T data;
	private String message;
	private String statusCode;
	
	public ResponseData(T data, String message, String statusCode) {
		super();
		this.data = data;
		this.message = message;
		this.statusCode = statusCode;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

}
