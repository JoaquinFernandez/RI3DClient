package com.gamanne.ri3d.client.swing;

public interface UserDataListener {

	public void inputData (String user, String password);
	
	public void createServer (String flavorId, String imageId);
	
	public void connectServer (String serverId);

	public void changeUser();

	public void instanceChange();
	
}
