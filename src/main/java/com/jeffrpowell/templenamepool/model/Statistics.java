package com.jeffrpowell.templenamepool.model;

import java.util.Map;

public class Statistics
{

	private final double percentOrdinancesCompleted;
	private final Map<Ordinance, Integer> numOrdinancesPerformed;
	private final Map<Ordinance, Integer> numMaleOrdinancesRemaining;
	private final Map<Ordinance, Integer> numFemaleOrdinancesRemaining;
	private final Map<Ordinance, Integer> numUnblockedMaleOrdinancesRemaining;
	private final Map<Ordinance, Integer> numUnblockedFemaleOrdinancesRemaining;
	private final Map<WardMember, Integer> nameSuppliersAndCountOfSubmissions;
	private final Map<WardMember, Integer> nameRequestersAndCountOfOrdinancesCompleted;

	public Statistics(double percentOrdinancesCompleted, Map<Ordinance, Integer> numOrdinancesPerformed, Map<Ordinance, Integer> numMaleOrdinancesRemaining, Map<Ordinance, Integer> numUnblockedMaleOrdinancesRemaining, Map<Ordinance, Integer> numFemaleOrdinancesRemaining, Map<Ordinance, Integer> numUnblockedFemaleOrdinancesRemaining, Map<WardMember, Integer> nameSuppliersAndCountOfSubmissions, Map<WardMember, Integer> nameRequestersAndCountOfOrdinancesCompleted)
	{
		this.percentOrdinancesCompleted = percentOrdinancesCompleted;
		this.numOrdinancesPerformed = numOrdinancesPerformed;
		this.numMaleOrdinancesRemaining = numMaleOrdinancesRemaining;
		this.numFemaleOrdinancesRemaining = numFemaleOrdinancesRemaining;
		this.numUnblockedMaleOrdinancesRemaining = numUnblockedMaleOrdinancesRemaining;
		this.numUnblockedFemaleOrdinancesRemaining = numUnblockedFemaleOrdinancesRemaining;
		this.nameSuppliersAndCountOfSubmissions = nameSuppliersAndCountOfSubmissions;
		this.nameRequestersAndCountOfOrdinancesCompleted = nameRequestersAndCountOfOrdinancesCompleted;
	}

	public double getPercentOrdinancesCompleted()
	{
		return percentOrdinancesCompleted;
	}

	public Map<Ordinance, Integer> getNumOrdinancesPerformed()
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

	public Map<Ordinance, Integer> getNumUnblockedMaleOrdinancesRemaining()
	{
		return numUnblockedMaleOrdinancesRemaining;
	}

	public Map<Ordinance, Integer> getNumUnblockedFemaleOrdinancesRemaining()
	{
		return numUnblockedFemaleOrdinancesRemaining;
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
		private Map<Ordinance, Integer> numOrdinancesPerformed;
		private Map<Ordinance, Integer> numMaleOrdinancesRemaining;
		private Map<Ordinance, Integer> numUnblockedMaleOrdinancesRemaining;
		private Map<Ordinance, Integer> numFemaleOrdinancesRemaining;
		private Map<Ordinance, Integer> numUnblockedFemaleOrdinancesRemaining;
		private Map<WardMember, Integer> nameSuppliersAndCountOfSubmissions;
		private Map<WardMember, Integer> nameRequestersAndCountOfOrdinancesCompleted;

		public Builder setPercentOrdinancesCompleted(double percentOrdinancesCompleted)
		{
			this.percentOrdinancesCompleted = percentOrdinancesCompleted;
			return this;
		}

		public Builder setNumOrdinancesPerformed(Map<Ordinance, Integer> numOrdinancesPerformed)
		{
			this.numOrdinancesPerformed = numOrdinancesPerformed;
			return this;
		}

		public Builder setNumMaleOrdinancesRemaining(Map<Ordinance, Integer> numMaleOrdinancesRemaining)
		{
			this.numMaleOrdinancesRemaining = numMaleOrdinancesRemaining;
			return this;
		}

		public Builder setNumUnblockedMaleOrdinancesRemaining(Map<Ordinance, Integer> numUnblockedMaleOrdinancesRemaining)
		{
			this.numUnblockedMaleOrdinancesRemaining = numUnblockedMaleOrdinancesRemaining;
			return this;
		}

		public Builder setNumFemaleOrdinancesRemaining(Map<Ordinance, Integer> numFemaleOrdinancesRemaining)
		{
			this.numFemaleOrdinancesRemaining = numFemaleOrdinancesRemaining;
			return this;
		}

		public Builder setNumUnblockedFemaleOrdinancesRemaining(Map<Ordinance, Integer> numUnblockedFemaleOrdinancesRemaining)
		{
			this.numUnblockedFemaleOrdinancesRemaining = numUnblockedFemaleOrdinancesRemaining;
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
			return new Statistics(percentOrdinancesCompleted, numOrdinancesPerformed, numMaleOrdinancesRemaining, numUnblockedMaleOrdinancesRemaining, numFemaleOrdinancesRemaining, numUnblockedFemaleOrdinancesRemaining, nameSuppliersAndCountOfSubmissions, nameRequestersAndCountOfOrdinancesCompleted);
		}
	}
}
