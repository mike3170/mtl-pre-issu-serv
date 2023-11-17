package com.stit.common;

import java.io.Serializable;

/**
 * envelope.
 
 */
public final class ApiResponse implements Serializable		 {
	private static final long serialVersionUID = 1L;

	public static enum Status {
		OK,
		ERROR
	}

	private Status status;
	private Object data;
	private Error error;

	public ApiResponse() {
	}

	public ApiResponse(Status status, Object data) {
		this(status, data, null);
	}

	public ApiResponse(Status status, Error error) {
		this(status, null, error);
	}

	public ApiResponse(Status status) {
		this(status, null, null);
	}

	public ApiResponse(Status status, Object data, Error error) {
		this.status = status;
		this.data = data;
		this.error = error;
	}

	public static ApiResponse ok() {
		return new ApiResponse(Status.OK, null, null);
	}

	public static ApiResponse ok(Object data) {
		return new ApiResponse(Status.OK, data, null);
	}

	public static ApiResponse error(String mesg) {
		return new ApiResponse(Status.ERROR, null, new Error(-1, mesg));
	}

	public Status getStatus() {
		return status;
	}

	public Object getData() {
		return data;
	}

	public Error getError() {
		return this.error;
	}

	//--------------------------------------------------
	public static final class Error {
		private final int code;
		private final String desc;

		public Error(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public int getCode() {
			return this.code;
		}

		public String getDesc() {
			return this.desc;
		}
	}

}
