package nuig.ece.third;

import java.net.MalformedURLException;
import java.util.*;

import org.w3c.dom.Node;

public class EPGService {
	
	private static ArrayList<ChannelEPG> channelData = new ArrayList<ChannelEPG>();
	
	public static final String 	RTE_ONE = 			"1:0:16:44D:3E9:2174:EEEE0000:0:0:0:",
								RTE_ONE_PLUS_ONE = 	"1:0:16:454:3E9:2174:EEEE0000:0:0:0:",
								RTE_TWO = 			"1:0:19:44E:3E9:2174:EEEE0000:0:0:0:",
								RTE_JR = 			"1:0:16:453:3E9:2174:EEEE0000:0:0:0:",
								RTE_NEWS_NOW = 		"1:0:16:451:3E9:2174:EEEE0000:0:0:0:",
								TV_THREE = 			"1:0:16:44F:3E9:2174:EEEE0000:0:0:0:",
								THREE_E = 			"1:0:16:452:3E9:2174:EEEE0000:0:0:0:",
								TG_FOUR = 			"1:0:16:450:3E9:2174:EEEE0000:0:0:0:";
	
	private static String currentChannel = null;

	public static final String[] serviceRefs = { 	RTE_ONE, RTE_ONE_PLUS_ONE,
													RTE_TWO, RTE_JR,
													RTE_NEWS_NOW, TV_THREE,
													THREE_E, TG_FOUR };
	
	public static void main( String[] args ) { // move into onCreate
		System.out.println( "Launching.. Trying to fetch initial epg data" );
		
		EPGService.fetchInitial( new Callback() {
			public void call( Response response ) {
				if ( response.getError() != null ) {
					System.err.println( "Error in initial fetch!" );
					return;
				}
				
				System.out.println( channelData );
				
			}
			
		});
	}
	
	public static void fetchInitial( final Callback fetchCallback ) {
		if ( DreamboxComms.getDreamboxIP() == null ) {
			System.out.println( "No IP - asking to discover" );
			
			DreamboxComms.discoverDreambox( new Callback() {
				public void call( Response response ) {
					System.out.println( "got ip " + response.getResponseText() );
					
					if ( response.getError() == null )
						EPGService.fetchInitial( fetchCallback );
				}
			});
			
			return;
		}
		
		final Response[] responses = new Response[ serviceRefs.length ];
		
		for ( int i = 0; i < serviceRefs.length; i++ ) {
			try {
				final String ref = serviceRefs[ i ];
				final int index = i;
				
				DreamboxComms.queryAPIEndpoint( "epgservice", "sRef=" + ref, new Callback() {
					
					public void call( Response response ) {						
						responses[ index ] = response;
						
						if ( response.getError() != null ) {
							System.err.println( response.getError() );
							return;
						}
						
						System.out.println( "Got EPG response" );
						
						E2EventList events = E2EventList.newEventListFromXMLNode( (Node) response.getResponseXML().getDocumentElement() );
					
						ChannelEPG channel = ChannelEPG.newChannelEPGFromE2EventList( events );
						
						System.out.println( "EPG for " + channel.getName() + " received!");
						
						channelData.add( channel );
						
						boolean complete = true;
						
						for ( Response resp : responses ) {
							if ( resp == null ) {
								complete = false;
								break;
							}
						}
						
						if ( complete ) {
							fetchCallback.call( new Response( channelData, null ) );
						}
						
					}
					
				});
				
			} catch (Exception e) {
				System.err.println( "Fetch Error: " + e.getMessage() );
				continue;
			}
		}
	}
	
}
