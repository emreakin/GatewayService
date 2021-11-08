package com.tesodev.gateway.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicHeader;
import org.springframework.http.HttpMethod;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "set")
public class HttpRequestModel implements Serializable {

	private static final long serialVersionUID = 9118781920705588667L;

	private String url;
	private String postData;
	private String charset;
	private String contentType;
	private HttpMethod method;
	private boolean useGzip;
	private String authorizationToken;
	private Map<String, String> requestParameters;
	private List<BasicHeader> headers;
	private String proxyHost;
	private int proxyPort;

}
