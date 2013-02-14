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
	
	public void run() { // demonstrates how you might use the network operations queue in an app		
		Scanner sc = new Scanner(System.in);
		boolean stop = false;
		
		System.out.println( "Enter IP Address of Dreambox <or 'auto'>: " );
		dreamboxIP = sc.next();
		
		if ( dreamboxIP.equalsIgnoreCase( "auto" ) )
			dreamboxIP = "172.20.10.10";
		
		while ( stop == false ) {
			System.out.println( "Enter API endpoint of query <or 'exit'>: " );
			
			String endpoint = sc.next();
			
			if ( endpoint.equalsIgnoreCase( "exit" ) ) {
				stop = true;
				break;
			}
			
			String url = getDreamboxURL( endpoint );
			
			try {
				operationsQueue.add( url, new Callback() {
					public void call( Response response ) {
						System.out.println( "\n\n" + response );
					}
				});
			} catch (MalformedURLException e) {
				System.err.println( "URL: " + url + " is not a valid URL!" );
			} catch (InvalidDreamboxAPICallException e) {
				System.err.println( "Endpoint: " + e.getEndpoint() + " is not supported!" );
			}
			
		}
	
		System.exit( 0 );
	}
	
	public static void findDreambox( Callback callback ) {
		
	}
	
	public static String getDreamboxURL( String endpoint ) {
		if ( dreamboxIP.length() == 0 )
			return null;
		
		return "http://" + dreamboxIP + "/web/" + endpoint;
	}
}
