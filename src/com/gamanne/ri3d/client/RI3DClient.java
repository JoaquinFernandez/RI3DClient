package com.gamanne.ri3d.client;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.gamanne.ri3d.client.connection.ProviderManager;
import com.gamanne.ri3d.client.connection.ServerInfo;
import com.gamanne.ri3d.client.exceptions.InvalidDBCredentialsException;
import com.gamanne.ri3d.client.swing.InitialScreenFrame;
import com.gamanne.ri3d.client.swing.ListenerNotifier;
import com.gamanne.ri3d.client.swing.RegionInfoContainer;
import com.gamanne.ri3d.client.swing.RegionSelectContainer;
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

	private static JFrame parentFrame = new JFrame();

	private static CardLayout parentLayout;

	private static Container contentPane;

	private ProviderManager providerManager;

	private static int provider;

	final public static String host = "192.168.1.113";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		RI3DClient client = new RI3DClient();
		ListenerNotifier.addListener(client);
		//To set full screen mode multi-platform
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice vc = env.getDefaultScreenDevice();
		//Set Frame main options
		parentFrame.setTitle("RI3D Client");
		parentFrame.setUndecorated(true);
		parentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		parentLayout = new CardLayout();
		parentFrame.setLayout(parentLayout);

		if (System.getProperty("os.name").indexOf("nix") >= 0)
			vc.setFullScreenWindow(parentFrame);
		else
			parentFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		//Add content pane
		contentPane = parentFrame.getContentPane();
		contentPane.add(new InitialScreenFrame());
		parentFrame.setVisible(true);
	}

	@Override
	public void inputData(String user, String password) {
		try {

			//To let the user know the application is loading
			contentPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			providerManager = new ProviderManager(user, password);
			//Choose provider
			provider = ProviderManager.OPENSTACK;
			ServerInfo serverInfo = providerManager.getServerInfo(provider);
			contentPane.add(new RegionSelectContainer(serverInfo));
			parentLayout.next(contentPane);

		} catch (InvalidDBCredentialsException e) {
			JOptionPane.showMessageDialog(contentPane, e.getMessage());
		}
		//return to default cursor and show error dialog
		contentPane.setCursor(Cursor.getDefaultCursor());
	}

	@Override
	public void showRegionData(String region) {
		provider = ProviderManager.OPENSTACK;
		ServerInfo serverInfo = providerManager.getServerInfo(provider);
		contentPane.add(new RegionInfoContainer(serverInfo, region));
		parentLayout.next(contentPane);
	}

	@Override
	public void createServer(String newServerName, String flavorId, String imageId) {
		//To let the user know the application is loading
		contentPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		String url = providerManager.createInstance(provider, newServerName, flavorId, imageId);
		if (url == null) {
			JOptionPane.showMessageDialog(contentPane, "There was an error creating the server");
		}
		else {
			//For calling the VncViewer
			String[] args = {"URL", url};
			contentPane.add(new VncViewer(args));
			parentLayout.next(contentPane);
		}

		//return to default cursor and show error dialog
		contentPane.setCursor(Cursor.getDefaultCursor());
	}

	@Override
	public void connectServer(String serverId) {
		//To let the user know the application is loading
		contentPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		//Check is http format
		String url = providerManager.connectInstance(provider, serverId);
		if (url == null) {
			JOptionPane.showMessageDialog(contentPane, "There server you selected has an error");
		}
		else {
			//For calling the VncViewer
			String[] args = {"URL", url};
			VncViewer viewer = new VncViewer(args);
			contentPane.add(viewer);
			parentLayout.next(contentPane);
		}
		//return to default cursor
		contentPane.setCursor(Cursor.getDefaultCursor());
	}

	@Override
	public void changeUser() {
		contentPane.add(new InitialScreenFrame());
		parentLayout.next(contentPane);
	}
}
