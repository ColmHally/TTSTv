package nuig.ece.third;

import java.util.Comparator;

public class ChannelEPGServiceRefComparator implements Comparator<ChannelEPG> {

	public int compare(ChannelEPG arg0, ChannelEPG arg1) {
		if ( arg0 == null || arg1 == null )
			return 0;
		
		return arg0.getServiceRef().compareTo(arg1.getServiceRef());
	}
	
}
