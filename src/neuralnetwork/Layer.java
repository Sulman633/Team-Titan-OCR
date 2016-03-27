package neuralnetwork;

import java.util.ArrayList;

public class Layer {
	
	private ArrayList<Neuron> inputNodes;
	private Neuron bias;
	
	public Layer(int nodes, int pointers){
		
		inputNodes = new ArrayList<Neuron>();
		Neuron toAdd;
		
		for(int i=0; i<nodes; i++){
			toAdd = new Neuron(pointers);
			inputNodes.add(toAdd);
		}
		
		bias = new Neuron(pointers);
		bias.setValue(1);
		
	}
	
	public Neuron getBias(){
		return bias;
	}
	
	public int numOfNeurons(){
		return inputNodes.size();
	}
	
	public int getPointers(){
		return inputNodes.get(0).getPointers();
	}
	
	public Neuron getNeuron(int x){
		return inputNodes.get(x);
	}
	
	public void setValues(double[] x){
		for(int i=0; i<numOfNeurons();i++){
			inputNodes.get(i).setValue(x[i]);
		}
		
	}
	
	public String stringOCROutput(){
		double[] outputValue = new double[numOfNeurons()];
		
		//return best output
		Neuron best = inputNodes.get(0);
		for (int i = 0; i < outputValue.length; i++){
			if (inputNodes.get(i).getValue() > best.getValue()){
				best = inputNodes.get(i);
			}
		}
		return best.letterRepresentation;
	}
	
	public String stringIrisOutput(){
		double[] outputValue = new double[numOfNeurons()];
		
		for(int i=0; i<outputValue.length;i++){
			double v = inputNodes.get(i).getValue();
			
			
			if(v>=.50) outputValue[i] = 1;
			else outputValue[i]=0;
		}
		
		return IrisOutput.getPlant(outputValue);
	}
	
	public void setExpected(double[] x){
		for(int i=0;i<numOfNeurons();i++){
			inputNodes.get(i).setExpected(x[i]);
		}
	}

}
