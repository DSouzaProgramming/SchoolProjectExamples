package packages;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import java.util.Random;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.*;




/** Simple example of JNA interface mapping and usage. */
public class Main extends Application
{      
	
	static HttpPost postChange;
	static Timer timer = new Timer();
    static boolean timeout = false;

    
    @Override
    public void start(Stage primaryStage) throws IOException {
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout.fxml")); 
		Pane pane = loader.load();
		Scene scene = new Scene(pane);
		
		//scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		scene.setFill(Color.LIGHTBLUE);
		
	    controllerInterface.mc = (MainController)loader.getController();
		
		controllerInterface.mc.RTG.setAnimated(false);

		DataProcessing thread = new DataProcessing();
		thread.setDaemon(true);
		thread.start();
		
		backgroundThread worker = new backgroundThread();
		worker.setDaemon(true);
		worker.start();


		primaryStage.setScene(scene);
		primaryStage.setTitle("BCI Client");
		primaryStage.setResizable(false);
		primaryStage.show();
		primaryStage.setOnCloseRequest(e -> shutdown());
		 
		
    	
    }
    public static void main(String[] args)
    {
    	

    	launch(args);
    	
    
    	
    	
    }
    public static void shutdown() {
    	System.out.println("Closed");
    	Platform.exit();
    	System.exit(0);
    }
    
    
    
    

}
