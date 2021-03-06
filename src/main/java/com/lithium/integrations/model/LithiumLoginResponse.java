package com.lithium.integrations.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class LithiumLoginResponse {

	private String status;

	public String getStatus() {
		return status;
	}

	@XmlAttribute
	public void setStatus(String status) {
		this.status = status;
	}

	private String value;

	public String getValue() {
		return value;
	}

	@XmlElement
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "LithiumLoginResponse [value=" + value + "]";
	}

}
