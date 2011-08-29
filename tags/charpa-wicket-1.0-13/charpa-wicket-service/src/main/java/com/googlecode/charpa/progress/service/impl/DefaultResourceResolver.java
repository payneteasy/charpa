package com.googlecode.charpa.progress.service.impl;

import com.googlecode.charpa.progress.service.spi.IResourceResolver;

public class DefaultResourceResolver implements IResourceResolver {

	public String resolve(String key, String defaultValue) {
		return defaultValue;
	}

}
