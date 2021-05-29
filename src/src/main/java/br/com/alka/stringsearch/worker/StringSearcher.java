package br.com.alka.stringsearch.worker;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.alka.stringsearch.constants.ThreadResultStatus;
import br.com.alka.stringsearch.datasource.DataSource;
import br.com.alka.stringsearch.dto.ThreadResultDTO;

/**
 * This is the class that implements the worker.
 * The worker is responsible for finding the target String from the provided data source.
 * @author Amir Leonardo Kessler Annahas
 */
public class StringSearcher extends Thread {
	
	private static final Logger logger = LoggerFactory.getLogger(StringSearcher.class);
	
	private String stringToFind;
	private DataSource dataSource;
	private int timeout;
	private Long bytesRead = 0L;
	private List<ThreadResultDTO> resultSyncList;
	
	/**
	 * Timer inner class that implements the timeout of the thread (worker).
	 */
	class TimeOutTask extends TimerTask {
	    private Thread t;
	    private Timer timer;

	    TimeOutTask(Thread t, Timer timer){
	        this.t = t;
	        this.timer = timer;
	    }
	 
	    public void run() {
	        if (t != null && t.isAlive()) {
	            t.interrupt();
	            timer.cancel();
	        }
	    }
	}

	/**
	 * This is the worker constructor.
	 * All parameters are required.
	 * @param stringToFind The target String that the worker is searching for.
	 * @param dataSource The data source that provides the data stream as String.
	 * @param timeout Timeout of the worker.
	 * @param resultSyncList Shared List used to return the status and stats of the search.
	 */
	public StringSearcher(String stringToFind, DataSource dataSource, int timeout, List<ThreadResultDTO> resultSyncList) {
		super();
		this.stringToFind = stringToFind;
		this.dataSource = dataSource;
		this.timeout = timeout;
		this.resultSyncList = resultSyncList;
	}

	@Override
	public void run() {
		try {
			// The time that the worker started
			long startTime = System.currentTimeMillis();			
			logger.info(Thread.currentThread().getName() + " - STARTED");
			
			// The timeout mechanism.
			Timer timer = new Timer();
			timer.schedule(new TimeOutTask(Thread.currentThread(), timer), this.timeout*1000);
			
			// If this Thread is interrupted, stop searching.
			while (!Thread.interrupted()) {
				
				// Get the next input of data.
				String data = dataSource.next();
				
				// Search for the string.
				int index = data.indexOf(stringToFind);
				
				// If the string was found.
				if (index > -1) {
					
					// Increment the bytes read (including the target String length).
					this.bytesRead += index + stringToFind.length();
					
					logger.info(Thread.currentThread().getName() + " - FOUND after " + this.bytesRead + " bytes read.");
					
					// Elapsed time calculation.
					long elapsedTime = System.currentTimeMillis() - startTime;
					
					// Verify if the elapsed time is bigger then the timeout.
					if (elapsedTime < this.timeout*1000) {
						this.resultSyncList.add(new ThreadResultDTO(elapsedTime, this.bytesRead, ThreadResultStatus.SUCCESS));
					} else {
						break;
					}
					return;
				}
				
				// Increment the bytes read.
				bytesRead += data.length();
				
			}
			 
			this.resultSyncList.add(ThreadResultDTO.createTimeoutResult());
			
			logger.info(Thread.currentThread().getName() + " - INTERRUPTED");
			return;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			logger.error(Thread.currentThread().getName() + " - ERROR");
			this.resultSyncList.add(ThreadResultDTO.createErrorResult());
			return;
		}
	}

}
