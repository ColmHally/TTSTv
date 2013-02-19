package nuig.ece.third;

import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

public class Response {
	private String 		responseText = null,
						URL = null;
	private Document	responseXML = null;
	private Exception	error = null;
	private Object		data = null;
	
	public Response( String URL, String responseText, Document responseXML, Exception error ) {
		setURL(URL);
		setResponseText( responseText );
		setResponseXML( responseXML );
		setError( error );
	}
	
	public Response( Object data, Exception error ) {
		setData( data );
		setError( error );
	}
	
	public Response( String URL ) {
		setURL( URL );	
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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}