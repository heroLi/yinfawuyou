package com.yifa.health_manage.model;

import java.io.Serializable;

public class ImageResponse implements Serializable{

	private String path;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	private String url;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
