package nuig.ece.third;

import java.util.Comparator;

public class ProgramEPGComparator implements Comparator<ProgramEPG> {

	public int compare(ProgramEPG arg0, ProgramEPG arg1) {
		return (int) (arg0.getStartDate().getTime() - arg1.getStartDate().getTime());
	}

}
