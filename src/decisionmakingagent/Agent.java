package decisionmakingagent;

import java.util.ArrayList;
import java.util.Random;

import neuralnetwork.Neuron;
/**
 * To understand this class it is vital to understand the use of the two wrapper classes it works closely with, 
 * {@link PotentialChar} and {@link PotentialWord}.
 * The flow through the program goes as follows:
 *
 * <p>The driver passes an array of PotentialChar objects to the method {@code assess}. There should be one 
 * PotentialChar object for every letter in the word that is being assessed, with the contents of each PotentialChar
 * being the array of Neurons that wordListed from the neural network processing the image associated with that character. 
 * 
 * <p>The array of PotentialChars is then passed to the {@code genCombos} method to generate an ArrayList of PotentialWords. 
 * Each of these potential words is once again just an array of Neurons. This time, however, instead of a number of 
 * Neurons representing the top candidates for one specific letter, each the list of Neurons spells out a word that 
 * can be formed out of the list of letters. See below for a visual example.
 * <p>
 * 
 * <img src="./doc/genCombos.png"
 * style="width: 75%" />
 * 
 * <p>Each word in the ArrayList is then scored 
 * 
 * 
 * @author Nathan Van Dyken
 *
 */
public class Agent {

	//Variables of interest:
	/**
	 * Determines at what level of confidence a letter is considered to be unknown when performing a Lexicon lookup.
	 */
	public final float UNKNOWN_LETTER_CUTOFF = 0.6f;
	
	
	
	PotentialChar [] currentWord;
	ArrayList<PotentialWord> wordList;
	
	public String assess( PotentialChar [] newWord ){
		
		wordList = genCombos(newWord);
		
		//**NOTE** To access the actual data in a word, use the following notation:
		//		       The character # in the word you're looking at V
		//									wordList.get( # ).CHARS[ # ]
		// The # of the potential word you're looking for ^ 

		//The actual character stored in the Neuron is called outputNodeRepresentation 
		//and the score from the NN is called value.
		
		
		//The scoreByLexicon method will generate additional entries to the wordList, so
		//it must be executed before scoreByProbability.
		scoreByLexicon();
		scoreByProbability();
		
		return makeFinalDecision();
		
	}
	/**
	 * Generates an ArrayList of all possible combinations of Neurons in the PotentialChars provided.
	 * @param pWord - the array of PotentialChars that will provide all possible letters to be used in the words.
	 * @return an ArrayList of all possible combinations of Neurons.
	 */
	ArrayList<PotentialWord> genCombos( PotentialChar [] pWord ){
		
		currentWord = pWord;
		
		ArrayList<PotentialWord> result = new ArrayList<PotentialWord>();
		
		int [] pattern = new int[ pWord.length ];
		java.util.Arrays.fill(pattern, 0);
		
		do{
			
			Neuron [] tempNeurons = new Neuron[ pattern.length ];
			
			for( int i = 0; i < pattern.length; i++ ){
				
				tempNeurons[i] = currentWord[i].NEURONS[pattern[i]];
				
			}
			
			result.add(new PotentialWord(tempNeurons));
			
		}while( increment( pattern ));
		
		return result;
		
	}
	
	void scoreByLexicon( ){
		
		for( int i = 0; i < wordList.size(); i++ ){
			
			//TODO Query DB, removing letters that fall below UNKNOWN_LETTER_CUTOFF
			//TODO Add new words to the wordList
			
		}
		
	}
	
	void scoreByProbability(){
		
		//TODO Score words based on the probability of their letters
		
	}
	
	public String makeFinalDecision(){
		
		//TODO Return the best string based on both scores
		
		return "";
		
	}
	
	/**
	 * Used by the {@code genCombos} method to increment the pattern that controls the Neurons added to the next PotentialWord.
	 * @param pattern - the pattern that is to be incremented.
	 * @return true if the pattern has not reached its maximum limit, false if the limit has been reached and no more combos
	 * should be generated.
	 */
	private boolean increment( int [] pattern ){

		int maxValue = pattern.length - 1;
		
		pattern[maxValue]++;
		
		for( int i = maxValue; i >= 0; i-- ){
			
			if( pattern[i] == 3 ){
				
				if( i == 0 ) return false;
				
				pattern[i] = 0;
				pattern[i - 1]++;
				
			}
			
		}
		
		return true;
		
	}
	
}
