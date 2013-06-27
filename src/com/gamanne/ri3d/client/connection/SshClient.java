package com.gamanne.ri3d.client.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import com.gamanne.ri3d.client.Config;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * SSHClient class, it creates a client for connecting to the server, it holds all the
 * information and methods to exchange information with the server
 * 
 * @author Joaquin Fernandez
 *
 */
public class SshClient {

	private static Logger LOGGER = Logger.getLogger("InfoLogging");
	private String password;
	private String user;
	private String host;
	private int port;

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
	public SshClient(String user, String password) {

		this.user = user;
		this.password = password;
		try {
			String file = retrieveFile();
			readFile(file);
			startServerSession();
		} catch (JSchException | IOException e) {
			LOGGER.info(e.getMessage());
			e.printStackTrace();
		}
	}

	private void startServerSession() throws JSchException {
		//Establish session with the host with the user and password given
		//in the port set in the config file in which the server will be listening
		JSch jsch = new JSch();
		//This line is to bypass the known host checking, in final version it should
		//not bypass but set the server as known host for each client
		JSch.setConfig("StrictHostKeyChecking", "no");
		Session session = jsch.getSession(user, host, port);
		session.setPassword(password);
		session.connect();
	}

	private void readFile(String file) {
		//Get the information from the configuration file of the server
		int startIndex = file.indexOf("newCommunicationPort = ") + 23;
		int endIndex = file.indexOf("\n");
		port = Integer.parseInt(file.substring(startIndex, endIndex));
	}

	private String retrieveFile() throws JSchException, IOException {
		String formattedFile = "";
		//Get the host info from the configuration file
		Config config = new Config("config.cfg");
		host = config.getProperty("host");
		int port = Integer.parseInt(config.getProperty("initialPort"));
		//Establish session with the host with the user and password given
		//in the port set in the config file in which the server will be listening
		JSch jsch = new JSch();
		//This line is to bypass the known host checking, in final version it should
		//not bypass but set the server as known host for each client
		JSch.setConfig("StrictHostKeyChecking", "no");
		Session session = jsch.getSession(user, host, port);
		session.setPassword(password);
		session.connect();

		String filename = "/home/joaquin/.ri3d/configDataClient.cfg";
		String command = "scp -f " + filename;
		ChannelExec channel = (ChannelExec) session.openChannel("exec");
		channel.setCommand(command);

		// get I/O streams for remote scp
		OutputStream out = channel.getOutputStream();
		InputStream in = channel.getInputStream();

		channel.connect();

		byte[] buf = new byte[1024];

		// send '\0'
		buf[0] = 0;
		out.write(buf, 0, 1); 
		out.flush();

		while(true){
			int c=checkAck(in);
			if(c!='C'){
				break;
			}

			// read '0644 '
			in.read(buf, 0, 5);

			long filesize=0L;
			while(true){
				if(in.read(buf, 0, 1)<0){
					// error
					break;
				}
				if(buf[0]==' ')break;
				filesize=filesize*10L+(long)(buf[0]-'0');
			}

			for (int i = 0; ;i++) {
				in.read(buf, i, 1);
				if (buf[i] == (byte) 0x0a) {
					new String(buf, 0, i);
					break;
				}
			}

			//System.out.println("filesize="+filesize+", file="+file);

			// send '\0'
			buf[0]=0; out.write(buf, 0, 1); out.flush();

			int bytes;
			while (true) {
				if(buf.length<filesize) bytes=buf.length;
				else bytes=(int)filesize;
				bytes=in.read(buf, 0, bytes);
				if(bytes<0){
					// error
					break;
				}
				formattedFile += new String(buf, "UTF8") + "\n";
				filesize-=bytes;
				if(filesize==0L) break;
			}

			if(checkAck(in)!=0){
				System.exit(0);
			}

			// send '\0'
			buf[0]=0; out.write(buf, 0, 1); 
			out.flush();
		}
		in.close();
		out.close();
		channel.disconnect();
		session.disconnect();
		return formattedFile;
	}

	private int checkAck(InputStream in) throws IOException {
		int b=in.read();
		// b may be 0 for success,
		// 1 for error,
		// 2 for fatal error,
		// -1
		if(b==0) return b;
		if(b==-1) return b;

		if(b==1 || b==2){
			StringBuffer sb=new StringBuffer();
			int c;
			do {
				c=in.read();
				sb.append((char)c);
			}
			while(c!='\n');
			if(b==1){ // error
				System.out.print(sb.toString());
			}
			if(b==2){ // fatal error
				System.out.print(sb.toString());
			}
		}
		return b;
	}

}
