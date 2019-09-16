package packages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ZWave {
	
	private static String address = "http://134.88.49.223";
	private static String port = ":8083";
	private static String authenticate = ZWave.address + ZWave.port  +"/ZAutomation/api/v1/login";
	private static HttpClient client;
	public static Scenes commandSettings;
	public static String currLightState;
	
	//get current address of http server
	public static String getAddress() {
		return ZWave.address;
	}
	// create instance of HttpClientBuilder 
	public static void create() {
		ZWave.client = HttpClientBuilder.create().build();
	}
	// set address
	public static void setAddress(String myAddress) {
		ZWave.address = myAddress;
		
	}
	// authenticate zway server
	public static void Authenticate() throws ClientProtocolException, IOException {
		HttpPost request = new HttpPost(ZWave.authenticate);
		request.setHeader(HttpHeaders.CONTENT_TYPE , "application/json");
		request.setHeader(HttpHeaders.ACCEPT, "application/json");
		String json = "{\"form\":\"true\" ,\"login\":\"admin\",\"password\":\"ece6123\",\"keepme\":\"false\", \"default_ui\":\"1\"}";
		StringEntity entity = new StringEntity(json);
		request.setEntity(entity);
		System.out.println(entity);
		HttpResponse statusResponse = client.execute(request);
		System.out.println(statusResponse.toString());
	
  		
	}
	
	// post request to z way server to target a device and brightness
	public static int post(int device, int brightness) throws ClientProtocolException, IOException {
		  //Gets the current status of the light
  	  
  	  	if(getStatus(device) > 0) { // Light on
  	  		brightness = 0;
  	  		
  	  	}
  	  		
		HttpPost postStatus = new HttpPost(ZWave.address + ZWave.port + "/ZWaveAPI/Run/devices%5B"+Integer.toString(device) +"%5D.instances%5B0%5D.Basic.Set%28" + Integer.toString(brightness)+"%29");
		HttpResponse postResponse = client.execute(postStatus);
		postStatus.releaseConnection();
		System.out.println(postResponse.toString());
		
		return 0;
		
	}
	// gets status of a specific device number 
	public static int getStatus(int device) throws ClientProtocolException, IOException {
		HttpPost getStatus = new HttpPost(ZWave.address + ZWave.port + "/ZWaveAPI/Run/devices%5B" + Integer.toString(device) + "%5D.instances%5B0%5D.commandClasses.SwitchMultilevel.data.level.value");
		HttpResponse statusResponse = client.execute(getStatus);
		BufferedReader statusOut = new BufferedReader(new InputStreamReader(statusResponse.getEntity().getContent()));
		getStatus.releaseConnection();
		return Integer.parseInt(statusOut.readLine());
		
	}
	// gets the receptacle status another function is needed because different API calls
	public static boolean getRecStatus(int device) throws IOException {
		HttpPost getStatus = new HttpPost(ZWave.address + ZWave.port + "/ZWaveAPI/Run/devices%5B" + Integer.toString(device) + "%5D.instances%5B0%5D.commandClasses.SwitchBinary.data.level.value");
		HttpResponse statusResponse = client.execute(getStatus);
		BufferedReader statusOut = new BufferedReader(new InputStreamReader(statusResponse.getEntity().getContent()));
		getStatus.releaseConnection();
		return Boolean.parseBoolean(statusOut.readLine());
		
	}
	// Turns on a combination of devices from a mental command action
	public static void scenePost(Map<Integer,Integer> obj,String state) throws ClientProtocolException, IOException{ 
		
		Iterator<Entry<Integer, Integer>> x = obj.entrySet().iterator(); // iterate through Map object
		while(x.hasNext()) {
			Map.Entry val = (Map.Entry)x.next(); // get next mab object	
			Service<Void> myservice = new Service<Void>() { // create new service for task
				@Override
				protected Task<Void> createTask() { // create new task
					// TODO Auto-generated method stub
					return new Task<Void>() {

						@Override
						protected Void call() throws Exception {
							// TODO Auto-generated method stub
							if(val.getValue().toString() == "0") {
								return null;
							}
							else {
								
								if(state == currLightState && getStatus(Integer.parseInt(val.getKey().toString()))>0) {
									sliderPost(Integer.parseInt(val.getKey().toString()),0);
								}
								else
									sliderPost(Integer.parseInt(val.getKey().toString()),Integer.parseInt(val.getValue().toString()));
								
								currLightState = state;
								return null;
							}
						}				
						@Override
						protected void succeeded() {
//							Platform.runLater(()->{
//								currLightState = state;
//								int key = Integer.parseInt(val.getKey().toString());
//								
//								if(key == ZWave.getDevice("Light1")){
//										controllerInterface.mc.getD1Status().setText(val.getValue().toString());
//										if(val.getValue().equals(0)) {
//											controllerInterface.mc.getDeviceOneImg().setImage(controllerInterface.mc.getLightOff());
//											controllerInterface.mc.getDeviceSlider(ZWave.getDevice("Light1")).setValue(Integer.parseInt(val.getValue().toString()));
//										}
//										else if( Integer.parseInt(val.getValue().toString()) > 0 && Integer.parseInt(val.getValue().toString()) < 99) {
//											controllerInterface.mc.getDeviceOneImg().setImage(controllerInterface.mc.getLightHalf());
//											controllerInterface.mc.getDeviceSlider(ZWave.getDevice("Light1")).setValue(Integer.parseInt(val.getValue().toString()));
//										}
//										else {
//											controllerInterface.mc.getDeviceOneImg().setImage(controllerInterface.mc.getLightOn());
//											controllerInterface.mc.getDeviceSlider(ZWave.getDevice("Light1")).setValue(Integer.parseInt(val.getValue().toString()));
//										}
//										
//										
//								}
//								else if(key == ZWave.getDevice("Light2")) {
//										controllerInterface.mc.getD2Status().setText(val.getValue().toString());
//										if(val.getValue().equals(0)) {
//											controllerInterface.mc.getDeviceTwoImg().setImage(controllerInterface.mc.getLightOff());
//											controllerInterface.mc.getDeviceSlider(ZWave.getDevice("Light2")).setValue(Integer.parseInt(val.getValue().toString()));
//										}
//										else if( Integer.parseInt(val.getValue().toString()) > 0 && Integer.parseInt(val.getValue().toString()) < 99) {
//											controllerInterface.mc.getDeviceOneImg().setImage(controllerInterface.mc.getLightHalf());
//											controllerInterface.mc.getDeviceSlider(ZWave.getDevice("Light2")).setValue(Integer.parseInt(val.getValue().toString()));
//										}
//										else {
//											controllerInterface.mc.getDeviceTwoImg().setImage(controllerInterface.mc.getLightOn());
//											controllerInterface.mc.getDeviceSlider(ZWave.getDevice("Light2")).setValue(Integer.parseInt(val.getValue().toString()));
//										}
//								}
//										
//								else {
//										controllerInterface.mc.getD3Status().setText(val.getValue().toString());
//										if(val.getValue().equals(0)) {
//											controllerInterface.mc.getDeviceThreeImg().setImage(controllerInterface.mc.getLightOff());
//											controllerInterface.mc.getDeviceSlider(ZWave.getDevice("Light3")).setValue(Integer.parseInt(val.getValue().toString()));
//										}
//										else if( Integer.parseInt(val.getValue().toString()) > 0 && Integer.parseInt(val.getValue().toString()) < 99) {
//											controllerInterface.mc.getDeviceOneImg().setImage(controllerInterface.mc.getLightHalf());
//											controllerInterface.mc.getDeviceSlider(ZWave.getDevice("Light3")).setValue(Integer.parseInt(val.getValue().toString()));
//										}
//										else {
//											controllerInterface.mc.getDeviceThreeImg().setImage(controllerInterface.mc.getLightOn());
//											controllerInterface.mc.getDeviceSlider(ZWave.getDevice("Light3")).setValue(Integer.parseInt(val.getValue().toString()));
//										}
//								}
//								
//							});
						}
						
					};
				};
			};
			myservice.start();
			
			
		}
		
	}

		
		
		
	
	public static void rcForward() throws ClientProtocolException, IOException {
		HttpPost postStatus = new HttpPost(ZWave.address + ZWave.port + "/ZWaveAPI/Run/devices%5B"+ Integer.toString(ZWave.getDevice("WheelChair"))  +"%5D.instances%5B0%5D.Basic.Set%28" + Integer.toString(20)+"%29");
		HttpResponse postResponse = client.execute(postStatus);
		postStatus.releaseConnection();
	
		
	}
	public static void rcReverse() throws ClientProtocolException, IOException {
		HttpPost postStatus = new HttpPost(ZWave.address + ZWave.port + "/ZWaveAPI/Run/devices%5B"+ Integer.toString(ZWave.getDevice("WheelChair"))  +"%5D.instances%5B0%5D.Basic.Set%28" + Integer.toString(40)+"%29");
		HttpResponse postResponse = client.execute(postStatus);
		postStatus.releaseConnection();
		
		
	}
	
	public static void rcStop() throws ClientProtocolException, IOException {
		HttpPost postStatus = new HttpPost(ZWave.address + ZWave.port + "/ZWaveAPI/Run/devices%5B"+ Integer.toString(ZWave.getDevice("WheelChair"))  +"%5D.instances%5B0%5D.Basic.Set%28" + Integer.toString(0)+"%29");
		HttpResponse postResponse = client.execute(postStatus);
		postStatus.releaseConnection();
		
		
	}
	public static void rcLeft() throws ClientProtocolException, IOException {
		HttpPost postStatus = new HttpPost(ZWave.address + ZWave.port + "/ZWaveAPI/Run/devices%5B"+ Integer.toString(ZWave.getDevice("WheelChair"))  +"%5D.instances%5B0%5D.Basic.Set%28" + Integer.toString(10)+"%29");
		HttpResponse postResponse = client.execute(postStatus);
		postStatus.releaseConnection();
		
		
	}
	public static void rcRight() throws ClientProtocolException, IOException {
		HttpPost postStatus = new HttpPost(ZWave.address + ZWave.port + "/ZWaveAPI/Run/devices%5B"+ Integer.toString(ZWave.getDevice("WheelChair"))  +"%5D.instances%5B0%5D.Basic.Set%28" + Integer.toString(30)+"%29");
		HttpResponse postResponse = client.execute(postStatus);
		postStatus.releaseConnection();
		
		
	}
	
	public static boolean toggleRec(int device) throws ClientProtocolException, IOException {
		
		
		boolean status;
		if(getRecStatus(device) == false) {
			status = true;
			
		}
		else {
			status = false;
		}
		
		
		HttpPost postStatus = new HttpPost(ZWave.address + ZWave.port + "/ZWaveAPI/Run/devices%5B"+Integer.toString(device) +"%5D.instances%5B0%5D.SwitchBinary.Set%28" + Boolean.toString(status)+"%29");
		HttpResponse postResponse = client.execute(postStatus);
		postStatus.releaseConnection();
			
		return status;
		
		
		
		
	}
	public static void sliderPost(int device, int brightness) throws ClientProtocolException, IOException {
	  	
	  	HttpPost postStatus = new HttpPost(ZWave.address + ZWave.port + "/ZWaveAPI/Run/devices%5B"+Integer.toString(device) +"%5D.instances%5B0%5D.SwitchMultilevel.Set%28" + Integer.toString(brightness)+"%29");
	  	HttpResponse postResponse = client.execute(postStatus);
	  	postStatus.releaseConnection();
	  	System.out.println(postResponse.toString());
		

	}
	public static int getDevice(String deviceName){
		return commandSettings.getDeviceMap().get(deviceName);
	}
	public static void addDevice(String deviceName, int deviceId) {
		commandSettings.getDeviceMap().put(deviceName, deviceId);
	}
	
	public static void setSettings(Map<String,Integer> map) {
		commandSettings.setDeviceMap(map);
		
	}
	
}
