package nuig.ece.third;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class E2Service {
	private String 	sRef = "",
					name = "";
	
	public E2Service( String sRef, String name) {
		setSRef( sRef );
		setName( name );
	}
	
	public String getSRef() {
		return sRef;
	}

	public void setSRef(String sRef) {
		this.sRef = sRef;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static E2Service newServiceFromXMLNode( Node serviceNode ) {
		Node	nameNode = getE2Node( serviceNode, "name" ),
				sRefNode = getE2Node( serviceNode, "reference" );
		
		return new E2Service( sRefNode.getTextContent(), nameNode.getTextContent() );
	}
	
	private static Node getE2Node( Node parentNode, String nodeName ) {		
		if ( parentNode.getNodeType() != Node.ELEMENT_NODE )
			return null;
		
		nodeName = "e2service" + nodeName;
		
		Element parentElement = (Element)parentNode; 
		
		NodeList nodes = parentElement.getElementsByTagName(nodeName);
		
		if ( nodes.getLength() > 0 )
			return nodes.item( 0 );
		
		return null;
	}

	
}
