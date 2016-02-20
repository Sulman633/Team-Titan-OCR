package neuralnetwork;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.*;

public class BackProp {
	static double learningRate = 0.3;
	
    public static void feedF(ArrayList<Neuron> input, ArrayList<Neuron> hidden, ArrayList<Neuron> output, int[] expected){
    	double sum;
    	double hsum;
    	
    	// finds the output
    	for (int b=0; b<output.size();b++){ // for each output node
    		hsum=0;
    	
	    	for(int i=0; i<hidden.size();i++){ // for each hidden node
	    		
	    		sum=0;
	    		
	    		for(int j=0;j<input.size();j++){ // for each input node
	    			
	    			sum = sum + input.get(j).value * input.get(j).weight.get(i); // gather the sum ( value*weight )
	    		}
	    		
	    		hidden.get(i).summation = sum; // set the summation within the hidden node
	    		
	    		hidden.get(i).value = sigmoid(sum); // perform the sigmoid function on the hidden node
	    		
	    		hsum = hsum + hidden.get(i).value * hidden.get(i).weight.get(b); // set the hidden node sum to the values * the connection weights
	    	}
	    	
	    	output.get(b).summation = hsum; // set the output summation value to equal the sum of hidden nodes
	    	
	    	output.get(b).value = sigmoid(hsum); // perform sigmoid again to get output node value
	    	
	    	// sets the error value
	    	output.get(b).setError(expected[b]);
    	}
    	
    	
    	// Perform back propagation on the NN
    	backP(input,hidden,output);
    	
    }
    
    public static void backP(ArrayList<Neuron> input, ArrayList<Neuron> hidden, ArrayList<Neuron> output){
    	double tempWeight;
	    		
		//derives the error for the hidden layer from the output error
		int hiddenNodeError = 0;
		
    	for(int i=0; i<hidden.size();i++){ // for each hidden node
    		
    		for (int k = 0; k < output.size(); k++){
    			
    			hiddenNodeError += hidden.get(i).weight.get(k) * output.get(k).error; // calculate the error at hidden node by multiplying the output error by weight
    			
    		}
    		hidden.get(i).error = hiddenNodeError;
    	}
    	
    	//updates each input weights using the hidden layer error values
    	for(int i=0; i<hidden.size();i++){ // for each hidden node
    		
    		for(int j=0; j<input.size();j++){ // for each input node
    			
    			// DO NOT NEED?
    			// Calculate the error at input node by multiplying hidden node error by weight
    			// input.get(j).error = input.get(j).weight.get(i) * hidden.get(i).error;
    			// DO NOT NEED?
    			
    			// Set the temporary new weight to the current rate plus the calculated change 
    			// (Learning rate * hidden layer error * derivative of hidden layer sum * input value)
    			tempWeight = input.get(j).weight.get(i) + (learningRate * hidden.get(i).error * der(hidden.get(i).summation) * input.get(j).value); 
    			
    			//set the weight of the input to hidden layer connection
    			input.get(j).weight.set(i, tempWeight);
    		}
    		
    	}
    	
    	//updates the hidden layer weights using output layer error value
    	for(int i=0;i<hidden.size();i++){
    		
    		for(int k=0; k<output.size();k++){
    			
	    		//same as input layer to hidden layer weight updates, performed on hidden layer to output layer weights
	    		tempWeight = hidden.get(i).weight.get(k) + (learningRate * output.get(k).error*der(output.get(k).summation)*hidden.get(i).value);
	    		
	    		hidden.get(i).weight.set(k, tempWeight);
    		}
    	}
	    	
    	
    }
    
    // derivative function
    public static double der(double x){
    	return Math.exp(x)/Math.pow(Math.exp(x)+1,2);
    }
    
    // sigmoid function
    public static double sigmoid(double x){
    	return 1/(1+Math.exp(-(x)));
    }
    
    //runs through each int in the array of pixel values and sets each input node to its matching pixel
    public static void setInputNodes(int[] input, ArrayList<Neuron> inputLayer){
    	for (int node = 0; node < inputLayer.size(); node++){
    		inputLayer.get(node).value = input[node];
    	}
    }
    
 
    //MAIN FUNCTION
    public static void main(String[] args) {
    	HelperTrainer ht = new HelperTrainer();
    	Random rand = new Random();
        int alphabetSize = 52;
        
    	System.out.println("How many pixels in the image?");
    	Scanner sc = new Scanner(System.in);
    	int inputLayerSize = sc.nextInt(); // Number of Input Nodes
    	System.out.println("Number of hidden nodes?");
        int hiddenLayerSize = sc.nextInt(); // Number of Hidden Nodes
    	System.out.println("Size of Alphabet is: " + alphabetSize);
        int outputLayerSize = alphabetSize; // Number of Output Nodes
        
        sc.close(); // close the scanner to minimize memory leaks

        Neuron tempNeuron;
        
        // sets up structure
        
        ArrayList<Neuron> inputLayer = new ArrayList<Neuron>();
        ArrayList<Neuron> hiddenLayer = new ArrayList<Neuron>();
        ArrayList<Neuron> outputLayer = new ArrayList<Neuron>();

        // initializes the 3 layers of neurons with random weights
        
        for (int i=0; i<inputLayerSize; i++){
            tempNeuron = new Neuron(hiddenLayerSize);
            inputLayer.add(tempNeuron);
        }
        
        for(int i=0;i<hiddenLayerSize;i++){
        	tempNeuron = new Neuron(outputLayerSize);
        	hiddenLayer.add(tempNeuron);
        }
        
        for(int i=0;i<outputLayerSize;i++){
        	tempNeuron = new Neuron();
        	outputLayer.add(tempNeuron);
        }
        
        // set output layer representations
        outputLayer = ht.setOutputNodeRepresentations(outputLayer);
                
        
        int[] expected = new int[outputLayer.size()]; //variable for expected value and also used as the biased value = 1
        
        	
    	//WE WILL USE THIS FOR NOW TO USE THE BASE CASE OF A LETTER A PASSED INTO OUR SYSTEM
    	int[] currentInput = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,0,1,1,1,0,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,1,1,0,0,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,0,0,1,1,1,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,1,0,0,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,1,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}; // must change this from a hard coded value
    	String passedLetter = "a"; // Must CHANGE from a hard coded value
    	
    	setInputNodes(currentInput, inputLayer);
    	
    	expected = ht.expectedOutputValues(passedLetter, outputLayer);
        	
        //epoch
        for (int count = 0; count < 1000; count++){
        	//Running the actual program
        	feedF(inputLayer,hiddenLayer,outputLayer,expected);//neuro network algorithm(feed forward and back propagation)
        	
        	//user output during training
        	System.out.println("Input: "+currentInput + "\t Output: " + outputLayer.get(1).value + "\t Error: "+ outputLayer.get(1).error );
        	
        }
        
        
        
//        System.out.println("-----------TRAINING COMPLETED SUCCESFULLY---------------");
//        System.out.println("-----------TRAINING DATA INFORMATION---------------");
//        System.out.println("Checking for odd parity");
//        System.out.println("Input Nodes: " + inputLayerSize);
//        System.out.println("Hidden Nodes: " + hiddenLayerSize);
//        System.out.println("Output Nodes: " + outputLayerSize);
//        System.out.println("learningRateing Rate: " + learningRate);
//        System.out.println("Epochs: 10000");
//        System.out.println("Bias: 1");
//        System.out.println("Range of Weights: -1 to +1");
//        System.out.println();
//        System.out.println("SYSTEM IS NOW READY FOR USE");
        
    }
}