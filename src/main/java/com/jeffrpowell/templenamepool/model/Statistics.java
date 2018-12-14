package com.jeffrpowell.templenamepool.model;

import java.util.Map;

public class Statistics
{

	private final Map<Ordinance, Integer> numOrdinancesPerformed;
	private final Map<Ordinance, Integer> numMaleOrdinancesRemaining;
	private final Map<Ordinance, Integer> numFemaleOrdinancesRemaining;
	private final Map<Ordinance, Integer> numUnblockedMaleOrdinancesRemaining;
	private final Map<Ordinance, Integer> numUnblockedFemaleOrdinancesRemaining;
	private final Map<Ordinance, Integer> numCheckedOutMaleOrdinances;
	private final Map<Ordinance, Integer> numCheckedOutFemaleOrdinances;
	private final Map<WardMember, Integer> nameSuppliersAndCountOfSubmissions;
	private final Map<WardMember, Integer> nameRequestersAndCountOfOrdinancesCompleted;

	public Statistics(Map<Ordinance, Integer> numOrdinancesPerformed, Map<Ordinance, Integer> numMaleOrdinancesRemaining, Map<Ordinance, Integer> numFemaleOrdinancesRemaining, Map<Ordinance, Integer> numUnblockedMaleOrdinancesRemaining, Map<Ordinance, Integer> numUnblockedFemaleOrdinancesRemaining, Map<Ordinance, Integer> numCheckedOutMaleOrdinances, Map<Ordinance, Integer> numCheckedOutFemaleOrdinances, Map<WardMember, Integer> nameSuppliersAndCountOfSubmissions, Map<WardMember, Integer> nameRequestersAndCountOfOrdinancesCompleted)
	{
		this.numOrdinancesPerformed = numOrdinancesPerformed;
		this.numMaleOrdinancesRemaining = numMaleOrdinancesRemaining;
		this.numFemaleOrdinancesRemaining = numFemaleOrdinancesRemaining;
		this.numUnblockedMaleOrdinancesRemaining = numUnblockedMaleOrdinancesRemaining;
		this.numUnblockedFemaleOrdinancesRemaining = numUnblockedFemaleOrdinancesRemaining;
		this.numCheckedOutMaleOrdinances = numCheckedOutMaleOrdinances;
		this.numCheckedOutFemaleOrdinances = numCheckedOutFemaleOrdinances;
		this.nameSuppliersAndCountOfSubmissions = nameSuppliersAndCountOfSubmissions;
		this.nameRequestersAndCountOfOrdinancesCompleted = nameRequestersAndCountOfOrdinancesCompleted;
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

	public Map<Ordinance, Integer> getNumCheckedOutMaleOrdinances()
	{
		return numCheckedOutMaleOrdinances;
	}

	public Map<Ordinance, Integer> getNumCheckedOutFemaleOrdinances()
	{
		return numCheckedOutFemaleOrdinances;
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

		private Map<Ordinance, Integer> numOrdinancesPerformed;
		private Map<Ordinance, Integer> numMaleOrdinancesRemaining;
		private Map<Ordinance, Integer> numUnblockedMaleOrdinancesRemaining;
		private Map<Ordinance, Integer> numCheckedOutMaleOrdinances;
		private Map<Ordinance, Integer> numFemaleOrdinancesRemaining;
		private Map<Ordinance, Integer> numUnblockedFemaleOrdinancesRemaining;
		private Map<Ordinance, Integer> numCheckedOutFemaleOrdinances;
		private Map<WardMember, Integer> nameSuppliersAndCountOfSubmissions;
		private Map<WardMember, Integer> nameRequestersAndCountOfOrdinancesCompleted;

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

		public Builder setNumCheckedOutMaleOrdinances(Map<Ordinance, Integer> numCheckedOutMaleOrdinances)
		{
			this.numCheckedOutMaleOrdinances = numCheckedOutMaleOrdinances;
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

		public Builder setNumCheckedOutFemaleOrdinances(Map<Ordinance, Integer> numCheckedOutFemaleOrdinances)
		{
			this.numCheckedOutFemaleOrdinances = numCheckedOutFemaleOrdinances;
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
			return new Statistics(numOrdinancesPerformed, numMaleOrdinancesRemaining, numFemaleOrdinancesRemaining, numUnblockedMaleOrdinancesRemaining, numUnblockedFemaleOrdinancesRemaining, numCheckedOutMaleOrdinances, numCheckedOutFemaleOrdinances, nameSuppliersAndCountOfSubmissions, nameRequestersAndCountOfOrdinancesCompleted);
		}
	}
}
