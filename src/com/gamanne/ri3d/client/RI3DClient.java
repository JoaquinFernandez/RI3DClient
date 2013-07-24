package com.gamanne.ri3d.client;

import com.gamanne.ri3d.client.connection.ConnectionClient;
import com.gamanne.ri3d.client.connection.ServerInfo;
import com.gamanne.ri3d.client.swing.InitialScreenFrame;
import com.gamanne.ri3d.client.swing.ServerDataSelect;
import com.gamanne.ri3d.client.swing.UserDataListener;
import com.gamanne.ri3d.client.swing.ListenerNotifier;

/**
 * RI3D main class it handles the initialization of the data, the creation of the
 * windows and asks for the authentication credentials
 * 
 * @author Joaquin Fernandez
 *
 */
public class RI3DClient implements UserDataListener {

	private static InitialScreenFrame frame;
	
	private ServerDataSelect serverDataSelect;
	
	private ConnectionClient client;
	
	/** I want only one instance of server info*/
	private static ServerInfo serverInfo;
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RI3DClient client = new RI3DClient();
		ListenerNotifier.addListener(client);
		frame = new InitialScreenFrame();
		frame.setVisible(true);
	}

	@Override
	public void inputData(String user, String password) {
		client = new ConnectionClient(user, password);
		serverInfo = client.getServerInfo();
		serverDataSelect = new ServerDataSelect(serverInfo);
		serverDataSelect.setVisible(true);
		frame.setVisible(false);
		frame.dispose();		
	}
}
