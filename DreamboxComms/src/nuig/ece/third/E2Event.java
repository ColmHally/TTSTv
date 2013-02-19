package nuig.ece.third;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class E2Event {
	private Long 	id,
					startTime,
					duration;
	
	private String 	title,
					description,
					extendedDescription,
					channelTitle,
					sRef;
	
	// XML Extraction
	
	public static E2Event newEventFromXMLNode( Node eventNode ) {
		Node 	idNode = getE2Node( eventNode, "id" ),
				startTimeNode = getE2Node( eventNode, "start" ),
				durationNode = getE2Node( eventNode, "duration" ),
				titleNode = getE2Node( eventNode, "title" ),
				descriptionNode = getE2Node( eventNode, "description" ),
				descriptionExtendedNode = getE2Node( eventNode, "descriptionextended" ),
				channelTitleNode = getE2Node( eventNode, "servicename" ),
				sRefNode = getE2Node( eventNode, "servicereference" );
		
		E2Event event = new E2Event();
		
		event.setId( Long.parseLong( idNode.getTextContent() ) );
		event.setStartTime( Long.parseLong( startTimeNode.getTextContent() ) );
		event.setDuration( Long.parseLong( durationNode.getTextContent() ) );
		event.setTitle( titleNode.getTextContent() );
		event.setDescription( descriptionNode.getTextContent() );
		event.setExtendedDescription( descriptionExtendedNode.getTextContent() );
		event.setChannelTitle( channelTitleNode.getTextContent() );
		event.setsRef( sRefNode.getTextContent() );
		
		return event;
	}
	
	private static Node getE2Node( Node parentNode, String nodeName ) {		
		if ( parentNode.getNodeType() != Node.ELEMENT_NODE )
			return null;
		
		nodeName = "e2event" + nodeName;
		Element parentElement = (Element)parentNode; 
		
		NodeList nodes = parentElement.getElementsByTagName(nodeName);
		
		if ( nodes.getLength() > 0 )
			return nodes.item( 0 );
		
		return null;
	}
	
	// toString
	
	public String toString() {
		return channelTitle + ": " + title + " at " + startTime;
	}
	
	
	// Accessors
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExtendedDescription() {
		return extendedDescription;
	}

	public void setExtendedDescription(String extendedDescription) {
		this.extendedDescription = extendedDescription;
	}

	public String getChannelTitle() {
		return channelTitle;
	}

	public void setChannelTitle(String channelTitle) {
		this.channelTitle = channelTitle;
	}

	public String getsRef() {
		return sRef;
	}

	public void setsRef(String sRef) {
		this.sRef = sRef;
	}
}
