package packages;


import java.io.IOException;
import java.util.Random;

import org.apache.http.client.ClientProtocolException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.TabPane;

public class Test extends Thread {
	

	public int curr;
	public XYChart.Series<String,Number> myseries;
	
	public Test() {
		
		myseries = new XYChart.Series<>();
		curr = 1;
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void run() {
		//new MainController().AddPoint(0.9, 10);
		controllerInterface.mc.RTG.getData().add(myseries);

		while(true) {
			String temp = controllerInterface.mc.getTabText();
			//System.out.println(temp);
			if(temp.equals("Graph")) {
				
						
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
						Platform.runLater(()-> {
							if(this.myseries.getData().toArray().length >= 10) {
								this.myseries.getData().remove(0); 


							}	
							this.myseries.getData().add(new XYChart.Data<String,Number>(Integer.toString(curr),(new Random().nextDouble())));

							
							
							curr++;		

						
						
						
						});
							
							
							
							
							
						
						
									
			
				
				
				
				
			}
		}
		
		
			
		
		
	}
	

}
