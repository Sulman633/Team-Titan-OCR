package neuralnetwork;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import preprocess.imgtest;



public class BackProp {

	static imgtest it = new imgtest();
	static HelperTrainer ht = new HelperTrainer();
	static Scanner sc = new Scanner(System.in);
	

	static double learningRate = 0.3;
	static int alphabetSize = 56;
	
	static String fileName = "NNSave.txt";
	
	// 3 layer structure of the neural network
	static ArrayList<Neuron> inputLayer = new ArrayList<Neuron>();
	static ArrayList<Neuron> hiddenLayer = new ArrayList<Neuron>();
	static ArrayList<Neuron> outputLayer = new ArrayList<Neuron>();
	
	
	/**
	 * The feed forward function of the neural network
	 * @param expected - int[] - expected values for training the neuralnetwork 
	 * @param trainNetwork - boolean - to check train the nerualnetwork
	 */
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
	    	if (trainNetwork){
	    		outputLayer.get(b).setError(expected[b]);
	    	}
    	}
    	
    	if (trainNetwork){
    		// Perform back propagation on the NN
    		backP(inputLayer,hiddenLayer,outputLayer);
    	}
    	
    }
   
    /**
     * back propagation and forward weight adjustment of the neural network
     * @param inputLayer - number of input layer nodes
     * @param hiddenLayer - number of hidden layer nodes 
     * @param outputLayer - number of output layer nodes
     */
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
    
  
    /**
     * derivative function
     * @param x - output layer weight 
     * @return - double - calculated derviate value of x
     */ 
    public static double der(double x){
    	return Math.exp(x)/Math.pow(Math.exp(x)+1,2);
    }
    
 
    /**
     * sigmoid function
     * @param x - summaztion of weight on hidden nodes and output nodes
     * @return - double - calculated values of x
     */
    public static double sigmoid(double x){
    	return 1/(1+Math.exp(-(x)));
    }
    
    /**
     * runs through each int in the array of pixel values and sets each input node to its matching pixel
     * @param inputValues - int[] - array of pixels of image 
     */
    public static void setInputNodes(int[] inputValues){
    	for (int node = 0; node < inputLayer.size(); node++){
    		inputLayer.get(node).value = inputValues[node];
    	}
    }
    
   
    /**
     * Run this method to determine the closest match to a passed in letter
     * @param input - int[] - array of pixels of image
     * @return - String - an character that closest match
     */
    public static String determineLetter(int[] input){
    	setInputNodes(input);
    	feedF(null, false);
    	
    	Neuron bestMatch = outputLayer.get(0);
    	for (int i = 0; i < alphabetSize; i++){
    		if (outputLayer.get(i).value > bestMatch.value){
    			bestMatch = outputLayer.get(i);
    		}
    	}
    	
    	return bestMatch.outputNodeRepresentation;
    }
    
 
     
    /**
     * initializes a new neural network
     */
    public static void initialization() {
    	
    	System.out.println("If you are loading in a save file, ENSURE the following settings are the same!!");
    	System.out.println("How many pixels in the image?");
    	int inputLayerSize = sc.nextInt(); // Number of Input Nodes
    	System.out.println("Number of hidden nodes?");
        int hiddenLayerSize = sc.nextInt(); // Number of Hidden Nodes
    	System.out.println("Size of Alphabet is: " + alphabetSize);
        int outputLayerSize = alphabetSize; // Number of Output Nodes
        

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
    
   
    /**
     * initializes a neural network with saved weights
     */
    //DO NOT CHANGE WITHOUT CREATING A MATCHING LOAD METHOD (Or just tell matt)
    public static void saveNeuralNetwork(){
    	System.out.println("Beginning save");
    	
    	try {
            // Assume default encoding.
            FileWriter fileWriter = new FileWriter(fileName);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            
            //get weights from input to hidden layers
        	for (int inputNode = 0; inputNode < inputLayer.size(); inputNode++){
        		for (int hiddenNode = 0; hiddenNode < hiddenLayer.size(); hiddenNode++ ){
        			bufferedWriter.write(inputLayer.get(inputNode).weight.get(hiddenNode).toString());
                    bufferedWriter.newLine();
        		}
        	}
        	
        	System.out.println();
        	//get weights from hidden to output nodes
        	for (int hiddenNode = 0; hiddenNode < hiddenLayer.size(); hiddenNode++){
        		for (int outputNode = 0; outputNode < outputLayer.size(); outputNode++ ){
        			bufferedWriter.write(hiddenLayer.get(hiddenNode).weight.get(outputNode).toString());
                    bufferedWriter.newLine();
        		}
        	}

            // Always close files.
            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println("Error writing to file '"+ fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
    	
    	System.out.println("Save Complete");
    }
    
    
    //loads in the neural network from the save file
    //DO NOT CHANGE WITHOUT CREATING A MATCHING SAVE METHOD (Or just tell matt)
    public static void loadNeuralNetwork(){
    	
    	try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

          //get weights from input to hidden layers
        	for (int inputNode = 0; inputNode < inputLayer.size(); inputNode++){
        		for (int hiddenNode = 0; hiddenNode < hiddenLayer.size(); hiddenNode++ ){
        			inputLayer.get(inputNode).weight.set(hiddenNode, Double.parseDouble(bufferedReader.readLine()));
        		}
        	}
        	
        	System.out.println();
        	//get weights from hidden to output nodes
        	for (int hiddenNode = 0; hiddenNode < hiddenLayer.size(); hiddenNode++){
        		for (int outputNode = 0; outputNode < outputLayer.size(); outputNode++ ){
        			hiddenLayer.get(hiddenNode).weight.set(outputNode, Double.parseDouble(bufferedReader.readLine()));
        		}
        	}
            
            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println("No file found: '" + fileName + "'");
        }
    	catch(IOException ex) {
    		System.out.println("An Error occured when trying to load the save file :(");
    	}
    	//TODO open save file and set weight to node
    	
    	
    	
    }
    
    /**
     * accepts a map of 56 keys (letters) and their matching NN inputs
     * @param trainingAlphabet
     */
    public static void trainMethod(Map trainingAlphabet){
        // // // ACTUAL TRAINING HAPPENS FROM HERE DOWN
    	int[] expected = new int[outputLayer.size()];
    	int[] currentInput;
    	String passedLetter;
    	
    	int epochs = 1000;
    	for (int count = 0; count < epochs; count++){
        
	    	// iterate through the map
	    	Iterator it = trainingAlphabet.entrySet().iterator();
	    	while (it.hasNext()){
	    		Map.Entry pair = (Map.Entry)it.next();
	    		currentInput = (int[]) pair.getValue();
	    		passedLetter = (String) pair.getKey();
	    		
	    		setInputNodes(currentInput);
	    		
	        	expected = ht.expectedOutputValues(passedLetter, outputLayer); // get the expected values
	        	
	        	//Running the actual program to train this instance
	        	feedF(expected, true); 
	        	
	        	//user output during training to tell us whats going on during training
	        	if (count%100 == 0){
	        		for (int nnOutputNode = 0; nnOutputNode < alphabetSize; nnOutputNode++){
	        			System.out.println("  --  " + passedLetter + "  --  Current output node: " + nnOutputNode + "\tOutput: " + outputLayer.get(nnOutputNode).value + "\t Error: "+ outputLayer.get(nnOutputNode).error );
	        		}
	        	}
	        	//System.out.println(count + " / " + epochs);
	    	}
        }
    }
    
    /**
     * Trains a neuralnetwork with single character 
     * @param letter - String - to train an neuralnetwork
     * @param representation - array of pixels of image of that representation
     */
    public static void singleTrain(String letter, int[] representation){
    	setInputNodes(representation);
    	int[] expected = new int[outputLayer.size()];
    	expected = ht.expectedOutputValues(letter, outputLayer); // get the expected values
    	
    	
    	//Running the actual program to train this instance
    	feedF(expected, true);
    	System.out.print(" Train done ");
    }
    
    /**
     * Tester 
     * @param trainingAlphabet
     */
    @SuppressWarnings("static-access")
	public static void tester(Map<String, int[]> trainingAlphabet){
    	//user testing the training!
    	System.out.println("type '1' or '2' to test images 1 or 2 (a or b)");
    	int in = sc.nextInt(); // Number of Input Nodes
    	String bestLetter = "";
    	
    	while (in == 1 || in == 2 || in == 5 || in == 6 || in == 7){
	    	if (in == 1){
	    		bestLetter = determineLetter(it.generateCluster("testCaseA.jpg"));
	    		for (int nnOutputNode = 0; nnOutputNode < alphabetSize; nnOutputNode++){
        			System.out.println("  --  Current output node: " + nnOutputNode + "\tOutput: " + outputLayer.get(nnOutputNode).value + "\t Error: "+ outputLayer.get(nnOutputNode).error );
        		}
	    		System.out.println("BEST LETTER MATCH: " + bestLetter);
	    	}
	    	else if (in == 2){
	    		bestLetter = determineLetter(it.generateCluster("testCaseB.jpg"));
	    		for (int nnOutputNode = 0; nnOutputNode < alphabetSize; nnOutputNode++){
        			System.out.println("  --  Current output node: " + nnOutputNode + "\tOutput: " + outputLayer.get(nnOutputNode).value + "\t Error: "+ outputLayer.get(nnOutputNode).error );
        		}
	    		System.out.println("BEST LETTER MATCH: " + bestLetter);
	    	}
	    	else if (in == 5){
	    		int[] aNNRepresentation = it.generateCluster("testCaseA.jpg");
	    		for (int j = 0; j < 52; j ++){
	    			System.out.println(aNNRepresentation[j]);
	    		}
	    		for (int i = 0; i < 100; i++){
	    			singleTrain("a", aNNRepresentation);
	    		}
	    	}
	    	else if (in == 6){
	    		int[] aNNRepresentation = it.generateCluster("testCaseB.jpg");
	    		for (int j = 0; j < 52; j ++){
	    			System.out.println(aNNRepresentation[j]);
	    		}
	    		for (int i = 0; i < 100; i++){
	    			singleTrain("b", aNNRepresentation);
	    		}
	    	}
	    	else if (in == 7) {
	    		System.out.println("Save");
	    		saveNeuralNetwork();
	    	}
	    	System.out.println("type '1' or '2' to test images 1 or 2 (a or b)");
	    	in = sc.nextInt(); // Number of Input Nodes
    	}
    }
    
    //  MAIN FUNCTION 
    public static void main(String[] args) {
    	boolean productionMode = true; // Are we running a pre trained neural network?
    	Map<String, int[]> trainingAlphabet = new HashMap<String, int[]>();
    	
    	if (productionMode){
    		initialization(); //initialize a plain NN
    		loadNeuralNetwork();
    	} else{
    		initialization(); //initialize a plain NN
    		trainingAlphabet = it.generateAlphabetMap();
    		trainMethod(trainingAlphabet); //begin training of the network
    	}
    	tester(trainingAlphabet);
    }
}
