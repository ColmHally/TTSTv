package nuig.ece.third;

import java.io.Serializable;
import java.util.Date;

public class ProgramEPG implements Comparable<ProgramEPG> {
	
	private String title = "", description = "";
	private Date startDate, endDate;
	
	// Constructor
	public ProgramEPG( Date startDate, Date endDate, String title, String description ) {
		setStartDate( startDate );
		setEndDate( endDate );
		setTitle( title );
		setDescription( description );
	}
	
	
	// Comparable interface
	public int compareTo(ProgramEPG program) {
		return (int) (getStartDate().getTime() - program.getStartDate().getTime());
	}
	
	// toString
	
	public String toString() {
		return "Program: " + title + " from " + startDate + " to " + endDate + "\n\"" + description + "\"";
	}
	
	// Accessors
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
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}
