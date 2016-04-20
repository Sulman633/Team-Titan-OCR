package decisionmakingagent;

import neuralnetwork.Neuron;
/**
 * Test harness for the Agent package. Currently tests the Agent's ability to generate words from a list of PotentialChars.
 * @author Nathan Van Dyken
 *
 */
public class AgentTestingHarness {

	PotentialChar [] testWord;
	Agent a;
	
	public AgentTestingHarness() {
		
		a = new Agent();
		
		initWords();
		a.assess(testWord);
		//testGenerationOfWords();
		
	}
	/**
	 * Manually set up neurons to be used by the Agent; they will not be necessary in actual implementation.
	 */
	private void initWords(){
		
		testWord = new PotentialChar [4];
		Neuron [] n0 = new Neuron[4];
		n0[0] = new Neuron();
		n0[0].outputNodeRepresentation = "f";
		n0[0].value = .90;
		n0[1] = new Neuron();
		n0[1].outputNodeRepresentation = "t";
		n0[1].value = .80;
		n0[2] = new Neuron();
		n0[2].outputNodeRepresentation = "l";
		n0[2].value = .75;
		n0[3] = new Neuron();
		n0[3].outputNodeRepresentation = "p";
		n0[3].value = .40;
		testWord[0] = new PotentialChar(n0);
		
		Neuron [] n1 = new Neuron[4];
		n1[0] = new Neuron();
		n1[0].outputNodeRepresentation = "r";
		n1[0].value = .95;
		n1[1] = new Neuron();
		n1[1].outputNodeRepresentation = "n";
		n1[1].value = .90;
		n1[2] = new Neuron();
		n1[2].outputNodeRepresentation = "p";
		n1[2].value = .90;
		n1[3] = new Neuron();
		n1[3].outputNodeRepresentation = "a";
		n1[3].value = .60;
		testWord[1] = new PotentialChar(n1);
		
		Neuron [] n2 = new Neuron[4];
		n2[0] = new Neuron();
		n2[0].outputNodeRepresentation = "o";
		n2[0].value = .90;
		n2[1] = new Neuron();
		n2[1].outputNodeRepresentation = "a";
		n2[1].value = .75;
		n2[2] = new Neuron();
		n2[2].outputNodeRepresentation = "c";
		n2[2].value = .30;
		n2[3] = new Neuron();
		n2[3].outputNodeRepresentation = "p";
		n2[3].value = .15;
		testWord[2] = new PotentialChar(n2);
		
		Neuron [] n3 = new Neuron[4];
		n3[0] = new Neuron();
		n3[0].outputNodeRepresentation = "g";
		n3[0].value = .85;
		n3[1] = new Neuron();
		n3[1].outputNodeRepresentation = "q";
		n3[1].value = .80;
		n3[2] = new Neuron();
		n3[2].outputNodeRepresentation = "a";
		n3[2].value = .80;
		n3[3] = new Neuron();
		n3[3].outputNodeRepresentation = "j";
		n3[3].value = .75;
		testWord[3] = new PotentialChar(n3);
		
	}	
	
	public static void main( String args[]){ new AgentTestingHarness(); }
	
}
