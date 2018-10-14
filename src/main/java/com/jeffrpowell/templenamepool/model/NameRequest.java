package com.jeffrpowell.templenamepool.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NameRequest {
	private WardMember requester;
	private Ordinance ordinance;
	private int numRequested;
    private boolean maleRequested;
	private LocalDate targetDate;

	public NameRequest()
	{
	}

	public NameRequest(WardMember requester, Ordinance ordinance, int numRequested, boolean maleRequested, LocalDate targetDate)
	{
		this.requester = requester;
		this.ordinance = ordinance;
		this.numRequested = numRequested;
        this.maleRequested = maleRequested;
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

    public boolean isMaleRequested() {
        return maleRequested;
    }
    
	public LocalDate getTargetDate()
	{
		return targetDate;
	}

	public void setRequester(WardMember requester)
	{
		this.requester = requester;
	}

	public void setOrdinance(Ordinance ordinance)
	{
		this.ordinance = ordinance;
	}

	public void setNumRequested(int numRequested)
	{
		this.numRequested = numRequested;
	}

    public void setMaleRequested(boolean maleRequested) {
        this.maleRequested = maleRequested;
    }

	public void setTargetDate(LocalDate targetDate)
	{
		this.targetDate = targetDate;
	}
	
	public String getFileName() {
		return requester.getName().replace(" ", "_") + "-" + ordinance.name().toLowerCase() + "-" + targetDate.format(DateTimeFormatter.ISO_DATE);
	}

}
