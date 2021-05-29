package br.com.alka.stringsearch.constants;

/**
 * Enum with the possible output status of one worker. 
 * @author Amir Leonardo Kessler Annahas
 */
public enum ThreadResultStatus {
	
	TIMEOUT("TIMEOUT"), ERROR("FAILURE"), SUCCESS("SUCCESS");
	
	private String message;
	
	private ThreadResultStatus(final String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
