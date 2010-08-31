package com.googlecode.charpa.web.spi;

public class DefaultResourceResolver implements IResourceResolver {

	public String resolve(String key, String defaultValue) {
		return defaultValue;
	}

}
