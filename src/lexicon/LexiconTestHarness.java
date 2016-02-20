package lexicon;

import java.util.ArrayList;

public class LexiconTestHarness {

	public LexiconTestHarness() {

		Lexicon testLexicon = new Lexicon();
		
		ArrayList<String> testStrings = new ArrayList<String>();
		testStrings.add("d_g");
		testStrings.add("do_");
		testStrings.add("_");
		
		ArrayList<PossibleWord> testResult = testLexicon.getMatches( testStrings );
		
		System.out.println( testResult.size() + " candidate words as a result of the test:");
		
		for( PossibleWord currentWord: testResult){
			
			System.out.println(currentWord.WORD + " : " + currentWord.FREQ  );
			
		}
		
	}
	
	public static void main( String args[]){ new LexiconTestHarness(); }
	
}
