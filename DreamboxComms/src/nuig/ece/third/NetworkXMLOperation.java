package nuig.ece.third;

import java.io.IOException;
import java.net.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class NetworkXMLOperation implements Runnable {

	private URL requestURL; // using a URL object ensures the requestURL is a valid URL
	private Document xmlDoc;
	private Long startTime;
	private boolean completed = false;
	private Callback callback;
	private Response response;
	private int ID;
	
	public NetworkXMLOperation( int id, URL requestURL, Callback callback ) {
		super();
		
		this.requestURL = requestURL;
		this.callback = callback;
		this.setID(id);
	}
	
	public void run() {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc = null;
		Exception error = null;
		
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			
			setStartTime(System.currentTimeMillis());
			
			doc = dBuilder.parse( requestURL.toString() );
			doc.getDocumentElement().normalize();
			
		} catch ( Exception e ) {
			error = e;
			System.err.println( e.getMessage() );
		}
		
		/*catch (ParserConfigurationException e) {
			System.err.println( e.getMessage() );
		} catch (SAXException e) {
			System.err.println( e.getMessage() );
		} catch (IOException e) {
			System.err.println( e.getMessage() );
		}*/
		
		if ( error != null )
			response = new Response( requestURL.toString(), null, null, error );
		
		else if ( doc != null )
			response = new Response( requestURL.toString(), null, doc, null );
		
		completed = true;
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

	public boolean isCompleted() {
		return completed;
	}

	public Response getResponse() {
		return response;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	
}
