package packages;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Font;

public class AddDeviceController implements Initializable
{
	
	@FXML
	private Button saveDevice;
	
	@FXML
	
	private ListView<String> devices;
	
	@FXML
	public TextField ID1;
	
	@FXML
	public TextField ID2;
	
	@FXML
	public TextField ID3;
	
	@FXML
	public TextField ID4;
	
	@FXML
	public TextField ID5;
	
	public Scenes deviceManager = ZWave.commandSettings;
	
	private StringBuilder deviceList;
	
	Alert success = new Alert(AlertType.CONFIRMATION);

	Alert listWindow = new Alert(AlertType.INFORMATION);

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		this.saveDevice.setFont(new Font("Calibri", 12));
		success.setTitle("Device Managed");
		success.setHeaderText("Success!");
		success.setContentText("Device IDs set");
		
		try
		{
			ID1.setText(Integer.toString(ZWave.getDevice("Light1")));
			ID2.setText(Integer.toString(ZWave.getDevice("Light2")));
			ID3.setText(Integer.toString(ZWave.getDevice("Light3")));
			ID4.setText(Integer.toString(ZWave.getDevice("Rec")));
			ID5.setText(Integer.toString(ZWave.getDevice("WheelChair")));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	@FXML
	public void addDevice()
	{
		deviceManager.setDeviceMap(new HashMap<String,Integer>());
		deviceManager.addDevice("Light1",Integer.parseInt(this.ID1.getText()));
		deviceManager.addDevice("Light2",Integer.parseInt(this.ID2.getText()));
		deviceManager.addDevice("Light3",Integer.parseInt(this.ID3.getText()));
		deviceManager.addDevice("Rec",Integer.parseInt(this.ID4.getText()));
		deviceManager.addDevice("WheelChair",Integer.parseInt(this.ID5.getText()));
		ZWave.setSettings(this.deviceManager.getDeviceMap());
		success.showAndWait();
		
	}
	
	
}
