package odrl.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import odrl.rest.exception.InternalServiceException;
import spark.Request;

public class ODRLService {

	private static final String ACCEPT_HEADER = "Accept";
	private static final String ACCEPT_HEADER_HTML = "text/html";
	private static final String ACCEPT_HEADER_JSON = "application/json";

	public static boolean shouldReturnHtml(Request request) {
		String accept = request.headers(ACCEPT_HEADER);
		return accept != null && accept.contains(ACCEPT_HEADER_HTML);
	}

	public static boolean shouldReturnJson(Request request) {
		String accept = request.headers(ACCEPT_HEADER);
		return accept != null && accept.contains(ACCEPT_HEADER_JSON);
	}
	

	public static final String concat(String ... args) {
		StringBuilder builder = new StringBuilder();
		for (String arg : args) {
			builder.append(arg);
		}
		return builder.toString();
	}
}
