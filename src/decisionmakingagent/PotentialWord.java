package decisionmakingagent;

import neuralnetwork.Neuron;

/**
 * The PotentialWord class acts solely as a wrapper for a number of Neurons that come as one potential combination of the Neurons in the PotentialChar objects that compose a word.
 * The resulting array of {@link Neuron}s is encapsulated to increase readability.
 * @author Nathan Van Dyken
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

}
