package nuig.ece.third;

import java.util.ArrayList;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class E2Service {
	private String 	sRef = "",
					name = "";
	
	private ArrayList<E2Service> serviceList;

	public E2Service( String sRef, String name, ArrayList<E2Service> serviceList ) {
		setSRef( sRef );
		setName( name );
		setServiceList( serviceList );
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

	public ArrayList<E2Service> getServiceList() {
		return serviceList;
	}

	public void setServiceList(ArrayList<E2Service> serviceList) {
		this.serviceList = serviceList;
	}
	
	public static E2Service serviceFromXMLNode( Node serviceNode ) {
		E2Service service = null;
		
		if ( serviceNode == null || serviceNode.getNodeName() != "e2service" )
			return null;
		
		String name = getTagValue( "e2servicename", serviceNode );
		
		return service;
	}
	
	private static String getTagValue( String tagName, Node node ) {
		if ( node.getNodeType() != Node.ELEMENT_NODE ) 
			return null;
		
		Element element = (Element)node;
		
		return "";
	}
	
}
