package com.tesodev.gateway.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.RequestAcceptEncoding;
import org.apache.http.client.protocol.RequestClientConnControl;
import org.apache.http.client.protocol.RequestDefaultHeaders;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpMethod;

import com.tesodev.gateway.model.GzipEntity;
import com.tesodev.gateway.model.HttpRequestModel;
import com.tesodev.gateway.model.HttpResponseModel;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(setterPrefix = "set")
public class HttpClientService implements InitializingBean, IHttpClientService {

	private static final int CONNECTION_EVICTION_TIMEOUT = 500;
	private static final int MAX_TTL = 60_000;

	protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

	protected static final int NUMBER_OF_TRIES = 3;
	protected static final String HEADER_SOAP_ACTION = "SOAPAction";

	protected HttpClient client;
	private String userAgent;
	private boolean useExpectContinue;
	private List<BasicHeader> defaultHeaders;
	private boolean bypassSSLChecks;
	private int requestTimeout;
	private int connectionTimeout;
	private int socketTimeout;
	private int defaultMaxPerRoute;
	private int maxTotal;

	@Override
	public void afterPropertiesSet() throws Exception {

		HttpProcessor processor = createHttpProcessor();

		SSLConnectionSocketFactory socketFactory = createSocketFactory();

		PoolingHttpClientConnectionManager connectionManager = createConnectionManager(socketFactory);

		RequestConfig.Builder requestBuilder = RequestConfig.custom()
				.setConnectTimeout(connectionTimeout)
				.setSocketTimeout(socketTimeout)
				.setConnectionRequestTimeout(requestTimeout);

		this.client = HttpClientBuilder.create()
				.setDefaultRequestConfig(requestBuilder.build())
		        .evictIdleConnections(CONNECTION_EVICTION_TIMEOUT, TimeUnit.MILLISECONDS)
		        .setConnectionTimeToLive(MAX_TTL, TimeUnit.MILLISECONDS)
		        .setConnectionManager(connectionManager)
				.setHttpProcessor(processor)
				.build();
	}

	private PoolingHttpClientConnectionManager createConnectionManager(SSLConnectionSocketFactory socketFactory) {
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory())
				.register("https", socketFactory)
				.build();
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		cm.setDefaultMaxPerRoute(defaultMaxPerRoute);
		cm.setMaxTotal(maxTotal);
		cm.setValidateAfterInactivity(MAX_TTL);
		cm.closeIdleConnections(CONNECTION_EVICTION_TIMEOUT, TimeUnit.MILLISECONDS);
		return cm;
	}

	private SSLConnectionSocketFactory createSocketFactory() throws NoSuchAlgorithmException, KeyManagementException {
		SSLConnectionSocketFactory socketFactory = null;

		if (bypassSSLChecks) {

			SSLContext sslContext = SSLContext.getInstance("TLS");

			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return new java.security.cert.X509Certificate[0];
				}

				public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
				}
			} };

			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

			socketFactory = new SSLConnectionSocketFactory(sslContext, (HostnameVerifier) null);

		} else {
			socketFactory = SSLConnectionSocketFactory.getSocketFactory();
		}
		return socketFactory;
	}

	private HttpProcessor createHttpProcessor() {
		HttpProcessorBuilder builder = HttpProcessorBuilder.create();

		if (userAgent != null)
			builder.add(new RequestUserAgent(userAgent));
		if (useExpectContinue)
			builder.add(new RequestExpectContinue(true));

		builder.add(new RequestAcceptEncoding());
		builder.add(new RequestConnControl());
		builder.add(new RequestTargetHost());
		builder.add(new ResponseContentEncoding());

		builder.add(new RequestContent());
		builder.add(new RequestClientConnControl());

		if (defaultHeaders != null && !defaultHeaders.isEmpty()) {
			List<Header> headers = new ArrayList<>(defaultHeaders.size());
			for (Header header : defaultHeaders) {
				headers.add(header);
			}
			builder.add(new RequestDefaultHeaders(headers));
		}

		return builder.build();
	}

	@Override
	public HttpResponseModel execute(HttpRequestModel params) throws Exception {

		HttpResponse response = executeInner(params);
		HttpEntity entity = response.getEntity();

		String string = parseEntity(entity);

		Header[] allHeaders = response.getAllHeaders();
		var responseHeaders = new ArrayList<>(Arrays.asList(allHeaders));
		return HttpResponseModel.builder()
				.headers(responseHeaders.stream().map(h -> new BasicHeader(h.getName(), h.getValue())).collect(Collectors.toList()))
				.response(string)
				.statusCode(response.getStatusLine().getStatusCode())
				.build();
	}

	private String parseEntity(HttpEntity entity) throws Exception{
		try {
			return EntityUtils.toString(entity);
		} catch (ParseException | IOException e) {
			throw e;
		}
	}

	protected HttpResponse executeInner(HttpRequestModel params) throws Exception, IOException {
		HttpRequestBase request = prepareRequest(params);
		return client.execute(request);
	}

	protected HttpRequestBase prepareRequest(HttpRequestModel params) throws Exception {

		String postData = params.getPostData();
		boolean isUseGzip = params.isUseGzip();
		String authToken = params.getAuthorizationToken();
		Map<String, String> urlParams = params.getRequestParameters();

		URI uri = constructURI(params, urlParams);
		HttpRequestBase request = createHttpRequest(params, postData, isUseGzip, uri);

		if (authToken != null && !authToken.isEmpty()) {
			BasicHeader header = new BasicHeader("Authorization", authToken);
			request.addHeader(header);
		}

		if (params.getHeaders() != null && !params.getHeaders().isEmpty()) {
			for (BasicHeader basicHeader : params.getHeaders()) {
				request.addHeader(basicHeader);
			}
		}

		if (params.getProxyHost() != null && params.getProxyPort() != 0) {
			HttpHost proxyHost = new HttpHost(params.getProxyHost(), params.getProxyPort());
			RequestConfig proxyConfig = RequestConfig.custom().setProxy(proxyHost).build();
			request.setConfig(proxyConfig);
		}

		return request;
	}

	private HttpRequestBase createHttpRequest(HttpRequestModel params, String postData, boolean isUseGzip, URI uri) {

		if (params.getMethod() == HttpMethod.GET) {
			return new HttpGet(uri);
		}

		HttpRequestBase request = new HttpPost(uri);

		HttpEntity strent = null;
		if (isUseGzip) {
			strent = new GzipEntity(postData, params.getContentType());
		} else {
			strent = new StringEntity(postData == null ? "" : postData, params.getCharset());
			if (params.getContentType() != null)
				((StringEntity) strent).setContentType(params.getContentType());
		}
		((HttpPost) request).setEntity(strent);
		return request;
	}

	private URI constructURI(HttpRequestModel params, Map<String, String> urlParams) throws Exception {
		URI uri = null;
		try {
			URIBuilder builder = new URIBuilder(params.getUrl());
			if (urlParams != null) {
				Iterator<Entry<String, String>> iter = urlParams.entrySet().iterator();
				while (iter.hasNext()) {
					Entry<String, String> next = iter.next();
					builder.addParameter(next.getKey(), next.getValue());
				}
			}
			uri = builder.build();
		} catch (URISyntaxException e) {
			throw e;
		}
		return uri;
	}

}