package com.gamanne.ri3d.client.connection;

import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ServerInfo {

	private static Logger LOGGER = Logger.getLogger("InfoLogging");

	private JSONObject serverInfo;

	private String[] regionNames;
	
	private String[] apiTypes;
	
	public ServerInfo(String readFile) {
		try {
			serverInfo = new JSONObject(readFile);
			JSONArray zones = serverInfo.names();
			int numberOfZones = zones.length();
			regionNames = new String[numberOfZones];
			
			for (int i = 0; i < numberOfZones; i++) {
				regionNames[i] = zones.getString(i);
				JSONObject zone = serverInfo.getJSONObject(regionNames[i]);
				JSONArray apis = zone.names();
				int numberOfApis = apis.length();
				apiTypes = new String[numberOfApis];
				for (int j = 0; j < numberOfApis; j++) {
					apiTypes[j] = apis.getString(j);
				}
			}
		} catch (JSONException e) {
			LOGGER.warning(e.getMessage());
			e.printStackTrace();
		}
	}
	public String[] getRegionNames() {
		return regionNames;
	}
	public String[] getApiTypes() {
		return apiTypes;
	}
	public int getApiTypeNames(String regionName, String apiType) {
		try {
			JSONArray apiTypeArray = serverInfo.getJSONObject(regionName).getJSONArray(apiType);
			int numberOfApiTypes = apiTypeArray.length();
			return numberOfApiTypes;
		} catch (JSONException e) {
			LOGGER.warning(e.getMessage());
			e.printStackTrace();
		}
		return 0;
	}
	public String[] getkeyNames(String regionName, String apiType, int apiTypePosition) {
		try {
			JSONObject apiTypeArray = serverInfo.getJSONObject(regionName).getJSONArray(apiType).getJSONObject(apiTypePosition);
			JSONArray apiTypePositionKey = apiTypeArray.names();
			int numberOfApiTypes = apiTypePositionKey.length();
			String[] keyNames = new String[numberOfApiTypes];
			for (int i = 0; i < numberOfApiTypes; i++) {
				keyNames[i] = apiTypePositionKey.getString(i);
			}
			return keyNames;
		} catch (JSONException e) {
			LOGGER.warning(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	public String getkeyValue(String regionName, String apiType, int apiTypePosition, String key) {
		try {
			String value = serverInfo.getJSONObject(regionName).getJSONArray(apiType).getJSONObject(apiTypePosition).getString(key);
			return value;
		} catch (JSONException e) {
			LOGGER.warning(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
