package nuig.ece.third;

import java.util.Comparator;

public class ChannelEPGNameComparator implements Comparator<ChannelEPG> {

	public int compare(ChannelEPG o1, ChannelEPG o2) {
		if ( o1 == null || o2 == null )
			return 0;
		
		return o1.getName().compareTo( o2.getName() );
	}

	

}
