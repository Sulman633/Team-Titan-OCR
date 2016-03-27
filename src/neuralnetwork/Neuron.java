/*
Name: Dan Usman & Iyuehan Yang(5300231)
Course: COSC 3P71
Assignment #3
 */
package neuralnetwork;

import java.util.ArrayList;
import java.util.Random;


public class Neuron {
    private double value= 0;
    private double error=0;
    private double summation = 0;
    private Random rand = new Random();
    
    String letterRepresentation = "";
    
    
    private ArrayList<Double> weight = new ArrayList<Double>();
    private ArrayList<Double> change = new ArrayList<Double>();
    private ArrayList<Double> gradient = new ArrayList<Double>();

    
    public Neuron(int size){        
    	double w=0;
    	//rand = new Random(rand.nextLong());
    	    
        for (int i=0; i<size;i++){
        	
        	w=0;
        	while(w==0){
        	//w = (rand.nextDouble()*2)-1; //sets initial weights from -1 to +1
        		w = (rand.nextDouble()*1)-.5; //sets initial weights from -.5 to +.5
        		//w = (rand.nextDouble()*.5)-.25; //sets initial weights from -.5 to +.5
        	}
        
        	//System.out.println(w);
            weight.add(w);            
        }
        
        for(int i=0; i<size;i++){
        	change.add(0.0);
        }
        
        for(int i=0; i<size;i++){
        	gradient.add(0.0);
        }

    }
    
    public void setExpected(double expected){
    	error = expected - value;
    	
    }
    
    
    
    public int getPointers(){
    	return weight.size();
    }
    
    public double getWeight(int x){
    	return weight.get(x);
    }
    
    public void setWeight(int i, double value){
    	weight.set(i, value);
    }
    
    public double getChange(int x){
    	return change.get(x);
    }
    
    public void setChange(int i, double value){
    	change.set(i, value);
    }
    
    public double getGradient(int x){
    	return gradient.get(x);
    }
    
    public void setGradient(int i, double value){
    	gradient.set(i, value);
    }
    
    public double getValue(){
    	return value;
    	
    }
    
    public void setValue(double x){
    	value = x;
    }
    
    public void setError(double x){
    	error = x;
    }
    
    public double getError(){
    	return error;
    }
    
    public double getSummation(){
    	return summation;
    }
    
    public void setSummation(double x){
    	summation = x;
    }
    
    
    
    
}
