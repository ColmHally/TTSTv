package nuig.ece.third;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

//import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DreamboxComms implements Runnable {
	private static String dreamboxIP = "";
	private static NetworkXMLOperationsQueue operationsQueue;
	
	public static void main( String[] args ) {
		ExecutorService pool = Executors.newFixedThreadPool( 2 );
		operationsQueue = new NetworkXMLOperationsQueue();
		DreamboxComms comms = new DreamboxComms();
		
		pool.execute( operationsQueue );
		pool.execute( comms );
	}
	
	public void run() {		
		Scanner sc = new Scanner(System.in);
		boolean stop = false;
		
		System.out.print( "Enter IP Address of Dreambox <or 'auto'>: " );
		dreamboxIP = sc.next();
		
		if ( dreamboxIP.equalsIgnoreCase( "auto" ) )
			dreamboxIP = "192.168.1.119";
		
		while ( stop == false ) {
			System.out.print( "Enter API endpoint of query <or 'exit'>: " );
			
			String endpoint = sc.next();
			
			if ( endpoint.equalsIgnoreCase( "exit" ) ) {
				stop = true;
				break;
			}
			
			String url = getDreamboxURL( endpoint );
			
			try {
				operationsQueue.addOperation( url, new Callback() {
					public void run( Response response ) {
						System.out.println( "\n\n" + response );
					}
				});
			} catch (MalformedURLException e) {
				System.err.println( "URL: " + url + " is not a valid URL!" );
			}
			
		}
	
		System.exit( 0 );
	}
	
	public static Response parseXMLURL( String xmlURL ) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc = null;
		Response response = null;
		
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			
			doc = dBuilder.parse( xmlURL );
			doc.getDocumentElement().normalize();
			
		} catch (ParserConfigurationException e) {
			System.err.println( e.getMessage() );
			response = new Response( xmlURL, null, null, e );
			
		} catch (SAXException e) { //! TODO: Network Errors
			System.err.println( e.getMessage() );
			response = new Response( xmlURL, null, null, e );
			
		} catch (IOException e) {
			System.err.println( e.getMessage() );
			response = new Response( xmlURL, null, null, e );
			
		}
		
		if ( response == null )
			response = new Response( xmlURL, null, doc, null );
		
		return response;
	}
	
	public static void findDreambox( Callback callback ) {
		
	}
	
	public static String getDreamboxURL( String endpoint ) {
		if ( dreamboxIP.length() == 0 )
			return null;
		
		return "http://" + dreamboxIP + "/web/" + endpoint;
	}
}
