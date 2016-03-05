package lexicon;
/**
 * Wrapper class to hold a word and its frequency of use. Will be returned by the {@link Lexicon} to the decision making agent that will judge what word the user has written, a decision that is based on, among a number of factors, the frequency of use.
 * @author Nathan Van Dyken
 */
public class PossibleWord implements Comparable<PossibleWord> {
	/**
	 * The actual word that is to be considered as a candidate.
	 */
	public final String WORD;
	/**
	 * The frequency with which the word is used, pulled from the DB.
	 */
	public final int FREQ;
	/**
	 * Default constructor to initialize the two fields with their values. As this class is only a wrapper, no manipulation of the data is necessary.
	 * @param newWord - the word to be stored in this instance of the wrapper class.
	 * @param newFreq - the frequency of use of word stored in this instance of the wrapper class.
	 */
	PossibleWord( String newWord, int newFreq ) {

		WORD = newWord;
		FREQ = newFreq;
		
	}
	/**
	 * Compares this PossibleWord to another possible word, following the convention of the {@link Comparable} interface. The key that is compared is the frequency of the word, and a negative integer, zero, or a positive integer are returned if this object is less than, equal to, or greater than the specified object.
	 */
	@Override
	public int compareTo(PossibleWord o) {
		
		return this.FREQ - o.FREQ;
		
	}
	
}
