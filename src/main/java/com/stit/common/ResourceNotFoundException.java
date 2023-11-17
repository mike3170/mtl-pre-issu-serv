package com.stit.common;

public class ResourceNotFoundException extends RuntimeException {
	private static final String title = "資料不存在!";

	public ResourceNotFoundException() {
		super(title);
	}

	public ResourceNotFoundException(String message) {
		super(title + "\n" + message);
	}

	public ResourceNotFoundException(String message, Throwable cause) {
		super(title + "\n" + message, cause);
	}

	public ResourceNotFoundException(Throwable cause) {
		super(cause);
	}
	
}
