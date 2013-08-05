package com.gamanne.ri3d.client.swing;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import com.gamanne.ri3d.client.connection.ServerInfo;

@SuppressWarnings("serial")
public class RegionSelectContainer extends Container {

	/**
	 * 
	 */
	private final JLabel lblChooseRegion = new JLabel("Choose region");

	/**
	 * Create the frame.
	 * @param serverInfo 
	 */
	public RegionSelectContainer(ServerInfo serverInfo) {
		setLayout(new GridLayout(7, 4, 0, 0));
		add(new JLabel());
		add(new JLabel());
		add(new JLabel());
		add(lblChooseRegion);
		add(new JLabel());
		add(new JLabel());
		add(new JLabel());

		String[] regionNames = serverInfo.getRegionNames();
		int numberOfRegions = regionNames.length;
		for (int i = 0; i < numberOfRegions; i++) {
			JButton btnNewButton = new JButton(regionNames[i]);
			btnNewButton.addActionListener(new RegionListener(regionNames[i]));
			add(new JLabel());
			add(btnNewButton);
			add(new JLabel());
		}
	}
	private class RegionListener implements ActionListener {

		private String regionName;

		public RegionListener(String regionName) {
			this.regionName = regionName;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			ListenerNotifier.notifyRegionChoosed(regionName);
		}
		
	}
}