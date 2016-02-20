package lexicon;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Lexicon {

	Connection conn;
	private final String DB_USERNAME = "sql5107588";
	private final String DB_PASSWORD = "RyLFh7bdvT"; // RyLFh7bdvT
	private final String DB_URL = "jdbc:mysql://sql5.freemysqlhosting.net/sql5107588";
	
	public Lexicon() {
		
		loadDB();
		
	}
	/**
	 * Establishes the connection with the SQL database.
	 */
	private void loadDB(){
		
		
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
	 * @param candidates an ArrayList of the candidate Strings to be evaluated, with the unknown characters replaced with underscore characters ('_'). "Unknown characters" refer to characters that have a level of confidence that falls below a certain level of tolerance, specified in the decision making agent.
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
	 * @param candidates an array of the candidate Strings to be evaluated, with the unknown characters replaced with underscore characters ('_'). "Unknown characters" refer to characters that have a level of confidence that falls below a certain level of tolerance, specified in the decision making agent.
	 * @return an array of possible words with a frequency of use for each word.
	 */
	public PossibleWord [] getMatches( String [] candidates ){
		
		ArrayList<String> temp = new ArrayList<String>(Arrays.asList(candidates));
		
		return (PossibleWord[]) getMatches( temp ).toArray();
		
	}
	/**
	 * Checks if the specified character pattern is present in the DB and returns matches, along with a frequency of use for each match.
	 * @param candidateWord	the character pattern that will be looked up in the DB.
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
	
	public void useWord( String newWord ){
		
		try {
			
			Statement stmt = conn.createStatement();
			
			if( !stmt.executeQuery("SELECT * FROM entries where word = '"+newWord+"'").next()){			//If there is no result from the query, add the word to the DB.
				
				stmt.executeUpdate("INSERT INTO entries VALUES ('"+newWord+"', 1)");
				
			}else{
				
				ResultSet rS = stmt.executeQuery("SELECT freq FROM entries where word = '"+newWord+"'");
				
				int tempFreq = rS.getInt("freq");
				
				stmt.executeUpdate("INSERT INTO entries VALUES ('"+newWord+"', 1)");
				
			}
			
			
		} catch (SQLException e) {
			System.out.println("Exception caught in Lexicon when adding a word to the DB in the addWord method.");
			e.printStackTrace();
		}
		
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
