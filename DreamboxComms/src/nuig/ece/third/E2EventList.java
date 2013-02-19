package nuig.ece.third;

import java.util.ArrayList;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class E2EventList {
	private ArrayList<E2Event> events;

	public E2EventList() {
		events = new ArrayList<E2Event>();
	}
	
	public E2EventList( ArrayList<E2Event> events ) {
		setEvents( events );
	}
	
	// Factory Methods
	
	public static E2EventList newEventListFromXMLNode( Node parentNode ) {
		Element parentElement = (Element) parentNode;
		
		if ( parentNode.getNodeType() != Node.ELEMENT_NODE )
			return null;
		
		ArrayList<E2Event> events = new ArrayList<E2Event>();
		NodeList eventNodes = parentElement.getElementsByTagName( "e2event" );
		
		if ( eventNodes.getLength() == 0 )
			return null;
		
		for ( int i = 0; i < eventNodes.getLength(); i++ ) {
			Node n = eventNodes.item( i );
			
			E2Event evt = E2Event.newEventFromXMLNode( n );
			
			if ( evt != null )
				events.add( evt );
		}
		
		return new E2EventList( events );		
	}
	
	// Accessors
	public ArrayList<E2Event> getEvents() {
		return events;
	}

	public void setEvents(ArrayList<E2Event> events) {
		this.events = events;
	}
	
	public String toString() {
		return events.toString();
	}
}
