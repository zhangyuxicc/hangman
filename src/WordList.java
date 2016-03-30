import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
/**
 * This class reads in all the words from dictionary.txt
 * and map each word with its length
 * @author Grace
 *
 */
public class WordList {
	/**
	 * instance variable
	 */
	private HashMap<Integer, ArrayList<String>> wordLength;
	/**
	 * constructor to initialize instance variable
	 */
	public WordList(){
		wordLength = new HashMap<Integer, ArrayList<String>>();
	}
	/**
	 * This method reads dictionary.txt to store all words into a HashMap
	 */
	public void getWords(){
		try {
			File inputFile = new File("dictionary.txt");
			Scanner in = new Scanner(inputFile);			
			ArrayList<String> words;
			while(in.hasNextLine()){
				String word = in.nextLine();
				
				if (wordLength.containsKey(word.length())) {
					words = wordLength.get(word.length());
					words.add(word);
				} else {
					words = new ArrayList<String>();
					words.add(word);
					wordLength.put(word.length(), words);
				}
			}		
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
	 * This method will construct a word list matching the input word length 
	 * @param len input word length
	 * @return the ArrayList of words
	 */
	public ArrayList<String> pickLength(int len){
		return wordLength.get(len);
	}

}
