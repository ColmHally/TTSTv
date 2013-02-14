package nuig.ece.third;

@SuppressWarnings("serial")
public class TooManyRetriesException extends Exception {

	public TooManyRetriesException() {
		
	}

	public TooManyRetriesException(String arg0) {
		super(arg0);
	}

	public TooManyRetriesException(Throwable arg0) {
		super(arg0);
	}

	public TooManyRetriesException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
