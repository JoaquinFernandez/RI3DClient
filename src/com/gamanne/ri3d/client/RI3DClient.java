package com.gamanne.ri3d.client;

import java.awt.Frame;

import com.gamanne.ri3d.client.connection.ProviderManager;
import com.gamanne.ri3d.client.connection.ServerInfo;
import com.gamanne.ri3d.client.swing.InitialScreenFrame;
import com.gamanne.ri3d.client.swing.ListenerNotifier;
import com.gamanne.ri3d.client.swing.ServerDataSelectFrame;
import com.gamanne.ri3d.client.swing.UserDataListener;
import com.gamanne.ri3d.client.vncwiewer.VncViewer;

/**
 * RI3D main class it handles the initialization of the data, the creation of the
 * windows and asks for the authentication credentials
 * 
 * @author Joaquin Fernandez
 *
 */
public class RI3DClient implements UserDataListener {

	private static InitialScreenFrame frame;

	private ServerDataSelectFrame serverDataSelectFrame;

	private ProviderManager providerManager;

	private Frame vncFrame;

	private static int provider;
	
	public static String host;
	
	public static int port;


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Get the host info from the configuration file
		Config config = new Config("config.cfg");
		host = config.getProperty("host");
		port = Integer.parseInt(config.getProperty("initialPort"));

		RI3DClient client = new RI3DClient();
		ListenerNotifier.addListener(client);
		frame = new InitialScreenFrame();
		frame.setVisible(true);
	}

	@Override
	public void inputData(String user, String password) {
		providerManager = new ProviderManager(user, password);
		//Choose provider
		provider = ProviderManager.OPENSTACK;
		ServerInfo serverInfo = providerManager.getServerInfo(provider);
		serverDataSelectFrame = new ServerDataSelectFrame(serverInfo);
		serverDataSelectFrame.setVisible(true);
		frame.setVisible(false);
		frame.dispose();		
	}

	@Override
	public void createServer(String flavorId, String imageId) {

		serverDataSelectFrame.setVisible(false);
		serverDataSelectFrame.dispose();
	}

	@Override
	public void connectServer(String serverId) {
		//Check is http format
		String url = providerManager.connectInstance(provider, serverId);
		//For calling the VncViewer
		String[] args = {"URL", url};
		VncViewer vnc = new VncViewer(args);
		vncFrame = vnc.getFrame();
		serverDataSelectFrame.setVisible(false);
		serverDataSelectFrame.dispose();
	}

	@Override
	public void changeUser() {
		vncFrame.setVisible(false);
		vncFrame.dispose();
		frame = new InitialScreenFrame();
		frame.setVisible(true);
	}

	@Override
	public void instanceChange() {
		// TODO Auto-generated method stub
		
	}
}
