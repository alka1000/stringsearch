package br.com.alka.stringsearch.datasource;

import java.util.Random;
import java.util.stream.Collectors;

/**
 * This class generates random Strings to be consumed simultaneously by consumers (workers). 
 * @author Amir Leonardo Kessler Annahas
 */
public class DataSource {

	private Random random = new Random();
	
	public String next() {
		// Thread-safe area to avoid simultaneous access by consumers.
		synchronized (random) {
			// Generate a random length of the data entry (from 1 to 5000).
			int length = random.ints(1, 1, 5000).findFirst().getAsInt();
			// Generate the random String as ASCII characters from 'A' to 'z'.
			return this.random.ints(length, 65, 123)
					.mapToObj(i -> String.valueOf((char) i))
					.collect(Collectors.joining());
		}
	}
	
}
