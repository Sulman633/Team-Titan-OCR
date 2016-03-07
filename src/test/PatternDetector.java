package test;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PatternDetector {
	
	private pixel[][] image;
	private int[] rowPixelWeight;
	private int averageLineHeight;
	private ArrayList<Integer> lineCentres;
	private int wordCount;
	private ArrayList<Integer> wordBeginning, wordEnding, wordHeight;
	private int letterCount;
	private ArrayList<Integer> letterBeginning, letterEnding, letterHeight, letterBelongsTo;
	private ArrayList<BufferedImage> letters;
	
	private final double THRESHOLD = 0.2;
	
	public PatternDetector () {
		
		lineCentres = new ArrayList();
		
		wordBeginning = new ArrayList();
		wordEnding = new ArrayList();
		wordHeight = new ArrayList();
		wordCount = 0;
		
		letterBeginning = new ArrayList();
		letterEnding = new ArrayList();
		letterHeight = new ArrayList();
		letterBelongsTo = new ArrayList();
		letterCount = 0;
		
		letters = new ArrayList();
		                                                                                                                                                                                                                                                                                                                                               
	}
		
	public void scan (pixel[][] i, boolean mode) {
		
		image = i;
		
		rowPixelWeight = new int[image[0].length];
		
		getRowWeights();
		guessLineHeight();
		findLineCentres();
		findWords();
		findLetters();
		if (mode)
			packageLetters();
		else
			packageLetters2();
		
	}
	
	//This gets the number of pixels in each row of the image.
	private void getRowWeights () {

		for (int row = 0; row < image[0].length; row++) {
			
			for (int col = 0; col < image.length; col++) {
				
				if (!image[col][row].checkWhite()) 
					rowPixelWeight[row]++;
					
			}

		}
		
	}
	
	//This will guess the average line height for scanning the lines for words.
	private void guessLineHeight () {
		
		boolean onInk = false;
		int maxHeight = 0;
		int mostOccurring = 0;
		int temp = 0;
		int count = 0;
		int[] heightRates;
		ArrayList<Integer> lineHeights = new ArrayList();
		
		//Iterate through all row weights and find heights of individual rows.
		//In this context, temp is the current line height.
		for (int row = 0; row < image[0].length; row++) {
			
			if (!onInk && rowPixelWeight[row] > 0)
				onInk = true;
			
			if (onInk) {
				
				//If the line has no more pixels, add the height to the list.
				if (rowPixelWeight[row] == 0) {
					
					onInk = false;
					lineHeights.add(temp);
					
					if (temp > maxHeight)
						maxHeight = temp;
					
					temp = 0;
					
				} else
					temp++;
				
			}
			
		}
		
		heightRates = new int[maxHeight + 1];
		
		//Iterate through the arraylist to find occurrences of unique heights.
		//In this context, temp is the current height.
		while (!lineHeights.isEmpty()) {
			
			temp = lineHeights.remove(0);
			heightRates[temp]++;
			
		}
		
		//This finds the most occurring line height.
		for (int i = 0; i < heightRates.length; i++) 
			if (heightRates[i] > heightRates[mostOccurring])
				mostOccurring = i;
			
		
		temp = 0;
		count = 0;
		
		for (int i = 0; i < heightRates.length; i++) {
			
			if (heightRates[i] != 0 && Math.abs(mostOccurring - i) < ((double) heightRates.length * THRESHOLD)) {
			
				temp += (i * heightRates[i]);
				count += heightRates[i];
				
			}
			
		}
		
		averageLineHeight = (int) ((double) temp / (double) count);
		
		System.out.println("Avg. Line Height:\t" + averageLineHeight);		
		
	}
	
	private void findLineCentres () {
		
		boolean onInk = false;
		int temp = 0;
		
		for (int row = 0; row < image[0].length; row++) {
			
			if (!onInk && rowPixelWeight[row] > 0)
				onInk = true;
			
			if (onInk) {
				
				temp++;
				
				if (rowPixelWeight[row] == 0) {
					
					if (temp >= averageLineHeight - (averageLineHeight * THRESHOLD * 2))
						lineCentres.add(row - (temp / 2));
					
					temp = 0;
					
				} 
				
			}
			
		}
		
		System.out.println("Line Locations:\t\t" + lineCentres.toString() + "\n");
		
	}
	
	private void findWords () {
		
		int halfHeight = averageLineHeight / 2;
		int currentLine;
		int[] linePixelWeight;
		int[] smoothedLinePixelWeight;
		int boostFactor = 5;
		int smoothFactor = 5;
		
		int wordStart = 0;
		int wordLength = 0;
		boolean onInk= false;
		
		for (int i = 0; i < lineCentres.size(); i++) {
			
			currentLine = lineCentres.get(i);
			linePixelWeight = new int[image.length];
			smoothedLinePixelWeight = new int[image.length];
			
			for (int col = 0; col < image.length; col++) {
				
				for (int row = currentLine - halfHeight; row <= currentLine + halfHeight; row++) {
					
					if (!image[col][row].checkWhite()) 
						linePixelWeight[col] += (averageLineHeight - (row - (currentLine - halfHeight))) * boostFactor;
					
				}
				
				for (int j = (smoothFactor / 2); j < smoothedLinePixelWeight.length - (smoothFactor / 2); j++) {
					
					for (int k = 0; k < smoothFactor; k++)
						smoothedLinePixelWeight[j] += linePixelWeight[j - (smoothFactor / 2) + k];
					
					smoothedLinePixelWeight[j] = smoothedLinePixelWeight[j] / smoothFactor;
					
				}
				
			}
			
			for (int col = 0; col < image.length; col++) {
				
				if (!onInk && smoothedLinePixelWeight[col] != 0) {
					
					onInk = true;
					wordStart = col;
					wordLength = 0;
					
				}
				
				if (onInk) {
					
					wordLength++;
					
					if (smoothedLinePixelWeight[col] == 0) {
				
						wordBeginning.add(wordStart);
						wordEnding.add(wordStart + wordLength);
						wordHeight.add(lineCentres.get(i) - halfHeight);
						wordCount++;
						
						onInk = false;
						
											
					}
					
				}
				
			}
			
		}
		
	}
	
	private void findLetters () {
		
		int currentStart = 0, currentEnd = 0, currentHeight = 0;
		int letterStart = 0, letterEnd = 0;
		boolean letterMode = false;
		
		int[] histogram;
		for (int word = 0; word < wordCount; word++) {
			
			currentStart = wordBeginning.get(word);
			currentEnd = wordEnding.get(word);
			currentHeight = wordHeight.get(word);
			
			histogram = new int[currentEnd - currentStart];
			
			for (int x = 0; x < histogram.length; x++) {
				
				for (int y = currentHeight; y < currentHeight + averageLineHeight; y++) {
					
					if (!image[currentStart + x][y].checkWhite())
						histogram[x] += 3;
					
				}
				
			}
						
			for (int x = 0; x < histogram.length; x++) {
				
				if (histogram[x] > 0 && !letterMode) {
					
					letterMode = true;
					letterStart = currentStart + x;
					
				} else if (histogram[x] == 0 && letterMode) {
					
					letterMode = false;
					letterEnd = currentStart + x;
					
					letterCount++;
					letterBeginning.add(letterStart);
					letterEnding.add(letterEnd);
					letterHeight.add(currentHeight);
					letterBelongsTo.add(word);
					
				}
				
				
			}
			
		}
			
	}
	
	private void packageLetters () {
		
		int letterLength = 0;
		int letterStartX = 0, letterStartY = 0;
		BufferedImage letter;
		int offset;
		
		for (int i = 0; i < letterCount; i++) {
			
			letterStartX = letterBeginning.get(i);
			letterStartY = letterHeight.get(i);
			letterLength = letterEnding.get(i) - letterBeginning.get(i);
			
			if (letterLength > averageLineHeight) {
				
				letter = new BufferedImage(letterLength, letterLength, BufferedImage.TYPE_INT_RGB);
				offset = 0;
				
			} else {
				
				letter = new BufferedImage(averageLineHeight, averageLineHeight, BufferedImage.TYPE_INT_RGB);
				offset = (averageLineHeight - letterLength) / 2;
				
			}
			
			for (int x = 0; x < letter.getWidth(); x++)
				for (int y = 0; y < letter.getHeight(); y++)
					letter.setRGB(x, y, -1);
		
			for (int x = 0; x < offset + letterLength; x++) {
				
				for (int y = 0; y < letter.getHeight(); y++) {
					
					if(image[letterStartX + x][letterStartY + y].checkWhite())
						letter.setRGB(x + offset, y, -1);
					else
						letter.setRGB(x + offset, y, -16777216);
					
				}
				
			}
			
			letters.add(letter);
	
		}
		
	}
	
	private void packageLetters2 () {
		
		int letterLength = 0;
		int letterStartX = 0, letterStartY = 0;
		BufferedImage letter;
		
		for (int i = 0; i < letterCount; i++) {
			
			letterStartX = letterBeginning.get(i);
			letterStartY = letterHeight.get(i);
			letterLength = letterEnding.get(i) - letterBeginning.get(i);
				
			letter = new BufferedImage(letterLength, averageLineHeight, BufferedImage.TYPE_INT_RGB);
			
			for (int x = 0; x < letter.getWidth(); x++)
				for (int y = 0; y < letter.getHeight(); y++)
					letter.setRGB(x, y, -1);
		
			for (int x = 0; x < letter.getWidth(); x++) {
				
				for (int y = 0; y < letter.getHeight(); y++) {
					
					if(image[letterStartX + x][letterStartY + y].checkWhite())
						letter.setRGB(x, y, -1);
					else
						letter.setRGB(x, y, -16777216);
					
				}
				
			}
			
			letters.add(letter);
	
		}
		
	}
	
	 
	private int[] smoothIntArray (int[] in, int radius) {
		
		int[] result = new int[in.length];
		int temp;
		
		for (int i = radius; i < result.length - radius; i++) {
			
			temp = 0;
			
			for (int j = -radius; j < radius; j++) {
				
				temp += in[i + j];
				
			}
			
			result[i] = temp;
			
		}
		
		return result;
		
	}
	
	private ArrayList<Integer> findMaxima (int[] in) {
		
		ArrayList<Integer> result = new ArrayList();
		int temp = 0;
		int max = 0;
		boolean rising = false;
		
		for (int i = 0; i < in.length; i++) {
			
			temp = in[i];
			
			if (temp > max) {
				
				max = temp;
				rising = true;
				
			} else {
				
				if (rising) {
					
					result.add(i);
					rising = false;
					
				}
				
			}
			
		}
		
		return result;
		
	}
	
	public int getLineHeight () {
		
		return averageLineHeight;
		
	}
	
	public ArrayList<Integer> getLineCentres () {
		
		return lineCentres;
		
	}
	
	public ArrayList<Integer> getWordBeginning () {
		
		return wordBeginning;
		
	}

	public ArrayList<Integer> getWordEnding () {
	
		return wordEnding;
	
	}

	public ArrayList<Integer> getWordHeight () {
	
		return wordHeight;
	
	}
	
	public ArrayList<Integer> getLetterBeginning () {
		
		return letterBeginning;
		
	}

	public ArrayList<Integer> getLetterEnding () {
	
		return letterEnding;
	
	}

	public ArrayList<Integer> getLetterHeight () {
	
		return letterHeight;
	
	}
	
	public int getWordCount () {
		
		return wordCount;
		
	}
	
	public int getLetterCount () {
		
		return letterCount;
		
	}
	
	public ArrayList<BufferedImage> getLetters () {
		
		return letters;
		
	}

}
