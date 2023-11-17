package com.stit.common;

import java.io.Serializable;

/**
 * envelope.
 
 */
public class ApiResponseGen<T> implements Serializable {
	public static enum Status {
		OK,
		ERROR
	}

	private Status status;
	private T data;
	private Error error;

	public ApiResponseGen() {
	}

	public ApiResponseGen(Status status, T data) {
		this(status, data, null);
	}

	public ApiResponseGen(Status status, T data, Error error) {
		this.status = status;
		this.data = data;
		this.error = error;
	}

	public Status getStatus() {
		return status;
	}

	public T getData() {
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
