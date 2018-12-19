package com.jeffrpowell.templenamepool.model;

import java.util.EnumSet;
import java.util.Set;

public enum Ordinance {
    BAPTISM_CONFIRMATION, INITIATORY, ENDOWMENT, SEALING_PARENTS, SEALING_SPOUSE;
	
	public Set<Ordinance> getPrerequisiteOrdinances() {
		Set<Ordinance> prereqs = EnumSet.noneOf(Ordinance.class);
		switch (this) {
			case BAPTISM_CONFIRMATION:
				break;
			case SEALING_SPOUSE:
			case SEALING_PARENTS:
				prereqs.add(ENDOWMENT); //spilling on purpose
			case ENDOWMENT:
				prereqs.add(INITIATORY); //spilling on purpose
			case INITIATORY:
				prereqs.add(BAPTISM_CONFIRMATION);
				break;
		}
		return prereqs;
	}
}
