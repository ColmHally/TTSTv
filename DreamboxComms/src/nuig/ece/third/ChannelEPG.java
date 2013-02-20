package nuig.ece.third;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class ChannelEPG implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String name = "",
			serviceRef = "";
	private ArrayList<ProgramEPG> programList;
	
	// Constructor
	public ChannelEPG( String name, String sRef, ArrayList<ProgramEPG> programs ) {
		setName( name );
		setServiceRef( sRef );
		
		Collections.sort( programs, new ProgramEPGComparator() );
		
		setProgramList( programs );
	}
	
	// Factories
	
	public static ChannelEPG newChannelEPGFromE2EventList( E2EventList eventlist ) {
		ArrayList<ProgramEPG> programs = new ArrayList<ProgramEPG>();
		ArrayList<E2Event> events = eventlist.getEvents();
		
		String 	name = null,
				sRef = null;
		
		for ( E2Event evt : events ) {
			if ( name == null )
				name = evt.getChannelTitle();
			
			if ( sRef == null )
				sRef = evt.getsRef();
			
			Date 	start = new Date( evt.getStartTime() * 1000 ),
					end = new Date( ( evt.getStartTime() + evt.getDuration() ) * 1000 );
			
			String 	title = evt.getTitle(),
					desc = evt.getExtendedDescription();
			
			programs.add( new ProgramEPG( start, end, title, desc ) );
					
		}
		
		return new ChannelEPG( name, sRef, programs );
	}
	
	// toString
	
	public String toString() {
		return "Channel " + name + "(" + serviceRef + "):\n" + programList.toString();
	}
	
	// Accessors
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getServiceRef() {
		return serviceRef;
	}

	public void setServiceRef(String serviceRef) {
		this.serviceRef = serviceRef;
	}

	public ArrayList<ProgramEPG> getProgramList() {
		return programList;
	}
	public void setProgramList(ArrayList<ProgramEPG> programList) {
		this.programList = programList;
	}
	
}
