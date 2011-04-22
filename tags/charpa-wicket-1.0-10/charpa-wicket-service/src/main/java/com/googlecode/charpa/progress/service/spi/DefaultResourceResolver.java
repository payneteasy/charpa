package com.googlecode.charpa.progress.service.spi;

public class DefaultResourceResolver implements IResourceResolver {

	public String resolve(String key, String defaultValue) {
		return defaultValue;
	}

}
