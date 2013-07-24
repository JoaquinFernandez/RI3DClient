package com.gamanne.ri3d.client.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import com.gamanne.ri3d.client.Config;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;

/**
 * SSHClient class, it creates a client for connecting to the server, it holds all the
 * information and methods to exchange information with the server
 * 
 * @author Joaquin Fernandez
 *
 */
public class ConnectionClient {

	private static Logger LOGGER = Logger.getLogger("InfoLogging");
	private String password;
	private String user;
	private String host;
	private int port;
	ServerInfo serverInfo;

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
	public ConnectionClient(String user, String password) {

		this.user = user;
		this.password = password;

		//Get the host info from the configuration file
		Config config = new Config("config.cfg");
		host = config.getProperty("host");
		port = Integer.parseInt(config.getProperty("initialPort"));
	}

	public ServerInfo getServerInfo() {
		try {
			serverInfo = new ServerInfo(readFile());
		} catch (Exception e) {
			LOGGER.warning(e.getMessage());
			e.printStackTrace();
		}
		return serverInfo;
	}


	//SshClient sshClient = SshClient.setUpDefaultClient();
	//sshClient.start();
	//connectClient(sshClient);
	/*private void connectClient(SshClient sshClient) {
		ConnectFuture sshConnection;
		try {
			sshConnection = sshClient.connect(host, port).await();
			if (!sshConnection.isConnected())
				LOGGER.warning("Connection Timed out");
			LOGGER.info("Client Connected, authenticating...");
			final ClientSession clientSession = sshConnection.getSession();
			clientSession.authPassword(user, password).await();
			LOGGER.info("Client authenticated, receiving server info...");

		}
		catch (Exception e) {
			LOGGER.warning(e.getMessage());
			e.printStackTrace();
		}
	}*/
	
	protected String readFile() throws Exception {
		LOGGER.info("Connecting client...");
		JSch sch = new JSch();
		com.jcraft.jsch.Session session = sch.getSession(user, host, port);
		session.setConfig("StrictHostKeyChecking", "no");
		session.setPassword(password);
		session.connect();
		LOGGER.info("Client connected, connecting exec channel...");

		ChannelExec channel = (ChannelExec)session.openChannel("exec");
		OutputStream out = channel.getOutputStream();
		InputStream in = channel.getInputStream();
		channel.setCommand("scp -f " + "/home/joaquin/.ri3d/serverInfo.txt"); //path is something like /home/username/sample.txt
		channel.connect();
		LOGGER.info("Channel connected, retrieving server info...");
		byte[] buf=new byte[1024];

		// send '\0'
		buf[0]=0; out.write(buf, 0, 1); out.flush();

		String fileContent = "";
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

			for(int i = 0; ;i++){
				in.read(buf, i, 1);
				if(buf[i] == (byte) 0x0a){
					new String(buf, 0, i);//Filename
					break;
				}
			}

			//System.out.println("filesize="+filesize+", file="+file);

			// send '\0'
			buf[0]=0; out.write(buf, 0, 1); out.flush();

			// read a content of lfile
			int foo;
			while(true){
				if(buf.length<filesize) 
					foo=buf.length;
				else 
					foo = (int) filesize;
				foo = in.read(buf, 0, foo);
				if(foo < 0) {
					break;//error
				}

				fileContent += new String(buf, 0, foo);
				filesize -= foo;
				if(filesize == 0L) 
					break;
			}

			if(checkAck(in) != 0){
				System.exit(0);
			}

			// send '\0'
			buf[0]=0; out.write(buf, 0, 1); out.flush();
		}
		out.close();
		in.close();
		channel.disconnect();
		session.disconnect();
		LOGGER.info("Server info retrieved");
		return fileContent;

	}

	static int checkAck(InputStream in) throws IOException{
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
