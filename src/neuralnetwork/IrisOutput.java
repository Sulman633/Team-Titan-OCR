package neuralnetwork;

public class IrisOutput {
	double[] expectedValue = null;
	String expectedString;
	static String a = "Iris-setosa";
	static String b = "Iris-versicolor";
	static String c = "Iris-virginica";
	
	public IrisOutput(String x){
		if(x.equals("Iris-setosa")) {
			double[] eV={0,0,1};
			expectedValue = eV;
			expectedString = a;
		}else if(x.equals("Iris-versicolor")) {
			double[] eV={0,1,0};
			expectedValue = eV;
			expectedString = b;
		}else if(x.equals("Iris-virginica")) {
			double[] eV={1,0,0};
			expectedValue = eV;
			expectedString = c;
		}
	}
	
	public double[] getExpectedValue(){
		return expectedValue;
	}
	
	public String getExpectedString(){
		return expectedString;
	}
	
	public static String getPlant(double[] x){
		
		if(x[0]==0 && x[1]==0 && x[2]==1){
			return a;
		}
		
		if(x[0]==0 && x[1]==1 && x[2]==0){
			return b;
		}
		
		if(x[0]==1 && x[1]==0 && x[2]==0){
			return c;
		}
		
		return "Unknown";
		
		
	}
	

}
