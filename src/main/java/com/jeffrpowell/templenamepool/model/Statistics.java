package com.jeffrpowell.templenamepool.model;

import java.util.Map;

public class Statistics
{

	private final double percentOrdinancesCompleted;
	private final int numOrdinancesPerformed;
	private final Map<Ordinance, Integer> numMaleOrdinancesRemaining;
	private final Map<Ordinance, Integer> numFemaleOrdinancesRemaining;
	private final Map<WardMember, Integer> nameSuppliersAndCountOfSubmissions;
	private final Map<WardMember, Integer> nameRequestersAndCountOfOrdinancesCompleted;

	public Statistics(double percentOrdinancesCompleted, int numOrdinancesPerformed, Map<Ordinance, Integer> numMaleOrdinancesRemaining, Map<Ordinance, Integer> numFemaleOrdinancesRemaining, Map<WardMember, Integer> nameSuppliersAndCountOfSubmissions, Map<WardMember, Integer> nameRequestersAndCountOfOrdinancesCompleted)
	{
		this.percentOrdinancesCompleted = percentOrdinancesCompleted;
		this.numOrdinancesPerformed = numOrdinancesPerformed;
		this.numMaleOrdinancesRemaining = numMaleOrdinancesRemaining;
		this.numFemaleOrdinancesRemaining = numFemaleOrdinancesRemaining;
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

	public Map<Ordinance, Integer> getNumMaleOrdinancesRemaining()
	{
		return numMaleOrdinancesRemaining;
	}

	public Map<Ordinance, Integer> getNumFemaleOrdinancesRemaining()
	{
		return numFemaleOrdinancesRemaining;
	}

	public Map<WardMember, Integer> getNameSuppliersAndCountOfSubmissions()
	{
		return nameSuppliersAndCountOfSubmissions;
	}

	public Map<WardMember, Integer> getNameRequestersAndCountOfOrdinancesCompleted()
	{
		return nameRequestersAndCountOfOrdinancesCompleted;
	}

	public static class Builder
	{

		private double percentOrdinancesCompleted;
		private int numOrdinancesPerformed;
		private Map<Ordinance, Integer> numMaleOrdinancesRemaining;
		private Map<Ordinance, Integer> numFemaleOrdinancesRemaining;
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

		public Builder setNumMaleOrdinancesRemaining(Map<Ordinance, Integer> numMaleOrdinancesRemaining)
		{
			this.numMaleOrdinancesRemaining = numMaleOrdinancesRemaining;
			return this;
		}

		public Builder setNumFemaleOrdinancesRemaining(Map<Ordinance, Integer> numFemaleOrdinancesRemaining)
		{
			this.numFemaleOrdinancesRemaining = numFemaleOrdinancesRemaining;
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

		public Statistics build()
		{
			return new Statistics(percentOrdinancesCompleted, numOrdinancesPerformed, numMaleOrdinancesRemaining, numFemaleOrdinancesRemaining, nameSuppliersAndCountOfSubmissions, nameRequestersAndCountOfOrdinancesCompleted);
		}
	}
}
