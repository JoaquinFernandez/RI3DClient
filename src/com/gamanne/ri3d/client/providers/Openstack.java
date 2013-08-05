package com.gamanne.ri3d.client.providers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.sshd.ClientChannel;
import org.apache.sshd.ClientSession;
import org.apache.sshd.SshClient;
import org.jclouds.ContextBuilder;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.NovaApiMetadata;
import org.jclouds.openstack.nova.v2_0.domain.Flavor;
import org.jclouds.openstack.nova.v2_0.domain.Image;
import org.jclouds.openstack.nova.v2_0.domain.Server;
import org.jclouds.openstack.nova.v2_0.domain.ServerCreated;
import org.jclouds.openstack.nova.v2_0.features.FlavorApi;
import org.jclouds.openstack.nova.v2_0.features.ImageApi;
import org.jclouds.openstack.nova.v2_0.features.ServerApi;
import org.jclouds.openstack.nova.v2_0.options.CreateServerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gamanne.ri3d.client.RI3DClient;
import com.gamanne.ri3d.client.connection.ServerInfo;
import com.gamanne.ri3d.client.connection.TeePipedOutputStream;

public class Openstack {

	private static Logger LOGGER = Logger.getLogger("InfoLogging");

	private NovaApi api;

	private List<String> zones = new ArrayList<String>();

	private String password;

	private ServerInfo serverInfo;

	private String userName;

	private String tenantName;


	public Openstack(List<String> list) {
		LOGGER.info("Initializing...");
		credentials(list);
		init();
	}

	private void credentials(List<String> list) {
		for (String string : list) {
			String[] valuePair = string.split(":");
			if (valuePair[0].equalsIgnoreCase("tenantname"))
				tenantName = valuePair[1];

			else if (valuePair[0].equalsIgnoreCase("username"))
				userName = valuePair[1];

			else if (valuePair[0].equalsIgnoreCase("password"))
				password = valuePair[1];
		}		
	}

	private void init() {

		api = ContextBuilder.newBuilder(new NovaApiMetadata())
				.endpoint("http://" + RI3DClient.host + ":5000/v2.0")
				.credentials(tenantName + ":" + userName, password)
				.buildApi(NovaApi.class);
		Set<String> zones = api.getConfiguredZones();
		for (String zone : zones) {
			this.zones.add(zone);
		}
	}

	private void retrieveServerInfo() {

		try {
			JSONObject serversInfo = new JSONObject();

			for (String zone: zones) {
				JSONObject serverInfo = new JSONObject();

				JSONArray serverArray = new JSONArray();
				ServerApi serverApi = api.getServerApiForZone(zone);
				for (Server server: serverApi.listInDetail().concat()) {
					JSONObject object = new JSONObject();
					object.put("id", server.getId());
					object.put("tenantId", server.getTenantId());
					object.put("userId", server.getUserId());
					object.put("flavorId", server.getFlavor().getId());
					object.put("name", server.getName());
					serverArray.put(object);
				}
				serverInfo.put("servers", serverArray);

				JSONArray flavorArray = new JSONArray();
				FlavorApi flavorApi = api.getFlavorApiForZone(zone);
				for (Flavor flavor: flavorApi.listInDetail().concat()) {
					JSONObject object = new JSONObject();
					object.put("id", flavor.getId());
					object.put("name", flavor.getName());
					object.put("ram", flavor.getRam());
					object.put("disk", flavor.getDisk());
					flavorArray.put(object);
				}
				serverInfo.put("flavors", flavorArray);


				JSONArray imageArray = new JSONArray();
				ImageApi imageApi = api.getImageApiForZone(zone);
				for (Image image: imageApi.listInDetail().concat()) {
					JSONObject object = new JSONObject();
					object.put("name", image.getName());
					object.put("status", image.getStatus());
					object.put("id", image.getId());
					imageArray.put(object);
				}
				serverInfo.put("images", imageArray);

				serversInfo.put(zone, serverInfo);
			}
			this.serverInfo = new ServerInfo(serversInfo);
		} catch (JSONException e) {
			LOGGER.warning(e.getMessage());
			e.printStackTrace();
		}
	}

	public ServerInfo getServerInfo() {
		while (api == null) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		retrieveServerInfo();

		return serverInfo;

	}

	public String connectInstance(String serverId) {

		//We would use this if JCLOUDS api supports it
		//Server server = api.getServerApiForZone(zones.get(0)).get(serverId);
		//meanwhile:
		String url = "";
		try {
			SshClient client = SshClient.setUpDefaultClient();
			client.start();
			ClientSession session = client.connect(RI3DClient.host, 22).await().getSession();
			session.authPassword("openstack", "server").await().isSuccess();
			ClientChannel channel = session.createChannel(ClientChannel.CHANNEL_SHELL);

			ByteArrayOutputStream sent = new ByteArrayOutputStream();
			PipedOutputStream pipedIn = new TeePipedOutputStream(sent);
			channel.setIn(new PipedInputStream(pipedIn));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ByteArrayOutputStream err = new ByteArrayOutputStream();
			channel.setOut(out);
			channel.setErr(err);
			channel.open();

			//credentials for tenant name demo and user demo
			String credentials = "--os-username " + userName + " " +
					"--os-tenant-name " + tenantName + 
					" --os-auth-url http://localhost:35357/v2.0 " +
					"--os-password " + password;
			String command = "nova " + credentials + " get-vnc-console " + serverId + " xvpvnc\n";
			pipedIn.write(command.getBytes());
			pipedIn.flush();

			pipedIn.write("exit\n".getBytes());
			pipedIn.flush();

			channel.waitFor(ClientChannel.CLOSED, 0);
			String response = new String(out.toByteArray());
			int beginIndex = response.indexOf("| xvpvnc |");
			response = response.substring(beginIndex + 11);//11 is the length of index
			beginIndex = response.indexOf("http://");
			int endIndex = response.substring(beginIndex).indexOf("|");
			url = response.substring(beginIndex, beginIndex + endIndex).trim();
			channel.close(false);
			client.stop();
		} catch (InterruptedException e) {
			LOGGER.warning(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			LOGGER.warning(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			LOGGER.warning(e.getMessage());
			e.printStackTrace();
		}
		return url;
	}

	public String createInstance(String newServerName, String flavorId, String imageId) {
		ServerApi serverApi = api.getServerApiForZone(zones.get(0));
		ServerCreated serverCreated = serverApi.create(newServerName, imageId, flavorId, new CreateServerOptions());
		Server server = api.getServerApiForZone(zones.get(0)).get(serverCreated.getId());
		//wait for the server to finish its build state (when it is finished it will be marked as build
		while (server.getStatus().equals(Server.Status.BUILD)) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			server = api.getServerApiForZone(zones.get(0)).get(serverCreated.getId());
		}
		//Check if the server status is active to request vnc url
		if (!server.getStatus().equals(Server.Status.ACTIVE))
			return null;
		String url = connectInstance(server.getId());
		return url;
	}
}
