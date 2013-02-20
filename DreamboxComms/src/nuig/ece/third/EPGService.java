package nuig.ece.third;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
								TG_FOUR = 			"1:0:16:450:3E9:2174:EEEE0000:0:0:0:",
								
								EPG_FILE_LOCATION = "epg.dat";
	
	private static final int	CURRENT_ITEM = 0,
								PREVIOUS_ITEM= 1,
								NEXT_ITEM = 2;
	
	private static ChannelEPG 	currentChannel = null,
								previousChannel = null,
								nextChannel = null;
	
	private static ProgramEPG 	currentProgram = null,
								previousProgram = null,
								nextProgram = null;

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
				
				EPGService.fetchCurrentChannel( new Callback() {
					public void call( Response response ) {
						if ( response.getError() != null ) {
							System.err.println( "Error in fetching!" );
							return;
						}
						
						currentChannel = (ChannelEPG) response.getData();
						System.out.println( "Got current channel " + currentChannel.getName() );
						
						currentProgram = findProgramForDate( currentChannel, new Date() );
						
						System.out.println( "Current Program: " + currentProgram );
						System.out.println( "System Ready!" ); // call method to notify of full initialisation
					}
				} );
								
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
			
			channelData = fetchCachedEPG();
			
			return;
		}
		
		if ( channelData == null )
			channelData = fetchCachedEPG();
		
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
						
						if ( response.getResponseXML() == null ) {
							System.err.println( "Null XML Doc for EPG Response: " + ref );
							return;
						}
						
						E2EventList events = E2EventList.newEventListFromXMLNode( (Node) response.getResponseXML().getDocumentElement() );
					
						ChannelEPG channel = ChannelEPG.newChannelEPGFromE2EventList( events );
						
						System.out.println( "EPG for " + channel.getName() + " received!");
						
						channelData.add( channel );
						
						Collections.sort( channelData, new ChannelEPGNameComparator());
						
						boolean complete = true;
						
						for ( Response resp : responses ) {
							if ( resp == null ) {
								complete = false;
								break;
							}
						}
						
						if ( complete ) {
							FileOutputStream fos;
							
							try {
//								fos = openFileOutput(EPG_FILE_LOCATION, Context.MODE_PRIVATE); // Android code
								fos = new FileOutputStream( EPG_FILE_LOCATION );
								
								ObjectOutputStream oos = new ObjectOutputStream( fos );
								
								oos.writeObject( channelData );
								
							} catch (Exception e) {
								e.printStackTrace();
							}
							
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
	
	public static void fetchCurrentChannel( final Callback fetchCallback ) {
		if ( DreamboxComms.getDreamboxIP() == null ) {
			System.out.println( "No IP - asking to discover" );
			
			DreamboxComms.discoverDreambox( new Callback() {
				public void call( Response response ) {
					System.out.println( "got ip " + response.getResponseText() );
					
					if ( response.getError() == null )
						EPGService.fetchCurrentChannel( fetchCallback );
				}
			});
			
			return;
		}
		
		try {
			DreamboxComms.queryAPIEndpoint("subservices", null, new Callback() {
				public void call( Response response ) {
					if ( response.getError() != null ) {
						System.err.println( response.getError() );
						return;
					}
					
					E2ServiceList servicelist = E2ServiceList.newServiceListFromXMLNode( response.getResponseXML().getDocumentElement() );
					String sRef = servicelist.getServices().get(0).getSRef();
					
					ChannelEPG channel = findChannelBySRef( sRef );
					
					if ( channel == null )
						fetchCallback.call( new Response( null, new Exception( "Current Channel not found for sRef " + sRef ) ) );
					else
						fetchCallback.call( new Response( channel, null ) );
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<ChannelEPG> fetchCachedEPG() {
		try {
//			FileInputStream fis = openFileInput( EPG_FILE_LOCATION, Context.MODE_PRIVATE ); // Android-specific code
			FileInputStream fis = new FileInputStream( EPG_FILE_LOCATION );
			ObjectInputStream ois = new ObjectInputStream( fis );
			
			System.out.println( "Reading cache data from file" );
			
			Object epg = ois.readObject();			
			
			if ( epg instanceof ArrayList ) {
				System.out.println( "Got Local EPG: " + epg );
				
				return (ArrayList<ChannelEPG>) epg;
			}
			
			else 
				return null;
		
		} catch (Exception e) {
			return null;
		}
	}
	
	private static void updateChannelPointers( ChannelEPG channel, int relativePos ) {		
		int size = channelData.size();
		
		int channelPos = getChannelIndex( channel );
				
		switch ( relativePos ) {
			case CURRENT_ITEM: {
				int	nextPos = (channelPos + 1) >= size ? 0 : channelPos + 1,
					previousPos = (channelPos - 1) < 0 ? size - 1 : channelPos - 1;
						
				previousChannel = channelData.get( nextPos );
				nextChannel = channelData.get( previousPos );
				
				break;
			}
			
			case PREVIOUS_ITEM: {
				int currPos = (channelPos + 1) >= size ? 0 : channelPos + 1,
					nextPos = (channelPos + 2) >= size ? 1 : channelPos + 2;
					
				currentChannel = channelData.get( currPos );
				nextChannel = channelData.get( nextPos );
				
				break;
			}
			
			case NEXT_ITEM: {
				int currPos = (channelPos - 1) < 0 ? size - 1 : channelPos - 1,
					prevPos = (channelPos - 2) < 0 ? size - 2 : channelPos - 2;
						
					currentChannel = channelData.get( currPos );
					previousChannel = channelData.get( prevPos );
				
				break;
			}
		}
	}
	
	private static void updateProgramPointers( ProgramEPG program, int relativePos ) {
		// rearrange program based on the channel and the pos
	}
	
	// EPG Search
	
	public static ChannelEPG findChannelBySRef( String sRef ) {
		for ( ChannelEPG channel : channelData ) {
			if ( channel == null )
				continue;
			
			if ( channel.getServiceRef().equals( sRef ) )
				return channel;
		}
		
		return null;
	}
	
	public static ProgramEPG findProgramForDate( ChannelEPG channel, Date searchDate ) {
		ProgramEPG foundProgram = null;
		Long searchTime = searchDate.getTime();
		
		for ( ProgramEPG program : channel.getProgramList() ) {
			if ( program.getStartDate().getTime() <= searchTime && program.getEndDate().getTime() > searchTime ) {
				foundProgram = program;
				break;
			}
		}
		
		return foundProgram;
	}
	
	public static int getChannelIndex( ChannelEPG channel ) {
		if ( channel == null || channel.getServiceRef() ==  null )
			return -1;
		
		return getChannelIndex( channel.getServiceRef() );
	}
	
	public static int getChannelIndex( String sRef ) {
		if ( sRef == null )
			return -1;
		
		ChannelEPG searchChannel = new ChannelEPG( "SearchChannel", sRef, null );
		
		return Collections.binarySearch( channelData, searchChannel, new ChannelEPGServiceRefComparator());
	}

	// Channel/Program API
	
	public static ChannelEPG getPreviousChannel() {
		return previousChannel;
	}

	public static void setPreviousChannel(ChannelEPG previousChannel) { // update everything when called
		EPGService.previousChannel = previousChannel;
		
		updateChannelPointers( previousChannel, PREVIOUS_ITEM );
	}

	public static ChannelEPG getNextChannel() {
		return nextChannel;
	}

	public static void setNextChannel(ChannelEPG nextChannel) {
		EPGService.nextChannel = nextChannel;
		
		updateChannelPointers( nextChannel, NEXT_ITEM );
	}

	public static ProgramEPG getPreviousProgram() {
		return previousProgram;
	}

	public static void setPreviousProgram(ProgramEPG previousProgram) {
		EPGService.previousProgram = previousProgram;
		
		updateProgramPointers( previousProgram, PREVIOUS_ITEM );
	}

	public static ProgramEPG getNextProgram() {
		return nextProgram;
	}

	public static void setNextProgram(ProgramEPG nextProgram) {
		EPGService.nextProgram = nextProgram;
		
		updateProgramPointers( nextProgram, NEXT_ITEM );
	}
	
}
