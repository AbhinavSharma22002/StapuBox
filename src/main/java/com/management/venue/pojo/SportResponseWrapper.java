package com.management.venue.pojo;

import java.util.List;

public class SportResponseWrapper {
	private String status;
	private String msg;
	private Object err;
	private List<SportData> data; // This matches the "data" array in your JSON

	// Getters and Setters
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<SportData> getData() {
		return data;
	}

	public void setData(List<SportData> data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getErr() {
		return err;
	}

	public void setErr(Object err) {
		this.err = err;
	}

}