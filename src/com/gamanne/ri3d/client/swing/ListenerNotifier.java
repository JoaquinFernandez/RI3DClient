package com.gamanne.ri3d.client.swing;

import java.util.ArrayList;
import java.util.List;

public class ListenerNotifier {
	
	private static List<UserDataListener> listeners = new ArrayList<UserDataListener>();
	
	public static void addListener(UserDataListener listener) {
		listeners.add(listener);
	}
	
	public static void notifyUserData(String user, String password) {
		for (UserDataListener listener : listeners) {
			listener.inputData(user, password);
		}
	}
	public static void notifyRegionChoosed(String region) {
		for (UserDataListener listener : listeners) {
			listener.showRegionData(region);
		}
	}
	public static void notifyCreateServer(String newServerName, String flavorId, String imageId) {
		for (UserDataListener listener : listeners) {
			listener.createServer(newServerName, flavorId, imageId);
		}
	}
	public static void notifyConnectServer(String serverId) {
		for (UserDataListener listener : listeners) {
			listener.connectServer(serverId);
		}
	}

	public static void notifyChangeUser() {
		for (UserDataListener listener : listeners) {
			listener.changeUser();
		}
	}
}
