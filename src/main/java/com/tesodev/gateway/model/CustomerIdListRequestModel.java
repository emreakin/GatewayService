package com.tesodev.gateway.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerIdListRequestModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<String> idList;
}
