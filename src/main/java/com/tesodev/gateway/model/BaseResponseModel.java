package com.tesodev.gateway.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class BaseResponseModel<T> implements Serializable {

	private static final long serialVersionUID = -2952094943698738582L;
	
	private T result;
	private String error;
}
