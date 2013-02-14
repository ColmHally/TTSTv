package nuig.ece.third;

@SuppressWarnings("serial")
public class InvalidDreamboxAPICallException extends Exception {
	private String endpoint = "";
	
	public InvalidDreamboxAPICallException( String endpoint ) {
		super( "The endpoint " + endpoint + " is not supported by the Dreambox API." );
		setEndpoint( endpoint );
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
}
