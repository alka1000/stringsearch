package br.com.alka.stringsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.alka.stringsearch.controller.SearchController;

/**
 * Main class. 
 * @author Amir Leonardo Kessler Annahas
 */
@SpringBootApplication
public class StringsearchApplication {

	/**
	 * Main method, initializes the application.
	 * @param args Expected to receive none or only the timeout (int) as a parameter.
	 */
	public static void main(String[] args) {
		SpringApplication.run(StringsearchApplication.class, args);
		
		SearchController searchController = new SearchController();
		
		int timeout = args.length == 0 ? 60 : Integer.parseInt(args[0]); 
		
		searchController.search(timeout);

	}

}
