package com.bridgelabz.bookstore.util;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import lombok.Data;

@SuppressWarnings("serial")
@Data
@Component
public class Email implements Serializable {
	
	String to;
	String from;
	String subject;
	String body;

}
