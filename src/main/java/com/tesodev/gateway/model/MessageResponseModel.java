package com.tesodev.gateway.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageResponseModel implements Serializable {

	private static final long serialVersionUID = 2939825506391699002L;

	private boolean status;
	private String message;
}
