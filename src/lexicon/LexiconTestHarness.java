package lexicon;

import java.util.ArrayList;
/**
 * Test harness for the lexicon package. Allows for user to test both a lookup of multiple words using testLookup, as well as the use of a word using testWordUse.
 * @author Nathan Van Dyken
 *
 */
public class LexiconTestHarness {

	Lexicon testLexicon;
	
	public LexiconTestHarness() {

		testLexicon = new Lexicon();
		
		testLookup();
		
		//testLexicon.useWord("dog");
		
	}
	/**
	 * Simulates the lookup of specified words in the database; the results are printed to the console, including the number of results, the individual words, and their corresponding frequencies of use.
	 */
	public void testLookup(){
		
		ArrayList<String> testStrings = new ArrayList<String>();
		
		/*	
		 * Edit the strings below to simulate possible inputs from the decision making agent's output. 
		 * There is no limit to the number of strings that can be used to query the DB, but the output will not be broken up word by word as this is not relevant to the decision making agent.
		 * In compliance with the format that the decision making agent will follow, enter a string with missing letters replaced by underscore characters ('_').
		*/
		
		testStrings.add("d_");
		testStrings.add("da_");
		testStrings.add("textThatWillNotMatchAnyWords");
		
		
		
		ArrayList<PossibleWord> testResult = testLexicon.getMatches( testStrings );
		
		System.out.println( testResult.size() + " candidate words as a result of the test:");
		
		for( PossibleWord currentWord: testResult){
			
			System.out.println(currentWord.WORD + " : " + currentWord.FREQ  );
			
		}
		
	}
	/**
	 * Simulates the use of a word by the decision making agent, which would subsequently call the following command. To check if the method is being executed properly, query the DB before and after you run the test.
	 * NOTE: MAKE SURE THAT YOU MANIPULATE THE DATABASE TO UNDO AND CHANGES MADE TO THE FREQUENCY OF THE WORDS. EVERY TIME THE FOLLOWING STATEMENT IS RUN, CHANGES WILL BE REFLECTED BY THE DATABASE, I.E., THE FREQUECY OF THE SPECIFIED WORD WILL BE INCREMENTED.
	 */
	public void testWordUse(){
		
		try{
			
			testLexicon.useWord("dog");
			testLexicon.useWord("wordThatDoesNotExist");
			
		}catch( InvalidWordException e){
			
			System.out.println( e.getMessage() );
			System.out.println("The first two tests of the useWord method threw an exception.");
			
		}
		
		try{
			
			testLexicon.useWord("");
			//testLexicon.useWord("");
			//testLexicon.useWord("");
			//testLexicon.useWord("");
			
			//TODO Come up with more examples of invalid words.
			
		}catch( InvalidWordException e){
			
			System.out.println( e.getMessage() );
			System.out.println("The last test of the useWord method threw an exception.");
			
		}
		
		
	}
	
	public static void main( String args[]){ new LexiconTestHarness(); }
	
}
