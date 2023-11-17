package com.stit.common;

import java.util.Objects;

// generic
public class LabelValue<K, V> {
	private K label;
	private V value;

	public LabelValue() {
	}

	public LabelValue(K label, V value) {
		this.label = label;
		this.value = value;
	}

	public K getLabel() {
		return label;
	}

	public void setLabel(K label) {
		this.label = label;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 53 * hash + Objects.hashCode(this.label);
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
		final LabelValue<?, ?> other = (LabelValue<?, ?>) obj;
		if (!Objects.equals(this.label, other.label)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "LabelValue{" + "label=" + label + ", value=" + value + '}';
	}

	
}
