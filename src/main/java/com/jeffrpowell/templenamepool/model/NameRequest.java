package com.jeffrpowell.templenamepool.model;

import java.time.LocalDate;

public class NameRequest {
    private final WardMember requester;
    private final Ordinance ordinance;
    private final int numRequested;
    private final LocalDate targetDate;

    public NameRequest(WardMember requester, Ordinance ordinance, int numRequested, LocalDate targetDate)
    {
	this.requester = requester;
	this.ordinance = ordinance;
	this.numRequested = numRequested;
	this.targetDate = targetDate;
    }

    public WardMember getRequester()
    {
	return requester;
    }

    public Ordinance getOrdinance()
    {
	return ordinance;
    }

    public int getNumRequested()
    {
	return numRequested;
    }

    public LocalDate getTargetDate()
    {
	return targetDate;
    }
    
}
