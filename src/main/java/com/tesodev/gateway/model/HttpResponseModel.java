package com.tesodev.gateway.model;

import java.io.Serializable;
import java.util.List;

import org.apache.http.message.BasicHeader;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HttpResponseModel implements Serializable {

	private static final long serialVersionUID = -5894369188203152925L;
	
	private String response;
	private List<BasicHeader> headers;
	private int statusCode;
}
