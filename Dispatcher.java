package packages;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.sun.jna.Pointer;

import packages.EmoState.EE_CognitivAction_t;

public class Dispatcher {
	
	public static boolean selectionNetwork;
	public static boolean targetCommands;
	public static boolean selectionSuite;
	public enum States {
		motorForward (20),
		motorLeft (10),
		motorRight (30),
		motorReverse(40);
		
		private int bit;
		States(int bitnumber){
			bit = bitnumber;
		}
		int ToInt() {
			return bit;
		}
	}
	
	
	
	public static void call(Pointer eState) throws ClientProtocolException, IOException {
		if(EmoState.INSTANCE.ES_ExpressivGetClenchExtent(eState) >0.5) {
			targetCommands = false;
		}
		else {
			targetCommands = true;
		}
		if(selectionSuite) { // pick passed on the current targeted application suite
			callCognitive(eState);
		}
		else
			callExpressive(eState);

		
	}
	
	public static void callExpressive(Pointer eState) throws ClientProtocolException, IOException {
		float currPower = EmoState.INSTANCE.ES_CognitivGetCurrentActionPower(eState);
		
		if(selectionNetwork) {
			
			//Check for pushing action at a power over 0.5 and timeout false
			if ((EmoState.INSTANCE.ES_ExpressivIsLeftWink(eState)==1)  && (LightTimer.timedout == true)) {
				LightTimer.initTimer();
				ZWave.scenePost(controllerInterface.mc.commandSettings.getCommandOne(),controllerInterface.mc.commandSettings.getCommandOneState());
			}
			else if((EmoState.INSTANCE.ES_ExpressivGetClenchExtent(eState)  > 0.5)  && (LightTimer.timedout == true)) {
				LightTimer.initTimer();
				ZWave.scenePost(controllerInterface.mc.commandSettings.getCommandTwo(),controllerInterface.mc.commandSettings.getCommandTwoState());
			}
			else if((EmoState.INSTANCE.ES_ExpressivIsLookingLeft(eState)==1)  && (LightTimer.timedout == true)) {
				LightTimer.initTimer();
				ZWave.scenePost(controllerInterface.mc.commandSettings.getCommandThree(),controllerInterface.mc.commandSettings.getCommandThreeState());
			}
			
			else if(EmoState.INSTANCE.ES_ExpressivIsLookingRight(eState)==1 &&(LightTimer.timedout==true)) {
				LightTimer.initTimer();
				ZWave.toggleRec(ZWave.getDevice("Rec"));
				
			
			}
			
			
		}
		else {
			
			if ((EmoState.INSTANCE.ES_ExpressivIsLeftWink(eState) == 1)  && (LightTimer.timedout == true)) {
				LightTimer.initTimer();
				ZWave.post(ZWave.getDevice("WheelChair"),States.motorForward.ToInt());
			}
			else if((EmoState.INSTANCE.ES_ExpressivGetClenchExtent(eState)  > 0.5) && (LightTimer.timedout == true)) {
				LightTimer.initTimer();
				ZWave.post(ZWave.getDevice("WheelChair"), States.motorReverse.ToInt());
			}
			else if(EmoState.INSTANCE.ES_ExpressivIsLookingLeft(eState)==1  && (LightTimer.timedout == true)) {
				LightTimer.initTimer();
				ZWave.post(ZWave.getDevice("WheelChair"),States.motorLeft.ToInt());
			}
			
			else if(EmoState.INSTANCE.ES_ExpressivIsLookingRight(eState)==1 &&(LightTimer.timedout==true)) {
				LightTimer.initTimer();
				ZWave.post(ZWave.getDevice("WheelChair"), States.motorRight.ToInt());
			
			}
			
			
			
			
		}
		
	}
	
