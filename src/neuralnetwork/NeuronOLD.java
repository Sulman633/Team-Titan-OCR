package neuralnetwork;

import java.util.ArrayList;
import java.util.Random;


public class NeuronOLD {
    public Double value = 0.0;
    double error=0;
    double summation;
    
    String outputNodeRepresentation; // The letter the neuron represents (in the case of being an output node)
    
    Random rand = new Random();
    ArrayList<Double> weight = new ArrayList<>();
    
    public NeuronOLD(){
    	
    }

    
    public NeuronOLD(int size){        
    	double w=0;
    	    
        for (int i=0; i<size;i++){
        	w=0;
        	while(w==0){
        	w = (rand.nextDouble())-0.5; //sets initial weights from -1 to +1
        	}
            weight.add(w);            
        }

    }
    
    public void setError(double expected){
    	error = expected - value;
    	
    }
    
    
}
