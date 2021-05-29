package br.com.alka.stringsearch.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.alka.stringsearch.constants.ThreadResultStatus;
import br.com.alka.stringsearch.datasource.DataSource;
import br.com.alka.stringsearch.dto.ThreadResultDTO;
import br.com.alka.stringsearch.worker.StringSearcher;

/**
 * This class creates the workers and provides the String to find and the Data Source.
 * @author Amir Leonardo Kessler Annahas
 */
public class SearchController {
	
	private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
	
	private static final String STRING_TO_FIND = "Lpfn";
	
	private static final int NUMBER_OF_THREADS = 10;
	
	/**
	 * Creates the workers and provides the String to find and the data source.
	 * @param timeout Timout of the workers.
	 */
	public void search(int timeout) {
		try {
			// Creates the data source
			DataSource dataSource = new DataSource();
							
			// Shared list with the results of the workers.
			List<ThreadResultDTO> resultList = new ArrayList<ThreadResultDTO>();
			List<ThreadResultDTO> resultSyncList = Collections.synchronizedList(resultList);
			
			List<StringSearcher> threadList = new ArrayList<StringSearcher>();
			
			// Creates and starts all threads (workers).
			for (int i = 0; i < NUMBER_OF_THREADS; i++) {
				StringSearcher newSearcher = new StringSearcher(STRING_TO_FIND, dataSource, timeout, resultSyncList);
				newSearcher.start();
				threadList.add(newSearcher);
			}
			
			logger.info(Thread.currentThread().getName() + " - all threads started");
			
			// Wait until all Threads ends.
			for (StringSearcher stringSearcher : threadList) {
				try {
					stringSearcher.join();
				} catch (InterruptedException e) {
					System.err.println(e.getMessage());
				}
			}
			
			logger.info(Thread.currentThread().getName() + " - all threads ended");
			
			// Print the output of each worker.
			resultSyncList.stream().sorted().forEach(res -> {
				if (res.getStatus() == ThreadResultStatus.SUCCESS) {
					System.out.println(res.getElapsed() + " " + res.getByte_cnt() + " " + res.getStatus().getMessage());
				} else {
					System.out.println(res.getStatus().getMessage());
				}
			});
			
			// Calculate stats.
			Double avgTime = resultSyncList.stream().filter(r -> r.getElapsed() != null).mapToLong(ThreadResultDTO::getElapsed).average().orElse(0.0);
			Double avgBytes = resultSyncList.stream().filter(r -> r.getByte_cnt() != null).mapToLong(ThreadResultDTO::getByte_cnt).average().orElse(0.0);
			
			// Print stats.
			System.out.println("Avarage bytes per millisecond: " + (avgTime > 0 ? avgBytes/avgTime : "-"));
			
			System.exit(0);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}
	}
	
	
}
