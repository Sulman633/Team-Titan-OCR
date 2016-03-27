package neuralnetwork;

import java.util.ArrayList;

public class HelperTrainer {

	//constructor
	public HelperTrainer(){
		
	}
	
	//Will set the matching output node to the letter it represents
	public void setOutputNodeRepresentations(Layer outputLayer){
		for (int node = 0; node < outputLayer.numOfNeurons(); node++){
			outputLayer.getNeuron(node).letterRepresentation = letterMatch(node);
		}
	}
	
	// 
	public int[] expectedOutputValues(String passedLetter, ArrayList<NeuronOLD> output){
		int[] expectedVals = new int[output.size()];
		
		for (int node = 0; node < output.size(); node++){
			if (passedLetter.equals(output.get(node).outputNodeRepresentation)){
				output.get(node).setError(1);
				expectedVals[node] = 1;
			}
			else{
				output.get(node).setError(0);
				expectedVals[node] = 0;
			}
		}
		return expectedVals;
	}
			
	//simply matches the letter to its defined order, output node 26 represents an "A"
	public String letterMatch(int position){
		
		switch(position){
		case 0:
			return "a";
		case 1:
			return "b";
		case 2:
			return "c";
		case 3:
			return "d";
		case 4:
			return "e";
		case 5:
			return "f";
		case 6:
			return "g";
		case 7:
			return "h";
		case 8:
			return "i";
		case 9:
			return "j";
		case 10:
			return "k";
		case 11:
			return "l";
		case 12:
			return "m";
		case 13:
			return "n";
		case 14:
			return "o";
		case 15:
			return "p";
		case 16:
			return "q";
		case 17:
			return "r";
		case 18:
			return "s";
		case 19:
			return "t";
		case 20:
			return "u";
		case 21:
			return "v";
		case 22:
			return "w";
		case 23:
			return "x";
		case 24:
			return "y";
		case 25:
			return "z";
		case 26:
			return "A";
		case 27:
			return "B";
		case 28:
			return "C";
		case 29:
			return "D";
		case 30:
			return "E";
		case 31:
			return "F";
		case 32:
			return "G";
		case 33:
			return "H";
		case 34:
			return "I";
		case 35:
			return "J";
		case 36:
			return "K";
		case 37:
			return "L";
		case 38:
			return "M";
		case 39:
			return "N";
		case 40:
			return "O";
		case 41:
			return "P";
		case 42:
			return "Q";
		case 43:
			return "R";
		case 44:
			return "S";
		case 45:
			return "T";
		case 46:
			return "U";
		case 47:
			return "V";
		case 48:
			return "W";
		case 49:
			return "X";
		case 50:
			return "Y";
		case 51:
			return "Z";
		case 52:
			return ",";
		case 53:
			return ".";
		case 54:
			return "?";
		case 55:
			return "!";
		}
		
		return "";
	}
}
