package neuralnetwork;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Program {
	
	static boolean log =false; //gives a faster, more approximate answer w/ quickprop
	static boolean quickprop = false;
	static double holdTrainingSet = .7;
	static double learningRate = 0.02; //.05 is ideal
	static double momentum = 0.001; //.01 is ideal
	
	
	static int numHiddenLayers = 1;
	static int numHiddenNodes = 23;
	static int maxEpochs = 500;
	//quickprop performs better w/ greater nodes(more consistent results)
	//steeper climbs
	//20 nodes for quickprop seems ideal
	//50 nodes seems to generate the fastest and better results
	static int k = 2;
	static Random rand = new Random();
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		

		
		
		//cancerNN();
		for(int i=0; i<20;i++){
			irisNN();
		}
		
		
				
		
		

		
	}
	
	
	
	public static void cancerNN(){
		int inputNodes = 30;
		int outputNodes = 2;
		String fileName = "cancer.txt";
		
		
		ArrayList<String> testing = getInputs(fileName);
		ArrayList<String> training = new ArrayList<String>();

		NeuralNetwork iris = new NeuralNetwork(inputNodes,outputNodes,numHiddenLayers,numHiddenNodes,log,learningRate,momentum,rand);
		
		split(testing,training,holdTrainingSet);

		crossValidation(iris, testing, training, k);

		
	}
	
	public static void irisNN(){
		int inputNodes = 4;
		int outputNodes = 3;
		String fileName = "iris.txt";
		
		ArrayList<String> testing = getInputs(fileName);
		ArrayList<String> training = new ArrayList<String>();

		NeuralNetwork iris = new NeuralNetwork(inputNodes,outputNodes,numHiddenLayers,numHiddenNodes,log,learningRate,momentum,rand);
		
		split(testing,training,holdTrainingSet);

		crossValidation(iris, testing, training, k);

		
	}
	
	public static void crossValidation(NeuralNetwork iris, ArrayList<String> testing, ArrayList<String> training, int k){
		
		kfold(iris,training,k);
		//System.out.println("--TESTING--");
		
		for(int i=0; i<1;i++){
			System.out.println(iris.validateIris(testing));			
		}
		
		
	}
	
	public static void split(ArrayList<String> testing, ArrayList<String> training, double hO){
		
		int trainCandidates = (int)Math.ceil(testing.size()*hO);
		
		
		for(int i=0;i<trainCandidates;i++){
			int toRemove = rand.nextInt(testing.size()-1);
			
			training.add(testing.remove(toRemove));
		}
		
	}
	
	public static void kfold(NeuralNetwork x,ArrayList<String> data, int k){
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
				trainingMeasure = x.trainIris(train,quickprop);
				//System.out.println("--Validation Set--");
				validationMeasure = x.trainIris(validate,quickprop);

				tsum = tsum + trainingMeasure;
				vsum = vsum+validationMeasure;
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

}
