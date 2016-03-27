package neuralnetwork;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class NeuralNetwork {
	
	Layer inputLayer;
	ArrayList<Layer> hiddenLayer;
	Layer outputLayer;
	boolean log;
	double learningRate;
	double momentum;
	int decimalPlace = 10;
	Random rand;
	double maxGrowth = 1.75;
	double growthCompare = maxGrowth/(1+maxGrowth);
	double decay = 0; //useless
	
	
	public NeuralNetwork(int inputNodes, int outputNodes, int numHiddenLayers, int numHiddenNodes, boolean log, double lr, double m, Random r) {
		// TODO Auto-generated method stub
		this.log = log;
		learningRate = lr;
		momentum = m;
		rand = r;

		
		//Creates structure
		ArrayList<Integer> hiddenNodes = new ArrayList<Integer>();
		for(int i=0; i<numHiddenLayers;i++){
			//can prompt user for each layer
			hiddenNodes.add(numHiddenNodes); //for now its 5 neurons  for every layer
		}
		
		inputLayer = new Layer(inputNodes,hiddenNodes.get(0));
		hiddenLayer = new ArrayList<Layer>();
		for(int i=0; i<numHiddenLayers;i++){
			Layer hiddenL;
			if(i == numHiddenLayers-1){
				hiddenL = new Layer(hiddenNodes.get(i),outputNodes);
			}
			else{
				hiddenL = new Layer(hiddenNodes.get(i),hiddenNodes.get(i+1));
			}
			
			hiddenLayer.add(hiddenL);
		}
		
		outputLayer = new Layer(outputNodes,0);
		//-----------------------------------------------------

	}
	
	
	public double trainOCR(ArrayList<String> data, boolean quickprop){
		
		double accuracy = 0;
		Collections.shuffle(data,rand);
		
		for(int j=0; j<data.size();j++){
			
			double[] inputLayerData = new double[inputLayer.numOfNeurons()];
			String[] retrieveData;
			OCROutput expected;
			
			retrieveData = data.get(j).split(",");
			
			for(int i=0; i<inputLayerData.length;i++){
				inputLayerData[i] = Double.parseDouble(retrieveData[i]);
			}
			expected = new OCROutput(retrieveData[retrieveData.length-1]); //The final item in the dataset will be a string letter
			
			for (int i = 0; i < expected.expectedValue.length; i++){
				System.out.print(expected.expectedValue[i] + ",");
			}
			System.out.println(expected.expectedString);
			Scanner test = new Scanner(System.in);
			test.nextLine();
			
			inputLayer.setValues(inputLayerData);
			feedForward();
			
			//System.out.println(expected.expectedString + " --> " + outputLayer.stringOCROutput());
			
			if(outputLayer.stringOCROutput().equals(expected.expectedString)){
				accuracy++;
			}
			//
			outputLayer.setExpected(expected.expectedValue);
			
			if(quickprop) quickPropagation();
			else backPropagation();
				
		}
		System.out.println(accuracy + "/" + data.size());
		
		return accuracy/data.size();
	}
	
	public double validateOCR(ArrayList<String> data){
		
		double accuracy = 0;
		Collections.shuffle(data,rand);
		
		for(int j=0; j<data.size();j++){
			double[] inputLayerData = new double[inputLayer.numOfNeurons()];
			String[] retrieveData;
			OCROutput expected;
			
			retrieveData = data.get(j).split(",");
			for(int i=0; i<inputLayerData.length;i++){
				inputLayerData[i] = Double.parseDouble(retrieveData[i]);
			}
			
			expected = new OCROutput(retrieveData[retrieveData.length-1]);
			
			inputLayer.setValues(inputLayerData);
			feedForward();
			
			if(outputLayer.stringOCROutput().equals(expected.expectedString)){
				accuracy++;
			}	
		}
		
		System.out.println(accuracy);
		System.out.println(data.size());
		testData(data);
		return accuracy/data.size();
	}
	
	public void testData(ArrayList<String> data){
		for(int j=0; j<data.size();j++){
			double[] inputLayerData = new double[inputLayer.numOfNeurons()];
			String[] retrieveData;
			OCROutput expected;
			
			retrieveData = data.get(j).split(",");
			for(int i=0; i<inputLayerData.length;i++){
				inputLayerData[i] = Double.parseDouble(retrieveData[i]);
			}
			
			expected = new OCROutput(retrieveData[retrieveData.length-1]);
			
			inputLayer.setValues(inputLayerData);
			
			feedForward();
			
			System.out.println(outputLayer.stringOCROutput() + " compared to " + expected.expectedString);
		}
	}

	
	public double trainIris(ArrayList<String> data,boolean quickprop){
				
		double accuracy = 0;
		Collections.shuffle(data,rand);
		
		for(int j=0; j<data.size();j++){
			double[] inputLayerData = new double[inputLayer.numOfNeurons()];
			String[] retrieveData;
			IrisOutput expected;
			retrieveData = data.get(j).split(",");
			for(int i=0; i<inputLayerData.length;i++){
				inputLayerData[i] = Double.parseDouble(retrieveData[i]);
			}
			
			expected = new IrisOutput(retrieveData[retrieveData.length-1]);
			
			//System.out.println("yo2");
			
			inputLayer.setValues(inputLayerData);
			//System.out.println("1");
			feedForward();
			//System.out.println("2");
			//System.out.println(outputLayer.getNeuron(0).getValue());
			/*System.out.println(inputLayer.getNeuron(0).getWeight(0));
			System.out.println(inputLayer.getNeuron(0).getGradient(0));
			System.out.println(inputLayer.getNeuron(0).getChange(0));
			*/
			
			//System.out.println(outputLayer.stringIrisOutput() + " --> " + expected.getExpectedString());
			//Scanner test = new Scanner(System.in);
			//String yo = test.nextLine();
			
			if(outputLayer.stringIrisOutput().equals(expected.getExpectedString())){
				//System.out.println(outputLayer.stringIrisOutput() + " --> " + expected.getExpectedString());
				accuracy++;
			}
			//
			outputLayer.setExpected(expected.getExpectedValue());
			//System.out.println("3");
			if(quickprop) quickPropagation();
			else backPropagation();
				
			//System.out.println(outputLayer.getNeuron(0).getError());
			//System.out.println(outputLayer.getNeuron(1).getError());
			//System.out.println(outputLayer.getNeuron(2).getError());
			//System.out.println("4");
		}
		
		//System.out.println("yo3");
		
		//System.out.println("accuracy:" + accuracy + "/" + data.size());
		//System.out.println(accuracy/data.size());
		return accuracy/data.size();
	}
	
	
	public double validateIris(ArrayList<String> data){
		
		double accuracy = 0;
		Collections.shuffle(data,rand);
		
		for(int j=0; j<data.size();j++){
			double[] inputLayerData = new double[inputLayer.numOfNeurons()];
			String[] retrieveData;
			IrisOutput expected;
			retrieveData = data.get(j).split(",");
			for(int i=0; i<inputLayerData.length;i++){
				inputLayerData[i] = Double.parseDouble(retrieveData[i]);
			}
			
			expected = new IrisOutput(retrieveData[retrieveData.length-1]);
			
			inputLayer.setValues(inputLayerData);
			feedForward();
			//System.out.println(outputLayer.stringIrisOutput() + " --> " + expected.getExpectedString());
			if(outputLayer.stringIrisOutput().equals(expected.getExpectedString())){
				//System.out.println(outputLayer.stringOutput() + " --> " + expected.getExpectedString());
				accuracy++;
			}
			//
			//outputLayer.setExpected(expected.getExpectedValue());
			//backPropagation();	
		}
		
		//System.out.println(accuracy + "/" + data.size());
		
		return accuracy/data.size();
	}
	
	

	public void feedForward(){
		Layer current;
		Layer next;
		double sum=0;
		double functionValue=0;
		
		for(int i=-1;i<hiddenLayer.size();i++){
			
			if(i==-1){
				current = inputLayer;
			}else{
				current = hiddenLayer.get(i);
			}
			
			if(i==hiddenLayer.size()-1){
				next = outputLayer;
			}else{
				next = hiddenLayer.get(i+1);
			}
				
			for(int j=0;j<next.numOfNeurons();j++){
				sum=0;
				functionValue=0;
				for(int k=0;k<current.numOfNeurons();k++){
					
					sum = sum + current.getNeuron(k).getWeight(j)*current.getNeuron(k).getValue();
				}
				
				sum = sum+current.getBias().getWeight(j)*current.getBias().getValue();
				
		
				
				next.getNeuron(j).setSummation(sum);
				functionValue = chooseFunction(sum,false);
				next.getNeuron(j).setValue(functionValue);
					
			
			}
			
		}
		
	}
	
	public void backPropagation(){
		
		setErrors();
		updateWeights();
	}
	

	public void quickPropagation(){
		
		setErrors();
		updateWeightsQP();
	}
	
	public void updateWeightsQP(){
		Layer current;
		Layer next;
		double currentWeight;
		double changeInWeight;
		double previousChangeInWeight;
		
		double gradient;
		double oldgradient;
		double newWeight;
		
		for(int i=-1;i<hiddenLayer.size();i++){
			
			if(i==-1){
				current = inputLayer;
			}else{
				current = hiddenLayer.get(i);
			}
			
			if(i==hiddenLayer.size()-1){
				next = outputLayer;
			}else{
				next = hiddenLayer.get(i+1);
			}
				
			for(int j=0;j<next.numOfNeurons();j++){
				for(int k=0;k<current.numOfNeurons();k++){
					changeInWeight=0;
					currentWeight = current.getNeuron(k).getWeight(j);
					gradient = next.getNeuron(j).getError()*chooseFunction(next.getNeuron(j).getSummation(),true)*current.getNeuron(k).getValue();
					gradient = gradient + currentWeight*decay;
					oldgradient = current.getNeuron(k).getGradient(j);
					previousChangeInWeight = current.getNeuron(k).getChange(j);
					
					if(previousChangeInWeight>0){
						if(gradient>0){
							changeInWeight = changeInWeight + gradient*learningRate;
						}
						
						if(gradient>growthCompare*oldgradient){
							changeInWeight = changeInWeight + maxGrowth*previousChangeInWeight;
						}else{
							changeInWeight = changeInWeight + (gradient/(oldgradient-gradient))*previousChangeInWeight;
						}
						
					}else if(previousChangeInWeight<0){
						
						if(gradient<0){ //found this to be very important in reducing training time
							changeInWeight = changeInWeight + gradient*learningRate;
						}
						
						if(gradient<growthCompare*oldgradient){
							changeInWeight = changeInWeight + maxGrowth*previousChangeInWeight;
						}else{
							changeInWeight = changeInWeight + (gradient/(oldgradient-gradient))*previousChangeInWeight;
						}
						
					}else{//if change is 0--first iteration
						changeInWeight = changeInWeight + gradient*learningRate;
					}
					
					newWeight = currentWeight + changeInWeight;
					
					current.getNeuron(k).setGradient(j, gradient);
					current.getNeuron(k).setChange(j, changeInWeight);
					current.getNeuron(k).setWeight(j, newWeight);
					
				}
				
				changeInWeight=0;
				currentWeight = current.getBias().getWeight(j);
				gradient = next.getNeuron(j).getError()*chooseFunction(next.getNeuron(j).getSummation(),true)*current.getBias().getValue();
				gradient = gradient + currentWeight*decay;
				oldgradient = current.getBias().getGradient(j);
				previousChangeInWeight = current.getBias().getChange(j);
				
				if(previousChangeInWeight>0){
					if(gradient>0){
						changeInWeight = changeInWeight + gradient*learningRate;
					}
					
					if(gradient>growthCompare*oldgradient){
						changeInWeight = changeInWeight + maxGrowth*previousChangeInWeight;
					}else{
						changeInWeight = changeInWeight + (gradient/(oldgradient-gradient))*previousChangeInWeight;
					}
					
				}else if(previousChangeInWeight<0){
					
					if(gradient<0){
						changeInWeight = changeInWeight + gradient*learningRate;
					}
					
					if(gradient<growthCompare*oldgradient){
						changeInWeight = changeInWeight + maxGrowth*previousChangeInWeight;
					}else{
						changeInWeight = changeInWeight + (gradient/(oldgradient-gradient))*previousChangeInWeight;
					}
					
				}else{//if change is 0--first iteration
					changeInWeight = changeInWeight + gradient*learningRate;
				}
				
				newWeight = currentWeight + changeInWeight;
				
				current.getBias().setGradient(j, gradient);
				current.getBias().setChange(j, changeInWeight);
				current.getBias().setWeight(j, newWeight);
				
							
				}
		}	
	}
	
	public void updateWeights(){
		Layer current;
		Layer next;
		double currentWeight;
		double changeInWeight;
		double changeInWeight1;
		double changeInWeight2=0;
		double newWeight;
		
		for(int i=-1;i<hiddenLayer.size();i++){
			
			if(i==-1){
				current = inputLayer;
			}else{
				current = hiddenLayer.get(i);
			}
			
			if(i==hiddenLayer.size()-1){
				next = outputLayer;
			}else{
				next = hiddenLayer.get(i+1);
			}
				
			for(int j=0;j<next.numOfNeurons();j++){
				for(int k=0;k<current.numOfNeurons();k++){
					currentWeight = current.getNeuron(k).getWeight(j);
					changeInWeight1 = next.getNeuron(j).getError()*chooseFunction(next.getNeuron(j).getSummation(),true)*current.getNeuron(k).getValue()*learningRate;
					changeInWeight2 = current.getNeuron(k).getChange(j)*momentum;
					changeInWeight = changeInWeight1 + changeInWeight2;
					current.getNeuron(k).setChange(j, changeInWeight);
					newWeight = currentWeight + changeInWeight;
					current.getNeuron(k).setWeight(j, newWeight);
				}
				
				currentWeight = current.getBias().getWeight(j);
				changeInWeight1 = next.getNeuron(j).getError()*chooseFunction(next.getNeuron(j).getSummation(),true)*current.getBias().getValue()*learningRate;
				changeInWeight2 = current.getBias().getChange(j)*momentum;
				changeInWeight = changeInWeight1 + changeInWeight2;
				current.getBias().setChange(j, changeInWeight);
				newWeight = currentWeight + changeInWeight;
				current.getBias().setWeight(j, newWeight);
				
							
				}
		}	
	}
	
	
	public void setErrors(){
		
		double sum;

		
		//get error for hidden layers
		for(int i = hiddenLayer.size()-1; i>=0;i--){
			
			for(int j=0;j<hiddenLayer.get(i).numOfNeurons();j++){
				sum=0;
				for(int k=0;k<hiddenLayer.get(i).getPointers();k++){
					if(i==hiddenLayer.size()-1){
						
						sum = sum + hiddenLayer.get(i).getNeuron(j).getWeight(k)*outputLayer.getNeuron(k).getError();
					}else{
						sum = sum + hiddenLayer.get(i).getNeuron(j).getWeight(k)*hiddenLayer.get(i+1).getNeuron(k).getError();
					}
				}
				hiddenLayer.get(i).getNeuron(j).setError(sum);
			}
		}	
	}
	
	public double chooseFunction(double x, boolean derivative){
		if(log){
			if(!derivative){
				return logFunction(x);
				
			}else{
				return logFunctionDerivative(x);
			}
			
		}
		else{
			//some other function
			if(!derivative){
				return tanhFunction(x);
				
			}else{
				return tanhFunctionDerivative(x);
			}
		}
	}
	
	public double logFunction(double x){
		double y = 1/(Math.exp(-x)+1);
		
		//if(Double.isNaN(y)) return 1;
		//else return y;
		return y;
	}
	
	public double logFunctionDerivative(double x){
		double y = Math.exp(x)/Math.pow((Math.exp(x)+1), 2);
		
		//if(Double.isNaN(y)) return 1;
		//else return y;
		return y;
	}
	
	public double tanhFunction(double x){
		return (Math.exp(x) - Math.exp(-x))/(Math.exp(x) + Math.exp(-x));
	}
	
	public static double tanhFunctionDerivative(double x){
		return (4*Math.exp(2*x))/Math.pow((Math.exp(2*x)+1), 2);
				
	}

}
