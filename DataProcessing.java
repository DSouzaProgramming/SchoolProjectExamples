package packages;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.chart.XYChart;

public class DataProcessing extends Thread {
	public XYChart.Series<String,Number> myseries;
	
	public DataProcessing() {  // constructor for instance of thread
		this.myseries = new XYChart.Series<>();
	}
	
	public void run()
	{
		controllerInterface.mc.RTG.getData().add(this.myseries); // add graphing series
		System.out.println("Running");
		try {
			CSVLogger.create();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	ZWave.create();
    	try {
			ZWave.Authenticate();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
   	
    	Pointer eEvent			= Edk.INSTANCE.EE_EmoEngineEventCreate();
    	Pointer eState			= Edk.INSTANCE.EE_EmoStateCreate();
    	IntByReference userID 	= null;
    	short composerPort		= 1726;
    	short enginePort		= 3008;
    	int option 				= 1;
    	int state  				= 0;
    	
    	
    	userID = new IntByReference(0);
    	
    	switch (option) {
		case 1:
		{
			//Connect to EmoEngine
			if (Edk.INSTANCE.EE_EngineRemoteConnect("127.0.0.1", enginePort, "Emotiv Systems-5") != EdkErrorCode.EDK_OK.ToInt()) {
				System.out.println("Emotiv Engine start up failed.");
				Platform.runLater(()->{
					controllerInterface.mc.getEmotivStatus().setText("Not Connected");
					
				});
				return;
			}
			System.out.println("Connected to EmoEngine on [127.0.0.1]");
			Platform.runLater(()->{
				controllerInterface.mc.getEmotivStatus().setText("Connected");
				
			});
			
			break;
		}
		case 2:
		{
			//Connect to EmoComposer
			System.out.println("Target IP of EmoComposer: [127.0.0.1] ");

			if (Edk.INSTANCE.EE_EngineRemoteConnect("127.0.0.1", composerPort, "Emotiv Systems-5") != EdkErrorCode.EDK_OK.ToInt()) {
				System.out.println("Cannot connect to EmoComposer on [127.0.0.1]");
				return;
			}
			System.out.println("Connected to EmoComposer on [127.0.0.1]");
			break;
		}
		default:
			System.out.println("Invalid option...");
			return;
    	}
    	
		while (true) 
		{
			state = Edk.INSTANCE.EE_EngineGetNextEvent(eEvent);
			
			// New event needs to be handled
			if (state == EdkErrorCode.EDK_OK.ToInt()) {
				
				int eventType = Edk.INSTANCE.EE_EmoEngineEventGetType(eEvent);
				Edk.INSTANCE.EE_EmoEngineEventGetUserId(eEvent, userID);

				// Log the EmoState if it has been updated
				if (eventType == Edk.EE_Event_t.EE_EmoStateUpdated.ToInt()) {

					Edk.INSTANCE.EE_EmoEngineEventGetEmoState(eEvent, eState);
					float timestamp = EmoState.INSTANCE.ES_GetTimeFromStart(eState);
					System.out.println(timestamp + " : New EmoState from user " + userID.getValue());
					
					System.out.print("WirelessSignalStatus: ");
					System.out.println(EmoState.INSTANCE.ES_GetWirelessSignalStatus(eState));
					Platform.runLater(()->{
					IntByReference battery = new IntByReference();
					IntByReference maxCharge = new IntByReference();
						
					EmoState.INSTANCE.ES_GetBatteryChargeLevel(eState, battery,maxCharge);
				
					Double batteryDbl = (double)battery.getValue();
					Double maxChargeDbl =(double)battery.getValue();
					Double batteryPercent = batteryDbl/4.00;
					controllerInterface.mc.getBatteryProgress().setProgress(batteryPercent);
					
					controllerInterface.mc.getSignalProgress().setProgress(EmoState.INSTANCE.ES_GetWirelessSignalStatus(eState)/2.00);
						
					});
					try {
						CSVLogger.log(EmoState.INSTANCE.ES_GetTimeFromStart(eState),Integer.toString(EmoState.INSTANCE.ES_CognitivGetCurrentAction(eState)),EmoState.INSTANCE.ES_CognitivGetCurrentActionPower(eState),EmoState.INSTANCE.ES_GetWirelessSignalStatus(eState));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//Logging Cognitive Actions 
					System.out.print("CognitivGetCurrentAction: ");
					System.out.println(EmoState.INSTANCE.ES_CognitivGetCurrentAction(eState));
					System.out.print("CurrentActionPower: ");
					System.out.println(EmoState.INSTANCE.ES_CognitivGetCurrentActionPower(eState));
					String timestampstr = String.format("%.2f",timestamp);
					
					Platform.runLater(()->{  // plots in application thread.
						if(this.myseries.getData().toArray().length >= 10) 
							this.myseries.getData().remove(0); 
						else
							this.myseries.getData().add(new XYChart.Data<String,Number>(timestampstr,EmoState.INSTANCE.ES_CognitivGetCurrentActionPower(eState)));
						
					});
					
					Dispatcher.checkNetwork();
					try {
						Dispatcher.call(eState);
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			}
			else if (state != EdkErrorCode.EDK_NO_EVENT.ToInt()) {
				System.out.println("Internal error in Emotiv Engine!");
				break;
			}
		}
    	
    	Edk.INSTANCE.EE_EngineDisconnect();
    	System.out.println("Disconnected!");
    }
}

	
