package com.stit.common;

import java.util.Objects;

/**
 * attn to hash code, using T, U 
 * @author User
 * @param <T>
 * @param <U>
 * @param <V> 
 */
public class Triple<T, U, V> {

	private T first;
	private U second;
	private V third;

	public Triple() {
	}

	public Triple(T first, U second, V third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	public T getFirst() {
		return first;
	}

	public void setFirst(T first) {
		this.first = first;
	}

	public U getSecond() {
		return second;
	}

	public void setSecond(U second) {
		this.second = second;
	}

	public V getThird() {
		return third;
	}

	public void setThird(V third) {
		this.third = third;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Triple<?, ?, ?> other = (Triple<?, ?, ?>) obj;
		if (!Objects.equals(this.first, other.first)) {
			return false;
		}
		if (!Objects.equals(this.second, other.second)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Triple{" + "first=" + first + ", second=" + second + ", third=" + third + '}';
	}

}
