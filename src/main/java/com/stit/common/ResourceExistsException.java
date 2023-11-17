package com.stit.common;

public class ResourceExistsException extends RuntimeException {
	private static final String title = "資料已存在!";

	public ResourceExistsException() {
		super(title);
	}


	public ResourceExistsException(String message) {
		super(title + "\n" + message);
	}

	public ResourceExistsException(String message, Throwable cause) {
		super(title + "\n" + message, cause);
	}

	public ResourceExistsException(Throwable cause) {
		super(cause);
	}
	
}
