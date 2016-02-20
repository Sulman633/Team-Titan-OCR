package neuralnetwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import preprocess.imgtest;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.*;

public class BackProp {

	static HelperTrainer ht = new HelperTrainer();

	static double learningRate = 0.3;
	static int alphabetSize = 56;
	
	// 3 layer structure of the neural network
	static ArrayList<Neuron> inputLayer = new ArrayList<Neuron>();
	static ArrayList<Neuron> hiddenLayer = new ArrayList<Neuron>();
	static ArrayList<Neuron> outputLayer = new ArrayList<Neuron>();
	
	//The feed forward function of the neural network
    public static void feedF(int[] expected, boolean trainNetwork){
    	double sum;
    	double hsum;
    	
    	// finds the output
    	for (int b=0; b<outputLayer.size();b++){ // for each output node
    		hsum=0;
    	
	    	for(int i=0; i<hiddenLayer.size();i++){ // for each hiddenLayer node
	    		
	    		sum=0;
	    		
	    		for(int j=0;j<inputLayer.size();j++){ // for each input node
	    			
	    			sum = sum + inputLayer.get(j).value * inputLayer.get(j).weight.get(i); // gather the sum ( value*weight )
	    		}
	    		
	    		hiddenLayer.get(i).summation = sum; // set the summation within the hidden node
	    		
	    		hiddenLayer.get(i).value = sigmoid(sum); // perform the sigmoid function on the hidden node
	    		
	    		hsum = hsum + hiddenLayer.get(i).value * hiddenLayer.get(i).weight.get(b); // set the hidden node sum to the values * the connection weights
	    	}
	    	
	    	outputLayer.get(b).summation = hsum; // set the output summation value to equal the sum of hidden nodes
	    	
	    	outputLayer.get(b).value = sigmoid(hsum); // perform sigmoid again to get output node value
	    	
	    	// sets the error value
	    	outputLayer.get(b).setError(expected[b]);
    	}
    	
    	if (trainNetwork){
    		// Perform back propagation on the NN
    		backP(inputLayer,hiddenLayer,outputLayer);
    	}
    	
    }
    
