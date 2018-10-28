//Trevor Nichols
//Hangman Manager
//Section: AM; William Ceriale

//Creates a manager for Evil Hangman that determines word patterns
//and creates word pools so the computer can delay picking a word.
//Records players guesses creating new patterns and word pools.
//Keeps track of the players current guesses and how many guesses
//the player has remaining.

import java.util.*;

public class HangmanManager {
    private Set<String> wordSet;
    private Set<Character> guessed;
    private int remainingGuesses;
    private String currentPattern;
    
    //Creates an instance of a Hangman game setting the target word length 
    //and number of guesses. Using a dictionary to create an initial word pool.
    //throws IllegalArgumentException if the length of the word is less that 1
    //or the number of guesses is less than zero
    public HangmanManager (Collection<String> dictionary, int length, int max){
        if ((length < 1)||(max < 0)){
            throw new IllegalArgumentException 
                        ("Either the length of the word is less than one or"
                               + " the number of guesses is less than zero");
        }       
        this.wordSet = new TreeSet<String>();
        this.guessed = new TreeSet<Character>();
        this.currentPattern = this.getKey(length);
        this.remainingGuesses = max; 
        for(String word : dictionary){
            if(word.length() == length){
                this.wordSet.add(word);
            }
        }         
    }
   
    //Returns the pool of words the computer is still considering for the game.
    public Set<String> words(){
        Set<String> output = new TreeSet<String>(this.wordSet);
        return output;
    }
   
    //Returns the number of guesses the player has remaining.
    public int guessesLeft(){
        return this.remainingGuesses;
    }
   
    //Returns the characters that have been guessed so far in the game.
    public SortedSet<Character> guesses(){
        SortedSet<Character> tempSet = new TreeSet<Character>(this.guessed); 
        return tempSet;
    }
   
    //Returns the current pattern the game is using.
    //The pattern in this case is a String representation of a Hangman word
    //where "-" are unknown letters and known letters are 
    //represented by themselves.
    //throws IllegalStateException if the current pool of words is empty.
    //example
    //      "- - - e -"
    public String pattern(){
        if(this.wordSet.isEmpty()){
            throw new IllegalStateException
                            ("the current set of words is empty");
        }
        return this.currentPattern;
    }
   
    //Records a player making a character guess for the current game.
    //Updates the pattern and word pool.
    //throws IllegalStateException if the remaining guesses a player 
    //has left is less than 1 or if the current word pool is empty.
    //throws IllegalArugumentExcpetion if the word pool is not empty and 
    //the player guesses a duplicate character.
    public int record(char guess){
        if((this.remainingGuesses < 1) || this.wordSet.isEmpty()){
            throw new IllegalStateException("There are no more remaining "
                                    + "guesses or the pool of words is empty");
        }      
        if(!this.wordSet.isEmpty() && this.guessed.contains(guess)){
            throw new IllegalArgumentException
                             ("That letter was already guessed");
        }
        this.guessed.add(guess);
        Map<String, Set<String>> tempStorage = 
                new TreeMap<String, Set<String>>();     
        tempStorage = this.createMap(guess);            
        String tempKey = this.determineLargest(tempStorage);  
        if (tempKey.equals(this.currentPattern)){
            this.remainingGuesses--;
        }      
        int numOccurrences = countOccurences(tempKey, guess);      
        this.currentPattern = tempKey;   
        this.wordSet = tempStorage.get(tempKey);
        return numOccurrences;
    }
   
    //Returns a pattern given an integer.
    private String getKey(int length){
        String output = "-";
        for(int i = 0; i < length-1; i++){
            output += " -";
        }
        return output;
    }
    
    //Returns a pattern given a character and a word.
    private String getKey(char input, String word){
        String output = "";
        int wordIndex = 0;
        for(int i = 0; i < this.currentPattern.length(); i++){
            if(this.currentPattern.charAt(i) != ' '){ 
                if(this.currentPattern.charAt(i)!= '-'){
                    output += this.currentPattern.charAt(i);
                }else if(input == word.charAt(wordIndex)){
                    output += input;
                }else{
                    output += "-";
                }
                wordIndex++;
            }else{
                output += " ";
            }
        }
        return output;
    }
    
    //Creates the pools of words for the computer to use
    private Map<String, Set<String>> createMap(char guess){
        Map<String, Set<String>> tempStorage = 
                new TreeMap<String, Set<String>>();     
        for(String word : this.wordSet){
            String key = this.getKey(guess, word);
            if(!tempStorage.containsKey(key)){
                tempStorage.put(key, new TreeSet<String>());
            }
            tempStorage.get(key).add(word);
        }
        return tempStorage;
    }
    
    //Determines the largest pool of words.
    private String determineLargest(Map<String , Set<String>> wordSet){
        int size = 0;
        String tempKey = "";      
        for(String key : wordSet.keySet()){
            if(size < wordSet.get(key).size() ){
                size = wordSet.get(key).size();
                tempKey = key;
            }
        }   
        return tempKey;
    }
    
    //Determines the number of occurrences of a given character in a given 
    //key.
    private int countOccurences(String key, char guess){
        int numOccurrences = 0;
        for(int i = 0; i < key.length(); i++){
            if(key.charAt(i) == guess){
                numOccurrences++;
            }
        }
        return numOccurrences;
    }
}
