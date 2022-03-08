package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*
 * This class contains modules that involve bash commands. All bash commands are
 * sent through this class.
 */
public class BashProcesses {

	/*
	 * This module is used to send bash commands. Some uses for this module include:
	 * - Writing to files - Removing words from files
	 */
	public static void sendProcesses(String cmd) {
		try {
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd); // Creates the whole command
			pb.start();
			Thread.sleep(100);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * This module is used to return values from bash commands. Some uses for this
	 * module include: - Reading files - Counting the number times a word occurs in
	 * a file
	 */
	public static String returnVariable(String cmd) throws IOException {
		String result = ""; // Initialises return value
		Runtime r = Runtime.getRuntime();

		Process p = r.exec(cmd);

		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			result += inputLine; // Adds the result from the bash command to the return value
		}
		in.close();
		return result; // Returns the result
	}

	/*
	 * This module is specifically used to return an ArrayList containing all words
	 * in the spelling list.
	 */
	public static ArrayList<String> returnArray(String cmd) throws IOException {
		ArrayList<String> result = new ArrayList<String>(); // Initialises return value (in this case, it is an
															// ArrayList rather than a String)
		Runtime r = Runtime.getRuntime();

		Process p = r.exec(cmd);

		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			result.add(inputLine); // Adds each line in the file to the ArrayList (in this case, there is one word
									// per line)
		}
		in.close();
		return result; // Returns all values
	}
}
