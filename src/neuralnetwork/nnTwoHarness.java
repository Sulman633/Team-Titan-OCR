package neuralnetwork;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import ppDan.Preprocess;
import test.ImgProcessMatt;

public class nnTwoHarness {
	ImgProcessMatt ipm = new ImgProcessMatt();

	
	int inputNodes = 100; 
	int outputNodes = 56;
	int numHiddenLayers = 1;
	int numHiddenNodes = 50;
	static double holdTrainingSet = 1.0;
	int k = 3;
	static int maxEpochs = 1000;
	boolean log = true;
	static boolean quickprop = false;
	double lr = 0.1;
	double m  = 0.01;
	static Random rand = new Random();

	public nnTwoHarness(){
		initializeNN();
	}
	
	public void initializeNN(){
		ArrayList<String> testing = new ArrayList();
		ArrayList<String> training = new ArrayList();
		
		// ----- Taken from old NN, gets us the Map of nn, we will use the T.A.'s input list
		ppDan.Ink danInk = new ppDan.Ink();
		danInk = Preprocess.getInk("TrainingSetBeta.jpg");
		int count = 0;
		//Get the letter images of our training set
		ArrayList<BufferedImage> letterImages = new ArrayList();		
		for (int line = 0; line < danInk.lines.size(); line++){			
			for (int word = 0; word < danInk.lines.get(line).words.size(); word++){
				BufferedImage bf = null;
				bf = Preprocess.Display(danInk.lines.get(line).words.get(word));
				letterImages.add(bf);
				count++;
			}
		}
		//get NN string
    	training = ipm.generateAlphabetMapTony(letterImages, (int) Math.sqrt(inputNodes));

		NeuralNetwork mainNN = new NeuralNetwork(inputNodes, outputNodes, numHiddenLayers, numHiddenNodes, log, lr, m, rand);
		
		setOutputRepresentations(mainNN);
		
		split(testing,training,holdTrainingSet);
		
		crossValidation(mainNN, testing, training, k);
		
	}
	
	public void setOutputRepresentations(NeuralNetwork mainNN){
		HelperTrainer ht = new HelperTrainer();
		for (int i = 0; i < mainNN.outputLayer.numOfNeurons(); i++){
			ht.setOutputNodeRepresentations(mainNN.outputLayer);
		}
	}
	
	public static void crossValidation(NeuralNetwork mainNN, ArrayList<String> testing, ArrayList<String> training, int k){
		
		kfold(mainNN,training,k);
		
		for(int i=0; i<1;i++){
			System.out.println(mainNN.validateIris(testing));			
		}
		
		
	}
	
	public static void split(ArrayList<String> testing, ArrayList<String> training, double hO){
		
		int trainCandidates = (int)Math.ceil(testing.size()*hO);
		
		
		for(int i=0;i<trainCandidates;i++){
			int toRemove = rand.nextInt(testing.size()-1);
			
			training.add(testing.remove(toRemove));
		}
		
	}

	public static void kfold(NeuralNetwork x, ArrayList<String> data, int k){
		ArrayList<List<String>> folds = new ArrayList<List<String>>(); 
		int eachSet = (int)Math.floor(data.size()/k);
		int from =0;
		int to =0;
		
		
		
		for(int i=0;i<data.size();i=i+eachSet){
			from  = i;
			to = i+eachSet;
			if(to>data.size()){
				to = data.size();
			}
			List<String> list = data.subList(from, to);
			//System.out.println(list.size());
			
			folds.add(list);
			if(to==data.size()-1) break;
			
		}
		
		
		
		
		
		boolean stopTraining = false;
		double avgValidation=0;
		double avgTraining=0;
		int numOfVariations=0;
		int epoch = 0;
		
		do{
			epoch++;
			
			
			double validationMeasure;
			double vsum =0;
			
			double trainingMeasure;
			double tsum=0;
			
			for(int i=0;i<folds.size();i++){
				ArrayList<String> train = new ArrayList<String>();
				ArrayList<String> validate = new ArrayList<String>();
				
				for(int j=0;j<folds.size();j++){
					if(i==j){
						validate.addAll(folds.get(j));
					}else{
						train.addAll(folds.get(j));
					}
					
					
				}
				
				
				
				//System.out.println("--Training Set--");
				trainingMeasure = x.trainOCR(train,quickprop);
				//System.out.println("--Validation Set--");
				validationMeasure = x.trainOCR(validate,quickprop);

				tsum = tsum + trainingMeasure;
				vsum = vsum + validationMeasure;
			}
			
			//System.out.println(vsum/folds.size() + "-->" + avgValidation);
			
			if(vsum/folds.size()<=avgValidation && tsum/folds.size()>=avgTraining){
				numOfVariations++;
				if(numOfVariations>3) stopTraining=true;
			}else{
				numOfVariations=0;
			}
			
			avgValidation = vsum/folds.size();
			avgTraining = tsum/folds.size();

			//System.out.println("--Avg Validation Accuracy of all folds--");
			//System.out.println(avgTraining + ", " + avgValidation);
			
			if(epoch == maxEpochs){
				stopTraining =true;
			}
			

		}while(!stopTraining);
		
		System.out.print(epoch + ", " + avgTraining + ", " + avgValidation + ", ");
		
		
		
		
		
		
		
	}

	
	public static ArrayList<String> getInputs(String fileName){
    	ArrayList<String> allInput = new ArrayList<>();
		
    	File file = new File(fileName);

        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                allInput.add(sc.nextLine());
            }

        	sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    	return allInput;
    }
	
	static public void main(String args[]){
		new nnTwoHarness();
	}
}