	public static void callCognitive(Pointer eState) throws ClientProtocolException, IOException {
		
		
		float currPower = EmoState.INSTANCE.ES_CognitivGetCurrentActionPower(eState);
		
		if(selectionNetwork) {
			System.out.println(EmoState.INSTANCE.ES_CognitivGetCurrentAction(eState));
			//Check for pushing action at a power over 0.5 and timeout false
			if ((EmoState.INSTANCE.ES_CognitivGetCurrentAction(eState) == EE_CognitivAction_t.COG_PUSH.ToInt() && targetCommands) && (EmoState.INSTANCE.ES_CognitivGetCurrentActionPower(eState) > 0.5) && (LightTimer.timedout == true)) {
				LightTimer.initTimer();
				ZWave.scenePost(controllerInterface.mc.commandSettings.getCommandOne(),controllerInterface.mc.commandSettings.getCommandOneState());
			}
			else if((EmoState.INSTANCE.ES_CognitivGetCurrentAction(eState) == EE_CognitivAction_t.COG_PULL.ToInt() && targetCommands) && (EmoState.INSTANCE.ES_CognitivGetCurrentActionPower(eState) > 0.5) && (LightTimer.timedout == true)) {
				LightTimer.initTimer();
				ZWave.scenePost(controllerInterface.mc.commandSettings.getCommandTwo(),controllerInterface.mc.commandSettings.getCommandTwoState());
			}
			else if((EmoState.INSTANCE.ES_CognitivGetCurrentAction(eState) == EE_CognitivAction_t.COG_PUSH.ToInt() && !targetCommands) && (EmoState.INSTANCE.ES_CognitivGetCurrentActionPower(eState) > 0.5) && (LightTimer.timedout == true)) {
				LightTimer.initTimer();
				ZWave.scenePost(controllerInterface.mc.commandSettings.getCommandThree(),controllerInterface.mc.commandSettings.getCommandThreeState());
			}
			
			else if((EmoState.INSTANCE.ES_CognitivGetCurrentAction(eState)==EE_CognitivAction_t.COG_PULL.ToInt() && !targetCommands) && (EmoState.INSTANCE.ES_CognitivGetCurrentAction(eState)>0.5)&&(LightTimer.timedout==true)) {
				LightTimer.initTimer();
				ZWave.toggleRec(ZWave.getDevice("Rec"));
			
			}
			
			
		}
		else {
			
			if ((EmoState.INSTANCE.ES_CognitivGetCurrentAction(eState) == EE_CognitivAction_t.COG_PUSH.ToInt() && targetCommands) && (EmoState.INSTANCE.ES_CognitivGetCurrentActionPower(eState) > 0.5) && (LightTimer.timedout == true)) {
				LightTimer.initTimer();
				ZWave.post(ZWave.getDevice("WheelChair"),States.motorForward.ToInt());
			}
			else if((EmoState.INSTANCE.ES_CognitivGetCurrentAction(eState) == EE_CognitivAction_t.COG_PULL.ToInt() && targetCommands) && (EmoState.INSTANCE.ES_CognitivGetCurrentActionPower(eState) > 0.5) && (LightTimer.timedout == true)) {
				LightTimer.initTimer();
				ZWave.post(ZWave.getDevice("WheelChair"), States.motorReverse.ToInt());
			}
			else if((EmoState.INSTANCE.ES_CognitivGetCurrentAction(eState) == EE_CognitivAction_t.COG_PUSH.ToInt() && !targetCommands) && (EmoState.INSTANCE.ES_CognitivGetCurrentActionPower(eState) > 0.5) && (LightTimer.timedout == true)) {
				LightTimer.initTimer();
				ZWave.post(ZWave.getDevice("WheelChair"),States.motorLeft.ToInt());
			}
			
			else if((EmoState.INSTANCE.ES_CognitivGetCurrentAction(eState)==EE_CognitivAction_t.COG_RIGHT.ToInt() && !targetCommands) && (EmoState.INSTANCE.ES_CognitivGetCurrentAction(eState)>0.5)&&(LightTimer.timedout==true)) {
				LightTimer.initTimer();
				ZWave.post(ZWave.getDevice("WheelChair"), States.motorRight.ToInt());
			
			}
			
			
			
			
		}
		
	}
	public static void checkNetwork() {
		
			
			selectionNetwork = controllerInterface.mc.getHome().isSelected();
			selectionSuite = controllerInterface.mc.getCognitive().isSelected();
	}
		
	

}


