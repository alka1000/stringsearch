package br.com.alka.stringsearch.dto;

import br.com.alka.stringsearch.constants.ThreadResultStatus;

/**
 * This class contains the status and stats of one worker.
 * @author Amir Leonardo Kessler Annahas
 */
public class ThreadResultDTO implements Comparable<ThreadResultDTO>{
	private Long elapsed;
	private Long byte_cnt;
	private ThreadResultStatus status;
	
	public ThreadResultDTO(Long elapsed, Long byte_cnt, ThreadResultStatus status) {
		super();
		this.elapsed = elapsed;
		this.byte_cnt = byte_cnt;
		this.status = status;
	}
	
	public ThreadResultDTO(Long elapsed, Long byte_cnt) {
		super();
		this.elapsed = elapsed;
		this.byte_cnt = byte_cnt;
		this.status = ThreadResultStatus.SUCCESS;
	}
	
	public static ThreadResultDTO createErrorResult() {
		return new ThreadResultDTO(null, null, ThreadResultStatus.ERROR);
	}
	
	public static ThreadResultDTO createTimeoutResult() {
		return new ThreadResultDTO(null, null, ThreadResultStatus.TIMEOUT);
	}

	public Long getElapsed() {
		return elapsed;
	}

	public void setElapsed(Long elapsed) {
		this.elapsed = elapsed;
	}

	public Long getByte_cnt() {
		return byte_cnt;
	}

	public void setByte_cnt(Long byte_cnt) {
		this.byte_cnt = byte_cnt;
	}

	public ThreadResultStatus getStatus() {
		return status;
	}

	public void setStatus(ThreadResultStatus status) {
		this.status = status;
	}

	/**
	 * Order by elapsed time.
	 */
	@Override
	public int compareTo(ThreadResultDTO other) {
		if (this.elapsed == null) return 1;
		if (other.getElapsed() == null) return -1;
		return (int)(this.elapsed - other.getElapsed());
	}
	
}
