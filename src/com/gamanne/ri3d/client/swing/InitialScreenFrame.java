package com.gamanne.ri3d.client.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.gamanne.ri3d.client.connection.SshClient;

/**
 * This class extends JFrame (Swing Library) and ActionListener, it creates a window
 * full size and without options bar, that asks the user for authentication,
 * when clicked the button log in (as it implements Action Listener) it starts
 * a ssh connection with the server to authenticate the user and creates a new window 
 * with the info received from the server
 * 
 * @author Joaquin Fernandez
 *
 */
public class InitialScreenFrame extends JFrame implements ActionListener {

	/** Serial version	 */
	private static final long serialVersionUID = 1L;
	
	/** The input text field that will contain the user that the user inputs */
	private JTextField userTextField;
	
	/** The input password field that will contain the password the user inputs */
	private JPasswordField passwordField;
	
	/**
	 * Constructor that will initialize all the components of the frame
	 */
	public InitialScreenFrame() {
		//Set Frame main options
		setTitle("RI3D Client");
		setExtendedState(MAXIMIZED_BOTH);
		setUndecorated(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		try {
			setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("foto.jpg")))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		JPanel loginPanel = getJPanelUserInfo();
		//Set the geometry container and contents
		GridLayout frameGeometryLayout = new GridLayout(4, 4);
		setLayout(frameGeometryLayout);
		//Add empty components untill getting to the position we want
		add(new JLabel());
		try {
			add(new JLabel(new ImageIcon(ImageIO.read(new File("logo.jpg")))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		add(new JLabel());
		add(new JLabel());
		add(new JLabel());
		add(new JLabel());
		add(new JLabel());
		add(new JLabel());
		add(loginPanel);
		add(new JLabel());
		add(new JLabel());

	}

	/**
	 * Auxiliary method that creates the components and add them to a panel
	 * @return the created panel to display in the window
	 */
	private JPanel getJPanelUserInfo() {
		//Create components
		JLabel userLabel = new JLabel("User");
		JLabel passwordLabel = new JLabel("Password");
		userTextField = new JTextField();
		passwordField = new JPasswordField();
		JButton button = new JButton("Log In");
		//Set button action Listener
		button.addActionListener(this);
		//Create panel
		JPanel loginPanel = new JPanel();
		//Set geometry layout
		GridBagLayout geometryLayout = new GridBagLayout();
		loginPanel.setLayout(geometryLayout);
		//Specify layout constaints for each component
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 0.1;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.BOTH;
		geometryLayout.setConstraints(userLabel, c);
		loginPanel.add(userLabel);
		c.gridx = 1;
		c.gridy = 0;
		geometryLayout.setConstraints(userTextField, c);
		loginPanel.add(userTextField);
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(10, 0, 0, 0);
		geometryLayout.setConstraints(passwordLabel, c);
		loginPanel.add(passwordLabel);
		c.gridx = 1;
		c.gridy = 1;
		geometryLayout.setConstraints(passwordField, c);
		loginPanel.add(passwordField);
		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(30, 20, 0, 0);
		geometryLayout.setConstraints(button, c);
		loginPanel.add(button);
		c.gridx = 2;
		c.gridy = 2;
		c.insets = new Insets(0, 0, 0, 0);
		JLabel auxLabel = new JLabel();
		geometryLayout.setConstraints(auxLabel, c);
		loginPanel.add(auxLabel);
		loginPanel.setOpaque(false);
		return loginPanel;
	}

	/**
	 * Overrided method actionPerformed that will be called when the log in
	 * button is pressed, it retrieves the info from the text fields, and creates
	 * an ssh connection with the server with the authentication info provided.
	 * 
	 * Once received the answer from the server, it creates a new window with the 
	 * info provided.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String user = userTextField.getText();
		String password = String.valueOf(passwordField.getPassword());
		new SshClient(user, password);
	}
}
