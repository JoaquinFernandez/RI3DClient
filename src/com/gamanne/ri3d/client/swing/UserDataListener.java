package com.gamanne.ri3d.client.swing;

public interface UserDataListener {

	public void inputData (String user, String password);

	public void showRegionData(String region);
	
	public void createServer (String newServerName, String flavorId, String imageId);
	
	public void connectServer (String serverId);

	public void changeUser();
	
}
