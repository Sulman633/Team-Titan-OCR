package neuralnetwork;

public class OCROutput {
	double[] expectedValue = null;
	String expectedString;
	
	public OCROutput(String x){
		if (x.equals("a")){
			expectedString = "a";
			double[] ev = {0.0,0.*0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,
					1.0,0.0,0.0,0.*0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
			expectedValue = ev;
		}
		
		else if (x.equals("b")){
			
		}
	}
	
	

}
