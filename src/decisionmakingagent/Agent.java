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
 * can be formed out of the list of letters.
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
	private final float UNKNOWN_LETTER_CUTOFF = 0.6f;
	/**
	 * If true, prints output to console explaining the steps it is going through.
	 * Performance is severely affected by the printing to the console; presentation should be disabled when running in production.
	 */
	private final boolean PRESENTATION_MODE = true;
	
	//End of variables of interest.
	
	PotentialChar [] currentWord;
	ArrayList<PotentialWord> wordList;
	Lexicon lex = new Lexicon();
	
	/**
	 * Given the output of the NN, generates and returns the word that is most likely the one composed of all the potential letters.
	 * @param newWord - the letters from the NN that need to be evaluated to produce one word.
	 * @return the best fit for the letters that were passed. See the class documentation for further explanation.
	 */
	public String assess( PotentialChar [] newWord ){
		
		System.out.println(" === Step 1 ===");
		System.out.println("A list of every combination of the letters provided is generated.");
		
		
		wordList = genCombos(newWord);
		
		if( PRESENTATION_MODE ){
			
			System.out.println("\n === Step 2 ===");
			System.out.println("Now generating words from the lexicon and scoring them based on the frequecy of use.");
			System.out.println("\nFirst, all the existing words from the running list are scored.");
			System.out.println("\nSecond, if a specific letter in a word falls below the specified tolerance level, the lexicon will be searched in an attempt to replace the letter to form another word.");
			
		}
		
		scoreByLexicon();
		
		if( PRESENTATION_MODE ){
			
			System.out.println("\n === Step 3 ===");
			System.out.println("Finally, the complete list of possible words are scored based on the proability of their individual letters.");
			
		}
		wordList = scoreByProbability( wordList );
		
		if( PRESENTATION_MODE ) presentWordList();
		
		return makeFinalDecision();
		
	}
	/**
	 * Generates an ArrayList of all possible combinations of Neurons in the PotentialChars provided.
	 * @param pWord - the array of PotentialChars that will provide all possible letters to be used in the words.
	 * @return an ArrayList of all possible combinations of Neurons.
	 */
	private ArrayList<PotentialWord> genCombos( PotentialChar [] pWord ){
		
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
				
				nextNewWord.lexiconScore = resultantWords[l].FREQ;
				
				boolean isDuplicate = false;
				
				//Tests if this word is a duplicate.
				for( int n = 0; n < wordList.size(); n++ ){
					
					if( nextNewWord.equals( wordList.get(n) ) ) isDuplicate = true;
					
				}
				
				for( int n = 0; n < newWords.size(); n++ ){
					
					if( nextNewWord.equals( newWords.get(n) ) ) isDuplicate = true;
					
				}
				
				if( !isDuplicate ){
					
					//For presentation; prints each new word as it's created.
					if( PRESENTATION_MODE ) System.out.println("The word \"" + nextNewWord.getString() + "\" was created by replacing letters in the word \"" + wordList.get(i).getString() + "\".");
					
					newWords.add( nextNewWord );
				}
				
			}
			
		}
		
		wordList.addAll(newWords);
		
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
		}
		return combos;
	}
	/**
	 * Used for presentation mode; prints out the contents of wordList and their scores in an easy-to-read format.
	 */
	private void presentWordList(){
		
		System.out.println("\nHere is the resulting list of words with their respective lexicon and probability scores:");
		for( PotentialWord curWord: wordList ){
			
			System.out.println(curWord.getString() + " - "+ curWord.lexiconScore + " - " + curWord.probabilityScore );
			
		}
		
	}
	
	/**
	 * Simply scores every word and returns the word with the highest score.
	 * @return the word that is the best fit for all the letters passed to the decision making agent.
	 */
	public String makeFinalDecision(){
		
		if( PRESENTATION_MODE ) System.out.println("\nBoth scores are taken into consideration and the final predicted word is returned to the driver.");
		
		PotentialWord bestWord = wordList.get(0);
		
		for( PotentialWord curWord: wordList ){
			
			if( getFinalScore(bestWord) < getFinalScore(curWord) ) bestWord = curWord;
			
		}
		
		if( PRESENTATION_MODE ) System.out.println( "The final word: "+ bestWord.getString() );
		
		return bestWord.getString();
		
	}
	
	/**
	 * Generates final score based on 
	 * @param pW - the potential word to score
	 * @return a score found by adding the probability score to 1% of the lexicon score.
	 */
	private double getFinalScore( PotentialWord pW ){
		
		return pW.probabilityScore + (pW.lexiconScore * 0.01);
		
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
