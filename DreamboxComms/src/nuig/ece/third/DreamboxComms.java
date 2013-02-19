package nuig.ece.third;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DreamboxComms implements Runnable {
	private static String dreamboxIP = null;
	private static NetworkXMLOperationsQueue operationsQueue;
	private static boolean findingIP = false;
	private static ArrayList<NetworkXMLOperation> findOps;
	
	// main and run are for testing purposes - disregard on android
	
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
		
		if ( dreamboxIP.equalsIgnoreCase( "auto" ) ) {
			System.out.println("Finding ip");
			
			try {
				findDreamboxIP();
			} 
			catch (IOException e1) 
			{
				System.out.print("Error in IP retrieve");
				e1.printStackTrace();
			}

			while (findingIP) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {}
			};
			
			System.out.print( "Dreambox Ip is: " + dreamboxIP + "\n");
		}

		
		while ( stop == false ) {
			System.out.println( "Enter API endpoint of query <or 'exit'>: " );
			
			String endpoint = sc.next();
			
			if ( endpoint.equalsIgnoreCase( "exit" ) ) {
				stop = true;
				break;
			}
			
			String url = composeDreamboxURL( endpoint );
			
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
	
	// End: testing functions
	
	
	public static void discoverDreambox( final Callback callback ) {
		if (findingIP == true)
			return;
		
		operationsQueue = new NetworkXMLOperationsQueue();
		new Thread( operationsQueue ).start();
		
		new Thread( new Runnable() {
			public void run() {
				System.out.println("Finding ip");
				
				try {
					findDreamboxIP();
				} 
				catch (IOException e1) 
				{
					System.out.print("Error in IP retrieval");
					e1.printStackTrace();
				}

				while (findingIP) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {}
				};
				
				System.out.print( "Dreambox Ip is: " + dreamboxIP + "\n");
				
				if (callback != null)
					callback.call( new Response( dreamboxIP, dreamboxIP, null, null ) );
			}
		}).start();
		
	}
	
	public static NetworkXMLOperation queryAPIEndpoint( String endpoint, String args, Callback callback ) throws MalformedURLException, InvalidDreamboxAPICallException {
		String URL = composeDreamboxURL( endpoint + "?" + args );
		
		return operationsQueue.add( URL, callback );
	}
	
	public static void sendMessage( String msg ) {
		try {
			queryAPIEndpoint( "message", "text=" + URLEncoder.encode( msg, "UTF-8" ) + "&type=3&timeout=10", null );
			
		} catch (Exception e) {
			System.err.println( "Couldn't print message: " + msg );
		}
	}
	
	public static String getDreamboxIP() {
		return dreamboxIP;
	}

	public static void setDreamboxIP(String dreamboxIP) {
		DreamboxComms.dreamboxIP = dreamboxIP;
	}
	
	
	private static String composeDreamboxURL( String endpoint ) {
		if ( dreamboxIP.length() == 0 )
			return null;
		
		return "http://" + dreamboxIP + "/web/" + endpoint;
	}
	
	private static void findDreamboxIP() throws IOException
	{
		findingIP = true;
		InetAddress inetAddress = InetAddress.getLocalHost();  		// get an instance of InetAddress for the local computer  
		String ipAddress = inetAddress.getHostAddress(); 			// get a string representation of the ip address

		findOps = new ArrayList<NetworkXMLOperation>();
		
		for(int i=1; i<25; i++){
			NetworkXMLOperation op = searchForBox(ipAddress, i);
			findOps.add( op );
		}
	}
	
	public static NetworkXMLOperation searchForBox(String ip, int i) throws IOException
	{
		StringTokenizer st = new StringTokenizer(ip, ".");
		String 	BoxIPPart1 = st.nextToken(),
				BoxIPPart2 = st.nextToken(),
				BoxIPPart3 = st.nextToken(),
				
				SearchDomainIP = BoxIPPart1 + "." + BoxIPPart2 + "." + BoxIPPart3 + ".";
		
		String SearchMe = ("http://" + SearchDomainIP + i + "/web/vol");
		
		System.out.print(SearchMe +"\n");
		
		try {
			NetworkXMLOperation op = operationsQueue.add(SearchMe, new Callback() {
				
				public void call(Response response) {
					if (response == null)
						return;
					
					if (response.getError() != null)
						return;
					
					System.out.println( "May have an ip - response: " + response );
					
					String text = response.getResponseText();
					text = text == null ? "" : text;
					
					if ( text.contains("e2result") == false ) // don't actually have the correct box
						return;
					
					Iterator<NetworkXMLOperation> ite = findOps.iterator();
					
					while (ite.hasNext()) {
						NetworkXMLOperation op = ite.next();
						op.setCancelled(true);
					}
					
					try {
						URL ipURL = new URL( response.getURL() );
						System.out.println("IP URL: " + ipURL);
						dreamboxIP = ipURL.getHost();
						System.out.println( "Think IP is " + dreamboxIP );
						
						sendMessage( "Connected to DreamSpeech!" );
						
					} catch (MalformedURLException e) {
						System.err.println("URL is malformed. Oh shite.");
					} 
					
					findingIP = false;
				}
			});
			
			return op;
		} catch (InvalidDreamboxAPICallException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
}
