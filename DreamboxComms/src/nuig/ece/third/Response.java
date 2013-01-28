package nuig.ece.third;

import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

public class Response {
	public String 		responseText,
						URL;
	public Document		responseXML;
	public Exception	error;
	
	public Response( String URL, String responseText, Document responseXML, Exception error ) {
		this.URL = URL;
		this.responseText = responseText;
		setResponseXML( responseXML );
		this.error = error;
	}
	
	public void setResponseXML( Document responseXML ) {
		this.responseXML = responseXML;
		
		if ( responseXML != null )
			this.responseText = getStringFromDoc( responseXML );
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
}