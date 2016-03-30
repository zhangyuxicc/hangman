import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * This class represents a hangman game using the evil hangman algorithm
 * @author Grace
 *
 */
public class Hangman {
	
	/**
	 * instance variables
	 */
	private ArrayList<String> candidateWords;
	private int numberOfGuesses;
	private int lengthOfWord;
	private StringBuilder result;
	private String position;
	private boolean knowWordNum;
	
	/**
	 * constructor to initialize all instance variables
	 */
	public Hangman(){
		candidateWords = new ArrayList<String>();
		numberOfGuesses = 0;
		lengthOfWord = 0;
		result = new StringBuilder();
		position = "";	
		knowWordNum = false;
	}
	
	/**
	 * This method initialize the game, 
	 * ask user to input word length and number of guesses,
	 * then generating the corresponding word family 
	 */
	private void initialize(){	
		WordList wl = new WordList();
		wl.getWords();
		boolean valid = false;
		while(valid != true){
			valid = getInput();
			candidateWords = wl.pickLength(lengthOfWord);
			if(candidateWords == null){
				valid = false;
				System.out.println("Sorry, word length is invalid(too long).");
			}
		}
		for(int i = 0; i < lengthOfWord; i++){
			result.append('-');
		}
		if(knowWordNum){
			System.out.println("Number of words:" + candidateWords.size());
		}		
	}
	
	/**
	 * This method starts the game of hangman
	 * player can choose to play again after each round
	 */
	public void playGame(){
		char guess;
		int count = 0;
		ArrayList<Character> letterGuessed = new ArrayList<Character>();

		initialize();
		Scanner in = new Scanner(System.in);
		while(numberOfGuesses > 0){
			
			// Display message to user to track the progress of game
			System.out.println("Remaining number of guesses: " + numberOfGuesses);
			System.out.println("Letters have been guessed " + letterGuessed);	
			System.out.println("Please make a guess");		
			// only take in the first character if user input more than one at a time
			guess = in.next().charAt(0);
			letterGuessed.add(guess);
			// get the most common word family based on user's guess
			getWordFamily(guess);
			// if user guess wrongly, decrement the remaining number of guesses
			// otherwise show the location of the letter to user
			if(position.length() == 0){
				numberOfGuesses --;
			}else{
				for(int i = 0; i < position.length(); i++){
					count = position.charAt(i) - 'A';
					result.setCharAt(count, guess);
				}
			}
			// if there is only one word remain in the word family 
			// and it is the same as user's guess result, user wins!
			if(candidateWords.size() == 1 && candidateWords.get(0).equals(result.toString())){		
				System.out.println(result);
				System.out.println("Congratulations!! You got it!!");
				return;
			}
			System.out.println(result);
		}
		System.out.println("Sorry, you have run out of guesses.");
		System.out.println("The secret word is: " + candidateWords.get(0));
		return;		
	}
	
	/**
	 * This method get player's input for word length and number of guesses
	 * @return true only if both input are valid, otherwise false
	 */
	private boolean getInput(){
		Scanner in = new Scanner(System.in);
		System.out.println("Please enter length of word and number of guesses:");
		try{
			lengthOfWord = in.nextInt();
			numberOfGuesses = in.nextInt();		
		} catch (InputMismatchException e){	
			return false;
		} catch (NoSuchElementException e){
			return false;
		}
		System.out.println("Do you want to know number of remaining words? y/n");
		String str = in.next();
		if(str.equals("y")) knowWordNum = true;
		
		return true;
	}
	
	/**
	 * This method finds out the most common word family based on player's input
	 * @param targetLetter letter entered by player
	 */
	private void getWordFamily(char targetLetter){
		
		HashMap<String, ArrayList<String>> wordFamily = new HashMap<String, ArrayList<String>>();
		ArrayList<String> words;
		// map letter position and words 
		for(String word:candidateWords){
			position = "";
			for(int i = 0; i < word.length();i++){
				if(word.charAt(i) == targetLetter){
					position += (char)('A' + i);
				}
			}
			if (wordFamily.containsKey(position)) {
				words = wordFamily.get(position);
				words.add(word);
			} else {
				words = new ArrayList<String>();
				words.add(word);
				wordFamily.put(position, words);
			}		
		}
		
		int number = 0;
		// iterate through the HashMap to get the most common word family
		for(String s: wordFamily.keySet()){
			if(number < wordFamily.get(s).size()){
				number = wordFamily.get(s).size();
				position = s;
				candidateWords = wordFamily.get(s);				
			}
			// if the number of two word families are same, 
			// choose the family with lesser target letter
			if(number == wordFamily.get(s).size()){
				if(s.length() < position.length()){
					position = s;
					candidateWords = wordFamily.get(s);	
				}
			}
		}
		
		// Track the remaining number of words, make it easy to test
		if(knowWordNum){
			System.out.println("number of remaining words is "+ number);
		}		
	}
}
