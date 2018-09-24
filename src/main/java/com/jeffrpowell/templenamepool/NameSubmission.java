package com.jeffrpowell.templenamepool;

import java.util.Set;

public class NameSubmission extends TempleName{
    private final WardMember supplier;

    public NameSubmission(String familySearchId, WardMember supplier, byte[] pdf, Set<Ordinance> ordinances)
    {
	super(familySearchId, pdf, ordinances);
	this.supplier = supplier;
    }

    public WardMember getSupplier()
    {
	return supplier;
    }
    
}
