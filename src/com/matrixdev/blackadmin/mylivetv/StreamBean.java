package com.matrixdev.blackadmin.mylivetv;

import java.io.Serializable;

public class StreamBean implements Serializable {
	private String displayName;
	private String protocol;
	private String serverAddress;
	private int serverPort;
	private String description;
	private String category;
	private String name;
	private boolean status;

	public StreamBean() {
		this("", "", "", 0, "", "", "", false);
	}

	public StreamBean(String displayName, String protocol,
			String serverAddress, int serverPort, String description,
			String category, String name, boolean status) {
		this.displayName = displayName;
		this.protocol = protocol;
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		this.description = description;
		this.category = category;
		this.name = name;
		this.status = status;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public int getServerPort() {
		return serverPort;
	}

	public String getDescription() {
		return description;
	}

	public String getCategory() {
		return category;
	}

	public String getName() {
		return name;
	}

	public boolean getStatus() {
		return status;
	}

	public String getFullAddress() {
		return protocol + "://" + serverAddress + ":" + serverPort + "/" + name;
	}
}
