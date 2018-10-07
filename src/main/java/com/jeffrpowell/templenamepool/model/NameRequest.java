package com.jeffrpowell.templenamepool.model;

import java.time.LocalDate;

public class NameRequest {
	private WardMember requester;
	private Ordinance ordinance;
	private int numRequested;
	private LocalDate targetDate;

	public NameRequest()
	{
	}

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

	public void setTargetDate(LocalDate targetDate)
	{
		this.targetDate = targetDate;
	}
	

}
