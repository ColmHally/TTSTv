package nuig.ece.third;

import java.util.ArrayList;
import java.util.Collections;

public class ChannelEPG {
	
	private String name = "",
			serviceRef = "";
	private ArrayList<ProgramEPG> programList;
	
	// Constructor
	public ChannelEPG( String name, ArrayList<ProgramEPG> programs ) {
		setName( name );
		
		Collections.sort( programs );
		
		setProgramList( programs );
	}
	
	// Accessors
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<ProgramEPG> getProgramList() {
		return programList;
	}
	public void setProgramList(ArrayList<ProgramEPG> programList) {
		this.programList = programList;
	}
	
}
