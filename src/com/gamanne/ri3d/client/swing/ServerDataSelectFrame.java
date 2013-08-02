package com.gamanne.ri3d.client.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.gamanne.ri3d.client.connection.ServerInfo;

public class ServerDataSelectFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private final JLabel lblChooseRegion = new JLabel("Choose region");

	/**
	 * Create the frame.
	 * @param serverInfo 
	 */
	public ServerDataSelectFrame(final ServerInfo serverInfo) {
		//Set Frame main options
		setTitle("RI3D Client");
		setExtendedState(MAXIMIZED_BOTH);
		setUndecorated(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{168, 113, 0};
		gbl_contentPane.rowHeights = new int[]{37, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		GridBagConstraints gbc_lblChooseRegion = new GridBagConstraints();
		gbc_lblChooseRegion.insets = new Insets(0, 0, 5, 5);
		gbc_lblChooseRegion.gridx = 0;
		gbc_lblChooseRegion.gridy = 0;
		contentPane.add(lblChooseRegion, gbc_lblChooseRegion);
		lblChooseRegion.setHorizontalAlignment(SwingConstants.LEFT);

		Box verticalBox = Box.createVerticalBox();
		GridBagConstraints gbc_verticalBox = new GridBagConstraints();
		gbc_verticalBox.insets = new Insets(0, 0, 5, 0);
		gbc_verticalBox.anchor = GridBagConstraints.NORTHWEST;
		gbc_verticalBox.gridx = 1;
		gbc_verticalBox.gridy = 0;
		contentPane.add(verticalBox, gbc_verticalBox);

		Box horizontalBox = Box.createHorizontalBox();
		verticalBox.add(horizontalBox);

		String[] regionNames = serverInfo.getRegionNames();
		int numberOfRegions = regionNames.length;
		for (int i = 0; i < numberOfRegions; i++) {
			JButton btnNewButton = new JButton(regionNames[i]);
			btnNewButton.addActionListener(new regionClickedListener(serverInfo, regionNames[i]));
			contentPane.add(btnNewButton);
		}

	}
	private class regionClickedListener implements ActionListener {

		private String regionName;
		private ServerInfo serverInfo;

		public regionClickedListener(ServerInfo serverInfo, String regionName) {
			this.serverInfo = serverInfo;
			this.regionName = regionName;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			//remove all components in panel.
			contentPane.removeAll();
			contentPane.setLayout(new GridLayout(2, 1, 0, 0));
			JPanel subContentPane = new JPanel();
			subContentPane.setLayout(new GridLayout(1, 3, 0, 0));
			String[] apiTypes = serverInfo.getApiTypes();
			int numberOfApiTypes = apiTypes.length;
			for (int i = 0; i < numberOfApiTypes; i++) {
				Box verticalBox = Box.createVerticalBox();
				JLabel lblApiType = new JLabel(apiTypes[i]);
				int apiTypeNames = serverInfo.getApiTypeNames(regionName, apiTypes[i]);
				for (int j = 0; j < apiTypeNames; j++) {
					Box verticalBox2 = Box.createVerticalBox();
					 verticalBox2.add(new JLabel(apiTypes[i] + " " + j + "\n"));
					String[] apiTypesKeys = serverInfo.getkeyNames(regionName, apiTypes[i], j);
					for (int k = 0; k < apiTypesKeys.length; k++) {
						Box horizontalBox = Box.createHorizontalBox();
						JLabel lblApiType2 = new JLabel(apiTypesKeys[k]);
						String keyValue = serverInfo.getkeyValue(regionName, apiTypes[i], j, apiTypesKeys[k]);
						JLabel lblApiType3 = new JLabel(keyValue);
						horizontalBox.add(lblApiType2);
						horizontalBox.add(new JLabel(" : "));
						horizontalBox.add(lblApiType3);
						verticalBox2.add(horizontalBox, 0);
					}
					verticalBox.add(verticalBox2, 0);
				}
				verticalBox.add(lblApiType, 0);
				subContentPane.add(verticalBox, 0);
			}
			contentPane.add(subContentPane);
			
			JPanel contentPane2 = new JPanel();
			contentPane2.setLayout(new GridLayout(7, 7, 0, 0));
			contentPane2.add(new JLabel());
			contentPane2.add(new JLabel());
			contentPane2.add(new JLabel());
			contentPane2.add(new JLabel());
			contentPane2.add(new JLabel());
			contentPane2.add(new JLabel());
			contentPane2.add(new JLabel());
			
			final JTextField textField = new JTextField();
			JButton button = new JButton("Connect to Instance");
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					String serverId = serverInfo.getServerId(textField.getText());
					ListenerNotifier.notifyConnectServer(serverId);
				}
			});
			contentPane2.add(new JLabel());
			contentPane2.add(new JLabel("Instert existing server name: "));
			contentPane2.add(new JLabel());
			contentPane2.add(textField);
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
			contentPane2.add(new JLabel("Instert flavor ID: "));
			contentPane2.add(new JLabel());
			JTextField textField2 = new JTextField();
			contentPane2.add(textField2);
			contentPane2.add(new JLabel());
			contentPane2.add(new JLabel());
			contentPane2.add(new JLabel());

			contentPane2.add(new JLabel());
			contentPane2.add(new JLabel());
			contentPane2.add(new JLabel());
			contentPane2.add(new JLabel());
			contentPane2.add(new JLabel());
			JButton button2 = new JButton("Create Instance and connect");
			contentPane2.add(button2);
			contentPane2.add(new JLabel());
			
			contentPane2.add(new JLabel());
			contentPane2.add(new JLabel("Instert image ID: "));
			contentPane2.add(new JLabel());
			JTextField textField3 = new JTextField();
			contentPane2.add(textField3);
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
			
			contentPane.add(contentPane2);
			// refresh the panel.
			contentPane.updateUI(); 
		}
		
	}

}
