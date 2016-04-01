package lexicon;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

import decisionmakingagent.PotentialWord;
/**
 * Used by the decision making agent to select a word from a list of possibilities, the lexicon generates a list of candidate words, along with a frequency of use, when presented with an incomplete word or list of words.
 * Words are pulled from a pre-existing mySQL database that is loaded from a specified address. The lexicon also updates the frequency of use of a specific word in the aforementioned database when the word is chosen as the best candidate by the decision making agent. 
 * @author Nathan Van Dyken
 */
public class Lexicon {

	/**
	 * The connection to the mySQL database. It is opened in the private loadDB method and is closed when the object is destroyed.
	 */
	Connection conn;

	/**
	 * The address of the mySQL server which holds the lexicon.
	 */
	private final String DB_URL = "jdbc:mysql://sql5.freemysqlhosting.net/sql5112409";
	/**
	 * The username that will be used to log into the mySql server.
	 */
	private final String DB_USERNAME = "sql5112409";
	/**
	 * The password that will be used to log into the mySql server.
	 */

	private final String DB_PASSWORD = "HTqAhsXIZj"; // RyLFh7bdvT

	
	public Lexicon() {
		
		loadDB();
		
	}
	/**
	 * Establishes the connection with the SQL database.
	 */
	public void loadDB(){
		
		
		try{
			
			String driverName = "com.mysql.jdbc.Driver";
			Class.forName(driverName);
			
			conn = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
		}catch(Exception e){
			
			System.out.println("Exception caught in Lexicon when loading the DB. Either the class specified by driverName was not found or a connection to the SQL Database could not be made.");
			
			e.printStackTrace();
			
		}
		
	}
	/**
	 * Called by the decision making agent; accepts a list of candidate partial-words and returns complete words that are possibly the words that the user attempted to write.
	 * @param candidates - an ArrayList of the candidate Strings to be evaluated, with the unknown characters replaced with underscore characters ('_'). "Unknown characters" refer to characters that have a level of confidence that falls below a certain level of tolerance, specified in the decision making agent.
	 * @return an ArrayList of possible words with a frequency of use for each word.
	 */
	public ArrayList<PossibleWord> getMatches( ArrayList<String> candidates ){
		
		ArrayList<PossibleWord> bestMatches = new ArrayList<PossibleWord>();
		
		for( String currentWord: candidates  ){															//For every potential word submitted from the NN, the best matches are found and added to the ArrayList to be returned.
			
			bestMatches.addAll(getPossibilities(currentWord));
			
		}
		
		return bestMatches;
		
	}
	/**
	 * Same as {@code Lexicon.getMatches(ArrayList<String> candidates)}, except that it accepts and returns arrays as opposed to  ArrayLists.
	 * @param candidates - an array of the candidate Strings to be evaluated, with the unknown characters replaced with underscore characters ('_'). "Unknown characters" refer to characters that have a level of confidence that falls below a certain level of tolerance, specified in the decision making agent.
	 * @return an array of possible words with a frequency of use for each word.
	 */
	public PossibleWord [] getMatches( String [] candidates ){
		
		ArrayList<String> temp = new ArrayList<String>(Arrays.asList(candidates));
		
		ArrayList<PossibleWord> resultantArrayList = getMatches( temp );
		PossibleWord [] resultantArray = new PossibleWord[resultantArrayList.size()];
		
		for( int i = 0; i < resultantArrayList.size(); i++ ){
			
			resultantArray[i] = resultantArrayList.get(i);
			
		}
		
		return resultantArray;
		
	}
	/**
	 * Checks if the specified character pattern is present in the DB and returns matches, along with a frequency of use for each match.
	 * @param candidateWord - the character pattern that will be looked up in the DB.
	 * @return an ArrayList of possible words, along with a frequency of use for each word.
	 */
	private ArrayList<PossibleWord> getPossibilities( String candidateWord ){
		
		ArrayList<PossibleWord> result = new ArrayList<>();
		candidateWord = candidateWord.toLowerCase();
		
		try {
			
			Statement stmt = conn.createStatement();
			ResultSet rS;
			
			rS = stmt.executeQuery("SELECT * FROM entries where word LIKE '"+candidateWord+"'");		//Query the DB for all Strings matching the specified pattern.
			
			while( rS.next() ){																			//For every result from the query, a new PossibleWord object is created with the frequency pulled from the query and will be returned at the end of the method.
				
				String word = rS.getString("word");
				int freq = rS.getInt("freq");
				result.add(new PossibleWord(word, freq));
				
			}
			
			
		} catch (SQLException e) {
			System.out.println("Exception caught in Lexicon when querying the DB in the getPossibilities method. There is an error in the SQL query made.");
			e.printStackTrace();
		}
		
		return result;
		
	}
	/**
	 * Updates the database to reflect the use of the specified word. If the word does not exist in the DB, a record is created and the frequency of use of the word is initialized to one. Else, if the word does exist, the frequency of use of the word is incremented by one.
	 * @param newWord - the word for which the frequency of use should be updated.
	 */
	public void useWord( String newWord ) throws InvalidWordException{
		
		newWord = newWord.toLowerCase();
		
		String message = isValidWord(newWord);
		
		if( message != null) throw new InvalidWordException(message);
		
		try {
			
			Statement stmt = conn.createStatement();
			
			ResultSet rS = stmt.executeQuery("SELECT * FROM entries where word = '"+newWord+"'");
			
			if( !rS.next()){																			//If there is no result from the query, add the word to the DB.
				
				stmt.executeUpdate("INSERT INTO entries VALUES ('"+newWord+"', 1)");
				
			}else{																						//Otherwise, get the current frequency, increment it by one, then update the corresponding field in the DB.
				
				int newFreq = rS.getInt("freq") + 1;
				
				stmt.executeUpdate("UPDATE entries SET freq = "+newFreq+" WHERE word ='"+newWord+"'");
				
			}
			
			
		} catch (SQLException e) {
			System.out.println("Exception caught in Lexicon when adding a word to the DB in the addWord method.");
			e.printStackTrace();
		}
		
	}
	/**
	 * Checks the validity of a word that is to be inserted into the database.
	 * @param word - the string to be checked. 
	 * @return an empty string if the word passed in can legally be inserted into the database without causing an exception.  Else, the message corresponding with the issue is returned.
	 */
	private String isValidWord(String word) {
		
		if( word.equals("") ) return "An empty string can not be inserted in the database as a word.";
		
		if( word.length() > 25 ) return "The provided word exceeds the 25 character limit of the database.";
		
		
		
		return "";
		
	}
	
	/**
	 * Called when the object is destroyed. Only functions to close to the connection to the MySQL database.
	 */
	public void finalize(){
		
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("Exception caught in Lexicon when closing the connection with the DB in the finalize method.");
			e.printStackTrace();
		}
		
	}
	
}
