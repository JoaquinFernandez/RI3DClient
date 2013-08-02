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
	public static void notifyCreateServer(String flavorId, String imageId) {
		for (UserDataListener listener : listeners) {
			listener.createServer(flavorId, imageId);
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

	public static void notifyInstanceChange() {
		for (UserDataListener listener : listeners) {
			listener.instanceChange();
		}
	}
}
