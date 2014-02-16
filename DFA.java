/**
 * DFA.java
 * Authors:  Jasper Forest
 * 
 * This programs takes in a text file form the command line with a given format and returns 
 * whether or not the given inputs into the DFA are accepting or rejecting. 
 * The file format has to be as follows:
 * 
 * 
 * 		# of states			Example: 6 
 * 		Language			Example: 01
 * 		Transition Function	Example: 1 '0' 4 
 * 			.
 * 			.	 Each one separated with a new line.
 * 			.
 * 		State State			Example: 1
 * 		Accepting States	Example: 3 6
 * 		Input Strings		Example: 010101010
 * 			.
 * 			.	Each one separated with a new line.
 * 			.
 * 
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DFA {
	
	public static void main(String [] args) {
		try {
			
			File inputFile = new File(args[0]); // Input File
			Scanner input; // Scanner to scan input file
			input = new Scanner(inputFile);
			int state_count = 0; // State count
			String alphabet = ""; // Alphabet
			int  start_state = 0; // Start state variable
			
			/*
			System.out.println("\nWelcome to Kyle and Jasper's DFA creating program!\n");
			System.out.println("Loading States...");
			*/
			
			state_count = input.nextInt(); // gets number of states
			
			/*
			System.out.println("Loading Alphabet...");
			*/
			
			alphabet = input.next(); // Gets alphabet
			
			/*
			System.out.println("Loading transition functions...");
			*/
			
			// Vertical - States, Horizontal - alphabet
			State_info[][] trans_mat = new State_info[state_count][alphabet.length()]; 
			
			//Loops through all the states and alphabet inputs and sets a transition to each in the matrix
			for(int trans_count = state_count * alphabet.length(); trans_count > 0; trans_count--){	
				
				int source_state, dest_state;// Starting and destination states.
				source_state = input.nextInt(); // source state Example: 1
				
				// Gets the character value of the source input.
				String tmp2; 
				tmp2 = input.next();  // alphabet input Example: 'c'
				int alph_input = alphabet.indexOf(tmp2.charAt(1)); // Finds the location of the character in the alphabet
				
				// Checks to see if the user's input file contained bad transitions functions and exits if so
				if(alph_input == -1){
					System.out.println("The character: \"" + tmp2.charAt(1) + "\" is not in the given alphabet of your language.");
					System.out.println("Program is terminating.");
					System.exit(0);
				}
				
				dest_state = input.nextInt(); // destination state Example: 
			
				//Puts all the information into the matrix
				trans_mat[source_state-1][alph_input] = new State_info(dest_state-1);
			}
			
			/*
			System.out.println("Loading start state...");
			*/
			
			start_state = input.nextInt()-1; // Loads start state
			
			/*
			System.out.println("Loading Accepting States...");
			*/
			
			// Switches delimiter to newline character because accepting states are with spaces.
			input.useDelimiter("\n");					
			String acc_states = input.next(); // loads accepting states into one string
			Scanner parse = new Scanner(acc_states); // parses string with delimiter as a space.
			parse.useDelimiter(" ");
			//parses and sets all the accepting states.
			while( parse.hasNext()){
				int tmp_acc_state = parse.nextInt() - 1;
				for(int idx = 0; idx < alphabet.length(); idx ++){
					trans_mat[tmp_acc_state][idx].setAccepting(true);
				}
			}
			parse.close();
			
			/*
			System.out.println("Loading inputs...\n");
			*/
			
			// Checks input strings to see if they are accepted or rejected.
			//Loops until the text file is empty
			while(input.hasNext()){
				boolean rtn = false; 
				String alph_input = input.next();// inputed DFA command Example: "00100101"
				
				/*
				System.out.print( alph_input + ": ");
				*/
				
				State_info result = new State_info(); // current state in.
				int state = start_state; // Sets state to start state value
				
				//Reads the input letter by letter until the string is done.
				for(int iter = 0; iter <= alph_input.length()-1; iter++){
					char c = alph_input.charAt(iter);
					int alph_value = alphabet.indexOf(c);
					result = trans_mat[state][alph_value];
					state = result.getNext_state();
					result = trans_mat[state][alph_value];
				}
				//Determines what the ending state is and returns either accept or reject
				rtn = result.getAccepting();
				if(rtn == true){
					System.out.println("Accept");
				}
				else{
					System.out.println("Reject");
				}	
			}
			input.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
			System.out.println("The file \"" + args[0] + "\" did not open correctly. Progarm is exiting.");
		}
		catch (ArrayIndexOutOfBoundsException e){
			System.out.println(e);
			System.out.println("The file \"" + args[0] + "\" did not open correctly. Progarm is exiting.");
		}
	}
	
	/*
	* Sub-Class that holds the information for each state of the machine
	*/
	private static class State_info{

		/* Fields */
		private boolean accepting; // true if accepting state, false if reject state.
		private int next_state; // has the index to the next state.
		
		/* Constructors */
		
		// Sets next to next_state variable Constructor
		public State_info(int next){
			setAccepting(false);
			setNext_state(next);
		}
		// Default Constructor
		public State_info(){
			setAccepting(false);
			setNext_state(-1);
		}
		
		/* Methods */
		// Returns if the state condition
		public boolean getAccepting() {
			return accepting;
		}
		// Sets the state condition
		public void setAccepting(boolean accepting) {
			this.accepting = accepting;
		}
		// Returns the next state index
		public int getNext_state() {
			return next_state;
		}
		// Sets the next state index
		public void setNext_state(int next_state) {
			this.next_state = next_state;
		}
	}
}