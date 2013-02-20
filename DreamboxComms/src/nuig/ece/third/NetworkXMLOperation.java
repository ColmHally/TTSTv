package nuig.ece.third;

import java.net.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;

public class NetworkXMLOperation implements Runnable {

	private URL requestURL; // using a URL object ensures the requestURL is a valid URL
	private Document xmlDoc;
	private boolean completed = false,
					cancelled = false;
	private Callback callback;
	private Response response;
	private int ID;
	
	public NetworkXMLOperation( int id, URL requestURL, Callback callback ) {
		super();
		
		if (callback == null)
			callback = new Callback() {
				public void call( Response response ) {
					return;
				}
			};
		
		this.requestURL = requestURL;
		this.callback = callback;
		this.setID(id);
	}
	
	public void run() {
		log( "running.. - " + requestURL.toString() );
		
		if ( isCancelled() )
			return;
		
		response = new Response( requestURL.toString() );
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc = null;
		Exception error = null;
		
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			
			log("parsing..");
			doc = dBuilder.parse( requestURL.toString() );
			doc.getDocumentElement().normalize();
			
		} catch ( Exception e ) {
			logError("error in parsing - " + e.getClass());
			error = e;
			logError( e.getMessage() );
		}
		
		if ( error != null )
			response.setError(error);
		
		else if ( doc != null )
			response.setResponseXML(doc);
		
		log("completed");
		
		completed = true;
	}
	
	// Log
	
	public void logError( String msg ) {
		log( msg, true );
	}
	
	public void log( String msg ) {
		log( msg, false );
	}
	
	public void log( String msg, boolean error ) {
		msg = "NW Op " + ID + ": " + msg;
		
		if ( !error )
			System.out.println(msg);
		
		else
			System.err.println(msg);
	}

	// Accessors
	
	public URL getRequestURL() {
		return requestURL;
	}

	public void setRequestURL(URL requestURL) {
		this.requestURL = requestURL;
	}

	public Document getXmlDoc() {
		return xmlDoc;
	}

	public void setXmlDoc(Document xmlDoc) {
		this.xmlDoc = xmlDoc;
	}

	public Callback getCallback() {
		return callback;
	}
	
	void setCompleted( boolean completed ) {
		this.completed = completed; 
	}
	
	public boolean isCompleted() {
		return completed;
	}

	public Response getResponse() {
		return response;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
}
