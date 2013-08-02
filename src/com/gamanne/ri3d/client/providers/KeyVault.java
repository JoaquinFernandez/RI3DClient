package com.gamanne.ri3d.client.providers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.gamanne.ri3d.client.RI3DClient;

public class KeyVault {

	private static Logger LOGGER = Logger.getLogger("InfoLogging");

	private List<String> openstackCredentials = new ArrayList<String>();

	private List<String> amazonCredentials = new ArrayList<String>();

	private List<String> trystackCredentials = new ArrayList<String>();

	private List<String> eucalyptusCredentials = new ArrayList<String>();

	public KeyVault(String user, String password) {
		String dburl = "jdbc:mysql://" + RI3DClient.host + ":3306/ri3d";
		String dbuser = "ri3dadmin";
		String dbpassword = "ri3ddb";
		String dbAESKey = "ri3dkey";
		String query = "SELECT openstack, amazon, trystack, eucalyptus FROM user WHERE (user = '" + user + "') AND" +
				" (password = AES_ENCRYPT('" + password + "', '" + dbAESKey + "'));";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(dburl, dbuser, dbpassword);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				String openstack = resultSet.getString(1);
				if (openstack != null) {
					String[] aux = openstack.split(";");
					for (String valuePair : aux) {
						openstackCredentials.add(valuePair);
					}
				}

				String amazon = resultSet.getString(2);
				if (amazon != null) {
					String[] aux = amazon.split(";");
					for (String valuePair : aux) {
						amazonCredentials.add(valuePair);
					}
				}
				String trystack = resultSet.getString(3);
				if (trystack != null) {
					String[] aux = trystack.split(";");
					for (String valuePair : aux) {
						trystackCredentials.add(valuePair);
					}
				}
				String eucalyptus = resultSet.getString(2);
				if (eucalyptus != null) {
					String[] aux = eucalyptus.split(";");
					for (String valuePair : aux) {
						eucalyptusCredentials.add(valuePair);
					}
				}
			}
			statement.close();
			connection.close();
		} catch (SQLException | ClassNotFoundException e) {
			LOGGER.info(e.getMessage());
			e.printStackTrace();
		}
	}

	public List<String> getOpenStackCredentials() {
		return openstackCredentials;
	}

	public List<String> getAmazonCredentials() {
		return amazonCredentials;
	}

	public List<String> getTrystackCredentials() {
		return trystackCredentials;
	}

	public List<String> getEucalyptusCredentials() {
		return eucalyptusCredentials;
	}
}
