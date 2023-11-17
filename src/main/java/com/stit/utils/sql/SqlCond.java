package com.stit.utils.sql;

public class SqlCond {

	public String column;
	public String operation;
	public String value;

	public SqlCond(String column, String operation, String value) {
		this.column = column;
		this.operation = operation;
		this.value = value;
	}

	@Override
	public String toString() {
		return "key op value = " + column + " , " + operation + " , " + value;
	}
}
