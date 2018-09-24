package com.jeffrpowell.templenamepool;

import java.util.Map;

public class Statistics {
    private final double percentOrdinancesCompleted;
    private final int numOrdinancesPerformed;
    private final Map<Ordinance, Integer> numOrdinancesRemaining;
    private final Map<WardMember, Integer> nameSuppliersAndCountOfSubmissions;
    private final Map<WardMember, Integer> nameRequestersAndCountOfOrdinancesCompleted;

    public Statistics(double percentOrdinancesCompleted, int numOrdinancesPerformed, Map<Ordinance, Integer> numOrdinancesRemaining, Map<WardMember, Integer> nameSuppliersAndCountOfSubmissions, Map<WardMember, Integer> nameRequestersAndCountOfOrdinancesCompleted)
    {
	this.percentOrdinancesCompleted = percentOrdinancesCompleted;
	this.numOrdinancesPerformed = numOrdinancesPerformed;
	this.numOrdinancesRemaining = numOrdinancesRemaining;
	this.nameSuppliersAndCountOfSubmissions = nameSuppliersAndCountOfSubmissions;
	this.nameRequestersAndCountOfOrdinancesCompleted = nameRequestersAndCountOfOrdinancesCompleted;
    }

    public double getPercentOrdinancesCompleted()
    {
	return percentOrdinancesCompleted;
    }

    public int getNumOrdinancesPerformed()
    {
	return numOrdinancesPerformed;
    }

    public Map<Ordinance, Integer> getNumOrdinancesRemaining()
    {
	return numOrdinancesRemaining;
    }

    public Map<WardMember, Integer> getNameSuppliersAndCountOfSubmissions()
    {
	return nameSuppliersAndCountOfSubmissions;
    }

    public Map<WardMember, Integer> getNameRequestersAndCountOfOrdinancesCompleted()
    {
	return nameRequestersAndCountOfOrdinancesCompleted;
    }
    
    public static class Builder {
	private double percentOrdinancesCompleted;
	private int numOrdinancesPerformed;
	private Map<Ordinance, Integer> numOrdinancesRemaining;
	private Map<WardMember, Integer> nameSuppliersAndCountOfSubmissions;
	private Map<WardMember, Integer> nameRequestersAndCountOfOrdinancesCompleted;

	public Builder setPercentOrdinancesCompleted(double percentOrdinancesCompleted)
	{
	    this.percentOrdinancesCompleted = percentOrdinancesCompleted;
	    return this;
	}

	public Builder setNumOrdinancesPerformed(int numOrdinancesPerformed)
	{
	    this.numOrdinancesPerformed = numOrdinancesPerformed;
	    return this;
	}

	public Builder setNumOrdinancesRemaining(Map<Ordinance, Integer> numOrdinancesRemaining)
	{
	    this.numOrdinancesRemaining = numOrdinancesRemaining;
	    return this;
	}

	public Builder setNameSuppliersAndCountOfSubmissions(Map<WardMember, Integer> nameSuppliersAndCountOfSubmissions)
	{
	    this.nameSuppliersAndCountOfSubmissions = nameSuppliersAndCountOfSubmissions;
	    return this;
	}

	public Builder setNameRequestersAndCountOfOrdinancesCompleted(Map<WardMember, Integer> nameRequestersAndCountOfOrdinancesCompleted)
	{
	    this.nameRequestersAndCountOfOrdinancesCompleted = nameRequestersAndCountOfOrdinancesCompleted;
	    return this;
	}
	
	public Statistics build() {
	    return new Statistics(percentOrdinancesCompleted, numOrdinancesPerformed, numOrdinancesRemaining, nameSuppliersAndCountOfSubmissions, nameRequestersAndCountOfOrdinancesCompleted);
	}
    }
}