    //back propagation and forward weight adjustment of the neural network
    public static void backP(ArrayList<Neuron> inputLayer, ArrayList<Neuron> hiddenLayer, ArrayList<Neuron> outputLayer){
    	double tempWeight;
	    		
		//derives the error for the hidden layer from the output error
		int hiddenNodeError = 0;
		
    	for(int i=0; i<hiddenLayer.size();i++){ // for each hidden node
    		
    		for (int k = 0; k < outputLayer.size(); k++){
    			
    			hiddenNodeError += hiddenLayer.get(i).weight.get(k) * outputLayer.get(k).error; // calculate the error at hidden node by multiplying the output error by weight
    			
    		}
    		hiddenLayer.get(i).error = hiddenNodeError;
    	}
    	
    	//updates each input weights using the hidden layer error values
    	for(int i=0; i<hiddenLayer.size();i++){ // for each hidden node
    		
    		for(int j=0; j<inputLayer.size();j++){ // for each input node
    			
    			// DO NOT NEED?
    			// Calculate the error at input node by multiplying hidden node error by weight
    			// input.get(j).error = input.get(j).weight.get(i) * hidden.get(i).error;
    			// DO NOT NEED?
    			
    			// Set the temporary new weight to the current rate plus the calculated change 
    			// (Learning rate * hidden layer error * derivative of hidden layer sum * input value)
    			tempWeight = inputLayer.get(j).weight.get(i) + (learningRate * hiddenLayer.get(i).error * der(hiddenLayer.get(i).summation) * inputLayer.get(j).value); 
    			
    			//set the weight of the input to hidden layer connection
    			inputLayer.get(j).weight.set(i, tempWeight);
    		}
    		
    	}
    	
    	//updates the hidden layer weights using output layer error value
    	for(int i=0;i<hiddenLayer.size();i++){
    		
    		for(int k=0; k<outputLayer.size();k++){
    			
	    		//same as input layer to hidden layer weight updates, performed on hidden layer to output layer weights
	    		tempWeight = hiddenLayer.get(i).weight.get(k) + (learningRate * outputLayer.get(k).error*der(outputLayer.get(k).summation)*hiddenLayer.get(i).value);
	    		
	    		hiddenLayer.get(i).weight.set(k, tempWeight);
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
    public static void setInputNodes(int[] inputValues, ArrayList<Neuron> inputLayer){
    	for (int node = 0; node < inputLayer.size(); node++){
    		inputLayer.get(node).value = inputValues[node];
    	}
    }
    
    //Run this method to determine the closest match to a passed in letter
    public String determineLetter(ArrayList<Neuron> inputLayer, ArrayList<Neuron> hiddenLayer, ArrayList<Neuron> outputLayer, int[] expected){
    	feedF(expected, false); 
    	Neuron bestMatch = outputLayer.get(0);
    	for (int i = 0; i < alphabetSize; i++){
    		if (outputLayer.get(i).value > bestMatch.value){
    			bestMatch = outputLayer.get(i);
    		}
    	}
    	
    	return bestMatch.outputNodeRepresentation;
    }
    
 
    // initializes a new neural network
    public static void initialization() {
    	
    	System.out.println("How many pixels in the image?");
    	Scanner sc = new Scanner(System.in);
    	int inputLayerSize = sc.nextInt(); // Number of Input Nodes
    	System.out.println("Number of hidden nodes?");
        int hiddenLayerSize = sc.nextInt(); // Number of Hidden Nodes
    	System.out.println("Size of Alphabet is: " + alphabetSize);
        int outputLayerSize = alphabetSize; // Number of Output Nodes
        
        sc.close(); // close the scanner to minimize memory leak

        // initializes the 3 layers of neurons with random weights
        Neuron tempNeuron;
        
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
        
        // set output layer representations (ie first output node represents an "a")
        outputLayer = ht.setOutputNodeRepresentations(outputLayer);

    }
    
    
    // initializes a neural network with saved weights
    public static void loadNeuralNetwork(){

    	
    }
    
    
    //accepts a map of 56 keys (letters) and their matching NN inputs
    public static void trainMethod(Map trainingAlphabet){
        // // // ACTUAL TRAINING HAPPENS FROM HERE DOWN
    	int[] expected = new int[outputLayer.size()];
    	int[] currentInput;
    	String passedLetter;
    	
    	 for (int count = 0; count < 1000; count++){
        
	    	// iterate through the map
	    	Iterator it = trainingAlphabet.entrySet().iterator();
	    	while (it.hasNext()){
	    		Map.Entry pair = (Map.Entry)it.next();
	    		currentInput = (int[]) pair.getValue();
	    		passedLetter = (String) pair.getKey();
	    		
	    		setInputNodes(currentInput, inputLayer);
	    		
	        	expected = ht.expectedOutputValues(passedLetter, outputLayer); // get the expected values
	        	
	        	//Running the actual program to train this instance
	        	feedF(expected, true); 
	        	
	        	//user output during training to tell us whats going on during training
	        	if (count%100 == 0){
	        		for (int nnOutputNode = 0; nnOutputNode < alphabetSize; nnOutputNode++){
	        			System.out.println("Current output node: " + nnOutputNode + "\tOutput: " + outputLayer.get(nnOutputNode).value + "\t Error: "+ outputLayer.get(nnOutputNode).error );
	        		}
	        	}
	    	}
        }
    }
    
    //  MAIN FUNCTION
    public static void main(String[] args) {
    	boolean productionMode = false; // Are we running a pre trained neural network?
    	Map<String, int[]> trainingAlphabet = new HashMap<String, int[]>();
    	
    	if (productionMode){
    		loadNeuralNetwork();
    	} else{
    		initialization(); //initialize a plain NN
    		imgtest it = new imgtest();
    		trainMethod(trainingAlphabet); //begin training of the network
    	}
    }
}
