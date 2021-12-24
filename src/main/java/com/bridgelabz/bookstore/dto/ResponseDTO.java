package com.bridgelabz.bookstore.dto;

import lombok.Data;

@Data
public class ResponseDTO {
	private int status;
	private String message;
	private Object data;

	public ResponseDTO(String message, Object data,int status) {
		this.status= status;
		this.message = message;
		this.data = data;
	}

}
