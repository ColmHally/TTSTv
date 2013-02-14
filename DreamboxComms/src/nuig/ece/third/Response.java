package nuig.ece.third;

import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

public class Response {
	private String 		responseText,
						URL;
	private Document	responseXML;
	private Exception	error;
	
	public Response( String URL, String responseText, Document responseXML, Exception error ) {
		this.URL = URL;
		this.responseText = responseText;
		setResponseXML( responseXML );
		this.error = error;
	}
	
	public static String getStringFromDoc(Document doc)    {
	    DOMImplementationLS domImplementation = (DOMImplementationLS) doc.getImplementation();
	    LSSerializer lsSerializer = domImplementation.createLSSerializer();
	    return lsSerializer.writeToString(doc);   
	}
	
	public String toString() {
		return "<Response URL: '" + URL +
				"'\nText: '" + responseText +
				"'\nXML: '" + responseXML +
				"'\nError: '" + (error != null ? error.getMessage() : "") + "'>";
	}
	
	// Accessors
	
	public void setURL(String URL) {
		this.URL = URL;
	}
	
	public String getURL() {
		return URL;
	}
	
	public void setResponseXML( Document responseXML ) {
		this.responseXML = responseXML;
		
		if ( responseXML != null )
			this.responseText = getStringFromDoc( responseXML );
	}
	
	public Document getResponseXML() {
		return responseXML;
	}
	
	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}

	public String getResponseText() {
		return responseText;
	}
	
	public void setError(Exception error) {
		this.error = error;
	}
	
	public Exception getError() {
		return error;
	}

}