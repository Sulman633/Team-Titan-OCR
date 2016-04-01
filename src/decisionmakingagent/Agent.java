package decisionmakingagent;

import java.util.ArrayList;
import lexicon.Lexicon;
import lexicon.PossibleWord;
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
 * <p>Each word in the ArrayList is then scored based on two criteria: the frequency of use and the cumulative probability 
 * of all of its letters.
 * 
 * 
 * @author Nathan Van Dyken
 * @author Sulman Qureshi
 * 
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
	Lexicon lex = new Lexicon();
	
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
		wordList = scoreByProbability( wordList );
		
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
	/**
	 * Scores each potential word with a score based on its frequency of use as stored in the Lexicon database.
	 * Generates new words if one or more of the letters in the word have a probability that is below the constant
	 *  UNKNOWN_LETTER_CUTOFF.
	 */
	void scoreByLexicon( ){
		
		//Iterates through every word in the current list of candidate words, scoring it based on the Lexicon.
		for( int h = 0; h < wordList.size(); h++ ){
			
			String [] thisString = {wordList.get(h).getString()};
			
			PossibleWord [] thisFullWord = lex.getMatches( thisString );
			
			//Sets score to zero if there is no match from the DB, otherwise sets it to the frequency stored in the DB.
			if(thisFullWord == null || thisFullWord.length == 0 ) wordList.get(h).lexiconScore = 0;
			else wordList.get(h).lexiconScore = thisFullWord[0].FREQ;
			
		}
		
		//From this point onward, we create new words and score them as they are made.
		ArrayList<PotentialWord> newWords = new ArrayList<PotentialWord>();
		
		for( int i = 0; i < wordList.size(); i++ ){
			
			System.out.println( "Now working on the #" + i + "word from the list of generated combos." );
			
			Neuron [] preSearchWord = new Neuron[wordList.get(i).CHARS.length];
			
			//Removes the letters that fall below a certain threshold.
			for( int j = 0; j < wordList.get(i).CHARS.length; j++ ){
				
				if( wordList.get(i).CHARS[j].value > UNKNOWN_LETTER_CUTOFF ) preSearchWord[j] = wordList.get(i).CHARS[j];
				
			}
			
			String [] result = {""};
			
			//Transfer the word to a new string to query the DB with.
			for( int k = 0; k < wordList.get(i).CHARS.length; k++ ){
				
				if( preSearchWord[k] != null  ) result[0] += preSearchWord[k].outputNodeRepresentation;
				else result[0] += "_";
				
			}
			
			
			PossibleWord [] resultantWords = lex.getMatches( result );
			
			//l is the number of the word in the resultantWords array that we are currently dealing with.
			for( int l = 0; l < resultantWords.length; l++ ){
				
				Neuron [] postSearchWord = new Neuron[resultantWords[l].WORD.length()];
				
				//m is the character that is being copied over into the resultant word.
				for( int m = 0; m < resultantWords[l].WORD.length(); m++ ){
					
					//Check to see if we're dealing with a blank character that has been filled in, meaning we have a new word.
					if( preSearchWord[m] == null || (preSearchWord[m].outputNodeRepresentation.charAt(0) != (resultantWords[l].WORD.charAt(m) ) )){
						
						postSearchWord[m] = new Neuron();
						postSearchWord[m].outputNodeRepresentation = "" + resultantWords[l].WORD.charAt(m);
						postSearchWord[m].value = 0.0;
						
					}else postSearchWord[m] = preSearchWord[m];
					
				}
				
				PotentialWord nextNewWord = new PotentialWord(postSearchWord);
				
				//For debugging; prints each new word as it's created.
				System.out.println(nextNewWord.getString());
				
				nextNewWord.lexiconScore = resultantWords[l].FREQ;
				
				boolean isDuplicate = false;
				
				//Tests if this word is a duplicate.
				for( int n = 0; n < newWords.size(); n++ ){
					
					if( nextNewWord.equals( wordList.get(n) ) ) isDuplicate = true;
					
				}
				
				if( !isDuplicate ) wordList.add( nextNewWord );
				
			}
			
		}
		
	}
	
	/**
	 * This method takes a type potentialWord arrayList which contains all possible word combinations.
	 * Then for each word possibility it calculates a averageScore that will represent the words likeliness. 
	 * @param combos - the ArrayList which holds all combinations of words.
	 * @return - returns combo arrayList with updated averageScore for each word in the array.
	 */
	ArrayList<PotentialWord> scoreByProbability(ArrayList<PotentialWord> combos){
		double avgProbability =0;
		for(int i =0;i<combos.size();i++){
			for(int j=0;j<combos.get(i).CHARS.length;j++){
				avgProbability += combos.get(i).CHARS[j].value;
			}
			avgProbability/=combos.get(i).CHARS.length;
			combos.get(i).setProbabilitiyScore(avgProbability);
			avgProbability =0;
			System.out.println(combos.get(i).getProbabilityScore());
		}
		return combos;
	}
	
	public String makeFinalDecision(){
		
		//TODO Return the best string based on both scores.
		
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
