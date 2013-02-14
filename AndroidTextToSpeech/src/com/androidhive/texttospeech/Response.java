package com.androidhive.texttospeech;

import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

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
	
	public static String getStringFromDoc(Document doc)  { 
	try
	{
		DOMSource domSource = new DOMSource(doc);
	    StringWriter writer = new StringWriter();
	    StreamResult result = new StreamResult(writer);
	    TransformerFactory tf = TransformerFactory.newInstance();
	    Transformer transformer = tf.newTransformer();
	    transformer.transform(domSource, result);
	    return writer.toString();
		
	   // DOMImplementationLS domImplementation = (DOMImplementationLS) doc.getImplementation();
	    //LSSerializer lsSerializer = domImplementation.createLSSerializer();
	    //return lsSerializer.writeToString(doc);   
	}
	catch(Exception e){
	System.err.println( "Error in getStringFromDoc: " + e.getMessage() );
	return null;
	}}
	
	public String toString() {
		return "<Response URL: '" + URL +
				"'\nText: '" + responseText +
				"'\nXML: '" + responseXML +
				"'\nError: '" + (error != null ? error.getMessage() : "") + "'>";
	}
}