package com.gamanne.ri3d.client.swing;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.gamanne.ri3d.client.connection.ServerInfo;


@SuppressWarnings("serial")
public class RegionInfoContainer extends Container {

	public RegionInfoContainer(final ServerInfo serverInfo, String regionName) {
		
		setLayout(new GridLayout(2, 1, 0, 0));
		JPanel subContentPane = new JPanel();
		subContentPane.setLayout(new GridLayout(1, 3, 0, 0));
		String[] apiTypes = serverInfo.getApiTypes();
		int numberOfApiTypes = apiTypes.length;
		for (int i = 0; i < numberOfApiTypes; i++) {
			Box verticalBox = Box.createVerticalBox();
			JLabel lblApiType = new JLabel(apiTypes[i].toUpperCase());
			int apiTypeNames = serverInfo.getApiTypeNames(regionName, apiTypes[i]);
			for (int j = 0; j < apiTypeNames; j++) {
				Box verticalBox2 = Box.createVerticalBox();
				String[] apiTypesKeys = serverInfo.getkeyNames(regionName, apiTypes[i], j);
				for (int k = 0; k < apiTypesKeys.length; k++) {
					Box horizontalBox = Box.createHorizontalBox();
					JLabel lblApiType2 = new JLabel(apiTypesKeys[k]);
					String keyValue = serverInfo.getkeyValue(regionName, apiTypes[i], j, apiTypesKeys[k]);
					JLabel lblApiType3 = new JLabel(keyValue);
					if (!lblApiType2.getText().toLowerCase().contains("id")) {
						if (!lblApiType2.getText().equals("name")) {
							horizontalBox.add(lblApiType2);
							horizontalBox.add(new JLabel(" : "));
						}
						horizontalBox.add(lblApiType3);
						verticalBox2.add(horizontalBox, 0);
					}
				}
				verticalBox2.add(new JLabel(" "));
				verticalBox.add(verticalBox2, 0);
			}
			verticalBox.add(lblApiType, 0);
			subContentPane.add(verticalBox, 0);
		}
		add(subContentPane);

		JPanel contentPane2 = new JPanel();
		final JTextField serverNameTextField = new JTextField();
		final JTextField flavorNameTextField = new JTextField();
		final JTextField imageNameTextField = new JTextField();
		final JTextField newServerNameTextField = new JTextField();
		contentPane2.setLayout(new GridLayout(7, 7, 0, 0));
		//add empty labels to frmat the grid layout
		contentPane2.add(new JLabel());
		contentPane2.add(new JLabel());
		contentPane2.add(new JLabel());
		contentPane2.add(new JLabel());
		contentPane2.add(new JLabel());
		contentPane2.add(new JLabel());
		contentPane2.add(new JLabel());
		JButton button = new JButton("Connect to Instance");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String serverId = serverInfo.getServerId(serverNameTextField.getText());
				if (serverId == null)
					errorDialog("Instance doesn´t exist");

				else
					ListenerNotifier.notifyConnectServer(serverId);
			}
		});
		contentPane2.add(new JLabel());
		contentPane2.add(new JLabel("Instert existing server name: "));
		contentPane2.add(new JLabel());
		contentPane2.add(serverNameTextField);
		contentPane2.add(new JLabel());
		contentPane2.add(button);
		contentPane2.add(new JLabel());

		contentPane2.add(new JLabel());
		contentPane2.add(new JLabel());
		contentPane2.add(new JLabel());
		contentPane2.add(new JLabel());
		contentPane2.add(new JLabel());
		contentPane2.add(new JLabel());
		contentPane2.add(new JLabel());

		contentPane2.add(new JLabel());
		contentPane2.add(new JLabel("Insert flavor name: "));
		contentPane2.add(new JLabel());
		contentPane2.add(flavorNameTextField);
		contentPane2.add(new JLabel());
		contentPane2.add(new JLabel());
		contentPane2.add(new JLabel());

		contentPane2.add(new JLabel());
		contentPane2.add(new JLabel("Insert the new server name: "));
		contentPane2.add(new JLabel());
		contentPane2.add(newServerNameTextField);
		contentPane2.add(new JLabel());
		JButton button2 = new JButton("Create Instance and connect");
		button2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {

				String flavorId = serverInfo.getFlavorServerId(flavorNameTextField.getText());
				String imageId = serverInfo.getImageId(imageNameTextField.getText());
				String newServerName = newServerNameTextField.getText();

				if (newServerName.trim().length() < 4)
					errorDialog("Please introduce a valid server name (not blank or less than 4 letters)");
				else if (flavorId == null || imageId == null) {
					if (flavorId == null)
						errorDialog("Flavor doesn´t exist");
					else if (imageId == null)
						errorDialog("Image doesn´t exist");
				}
				else
					ListenerNotifier.notifyCreateServer(newServerName, flavorId, imageId);
			}

		});
		contentPane2.add(button2);
		contentPane2.add(new JLabel());

		contentPane2.add(new JLabel());
		contentPane2.add(new JLabel("Instert image name: "));
		contentPane2.add(new JLabel());
		contentPane2.add(imageNameTextField);
		contentPane2.add(new JLabel());
		contentPane2.add(new JLabel());
		contentPane2.add(new JLabel());

		contentPane2.add(new JLabel());
		contentPane2.add(new JLabel());
		contentPane2.add(new JLabel());
		contentPane2.add(new JLabel());
		contentPane2.add(new JLabel());
		contentPane2.add(new JLabel());
		contentPane2.add(new JLabel());

		add(contentPane2);
	}

	protected void errorDialog(String string) {
		JOptionPane.showMessageDialog(this, string);
	}
}


