package odrl.rest.exception;

import org.apache.http.protocol.HTTP;

import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

public class InternalServiceException extends RuntimeException {
	
	private static final long serialVersionUID = -7982702000551927171L;

	public InternalServiceException(String msg) {
		super(msg);
	}

	@SuppressWarnings("rawtypes")
	public static final ExceptionHandler handle = (Exception exception, Request request, Response response) -> {
		response.status(500);
		response.header(HTTP.CONTENT_TYPE, "application/json");
	};
	
}
