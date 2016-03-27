package neuralnetwork;

import java.util.ArrayList;

public class OCROutput {
	double[] expectedValue = null;
	String expectedString;
	
	public OCROutput(String x){

		String[] array = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z", ",",".","?","!"};
		double[] ev = new double[array.length];
		System.out.println(array.length);
		for(int i=0; i<56; i++){
			if(x.equals(array[i])){
				ev[i] = 1;
				expectedValue = ev;
				break;
			}
		}
	
		for(int i=0; i<expectedValue.length; i++){
			System.out.print(expectedValue[i] + ",");
		}
	}
}
