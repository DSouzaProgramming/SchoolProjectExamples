package packages;

import java.util.HashMap;

public enum MentalMode {
	
	NEUTRAL		(1),
	
	PUSH 		(2),
	
	PULL		(4),
	
	DROP		(16);
	
	
	private int bit;
	MentalMode(int bitNumber)
	{
		bit = bitNumber;
	}
	public int ToInt()
	{
		return(bit);
	}
	public static String getname(int sel) {
		switch(sel) {
			case 1:
				return "NEUTRAL";
			case 2:
				return "PUSH";
			case 4:
				return "PULL";
			case 16:
				return "DROP";
			default:
				return "NULL";
		}
	}
}
