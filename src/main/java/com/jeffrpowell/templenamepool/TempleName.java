package com.jeffrpowell.templenamepool;

import java.util.Set;

public class TempleName {
    protected final String familySearchId;
    protected final byte[] pdf;
    protected final Set<Ordinance> ordinances;

    public TempleName(String familySearchId, byte[] pdf, Set<Ordinance> ordinances)
    {
	this.familySearchId = familySearchId;
	this.pdf = pdf;
	this.ordinances = ordinances;
    }

    public String getFamilySearchId()
    {
	return familySearchId;
    }

    public byte[] getPdf()
    {
	return pdf;
    }

    public Set<Ordinance> getOrdinances()
    {
	return ordinances;
    }
}
