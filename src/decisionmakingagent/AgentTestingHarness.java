package decisionmakingagent;

import java.util.ArrayList;

import neuralnetwork.Neuron;
/**
 * Test harness for the Agent package. Currently tests the Agent's ability to generate words from a list of PotentialChars.
 * @author Nathan Van Dyken
 *
 */
public class AgentTestingHarness {

	public AgentTestingHarness() {
		
		testGenerationOfWords();
		
		
	}
	/**
	 * Tests the agent's ability to generate words from nodes in a situation identical to what it would see once integrated. 
	 */
	private void testGenerationOfWords(){
		
		//It's a mess, but the following ~50 lines of code are only to manually set up neurons to be used by the Agent,
		//so they will not be necessary in actual implementation. See below for where the actual test begins.
				
		PotentialChar [] testWord = new PotentialChar [4];
		Neuron [] n0 = new Neuron[3];
		n0[0] = new Neuron();
		n0[0].outputNodeRepresentation = "f";
		n0[0].value = .90;
		n0[1] = new Neuron();
		n0[1].outputNodeRepresentation = "t";
		n0[1].value = .80;
		n0[2] = new Neuron();
		n0[2].outputNodeRepresentation = "l";
		n0[2].value = .75;
		testWord[0] = new PotentialChar(n0); 
		
		Neuron [] n1 = new Neuron[3];
		n1[0] = new Neuron();
		n1[0].outputNodeRepresentation = "r";
		n1[0].value = .95;
		n1[1] = new Neuron();
		n1[1].outputNodeRepresentation = "n";
		n1[1].value = .90;
		n1[2] = new Neuron();
		n1[2].outputNodeRepresentation = "c";
		n1[2].value = .90;
		testWord[1] = new PotentialChar(n1);
		
		Neuron [] n2 = new Neuron[3];
		n2[0] = new Neuron();
		n2[0].outputNodeRepresentation = "o";
		n2[0].value = .90;
		n2[1] = new Neuron();
		n2[1].outputNodeRepresentation = "a";
		n2[1].value = .75;
		n2[2] = new Neuron();
		n2[2].outputNodeRepresentation = "c";
		n2[2].value = .30;
		testWord[2] = new PotentialChar(n2);
		
		Neuron [] n3 = new Neuron[3];
		n3[0] = new Neuron();
		n3[0].outputNodeRepresentation = "g";
		n3[0].value = .85;
		n3[1] = new Neuron();
		n3[1].outputNodeRepresentation = "j";
		n3[1].value = .80;
		n3[2] = new Neuron();
		n3[2].outputNodeRepresentation = "y";
		n3[2].value = .80;
		testWord[3] = new PotentialChar(n3);
		
		//Here is where the madness ends.
		
		Agent a = new Agent();
		
		ArrayList<PotentialWord> combos=  a.genCombos(testWord);
		
		a.scoreByProbability(combos);
		for(int i=0;i<combos.size();i++){
			System.out.println(combos.get(i));
		}
		
	}
	
	/**
	 * Prints out the contents of a potential word.
	 * @param p - the {@link PotentialWord} to print out to the console for testing purposes.
	 */
	private static void printWord( PotentialWord p ){
		
		for( int i = 0; i < p.CHARS.length; i++ ){
			
			System.out.print( p.CHARS[i].outputNodeRepresentation + " - " + p.CHARS[i].value + "\t");
			
		}
		
		System.out.println();
		
	}
	
	
	public static void main( String args[]){ new AgentTestingHarness(); }
	
}
