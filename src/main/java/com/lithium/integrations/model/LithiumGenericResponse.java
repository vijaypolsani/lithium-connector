package com.lithium.integrations.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class LithiumGenericResponse {

	private String status;

	public String getStatus() {
		return status;
	}

	@XmlAttribute
	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "LithiumGenericResponse [Status=" + status + "]";
	}

}
