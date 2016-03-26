package decisionmakingagent;

import neuralnetwork.Neuron;;
/**
 * The PotentialChar class acts solely as a wrapper for the output of the neural network.
 * The resulting array of {@link Neuron}s is encapsulated to increase readability.
 * @author Nathan Van Dyken
 *
 */
public class PotentialChar {

	/**
	 * The output from the neural net is held assigned to the NEURONS field.
	 */
	public final Neuron [] NEURONS;
	
	public PotentialChar( Neuron [] newNeurons ) {

		NEURONS = newNeurons;
		
	}

}
