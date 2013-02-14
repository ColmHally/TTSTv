package nuig.ece.third;

import java.util.ArrayList;

public class E2ServiceList {
	private ArrayList<E2Service> services;
	
	public E2ServiceList( ArrayList<E2Service> services ) {
		setServices( services );
	}
	
	// Factory Methods
	
	
	
	// Accessors

	public ArrayList<E2Service> getServices() {
		return services;
	}

	public void setServices(ArrayList<E2Service> services) {
		this.services = services;
	}
}
