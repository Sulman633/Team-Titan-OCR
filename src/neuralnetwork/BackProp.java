package neuralnetwork;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.util.*;

import ppDan.Preprocess;
import test.ImgProcessMatt;
import test.PatternDetector;


public class BackProp {

	static ImgProcessMatt it = new ImgProcessMatt();
	static HelperTrainer ht = new HelperTrainer();
	static Shuffler sh = new Shuffler();
	static Scanner sc = new Scanner(System.in);
	static String fileName = "NNSave.txt"; // Name of save file for NN
	
	//PARAMETERS TO SET
	static boolean productionMode = false; // Are we running a pre trained neural network?
	static boolean tonyTrain = false; // Do we train using Tony's character Recognition?
	static boolean danTrain = true;
	static int imgSize = 10;
	static int epochs = 400; // Number of epochs while learning
	static double learningRate = 0.15;
	static int alphabetSize = 56; // Size of the alphabet (Number of output nodes) (Do not change)
	static int returnedValues = 2; // Number of results to return for each letter checked
	
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
    		inputLayer.get(node).value = (double) inputValues[node];
    	}
    }
    
   
    /**
     * Run this method to determine the closest match to a passed in letter
     * @param input - int[] - array of pixels of image
     * @return - String - an character that closest match
     */
    public static Neuron[] determineLetter(int[] input){
    	setInputNodes(input);
    	feedF(null, false);
    	
    	//Returns the best single element
    	/*
    	Neuron bestMatch = outputLayer.get(0);
    	for (int i = 0; i < alphabetSize; i++){
    		if (outputLayer.get(i).value > bestMatch.value){
    			bestMatch = outputLayer.get(i);
    		}
    	}
    	
    	return bestMatch.outputNodeRepresentation;
    	*/
    	
    	//Returns the top X elements from the outputs
    	Neuron smallestNeuron;
    	int smallestLocation;
    	
    	Neuron[] bestMatches = new Neuron[returnedValues];
    	
    	for (int i = 0; i < returnedValues; i++){
    		bestMatches[i] = outputLayer.get(i);
    	}
    	
    	for (int i = returnedValues; i < outputLayer.size(); i++){
    		smallestLocation = findSmallestElement(bestMatches);
    		smallestNeuron = bestMatches[smallestLocation];
    		
    		if (outputLayer.get(i).value > smallestNeuron.value){
    			bestMatches[smallestLocation] = outputLayer.get(i);
    		}
    	}
    	
    	for (int n = 0; n < bestMatches.length; n++){
    		System.out.println(bestMatches[n].outputNodeRepresentation + " at " + bestMatches[n].value + "% ");
    	}
    	
    	return bestMatches;
    }
    
    //Helper method for determineLetter()
    //Returns the INDEX of the smallest Neuron in the passed list
    public static int findSmallestElement(Neuron[] n){
    	int smallestIndex = 0;
    	double smallest = n[0].value;
    	for (int i = 0; i < n.length; i ++){
    		if (n[i].value < smallest) {
    			smallest = n[i].value;
    			smallestIndex = i;
    		}
    	}
    	return smallestIndex;
    }
    
 
     
    /**
     * initializes a new neural network
     */
    public static void initialization() {
    	
    	int inputLayerSize = imgSize*imgSize; // Number of Input Nodes
    	System.out.println("Number of hidden nodes?");
        int hiddenLayerSize = sc.nextInt(); // Number of Hidden Nodes
    	System.out.println("Size of Alphabet is: " + alphabetSize);
        int outputLayerSize = alphabetSize; // Number of Output Nodes
        System.out.println("Number of training epochs?");
        epochs = sc.nextInt(); // Number of Hidden Nodes
        

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
    public static void trainMethod(Map[] trainingAlphabetList){
        // // // ACTUAL TRAINING HAPPENS FROM HERE DOWN
    	Map<String, int[]> trainingAlphabet = new HashMap<String, int[]>();
    	int[] expected = new int[outputLayer.size()];
    	int[] currentInput;
    	String passedLetter;
    	
    	long startTime = System.currentTimeMillis();
    	long currTime;
    	int endTime;
    	int totalCount = 0;
    	
    	int trainingRuns = 4;
    	
    	for (int currentRun = 0; currentRun < trainingRuns; currentRun++){ //we will train through the list of alphabets multiple times
   
	    	for (int currentAlph = 0; currentAlph < trainingAlphabetList.length; currentAlph++ ){ //For every alphabet
	    		trainingAlphabet = trainingAlphabetList[currentAlph];
	    		
		    	for (int count = 0; count < epochs/(trainingAlphabetList.length*trainingRuns); count++){
			    	// iterate through the map
			    	Iterator it = trainingAlphabet.entrySet().iterator();
			    	
			    	
			    	String letterToTrain;
			    	int[] randomNums = sh.createList(52);
			    	sh.shuffle(randomNums);
			    	
			    	for (int num = 0; num < randomNums.length; num++){
			    		letterToTrain = ht.letterMatch(randomNums[num]); // Returns the specific letter
			    		
			    		//System.out.println(letterToTrain);
			    		currentInput = (int[]) trainingAlphabet.get(letterToTrain); // Gets the NN representation
			    		
			    		setInputNodes(currentInput);
			    		
			        	expected = ht.expectedOutputValues(letterToTrain, outputLayer); // get the expected values
			        	
			        	//Running the actual program to train this instance
			        	feedF(expected, true);
			        	
			        	//user output during training to tell us whats going on during training
			        	if (totalCount%100 == 0){
			        		for (int nnOutputNode = 0; nnOutputNode < 1; nnOutputNode++){
			        			System.out.println("  --  " + letterToTrain + "  --  Current output node: " + nnOutputNode + "\tOutput: " + outputLayer.get(nnOutputNode).value + "\t Error: "+ outputLayer.get(nnOutputNode).error );
			        		}
			        	}
			        }
			        if (totalCount%10 == 0){
			        	if (count > 0){
			        		System.out.println("Training... : " + totalCount + "/" + epochs + " on trainingAlphabetList[" + currentAlph + "]");
			        		currTime = System.currentTimeMillis();
			        		endTime = (int) ((currTime-startTime)/totalCount)*(epochs-totalCount);
			        		System.out.println("Estimated time to completion: " + (endTime/1000)/60 + " minutes and " + (endTime/1000)%60 + " seconds..");
			        	}
			        }
			        totalCount++;
		        }
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
	public static void tester(){
    	//user testing the training!
    	System.out.println("type '1' or '2' to test images 1 or 2 (a or b)");
    	System.out.println("type '5' or '6' to train on images 1 or 2 (a or b)");
    	System.out.println("type '7' to save the NN");
    	int in = sc.nextInt(); // Number of Input Nodes
    	Neuron[] bestLetters;
    	
    	while (in == 1 || in == 2 || in == 5 || in == 6 || in == 7){
	    	if (in == 1){
	    		bestLetters = determineLetter(it.generateCluster("testCaseA.jpg", null, imgSize));
	    		for (int nnOutputNode = 0; nnOutputNode < alphabetSize; nnOutputNode++){
        			System.out.println("Current output node: " + nnOutputNode + "\tOutput: " + outputLayer.get(nnOutputNode).value + "\t Error: "+ outputLayer.get(nnOutputNode).error );
        		}
	    	}
	    	else if (in == 2){
	    		bestLetters = determineLetter(it.generateCluster("testCaseB.jpg", null, imgSize));
	    		for (int nnOutputNode = 0; nnOutputNode < alphabetSize; nnOutputNode++){
        			System.out.println("Current output node: " + nnOutputNode + "\tOutput: " + outputLayer.get(nnOutputNode).value + "\t Error: "+ outputLayer.get(nnOutputNode).error );
        		}
	    	}
	    	else if (in == 5){
	    		int[] aNNRepresentation = it.generateCluster("testCaseA.jpg", null, imgSize);
	    		for (int i = 0; i < 100; i++){
	    			singleTrain("a", aNNRepresentation);
	    		}
	    	}
	    	else if (in == 6){
	    		int[] aNNRepresentation = it.generateCluster("testCaseB.jpg", null, imgSize);
	    		for (int i = 0; i < 100; i++){
	    			singleTrain("b", aNNRepresentation);
	    		}
	    	}
	    	else if (in == 7) {
	    		System.out.println("Save");
	    		saveNeuralNetwork();
	    	}
	    	System.out.println("type '1' or '2' to test images 1 or 2 (a or b)");
	    	System.out.println("type '5' or '6' to train on images 1 or 2 (a or b)");
	    	System.out.println("type '7' to save the NN");
	    	in = sc.nextInt(); // Number of Input Nodes
    	}
    }
    
    public static void testerTony(ArrayList<BufferedImage> testingLetters){
    	String results = "";
    	Neuron[] ns;
    	
    	for (int k = 0; k < testingLetters.size(); k++){
    		 ns = determineLetter(it.generateCluster(null, testingLetters.get(k), imgSize));
    		 
    		 double best = 0;
    		 Neuron bestn = null;
    		 for (int i = 0; i<ns.length; i++){
    			 if (ns[i].value > best){
    				 best = ns[i].value;
    				 bestn = ns[i];
    			 }
    		 }
    		 
    		 results = results + bestn.outputNodeRepresentation;
    		 System.out.println(results);
    		 
    	}
    	System.out.println("Complete.");
	    
    }
    
    public static Map<String, int[]> danTrainHelper(String fileName){
    	ppDan.Ink danInk = new ppDan.Ink();
		danInk = Preprocess.getInk(fileName);
		
		int count = 0;
		ArrayList<BufferedImage> letterImages = new ArrayList();
		
		//System.out.println("SIZE OF LINES: " + danInk.lines.size());
		
		for (int line = 0; line < danInk.lines.size(); line++){
			
			//System.out.println("Line: " + line + ", LETTERS: " + danInk.lines.get(line).words.size());
			
			for (int word = 0; word < danInk.lines.get(line).words.size(); word++){
				BufferedImage bf = null;
				bf = Preprocess.Display(danInk.lines.get(line).words.get(word));
				letterImages.add(bf);
				count++;
				
			}
		}
		
		return it.generateAlphabetMapTony(letterImages, imgSize);
		
    }
    
    //  MAIN FUNCTION 
    public static void main(String[] args) {
    	PatternDetector pd = new PatternDetector();
    	PatternDetector pd2 = new PatternDetector();
    	
    	Map<String, int[]> trainingAlphabet = new HashMap<String, int[]>();
    	
    	//training
    	if (productionMode){
        	System.out.println("ENSURE the following settings are the same!!");
    		initialization(); //initialize a plain NN
    		loadNeuralNetwork();
    		
    	} else{
    		initialization(); //initialize a plain NN
    		
    		if (tonyTrain){
    			Map[] trainingAlphabetList = new Map[1];
    			it.generateClusterTony(pd, "TrainingSetBeta.jpg");
    			trainingAlphabetList[0] = it.generateAlphabetMapTony(pd.getLetters(), imgSize);
    			trainMethod(trainingAlphabetList); //begin training of the network
    		} 
    		
    		else if (danTrain) {
    			Map[] trainingAlphabetList = new Map[3];
    			
    			trainingAlphabetList[0] = danTrainHelper("TrainingSetBeta.jpg");
    			
    			trainingAlphabetList[1] = danTrainHelper("TrainingSetCharlie.jpg");
    			
    			trainingAlphabetList[2] = danTrainHelper("TrainingSetDelta.jpg");
    			    			
    			trainMethod(trainingAlphabetList);
    			
    			    			
    		}
    		else{
    			Map[] trainingAlphabetList = new Map[1];
    			trainingAlphabetList[0] = it.generateAlphabetMap(imgSize);
    			trainMethod(trainingAlphabetList); //begin training of the network
    		}
    	}
    	
    	//testing
    	if (tonyTrain){
    		it.generateClusterTony(pd2, "TrainingSetBeta.jpg");
    		testerTony(pd2.getLetters());
    	}
    	else if (danTrain){
    		ppDan.Ink danInk2 = new ppDan.Ink();
			danInk2 = Preprocess.getInk("TrainingSetBeta.jpg");
			
			int count = 0;
			ArrayList<BufferedImage> letterImages = new ArrayList();
			for (int line = 0; line < danInk2.lines.size(); line++){
				for (int word = 0; word < danInk2.lines.get(line).words.size(); word++){
					BufferedImage bf = Preprocess.Display(danInk2.lines.get(line).words.get(word));
					letterImages.add(bf);
					count++;
				}
			}
			
			testerTony(letterImages);
			
			tester();
    		
    	}
    	else{
    		tester();
    	}
    }
}
