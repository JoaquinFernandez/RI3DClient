package com.jfernandez.ri3d.client;

import com.jfernandez.ri3d.client.swing.InitialScreenFrame;

/**
 * RI3D main class it handles the initialization of the data, the creation of the
 * windows and asks for the authentication credentials
 * 
 * @author Joaquin Fernandez
 *
 */
public class RI3D {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InitialScreenFrame frame = new InitialScreenFrame();
		frame.setVisible(true);
	}

}
