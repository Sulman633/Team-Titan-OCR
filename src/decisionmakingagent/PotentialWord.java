package decisionmakingagent;

import neuralnetwork.Neuron;

/**
 * The PotentialWord class acts solely as a wrapper for a number of Neurons that come as one potential combination of the Neurons in the PotentialChar objects that compose a word.
 * The resulting array of {@link Neuron}s is encapsulated to increase readability.
 * @author Nathan Van Dyken
 * @author Sulman Qureshi
 *
 */
public class PotentialWord {

	/**
	 * An array of Neurons with associated characters are held in this field.
	 * Essentially, in this context, the neurons are used to keep the score for each character with the associated character.
	 */
	public final Neuron [] CHARS;
	public int lexiconScore;
	public double probabilityScore;
	
	public PotentialWord( Neuron [] newChars ) {

		CHARS = newChars;
		
	}
	/**
	 * Compares the String representations of each PotentialWord to determine if they represent the same English word.
	 * @param otherWord - the word to be compared to.
	 * @return true if the String representations of the words match, else false.
	 */
	public boolean equals( PotentialWord otherWord ){
		
		if( this.CHARS.length != otherWord.CHARS.length ) return false;
		
		for( int i = 0; i < CHARS.length; i++ ){
			
			if( ! this.CHARS[i].outputNodeRepresentation.equals(otherWord.CHARS[i].outputNodeRepresentation ) ) return false;
			
		}
		
		return true;
		
	}
	
	/**
	 * Returns a string representation of the Neurons in this PotentialWord.
	 * @return a string that contains the word that this PotentialWord represents.
	 */
	public String getString(){
		
		String result = "";
		
		for( int i = 0; i < CHARS.length; i++ ){
			
			result += CHARS[i].outputNodeRepresentation;
			
		}
	
		return result;
		
	}
	
	public void setProbabilitiyScore(double prob){
		this.probabilityScore=prob;
	}
	public double getProbabilityScore(){
		return this.probabilityScore;
	}

}
