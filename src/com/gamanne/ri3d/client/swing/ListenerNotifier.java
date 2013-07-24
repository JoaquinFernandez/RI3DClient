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
}
