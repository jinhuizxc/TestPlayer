package com.example.jh.testplayer;

public class MusicInfo {

	private String name;
	private String path;
	private long duration;

	public MusicInfo(String name, String path, long duration) {
		super();
		this.name = name;
		this.path = path;
		this.duration = duration;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

}
