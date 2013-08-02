package com.gamanne.ri3d.client.connection;

import java.util.logging.Logger;

import com.gamanne.ri3d.client.providers.KeyVault;
import com.gamanne.ri3d.client.providers.Openstack;

/**
 * SSHClient class, it creates a client for connecting to the server, it holds all the
 * information and methods to exchange information with the server
 * 
 * @author Joaquin Fernandez
 *
 */
public class ProviderManager {

	private static Logger LOGGER = Logger.getLogger("InfoLogging");

	private KeyVault keyVault;

	public final static int OPENSTACK = 0;

	private Openstack openstack;
	/**
	 * This public method gets the connection info (hostname) from the configuration
	 * file and starts a session with the data provided (user, password) with the server,
	 * gets the response from the server and returns it to the application
	 * 
	 * @param user Authentication user
	 * @param password Authentication password
	 * @return 
	 * 
	 */
	public ProviderManager(String user, String password) {

		keyVault = new KeyVault(user, password);
		init();
		LOGGER.info("Server info retrieved");
	}

	private void init() {
		openstack = new Openstack(keyVault.getOpenStackCredentials());
	}

	public ServerInfo getServerInfo(int provider) {
		switch (provider) {
		case OPENSTACK:
			return openstack.getServerInfo();
		default:
			return openstack.getServerInfo();
		}
	}

	public String connectInstance(int provider, String serverId) {
		switch (provider) {
		case OPENSTACK:
			return openstack.connectInstance(serverId);
		default:
			return openstack.connectInstance(serverId);
		}
	}
}
