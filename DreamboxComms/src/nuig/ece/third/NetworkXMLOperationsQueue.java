package nuig.ece.third;

import java.net.URL;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.*;

public class NetworkXMLOperationsQueue implements Runnable {

	private ArrayList<NetworkXMLOperation> operations;
	private boolean shouldExit = false;
	private ExecutorService threadPool;
	private static int operationsID = 1;
	
	static final int threadPoolSize = 8;
	static final Long timeoutTime = 12000L; // timeout after twelve seconds
	
	public void run() {
		threadPool = Executors.newFixedThreadPool( threadPoolSize );
		operations = new ArrayList<NetworkXMLOperation>();
		
		while ( shouldExit == false ) {
			if ( operations.size() > 0 ) {				
				Iterator<NetworkXMLOperation> iterator = operations.iterator();
				
				while ( iterator.hasNext() ) {
					NetworkXMLOperation op = iterator.next();
					
					if ( op.isCompleted() ) {
						iterator.remove();
						operations.remove( op );
						op.getCallback().run( op.getResponse() );
					
					} else {
						Long currentTime = System.currentTimeMillis();
						
						if ( currentTime - op.getStartTime() > timeoutTime ) {
							System.err.println( op.getRequestURL() + " timed out!");
							// cancel and retry
						}
					}
				}
			
			} else {
				try {
					Thread.sleep( 250 );
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		threadPool.shutdown();
		
	}
	
	public synchronized NetworkXMLOperation addOperation( String requestURL, Callback callback ) throws MalformedURLException {		
		NetworkXMLOperation operation = new NetworkXMLOperation( nextID(), new URL( requestURL ), callback );
		
		operations.add( operation );
		threadPool.execute( operation );
		
		return operation;
	}
	
	private static int nextID() {
		return operationsID++ % 10000;
	}
	
}
