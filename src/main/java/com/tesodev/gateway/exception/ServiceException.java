package com.tesodev.gateway.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ServiceException extends Exception {

	private static final long serialVersionUID = -8724510615633786685L;

	private String errorCode;
	private String errorMessage;
}
