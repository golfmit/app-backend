package com.serverless;

public class Response {

	private final String message;
	private final Object input;

	public Response(String message, Object input) {
		this.message = message;
		this.input = input;
	}

	public String getMessage() {
		return this.message;
	}

	public Object getInput() {
		return this.input;
	}
}
