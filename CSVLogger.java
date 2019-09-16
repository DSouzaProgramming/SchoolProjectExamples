package packages;

import java.io.FileWriter;
import java.io.IOException;




public class CSVLogger {
	static public FileWriter fw;
	

	
	public static void log(double d, String mentalAction, double actionPower,int wireless) throws IOException {
		fw.write(CSVLogger.delim(d, mentalAction, actionPower, wireless));
		
	}
	
	public static void create() throws IOException {
		CSVLogger.fw = new FileWriter("data.csv");
	}
	public static String delim(double time, String mentalAction,double actionPower, int wireless){
		StringBuilder sb = new StringBuilder();
		sb.append(String.join(",", Double.toString(time),mentalAction,Double.toString(actionPower),Integer.toString(wireless),"\n"));
		return sb.toString();
	}
		
		
		
	
	public static void setHeaders() {
		
	}
	
	public static void close() throws IOException {
		CSVLogger.fw.close();
	}
	
	
	

}
