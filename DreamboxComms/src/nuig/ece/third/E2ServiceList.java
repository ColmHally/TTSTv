package nuig.ece.third;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class E2ServiceList {
	private ArrayList<E2Service> services;
	
	public E2ServiceList( ArrayList<E2Service> services ) {
		setServices( services );
	}
	
	// Factory Methods
	
	public static E2ServiceList newServiceListFromXMLNode( Node listNode ) {
		Element parentElement = (Element) listNode;
		
		if ( listNode.getNodeType() != Node.ELEMENT_NODE )
			return null;
		
		ArrayList<E2Service> services = new ArrayList<E2Service>();
		NodeList serviceNodes = parentElement.getElementsByTagName( "e2service" );
		
		if ( serviceNodes.getLength() == 0 )
			return null;
		
		for ( int i = 0; i < serviceNodes.getLength(); i++ ) {
			Node n = serviceNodes.item( i );
			
			E2Service evt = E2Service.newServiceFromXMLNode( n );
			
			if ( evt != null )
				services.add( evt );
		}
		
		return new E2ServiceList( services );	
	}
	
	// Accessors

	public ArrayList<E2Service> getServices() {
		return services;
	}

	public void setServices(ArrayList<E2Service> services) {
		this.services = services;
	}
}
