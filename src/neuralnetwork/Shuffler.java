/*	Shuffler
	Anthony Ferski
	5192885
*/	

package neuralnetwork;

import java.lang.Math;

public class Shuffler {

	public void printList(int[] list) {

		//BEGIN
		//iteratively print the list
		for (int i = 0; i < list.length; i++)
			System.out.print(list[i] + ", ");
		System.out.println();

	} //END printList

	public int[] createList(int length) {

		//IS
		int[] r = new int[length];

		//BEGIN
		//iteratively create a list of ordered numbers from 0 to i
		for (int i = 0; i < r.length; i++)
			r[i] = i;

		return r;

	} //END createList

	public int[] shuffle(int[] list) {

		//IS
		int current, temp;

		//BEGIN
		//the i value begins at the end and approaches the beginning of the array
		//numbers from the beginning to i are selected at random to be swapped to the i position
		//in this way, values coming after the ith element are in random order
		for (int i = list.length; i > 0; i--) {

			current = (int) ((double) i * Math.random());	
			temp = list[i - 1];
			list[i - 1] = list[current];
			list[current] = temp;

		}

		return list;
	
	} //END shuffle

}